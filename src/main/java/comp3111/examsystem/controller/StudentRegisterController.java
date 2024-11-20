package comp3111.examsystem.controller;

import comp3111.examsystem.model.Student;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentRegisterController implements Initializable {
    public Database<Student> studentDatabase;
    public Integer studentNum;

    @FXML
    public TextField ageTxt;

    @FXML
    public Button closeButton;

    @FXML
    public TextField departmentTxt;

    @FXML
    public ChoiceBox<String> genderChoice;

    @FXML
    public TextField nameTxt;

    @FXML
    public PasswordField passwordConfirmTxt;

    @FXML
    public PasswordField passwordTxt;

    @FXML
    public TextField usernameTxt;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.studentDatabase = new Database<>(Student.class);
    }

    @FXML
    void closeWindow() {
        Stage stage = (Stage) usernameTxt.getScene().getWindow();
        stage.close();
    }

    @FXML
    void register(ActionEvent event) {
        String age = ageTxt.getText();
        String department = departmentTxt.getText();
        String gender = genderChoice.getValue();
        String name = nameTxt.getText();
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();
        String passConfirm = passwordConfirmTxt.getText();
        if(validateFields(age, department, gender, name, username, password, passConfirm)){
            Student tmp = new Student(username, name, gender, age, department, password, "0", 0);
            studentDatabase.add(tmp);
            MsgSender.showMsg("Registration Successful! You can now log in.");
            closeWindow();
        }
    }

    private boolean validateFields(String age, String department, String gender, String name, String username, String password, String passConfirm){
        if(age.isEmpty() || department.isEmpty() || gender.isEmpty() || name.isEmpty() || username.isEmpty() || password.isEmpty() || passConfirm.isEmpty()){
            MsgSender.showMsg("All fields should be filled.");
            return false;
        }
        else if (!age.matches("\\d+")) {
            MsgSender.showMsg("Age must be a number.");
            return false;
        }
        else if (Integer.parseInt(age) < 10 || Integer.parseInt(age) > 100) {
            MsgSender.showMsg("Must be a valid age.");
            return false;
        }
        else if(!password.equals(passConfirm)){
            MsgSender.showMsg("Passwords do not match.");
            return false;
        }
        else if(!studentDatabase.queryByField("username", username).isEmpty()){
            MsgSender.showMsg("This student already exists.");
            return false;
        }
        return true;
    }

}
