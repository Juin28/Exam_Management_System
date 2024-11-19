package comp3111.examsystem.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import comp3111.examsystem.Main;
import comp3111.examsystem.model.Teacher;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Controller for the Teacher's Login page
 */
public class TeacherLoginController implements Initializable {
    public Database<Teacher> teacherDatabase;
    public List<Teacher> allTeachers;

    @FXML
    public TextField usernameTxt;
    @FXML
    public PasswordField passwordTxt;

    /**
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        this.teacherDatabase = new Database<>(Teacher.class);
        allTeachers = teacherDatabase.getAll();
    }

    /**
     * Handle the login button action
     * @param e the ActionEvent
     */
    @FXML
    public void login(ActionEvent e) {
        // Get the username and password from the text fields
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();

        // Check credentials
        boolean loginStatus = handleTeacherLogin(username, password);
        String message = loginStatus ? "Login successful" : "Login failed, please try again";

        // Use MsgSender to display the popup message
        MsgSender.showMsg(message);

        if (loginStatus) {
            // Load the Teacher's main UI if login is successful
            loadTeacherMainUI(e);
        }
    }

    /**
     * Handle the login process
     * @param username the username of the teacher
     * @param password the password of the teacher
     * @return true if login is successful, false otherwise
     */
    public boolean handleTeacherLogin(String username, String password) {
        if (validInput(username, password)) {
            return checkTeacherCredentials(username, password);
        } else {
            MsgSender.showMsg("Please fill in both fields");
            return false;
        }
    }

    /**
     * Check if the input is valid
     * @param username the username of the teacher
     * @param password the password of the teacher
     * @return true if both fields are not empty, false otherwise
     */
    public boolean validInput(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    /**
     * Check if the teacher's credentials are correct
     * @param username the username of the teacher
     * @param password the password of the teacher
     * @return true if the credentials are correct, false otherwise
     */
    public boolean checkTeacherCredentials(String username, String password) {
        this.teacherDatabase = new Database<>(Teacher.class);
        allTeachers = teacherDatabase.getAll();

        for (Teacher teacher : allTeachers) {
            if (teacher.getUsername().equals(username) && teacher.getPassword().equals(password)) {
                return true;
            }
        }
        MsgSender.showMsg("Invalid username or password");
        return false;
    }

    /**
     * Load the Teacher's main UI
     * @param e the ActionEvent
     */
    private void loadTeacherMainUI(ActionEvent e) {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherMainUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Hi " + usernameTxt.getText() + ", Welcome to HKUST Examination System");
            try {
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.show();

                ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    /**
     * Handle the register button action
     * @param e the ActionEvent
     */
    @FXML
    public void register(ActionEvent e) {
        // Load the Teacher's registration UI
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherRegisterUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Teacher Registration");

        try {
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();

            // Close the current login window
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();

        } catch (IOException err) {
            err.printStackTrace();
        }
    }
}