package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.service.MsgSender;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.model.Manager;

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

public class ManagerLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

    private Database<Manager> managerDatabase;
    private List<Manager> allManagers;

    public void initialize(URL location, ResourceBundle resources) {
        this.managerDatabase = new Database<>(Manager.class);
        if (managerDatabase.queryByField("username", "admin").isEmpty()) {
            // admin does not exist, create an admin and insert into the database
            Manager manager = new Manager();
            managerDatabase.add(manager);
        }
    }

    @FXML
    public void login(ActionEvent e) {
        // get the username and password
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();

        // call handleManagerLogin to validate the credentials
        boolean loginStatus = handleManagerLogin(username,password);
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

    public boolean handleManagerLogin(String username, String password)
    {
        // since there is only one manager, hardcode the username and password
        // to ""admin" and "admin" respectively
        for(Manager manager: managerDatabase.getAll()) {
            if (manager.getUsername().equals(username) && manager.getPassword().equals(password)) {
                System.out.println("Manager login successfully");
                return true;
            }
        }
        System.out.println("Manager login failed");
        return false;
    }

}
