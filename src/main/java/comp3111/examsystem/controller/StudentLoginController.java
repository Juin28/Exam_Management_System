package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.model.Student;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;
    private Database<Student> studentDatabase;
    // variable to keep track of the loggedInStudent
    public static Student loggedInStudent;

    public void initialize(URL location, ResourceBundle resources) {
        studentDatabase = new Database<>(Student.class);
    }

    @FXML
    public void login(ActionEvent e) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentMainUI.fxml"));
        Stage stage = new Stage();
        // list to store student with the given username
        List<Student> studentList = studentDatabase.queryByField("username", usernameTxt.getText());
        // if the student does not exist
        if(studentList == null || studentList.isEmpty()){
            MsgSender.showMsg("This user does not exist. Please register for an account.");
        }
        // if the student exists
        else{
            // check if the entered password matches the password in the database
            // if passwords do not match
            if(!studentList.getFirst().getPassword().equals(passwordTxt.getText())){
                MsgSender.showMsg("Incorrect password. Please re-enter password.");
            }
            // if passwords match
            else{
                loggedInStudent = studentList.getFirst();
                stage.setTitle("Hi " + usernameTxt.getText() +", Welcome to HKUST Examination System");
                try {
                    stage.setScene(new Scene(fxmlLoader.load()));
                    stage.show();
                    ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void register(ActionEvent e) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentRegisterUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Student Register");
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }
}
