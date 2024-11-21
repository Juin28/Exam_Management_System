package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.model.Course;
import comp3111.examsystem.model.Grade;
import comp3111.examsystem.model.Quiz;
import comp3111.examsystem.model.Student;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class for handling student interactions in the main student interface.
 */
public class StudentMainController implements Initializable {
    @FXML
    ComboBox<String> examCombox;
    public Database<Quiz> quizDatabase;
    public Database<Grade> gradeDatabase;
    // Variable used to keep track of the loggedInStudent
    public Student student;
    public List<String> quizzes;
    public List<Grade> studentGrades;
    public static Quiz chosenQuiz;

    /**
     * Initializes the controller.
     *
     * @param url   The location used to resolve relative paths for the root object.
     * @param resourceBundle  The resources used to localize the root object.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initializing the quizDatabase
        quizDatabase = new Database<>(Quiz.class);
        gradeDatabase = new Database<>(Grade.class);
        quizzes = new ArrayList<>();
        studentGrades = new ArrayList<>();
        student = StudentLoginController.getLoggedInStudent();
        loadStudentGrades();
        checkQuizValidity();
        // dynamically adding quizzes to the examCombox
        examCombox.getItems().addAll(quizzes);
    }

    /**
     * Loads the student's grades.
     */
    public void loadStudentGrades(){
        // getting the student's grades
        for(int i = 0; i < gradeDatabase.getAll().size(); ++i){
            String gradeId = gradeDatabase.getAll().get(i).getStudentId();
            if(gradeId.equals(Long.toString(student.getId()))){
                studentGrades.add(gradeDatabase.getAll().get(i));
            }
        }
    }

    /**
     * Checks the validity of the quizzes.
     */
    public void checkQuizValidity(){
        // formatting the quiz information
        for(Quiz quiz : quizDatabase.getAll()){
            String quizId = Long.toString(quiz.getId());
            String quizPublished = quiz.getPublishStatus();
            if (quizPublished.equalsIgnoreCase("yes")){
                quizPublished = "true";
            }
            else{
                quizPublished = "false";
            }
            boolean taken = false;

            // filtering out quizzes that have already been taken
            for (Grade studentGrade : studentGrades) {
                if (studentGrade.getQuestionId().equals(quizId)) {
                    taken = true;
                    break;
                }
            }
            formatQuizInfo(taken, Boolean.parseBoolean(quizPublished), quiz);
        }
    }

    /**
     * Formats the quiz information to be displayed in the examCombox.
     *
     * @param taken     A boolean indicating whether the quiz has been taken.
     * @param published A boolean indicating whether the quiz has been published.
     * @param quiz      The quiz to be formatted.
     */
    public void formatQuizInfo(Boolean taken, Boolean published, Quiz quiz){
        if(taken || !published){
            return;
        }
        String tmp = quiz.getCourseID();
        tmp = tmp.concat(" | ");
        tmp = tmp.concat(quiz.getQuizName());
        // adding each quiz to the quizzes list
        this.quizzes.add(tmp);
    }

    /**
     * Handles the action when the "Start Exam" button is clicked.
     *
     * @param e The ActionEvent corresponding to the "Start Exam" button click.
     */
    @FXML
    public void openExamUI(ActionEvent e) {
        if(examCombox.getValue() == null){
            MsgSender.showMsg("Please select a quiz to start.");
        }
        else{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("QuizViewUI.fxml"));
            Stage stage = new Stage();
            // Remove spaces and split the by "|"
            String[] parts = formatString(examCombox.getValue());
            try {
                List<Quiz> listOfQuizzes = quizDatabase.queryByField("courseID", parts[0]);
                for(int i = 0; i < listOfQuizzes.size(); ++i){
                    if(listOfQuizzes.get(i).getQuizName().replace(" ", "").equals(parts[1])){
                        chosenQuiz = listOfQuizzes.get(i);
                    }
                }
                stage.setTitle("Start Exam");
                try {
                    stage.setScene(new Scene(fxmlLoader.load()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                stage.show();
                ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
            } catch (Exception e1) {
                MsgSender.showMsg("Error: No Quizzes not found.");
                e1.printStackTrace();
            }
//            List<Quiz> listOfQuizzes = quizDatabase.queryByField("courseID", parts[0]);
            // Setting the chosenQuiz
//            for(int i = 0; i < listOfQuizzes.size(); ++i){
//                if(listOfQuizzes.get(i).getQuizName().replace(" ", "").equals(parts[1])){
//                    chosenQuiz = listOfQuizzes.get(i);
//                }
//            }
//            stage.setTitle("Start Exam");
//            try {
//                stage.setScene(new Scene(fxmlLoader.load()));
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            stage.show();
//            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        }
    }

    /**
     * Handles the action when the "View Grades" button is clicked.
     *
     * @param e The ActionEvent corresponding to the "View Grades" button click.
     */
    @FXML
    public void openGradeStatistic(ActionEvent e) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentGradeStatisticUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Student Grade Statistics");
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }


    /**
     * Handles the action of exiting the application.
     */
    @FXML
    public void exit() {
        System.exit(0);
    }

    // Helper function to remove whitespaces and split by "|"
    public static String[] formatString(String input) {
        if (input == null) {
            return null; // Handle null case
        }
        return splitByPipe(input.replace(" ", ""));
    }

    // Helper function to split a String by "|"
    public static String[] splitByPipe(String input) {
//        if (input == null) {
//            return new String[0]; // Return an empty array if input is null
//        }
        return input.split("\\|"); // Split by "|" character
    }
}
