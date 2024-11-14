package comp3111.examsystem.controller;

import comp3111.examsystem.model.Grade;
import comp3111.examsystem.model.Question;
import comp3111.examsystem.model.Quiz;
import comp3111.examsystem.model.Student;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class QuizViewController implements Initializable {
    private Student curStudent;
    private Database<Question> questionDatabase;
    private Database<Grade> gradeDatabase;
    private Quiz currentQuiz;
    private Question currQuestion;
    private String questionDesc;
    private String[] questionIds;
    private List<String> questions;
    private Map<Long, List<String>> selectedAnswers;

    @FXML
    private Label ansA;

    @FXML
    private Label ansB;

    @FXML
    private Label ansC;

    @FXML
    private Label ansD;

    @FXML
    private RadioButton choiceA;

    @FXML
    private RadioButton choiceB;

    @FXML
    private RadioButton choiceC;

    @FXML
    private Label currQNum;

    @FXML
    private RadioButton choiceD;

    @FXML
    private Label currQuiz;

    @FXML
    private Label numQ;

    @FXML
    private Label question;

    @FXML
    private ListView<String> questionList;

    @FXML
    private Label rTime;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionDatabase = new Database<>(Question.class);
        gradeDatabase = new Database<>(Grade.class);
        questions = new ArrayList<>();
        selectedAnswers = new HashMap<>();
        currentQuiz = StudentMainController.chosenQuiz;
        curStudent = StudentLoginController.loggedInStudent;

        numQ.setText(currentQuiz.getNumQuestions());
        currQuiz.setText(currentQuiz.getCourseId() + " " + currentQuiz.getQuizName());

        setTimer(currentQuiz.getQuizTime());
        showQuestions(currentQuiz);
        showQuestionDet(currentQuiz);
        Platform.runLater(this::setupCloseRequestHandler);
    }

    private void setupCloseRequestHandler() {
        Stage primaryStage = (Stage) currQuiz.getScene().getWindow();

        primaryStage.setOnCloseRequest(event -> {
            checkAnswers();
            MsgSender.showMsg("Quiz ended, your answers have been submitted.");
        });
    }

    // Helper function to show question details
    private void showQuestionDet(Quiz currentQ) {
        questionList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                questionDesc = questionList.getSelectionModel().getSelectedItem();
                currQuestion = questionDatabase.queryByField("questionDescription", questionDesc).getFirst();
                question.setText(questionDesc);
                setAnsDescriptions(currQuestion);
                // Load selected answer for this question, if available
                loadSelectedAnswers();
                setChoiceListeners();
                System.out.println(selectedAnswers);
            }
        });
        Platform.runLater(this::setupCloseRequestHandler);
    }

    // Helper function to answer description
    private void setAnsDescriptions(Question question){
        ansA.setText(question.getOptionA());
        ansB.setText(question.getOptionB());
        ansC.setText(question.getOptionC());
        ansD.setText(question.getOptionD());
    }

    // Helper function to show questions in the side bar
    private void showQuestions(Quiz quiz){
        questionIds = StudentMainController.splitByPipe(quiz.getQuestions());
        for(int i = 0; i < questionIds.length; ++i){
            Question q = questionDatabase.queryByField("id", questionIds[i]).getFirst();
            questions.add(q.questionDescription);
        }
        questionList.getItems().addAll(questions);
    }

    // Helper function to set the timer
    private void setTimer(String time){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            int counter = Integer.parseInt(time)*60;
            @Override
            public void run() {
                if(counter > 0){
                    int minutes = counter / 60;
                    int seconds = counter % 60;
                    String remainingT = minutes + " minutes " + seconds + " seconds";
                    Platform.runLater(() -> rTime.setText(remainingT));
                    counter--;
                }
                else{
                    Platform.runLater(() -> {
                        MsgSender.showMsg("Time's Up!");
                    });
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    // Adds listeners to each RadioButton to track selection changes
    private void setChoiceListeners() {
        choiceA.setOnAction(event -> updateSelectedAnswers("A", choiceA.isSelected()));
        choiceB.setOnAction(event -> updateSelectedAnswers("B", choiceB.isSelected()));
        choiceC.setOnAction(event -> updateSelectedAnswers("C", choiceC.isSelected()));
        choiceD.setOnAction(event -> updateSelectedAnswers("D", choiceD.isSelected()));
    }

    // Updates the selected answers map based on user selection
    private void updateSelectedAnswers(String option, boolean isSelected) {
        Long questionId = currQuestion.getId();
        selectedAnswers.putIfAbsent(questionId, new ArrayList<>());
        List<String> answers = selectedAnswers.get(questionId);

        if (isSelected) {
            if (!answers.contains(option)) answers.add(option);  // Add if not already selected
        } else {
            answers.remove(option);  // Remove if deselected
        }
    }

    // Loads previously selected answers for the current question
    private void loadSelectedAnswers() {
        Long questionId = currQuestion.getId();
        List<String> selected = selectedAnswers.getOrDefault(questionId, new ArrayList<>());
        choiceA.setSelected(selected.contains("A"));
        choiceB.setSelected(selected.contains("B"));
        choiceC.setSelected(selected.contains("C"));
        choiceD.setSelected(selected.contains("D"));
    }

    private void checkAnswers(){
        int score = 0;
        for(int i = 0; i < questionIds.length; ++i){
            Question ques = questionDatabase.queryByField("id", questionIds[i]).getFirst();
            List<String> choices = selectedAnswers.get(Long.parseLong(questionIds[i]));
            Collections.sort(choices);
            String ans = "";
            for(int j = 0; j < choices.size(); ++j){
                ans = ans.concat(choices.get(j));
            }
            if(ans.equals(ques.getAnswer())){
                score += Integer.parseInt(ques.getQuestionScore());
            }
        }
//        System.out.println(score);
        Grade tmp = new Grade(String.valueOf(curStudent.getId()), String.valueOf(currentQuiz.getId()), String.valueOf(score));
        gradeDatabase.add(tmp);
    }

}

