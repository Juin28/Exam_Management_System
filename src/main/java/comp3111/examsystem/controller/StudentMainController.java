package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.model.Course;
import comp3111.examsystem.model.Grade;
import comp3111.examsystem.model.Quiz;
import comp3111.examsystem.model.Student;
import comp3111.examsystem.service.Database;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StudentMainController implements Initializable {
    @FXML
    ComboBox<String> examCombox;
    private Database<Quiz> quizDatabase;
    private Database<Grade> gradeDatabase;
    // Variable used to keep track of the loggedInStudent
    private Student student;
    private List<String> quizzes;
    private List<Grade> studentGrades;

    public void initialize(URL location, ResourceBundle resources) {
        // initializing the quizDatabase
        quizDatabase = new Database<>(Quiz.class);
        gradeDatabase = new Database<>(Grade.class);
        // test data for quizDatabase
//        Quiz tmpCourse = new Quiz("Quiz1", "30", "3", 0, "COMP3111", "Q1");
//        Quiz tmpCourse2 = new Quiz("Quiz3", "60", "5", 0, "COMP3511", "Q1");
//        quizDatabase.add(tmpCourse1);
//        quizDatabase.add(tmpCourse2);
//        Grade grade1 = new Grade("1731479984127", "1731501546532", "0");
//        Grade grade2 = new Grade("1731479984127", "1731502244132", "0");
//        gradeDatabase.add(grade1);
//        gradeDatabase.add(grade2);
        quizzes = new ArrayList<>();
        studentGrades = new ArrayList<>();
        student = StudentLoginController.loggedInStudent;

        for(int i = 0; i < gradeDatabase.getAll().size(); ++i){
            if(gradeDatabase.getAll().get(i).getStudentId().equals(Long.toString(student.getId()))){
                studentGrades.add(gradeDatabase.getAll().get(i));
            }
        }
        System.out.println(studentGrades);
        // formatting the quiz information
        for(int i = 0; i < quizDatabase.getAll().size(); ++i){
            Quiz quiz = quizDatabase.getAll().get(i);
            String quizId = Long.toString(quiz.getId());
            boolean taken = false;

            for(int j = 0; j < studentGrades.size(); ++j){
                if(studentGrades.get(j).getQuestionId().equals(quizId)){
                    System.out.println("this quiz has been taken");
                    taken = true;
                }
            }
            if (!taken){
                String tmp = quiz.getCourseId();
                tmp = tmp.concat(" | ");
                tmp = tmp.concat(quiz.getQuizName());
                // adding each quiz to the quizzes list
                this.quizzes.add(tmp);
            }
        }
        // dynamically adding quizzes to the examCombox
        examCombox.getItems().addAll(quizzes);
    }

    @FXML
    public void openExamUI() {
    }

    @FXML
    public void openGradeStatistic() {
    }

    @FXML
    public void exit() {
        System.exit(0);
    }
}
