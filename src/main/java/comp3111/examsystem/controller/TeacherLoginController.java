//package comp3111.examsystem.controller;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.List;
//import java.util.ResourceBundle;
//
//import comp3111.examsystem.Main;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;
//
//public class TeacherLoginController implements Initializable {
//    @FXML
//    private TextField usernameTxt;
//    @FXML
//    private PasswordField passwordTxt;
//
//    public void initialize(URL location, ResourceBundle resources) {
//
//    }
//
//    @FXML
//    public void login(ActionEvent e) {
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherMainUI.fxml"));
//        Stage stage = new Stage();
//        stage.setTitle("Hi " + usernameTxt.getText() +", Welcome to HKUST Examination System");
//        try {
//            stage.setScene(new Scene(fxmlLoader.load()));
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        stage.show();
//        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
//    }
//
//    @FXML
//    public void register() {
//    }
//
//}

package comp3111.examsystem.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import comp3111.examsystem.Main;
import comp3111.examsystem.model.Teacher;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TeacherLoginController implements Initializable {
    private Database<Teacher> teacherDatabase;
    private List<Teacher> allTeachers;

    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

//    public void initialize(URL location, ResourceBundle resources) {
//    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        this.teacherDatabase = new Database<>(Teacher.class);
        allTeachers = teacherDatabase.getAll();
    }

    @FXML
    public void login(ActionEvent e) {
        // Check credentials
        boolean loginStatus = handleTeacherLogin();
        String message = loginStatus ? "Login successful" : "Login failed, please try again";

        // Use MsgSender to display the popup message
        MsgSender.showMsg(message);

        if (loginStatus) {
            // Load the Teacher's main UI if login is successful
            loadTeacherMainUI(e);
        }
    }

    private boolean handleTeacherLogin() {
        // Get the username and password from the text fields
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();

        if (validInput(username, password)) {
            return checkTeacherCredentials(username, password);
        } else {
            MsgSender.showMsg("Please enter both username and password.");
            return false;
        }
    }

    private boolean validInput(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    private boolean checkTeacherCredentials(String username, String password) {
        for (Teacher teacher : allTeachers) {
            if (teacher.getUsername().equals(username) && teacher.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private void loadTeacherMainUI(ActionEvent e) {
        // Load the Teacher's main UI
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherMainUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Hi " + usernameTxt.getText() + ", Welcome to HKUST Examination System");
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();

            // Close the current login window
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

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