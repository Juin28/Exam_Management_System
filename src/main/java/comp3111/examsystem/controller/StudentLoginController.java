package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.model.Student;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentLoginController implements Initializable {
    @FXML
    public TextField usernameTxt;
    @FXML
    public PasswordField passwordTxt;
    public Database<Student> studentDatabase;
    public List<Student> studentList;
    /**
     * Static variable to keep track of the currently logged-in student.
     */
    public static Student loggedInStudent;

    /**
     * Initializes the controller.
     *
     * @param location   The location used to resolve relative paths for the root object.
     * @param resources  The resources used to localize the root object.
     */
    public void initialize(URL location, ResourceBundle resources) {
        studentDatabase = new Database<>(Student.class);
    }

    /**
     * Handles the login action when the login button is clicked.
     *
     * @param e The ActionEvent corresponding to the login action.
     */
    @FXML
    public void login(ActionEvent e) {
        // list to store student with the given username
        studentList = studentDatabase.queryByField("username", usernameTxt.getText());
        // if the student does not exist
        if(studentList == null || studentList.isEmpty()){
            MsgSender.showMsg("This user does not exist. Please register for an account.");
        }
        // if the student exists
        else{
            // check if the entered password matches the password in the database
            // if passwords do not match
            if(!studentList.get(0).getPassword().equals(passwordTxt.getText())){
                MsgSender.showMsg("Incorrect password. Please re-enter password.");
            }
            // if passwords match
            else{
                System.out.println("hi");
                MsgSender.showMsg("Login Successful");
                loggedInStudent = studentList.getFirst();
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentMainUI.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Hi " + usernameTxt.getText() +", Welcome to HKUST Examination System");
                    stage.setScene(new Scene(fxmlLoader.load()));
                    stage.show();
                    if (e.getSource() instanceof Button) {
                        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                };
            }
        }
    }

    /**
     * Handles the registration action when the register button is clicked.
     *
     * @param e The ActionEvent corresponding to the registration action.
     */
    @FXML
    public void register(ActionEvent e) {
        Platform.runLater(() ->{
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
        });
    }
}
