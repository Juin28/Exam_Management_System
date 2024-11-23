package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Teacher Main UI in the HKUST Examination System.
 * Provides navigation options for managing the question bank, exams, and grade statistics.
 * Includes functionality to exit the application.
 */
public class TeacherMainController implements Initializable {
    @FXML
    public VBox mainbox;

    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Open the question bank management UI
     */
    @FXML
    public void openQuestionManageUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherQuestionBankManagementUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Question Bank Management");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the exam management UI
     */
    @FXML
    public void openExamManageUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherExamManagementUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Exam Management");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the exam statistics UI
     */
    @FXML
    public void openGradeStatistic() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherGradeStatistic.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Grade Statistics");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Exit the application
     */
    @FXML
    public void exit() {
        System.exit(0);
    }
}
