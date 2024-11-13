package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
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
import comp3111.examsystem.service.MsgSender;

public class ManagerLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void login(ActionEvent e) {
        boolean loginStatus = handleManagerLogin();
        String message = loginStatus ? "Login successful" : "Login failed, please try again";

        // Display popup for login status
        MsgSender.showMsg(message);
        // if the login status is successful, show the managerUI
        if (loginStatus) {
            showManagerUI(e.getSource());
        }
    }

    public void showManagerUI(Object eventSource)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ManagerMainUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Hi " + usernameTxt.getText() + ", Welcome to HKUST Examination System");
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
        ((Stage) ((Button) eventSource).getScene().getWindow()).close();
    }

    public boolean handleManagerLogin()
    {
        // get the username and password
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();

        // since there is only one manager, hardcode the username and password
        // to ""admin" and "admin" respectively
        if (username.equals("admin") && password.equals("admin")) {
            System.out.println("Manager login successfully");
            return true;
        } else {
            System.out.println("Manager login failed");
            return false;
        }
    }

}
