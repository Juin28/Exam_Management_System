package comp3111.examsystem.controller;

import comp3111.examsystem.model.Teacher;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.List;

public class TeacherRegisterController {
    private Database<Teacher> teacherDatabase;
    private List<Teacher> allTeachers;

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
        this.teacherDatabase = new Database<>(Teacher.class);
        allTeachers = teacherDatabase.getAll();
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

        if (!validInputs(username, name, gender, age, position, department, password, confirmPassword)) {
            return;
        }

        try {
            int teacherId = allTeachers.size() + 1;
            Teacher newTeacher = new Teacher(username, name, gender, age, department, password, position, teacherId);

            // Store the teacher credentials in the database
            teacherDatabase.add(newTeacher);

            MsgSender.showMsg("Registration successful! You can now log in.");
            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            MsgSender.showMsg("An error occurred while registering the teacher.");
        }
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) usernameTxt.getScene().getWindow();
        stage.close();
    }

    private boolean validInputs(String username, String name, String gender, String ageStr, String position, String department, String password, String confirmPassword) {
        if (username.isEmpty() || name.isEmpty() || gender == null ||
                ageStr.isEmpty() || position == null || department.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()) {
            MsgSender.showMsg("All fields are required.");
            return false;
        }

        int age = 0;
        try {
            age = Integer.parseInt(ageStr);
            if (age <= 0) {
                MsgSender.showMsg("Age must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            MsgSender.showMsg("Age must be an integer.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            MsgSender.showMsg("Password does not match with Confirm Password.");
            return false;
        }

        List<Teacher> existingTeachers = teacherDatabase.queryByField("username", username);
        if (!existingTeachers.isEmpty()) {
            MsgSender.showMsg("Username already exists.");
            return false;
        }

        return true;
    }
}