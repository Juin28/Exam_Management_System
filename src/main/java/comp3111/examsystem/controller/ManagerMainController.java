package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Manager Main UI.
 * Provides navigation options for managing students, teachers, and courses.
 * Includes an option to exit the application.
 */
public class ManagerMainController implements Initializable {

    /**
     * The main container for the Manager Main UI.
     */
    @FXML
    private VBox mainbox;

    /**
     * Initializes the Manager Main Controller.
     * This method is called after the FXML components are loaded.
     *
     * @param location  The location of the FXML file (not used).
     * @param resources The resources used to localize the FXML (not used).
     */
    public void initialize(URL location, ResourceBundle resources) {
        // No initialization required for this controller
    }


    /**
     * Opens the Student Management UI in a new window.
     * Loads the "StudentManagementUI.fxml" file and displays the scene in a new stage.
     */
    @FXML
    public void openStudentManageUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentManagementUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("StudentManagementUI");
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
    }

    /**
     * Opens the Teacher Management UI in a new window.
     * Loads the "TeacherManagementUI.fxml" file and displays the scene in a new stage.
     */
    @FXML
    public void openTeacherManageUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherManagementUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("TeacherManagementUI");
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
    }

    /**
     * Opens the Course Management UI in a new window.
     * Loads the "CourseManagementUI.fxml" file and displays the scene in a new stage.
     */
    @FXML
    public void openCourseManageUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CourseManagementUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("CourseManagementUI");
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
    }

    /**
     * Exits the application.
     * This method terminates the application immediately.
     */
    @FXML
    public void exit() {
        System.exit(0);
    }
}
