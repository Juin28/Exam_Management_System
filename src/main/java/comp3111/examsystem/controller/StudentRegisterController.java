package comp3111.examsystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentRegisterController implements Initializable {

    @FXML
    private TextField ageTxt;

    @FXML
    private Button closeButton;

    @FXML
    private TextField departmentTxt;

    @FXML
    private ChoiceBox<String> genderChoice;

    @FXML
    private TextField nameTxt;

    @FXML
    private PasswordField passwordConfirmTxt;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private Button registerButton;

    @FXML
    private TextField usernameTxt;

    @FXML
    void closeWindow(ActionEvent event) {

    }

    @FXML
    void register(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
