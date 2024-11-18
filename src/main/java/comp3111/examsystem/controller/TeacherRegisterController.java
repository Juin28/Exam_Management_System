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

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Teacher's Registration page
 */
public class TeacherRegisterController {
    public Database<Teacher> teacherDatabase;
    public List<Teacher> allTeachers = new ArrayList<>();

    @FXML
    public TextField usernameTxt;
    @FXML
    public TextField nameTxt;
    @FXML
    public ChoiceBox<String> genderChoice;
    @FXML
    public TextField ageTxt;
    @FXML
    public ChoiceBox<String> positionChoice;
    @FXML
    public TextField departmentTxt;
    @FXML
    public PasswordField passwordTxt;
    @FXML
    public PasswordField passwordConfirmTxt;
    @FXML
    public Button registerButton;
    @FXML
    public Button closeButton;

    /**
     * Initialize the controller
     */
    @FXML
    public void initialize() {
        this.teacherDatabase = new Database<>(Teacher.class);
        allTeachers = teacherDatabase.getAll();
    }

    /**
     * Handle the registration button action
     */
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

        boolean registrationStatus = handleTeacherRegistration(username, name, gender, age, position, department, password, confirmPassword);
        String message = registrationStatus ? "Registration successful! You can now log in." : "An error occurred while registering the teacher.";

        if (registrationStatus) {
            MsgSender.showMsg(message);
            closeWindow();
        } else {
            MsgSender.showMsg(message);
        }
    }

    /**
     * Handle the registration process
     * @param username the username of the teacher
     * @param name the name of the teacher
     * @param gender the gender of the teacher
     * @param age the age of the teacher
     * @param position the position of the teacher
     * @param department the department of the teacher
     * @param password the password of the teacher
     * @param confirmPassword the confirm password of the teacher
     * @return true if registration is successful, false otherwise
     */
    public boolean handleTeacherRegistration(String username, String name, String gender, String age, String position, String department, String password, String confirmPassword) {
        if (validInputs(username, name, gender, age, position, department, password, confirmPassword)) {
            return storeTeacherCredentials(username, name, gender, age, position, department, password);
        } else {
            return false;
        }
    }

    /**
     * Store the teacher credentials in the database
     * @param username the username of the teacher
     * @param name the name of the teacher
     * @param gender the gender of the teacher
     * @param age the age of the teacher
     * @param position the position of the teacher
     * @param department the department of the teacher
     * @param password the password of the teacher
     * @return true if the credentials are stored successfully, false otherwise
     */
    public boolean storeTeacherCredentials(String username, String name, String gender, String age, String position, String department, String password) {
        try {
            int teacherId = allTeachers.size() + 1;
            Teacher newTeacher = new Teacher(username, name, gender, age, department, password, position, teacherId);

            // Store the teacher credentials in the database
            teacherDatabase.add(newTeacher);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Close the registration window
     */
    @FXML
    public void closeWindow() {
        Stage stage = (Stage) usernameTxt.getScene().getWindow();
        stage.close();
    }

    /**
     * Validate the inputs
     * @param username the username of the teacher
     * @param name the name of the teacher
     * @param gender the gender of the teacher
     * @param ageStr the age of the teacher
     * @param position the position of the teacher
     * @param department the department of the teacher
     * @param password the password of the teacher
     * @param confirmPassword the confirm password of the teacher
     * @return true if the inputs are valid, false otherwise
     */
    public boolean validInputs(String username, String name, String gender, String ageStr, String position, String department, String password, String confirmPassword) {
        // Check if any of the fields are empty
        if (username.isEmpty() || name.isEmpty() || gender == null ||
                ageStr.isEmpty() || position == null || department.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()) {
            MsgSender.showMsg("Please fill in all the fields.");
            return false;
        }

        // Check if the age is a valid number
        int age = 0;
        try {
            age = Integer.parseInt(ageStr);
            // Check if the age is a positive number
            if (age <= 0) {
                MsgSender.showMsg("The age must be a positive number. Please try again.");
                return false;
            }
        } catch (NumberFormatException e) {
            MsgSender.showMsg("The age must be a valid number. Please try again.");
            return false;
        }

        // Check if the password and confirm password match
        if (!password.equals(confirmPassword)) {
            MsgSender.showMsg("The passwords do not match. Please try again.");
            return false;
        }

        // Check if the username already exists
        List<Teacher> existingTeachers = teacherDatabase.queryByField("username", username);
        if (!existingTeachers.isEmpty()) {
            MsgSender.showMsg("The username already exists. Please choose a different username.");
            return false;
        }

        return true;
    }
}