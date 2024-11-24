package comp3111.examsystem.controller;

import java.io.IOException;

import comp3111.examsystem.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller for managing login selection in the HKUST Examination System.
 * Provides functionality to navigate to the login interfaces for students, teachers, and managers.
 */
public class SelectLoginController {

    /**
     * Opens the Student Login UI in a new window.
     * Loads the "StudentLoginUI.fxml" file and displays it in a new stage.
     * Handles exceptions by printing stack traces.
     */
    @FXML
    public void studentLogin() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentLoginUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Student Login");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Teacher Login UI in a new window.
     * Loads the "TeacherLoginUI.fxml" file and displays it in a new stage.
     * Handles exceptions by printing stack traces.
     */
    @FXML
    public void teacherLogin() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherLoginUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Teacher Login");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Manager Login UI in a new window.
     * Loads the "ManagerLoginUI.fxml" file and displays it in a new stage.
     * Handles exceptions by printing stack traces.
     */
    public void managerLogin() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ManagerLoginUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Manager Login");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
