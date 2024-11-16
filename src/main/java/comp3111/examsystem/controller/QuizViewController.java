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
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class QuizViewController implements Initializable {
    private Student curStudent;
    private Database<Question> questionDatabase;
    private Database<Grade> gradeDatabase;
    private DecimalFormat df;

    // variable to store number of questions
    private int totalNumQ;
    // variable to store current quiz
    private Quiz currentQuiz;
    // variable to store current question
    private Question currQuestion;
    // variable to store the current question index
    private int currentQuestionIndex;
    // variable to store the question description
    private String questionDesc;
    // array to store questionIds
    private String[] questionIds;
    // list to store all question descriptions
    private List<String> allQuestionDesc;
    // hashmap to store selected answers with id as key and list of answers as values
    private Map<Long, List<String>> selectedAnswers;


    private int numOfCorrect = 0;
    private int totalScore;
    private int score = 0;
    private Timer timer;

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

    @FXML
    private Button submit;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionDatabase = new Database<>(Question.class);
        gradeDatabase = new Database<>(Grade.class);
        allQuestionDesc = new ArrayList<>();
        selectedAnswers = new HashMap<>();

        currentQuiz = StudentMainController.chosenQuiz;
        curStudent = StudentLoginController.loggedInStudent;
        timer = new Timer();
        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        showQuestions(currentQuiz);
        numQ.setText(String.valueOf(totalNumQ));
        currQuiz.setText(currentQuiz.getCourseID() + " " + currentQuiz.getQuizName());

        setTimer(currentQuiz.getQuizTime());
        showQuestionDet();
        Platform.runLater(this::setupCloseRequestHandler);
    }

    public void setupCloseRequestHandler() {
        Stage primaryStage = (Stage) currQuiz.getScene().getWindow();
        primaryStage.setOnCloseRequest(event -> {
            event.consume(); // Prevent the window from closing immediately
            MsgSender.showConfirm(
                    "Confirm Submit",
                    "Are you sure you want to submit the quiz and exit?",
                    () -> {
                        checkAnswers();
                        MsgSender.showMsg(numOfCorrect + "/" + totalNumQ +
                                " Correct, the precision is " +
                                df.format((numOfCorrect/totalNumQ)*100) +
                                "%, the score is " + score + "/" + totalScore);
                        timer.cancel();
                        primaryStage.close(); // Close the window after confirmation
                    });
        });
    }

    @FXML
    private void handleSubmit() {
        checkAnswers();  // Check answers when submitting
        MsgSender.showMsg(numOfCorrect + "/" +
                totalNumQ + " Correct, the precision is " +
                df.format((numOfCorrect / (double) totalNumQ) * 100) + "%, the score is " +
                score + "/" + totalScore);
        Stage primaryStage = (Stage) submit.getScene().getWindow();
        primaryStage.close();  // Close the window after submitting
        timer.cancel();
    }

    @FXML
    private void handleNext() {
        if (currentQuestionIndex < allQuestionDesc.size() - 1) {
            currentQuestionIndex++;
            updateQuestionUI();
        } else {
            MsgSender.showMsg("This is the last question.");
        }
    }

    private void updateQuestionUI() {
        // Get the next question description
        questionDesc = allQuestionDesc.get(currentQuestionIndex);

        // Update the question details in the UI
        currQuestion = questionDatabase.queryByField("questionDescription", questionDesc).getFirst();
        question.setText(questionDesc);
        currQNum.setText("Question " + (currentQuestionIndex + 1));

        // Set the answer descriptions
        setAnsDescriptions(currQuestion);

        // Load the selected answers for this question (if any)
        loadSelectedAnswers();
    }

    // Helper function to show question details
    private void showQuestionDet() {
        setChoiceListeners();
        questionDesc = allQuestionDesc.getFirst();
        currQNum.setText("Question 1");
        question.setText(questionDesc);
        currQuestion = questionDatabase.queryByField("questionDescription", questionDesc).getFirst();
        setAnsDescriptions(currQuestion);
        questionList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                questionDesc = questionList.getSelectionModel().getSelectedItem();
                currQuestion = questionDatabase.queryByField("questionDescription", questionDesc).getFirst();
                question.setText(questionDesc);
                currQNum.setText("Question " + (allQuestionDesc.indexOf(questionDesc)+1));
                currentQuestionIndex = allQuestionDesc.indexOf(questionDesc);
                setAnsDescriptions(currQuestion);
                // Load selected answer for this question, if available
                loadSelectedAnswers();
//                setChoiceListeners();
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

    // Helper function to show allQuestionDesc in the sidebar
    private void showQuestions(Quiz quiz){
        questionIds = StudentMainController.splitByPipe(quiz.getQuestionIDs());
        totalNumQ = questionIds.length;
        for(int i = 0; i < questionIds.length; ++i){
            Question q = questionDatabase.queryByField("id", questionIds[i]).getFirst();
            allQuestionDesc.add(q.questionDescription);
        }
        questionList.getItems().addAll(allQuestionDesc);
    }

    // Helper function to set the timer
    private void setTimer(String time){
//        Timer timer = new Timer();
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
                        handleSubmit();
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
        score = 0;
        numOfCorrect = 0;
        for(int i = 0; i < questionIds.length; ++i){
            Question ques = questionDatabase.queryByField("id", questionIds[i]).getFirst();
            totalScore += Integer.parseInt(ques.getQuestionScore());
            List<String> choices = selectedAnswers.get(Long.parseLong(questionIds[i]));
            if(choices != null){
                Collections.sort(choices);
                String ans = "";
                for(int j = 0; j < choices.size(); ++j){
                    ans = ans.concat(choices.get(j));
                }
                if(ans.equals(ques.getAnswer())){
                    score += Integer.parseInt(ques.getQuestionScore());
                    numOfCorrect += 1;
                }
            }

        }
        Grade tmp = new Grade(String.valueOf(curStudent.getId()), String.valueOf(currentQuiz.getId()), String.valueOf(score));
        gradeDatabase.add(tmp);
    }

}

