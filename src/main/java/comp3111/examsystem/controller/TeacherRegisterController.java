//package comp3111.examsystem.controller;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.ResourceBundle;
//
//import comp3111.examsystem.Main;
//import comp3111.examsystem.service.MsgSender;
//import comp3111.examsystem.service.Database;
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
////public class TeacherRegisterController {
////    @FXML
////    private TextField usernameTxt;
////    @FXML
////    private PasswordField passwordTxt;
////    @FXML
////    private PasswordField confirmPasswordTxt;
////
////    @FXML
////    public void register() {
////        String username = usernameTxt.getText();
////        String password = passwordTxt.getText();
////        String confirmPassword = confirmPasswordTxt.getText();
////
////        // Validate input fields
////        if (validateFields(username, password, confirmPassword)) {
////            // Store the teacher credentials in the database
//////            Database.storeTeacherCredentials(username, password); // Adjust based on your database helper method
////            // Show success message or redirect to the login screen
////            MsgSender.showMsg("Registration successful! You can now log in.");
////            closeWindow();
////        }
////    }
////
////    private boolean validateFields(String username, String password, String confirmPassword) {
////        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
////            MsgSender.showMsg("All fields are required.");
////            return false;
////        }
////        if (!password.equals(confirmPassword)) {
////            MsgSender.showMsg("Passwords do not match.");
////            return false;
////        }
////        // Additional checks (e.g., username uniqueness) can be added here
////        return true;
////    }
////
////    private void closeWindow() {
////        // Close the registration window
////        Stage stage = (Stage) usernameTxt.getScene().getWindow();
////        stage.close();
////    }
////}
//
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.ChoiceBox;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;
//import comp3111.examsystem.service.Database;
//import comp3111.examsystem.service.MsgSender;
//
//public class TeacherRegisterController {
//    @FXML
//    private TextField usernameTxt;
//    @FXML
//    private TextField nameTxt;
//    @FXML
//    private ChoiceBox<String> genderChoice;
//    @FXML
//    private TextField ageTxt;
//    @FXML
//    private ChoiceBox<String> positionChoice;
//    @FXML
//    private TextField departmentTxt;
//    @FXML
//    private PasswordField passwordTxt;
//    @FXML
//    private PasswordField passwordConfirmTxt;
//    @FXML
//    private Button registerButton;
//    @FXML
//    private Button closeButton;
//
//    @FXML
//    public void initialize() {
//    }
//
//    @FXML
//    public void register() {
//        String username = usernameTxt.getText();
//        String name = nameTxt.getText();
//        String gender = genderChoice.getValue();
//        String age = ageTxt.getText();
//        String position = positionChoice.getValue();
//        String department = departmentTxt.getText();
//        String password = passwordTxt.getText();
//        String confirmPassword = passwordConfirmTxt.getText();
//
//        if (passValidateFields(username, name, gender, age, position, department, password, confirmPassword)) {
//            // [TODO] Store the teacher credentials in the database
////            Database.storeTeacherCredentials(username, name, gender, age, position, department, password);
//            MsgSender.showMsg("Registration successful! You can now log in.");
//            closeWindow();
//        }
//    }
//
//    @FXML
//    public void closeWindow() {
//        Stage stage = (Stage) usernameTxt.getScene().getWindow();
//        stage.close();
//    }
//
//    private boolean passValidateFields(String username, String name, String gender, String age, String position, String department, String password, String confirmPassword) {
//
//        if (username.isEmpty() || name.isEmpty() || gender == null || age.isEmpty() || position == null || department.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
//            MsgSender.showMsg("All fields are required.");
//            return false;
//        } else if (!age.matches("\\d+")) {
//            MsgSender.showMsg("Age must be a number.");
//            return false;
//        } else if (Integer.parseInt(age) > 0) {
//            MsgSender.showMsg("Age cannot be negative.");
//            return false;
//        } else if (!password.equals(confirmPassword)) {
//            MsgSender.showMsg("Passwords do not match.");
//            return false;
//        } else if (Database.queryByField("username", username) != null) {
//            MsgSender.showMsg("Username already exists.");
//            return false;
//        }
//        // Add any other validation checks as needed
//        return true;
//    }
//}

package comp3111.examsystem.controller;

import comp3111.examsystem.model.Teacher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;

public class TeacherRegisterController {
    private Database<Teacher> database;

    @FXML
    private TextField usernameTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private ChoiceBox<String> genderChoice;
    @FXML
    private TextField ageTxt;
    @FXML
    private ChoiceBox<String> positionChoice;
    @FXML
    private TextField departmentTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private PasswordField passwordConfirmTxt;
    @FXML
    private Button registerButton;
    @FXML
    private Button closeButton;

    @FXML
    public void initialize() {
        // Initialize any necessary components or data here
    }

    @FXML
    public void register() {
        String username = usernameTxt.getText();
        String name = nameTxt.getText();
        String gender = genderChoice.getValue();
        String age = ageTxt.getText();
        String position = positionChoice.getValue();
        String department = departmentTxt.getText();
        String password = passwordTxt.getText();
        String confirmPassword = passwordConfirmTxt.getText();

        if (passValidateFields(username, name, gender, age, position, department, password, confirmPassword)) {
            // Store the teacher credentials in the database
            // Database.storeTeacherCredentials(username, name, gender, age, position, department, password);
            MsgSender.showMsg("Registration successful! You can now log in.");
            closeWindow();
        }
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) usernameTxt.getScene().getWindow();
        stage.close();
    }

    private boolean passValidateFields(String username, String name, String gender, String age, String position, String department, String password, String confirmPassword) {
        if (username.isEmpty() || name.isEmpty() || gender == null || age.isEmpty() || position == null || department.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            MsgSender.showMsg("All fields are required.");
            return false;
        } else if (!age.matches("\\d+")) {
            MsgSender.showMsg("Age must be a number.");
            return false;
        } else if (Integer.parseInt(age) <= 0) {
            MsgSender.showMsg("Age must be a positive number.");
            return false;
        } else if (!password.equals(confirmPassword)) {
            MsgSender.showMsg("Passwords do not match.");
            return false;
        }
//        else if (Database.queryByField("username", username) != null) {
//            MsgSender.showMsg("Username already exists.");
//            return false;
//        }
        // Add any other validation checks as needed
        return true;
    }
}