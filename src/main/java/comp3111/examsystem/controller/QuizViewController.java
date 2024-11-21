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

/**
 * Controller class for handling student interactions in the quiz view.
 */
public class QuizViewController implements Initializable {
    public Student curStudent;
    public Database<Question> questionDatabase;
    public Database<Grade> gradeDatabase;
    public DecimalFormat df;

    // variable to store number of questions
    public int totalNumQ;
    // variable to store current quiz
    public Quiz currentQuiz;
    // variable to store current question
    public Question currQuestion;
    // variable to store the current question index
    public int currentQuestionIndex;
    // variable to store the question description
    public String questionDesc;
    // array to store questionIds
    public String[] questionIds;
    // list to store all question descriptions
    public List<String> allQuestionDesc;
    // hashmap to store selected answers with id as key and list of answers as values
    public Map<Long, List<String>> selectedAnswers;

    public long startTime;
    public int numOfCorrect = 0;
    public int totalScore;
    public int score = 0;
    public Timer timer;

    @FXML
    public Label ansA;

    @FXML
    public Label ansB;

    @FXML
    public Label ansC;

    @FXML
    public Label ansD;

    @FXML
    public RadioButton choiceA;

    @FXML
    public RadioButton choiceB;

    @FXML
    public RadioButton choiceC;

    @FXML
    public Label currQNum;

    @FXML
    public RadioButton choiceD;

    @FXML
    public Label currQuiz;

    @FXML
    public Label numQ;

    @FXML
    public Label question;

    @FXML
    public ListView<String> questionList;

    @FXML
    public Label rTime;

    @FXML
    public Button submit;

    /**
     * Initializes the controller.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            questionDatabase = new Database<>(Question.class);
            gradeDatabase = new Database<>(Grade.class);
            allQuestionDesc = new ArrayList<>();
            selectedAnswers = new HashMap<>();
        }
        catch (Exception e){
            MsgSender.showMsg("Error fetching database");
            e.printStackTrace();
        }

        currentQuiz = StudentMainController.chosenQuiz;
        curStudent = StudentLoginController.loggedInStudent;
        startTime = System.currentTimeMillis();
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

    /**
     * Sets up the close request handler for the quiz window.
     */
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

    /**
     * Handles the submit button action.
     */
    @FXML
    public void handleSubmit() {
        checkAnswers();  // Check answers when submitting
        MsgSender.showMsg(numOfCorrect + "/" +
                totalNumQ + " Correct, the precision is " +
                df.format((numOfCorrect / (double) totalNumQ) * 100) + "%, the score is " +
                score + "/" + totalScore);
        Stage primaryStage = (Stage) submit.getScene().getWindow();
        primaryStage.close();  // Close the window after submitting
        timer.cancel();
    }

    /**
     * Handles the next button action.
     */
    @FXML
    public void handleNext() {
        try {
            if (currentQuestionIndex < allQuestionDesc.size() - 1) {
                currentQuestionIndex++;
                updateQuestionUI();
            } else {
                MsgSender.showMsg("This is the last question.");
            }
        } catch (Exception e) {
            MsgSender.showMsg("An error occurred while moving to the next question.");
            e.printStackTrace();
        }
    }

    /**
     * Updates the question details in the UI based on the current question index.
     */
    public void updateQuestionUI() {
        try {
            // Get the next question description
            questionDesc = allQuestionDesc.get(currentQuestionIndex);
            // Update the question details in the UI
            currQuestion = questionDatabase.queryByField("questionDescription", questionDesc).get(0);
            question.setText(questionDesc);
            currQNum.setText("Question " + (currentQuestionIndex + 1));

            // Set the answer descriptions
            setAnsDescriptions(currQuestion);
            // Load the selected answers for this question (if any)
            loadSelectedAnswers();
        } catch (Exception e) {
            MsgSender.showMsg("An error occurred while updating the question UI.");
            e.printStackTrace();
        }
    }

    /**
     * Shows the details of the selected question.
     * Initializes the choice listeners, sets the first question description,
     * and updates the UI with the question details.
     */
    public void showQuestionDet() {
        // Set listeners for choice selection
        setChoiceListeners();

        // Get the first question description and update the UI
        questionDesc = allQuestionDesc.getFirst();
        currQNum.setText("Question 1");
        question.setText(questionDesc);

        try {
            // Query the database for the first question and set its answer descriptions
            currQuestion = questionDatabase.queryByField("questionDescription", questionDesc).getFirst();
            setAnsDescriptions(currQuestion);

            // Add a listener to update the UI when a different question is selected from the list
            questionList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    // Update the question description and UI elements based on the selected question
                    questionDesc = questionList.getSelectionModel().getSelectedItem();
                    currQuestion = questionDatabase.queryByField("questionDescription", questionDesc).getFirst();
                    question.setText(questionDesc);
                    currQNum.setText("Question " + (allQuestionDesc.indexOf(questionDesc) + 1));
                    currentQuestionIndex = allQuestionDesc.indexOf(questionDesc);
                    setAnsDescriptions(currQuestion);
                    // Load selected answers for the current question, if available
                    loadSelectedAnswers();
                }
            });

            // Set up the close request handler for the quiz window
            Platform.runLater(this::setupCloseRequestHandler);
        } catch (Exception e) {
            // Show an error message if no questions are found and print the stack trace
            MsgSender.showMsg("Error: No questions found.");
            e.printStackTrace();
        }
    }

    /**
     * Sets the answer descriptions for the current question.
     *
     * @param question The question object containing answer descriptions.
     */
    public void setAnsDescriptions(Question question){
        ansA.setText(question.getOptionA());
        ansB.setText(question.getOptionB());
        ansC.setText(question.getOptionC());
        ansD.setText(question.getOptionD());
    }

    /**
     * Shows all questions in the sidebar for the current quiz.
     *
     * @param quiz The quiz object containing questions to display.
     */
    public void showQuestions(Quiz quiz){
        questionIds = StudentMainController.splitByPipe(quiz.getQuestionIDs());

        totalNumQ = questionIds.length;
        for(int i = 0; i < questionIds.length; ++i){
            Question q = questionDatabase.queryByField("id", questionIds[i]).get(0);
            allQuestionDesc.add(q.questionDescription);
        }
        questionList.getItems().addAll(allQuestionDesc);
    }

    /**
     * Sets the timer for the quiz.
     *
     * @param time The time duration for the quiz timer.
     */
    public void setTimer(String time){

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
                        MsgSender.showMsg("Time's up!");
                        handleSubmit();
                    });
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    /**
     * Adds listeners to each RadioButton to track selection changes.
     */
    private void setChoiceListeners() {
        choiceA.setOnAction(event -> updateSelectedAnswers("A", choiceA.isSelected()));
        choiceB.setOnAction(event -> updateSelectedAnswers("B", choiceB.isSelected()));
        choiceC.setOnAction(event -> updateSelectedAnswers("C", choiceC.isSelected()));
        choiceD.setOnAction(event -> updateSelectedAnswers("D", choiceD.isSelected()));
    }

    /**
     * Updates the selected answers map based on user selection.
     *
     * @param option The selected answer option.
     * @param isSelected A boolean indicating whether the option is selected.
     */
    public void updateSelectedAnswers(String option, boolean isSelected) {
        Long questionId = currQuestion.getId();
        selectedAnswers.putIfAbsent(questionId, new ArrayList<>());
        List<String> answers = selectedAnswers.get(questionId);

        if (isSelected) {
            if (!answers.contains(option)) answers.add(option);  // Add if not already selected
        } else {
            answers.remove(option);  // Remove if deselected
        }
    }

    /**
     * Loads previously selected answers for the current question.
     */
    public void loadSelectedAnswers() {
        Long questionId = currQuestion.getId();
        List<String> selected = selectedAnswers.getOrDefault(questionId, new ArrayList<>());
        choiceA.setSelected(selected.contains("A"));
        choiceB.setSelected(selected.contains("B"));
        choiceC.setSelected(selected.contains("C"));
        choiceD.setSelected(selected.contains("D"));
    }

    /**
     * Checks the answers for the quiz and calculates the score.
     */
    public void checkAnswers() {
        score = 0;
        numOfCorrect = 0;
        long endTime = System.currentTimeMillis();
        try {
            for (int i = 0; i < questionIds.length; ++i) {
                // Fetch the question from the database
                Question ques = questionDatabase.queryByField("id", questionIds[i]).getFirst();
                totalScore += Integer.parseInt(ques.getQuestionScore());
                List<String> choices = selectedAnswers.get(Long.parseLong(questionIds[i]));
                if (choices != null) {
                    Collections.sort(choices);
                    String ans = "";
                    for (int j = 0; j < choices.size(); ++j) {
                        ans = ans.concat(choices.get(j));
                    }
                    // Check if the selected answer matches the correct answer
                    if (ans.equals(ques.getAnswer())) {
                        score += Integer.parseInt(ques.getQuestionScore());
                        numOfCorrect += 1;
                    }
                }
            }
            // Calculate the time used for the quiz
            Long usedTime = ((endTime - startTime) / 1000) / 60;
            if (usedTime >= Long.parseLong(currentQuiz.getQuizTime())) {
                usedTime = Long.parseLong(currentQuiz.getQuizTime());
            }
            // Create a new Grade object and add it to the database
            Grade tmp = new Grade(
                    String.valueOf(curStudent.getId()),
                    String.valueOf(currentQuiz.getId()),
                    String.valueOf(score),
                    String.valueOf(usedTime));
            gradeDatabase.add(tmp);
        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            MsgSender.showMsg("An error occurred while checking answers.");
            e.printStackTrace();
        }
    }

}

