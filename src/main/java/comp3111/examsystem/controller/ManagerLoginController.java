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

/**
 * Controller for the Manager Login interface.
 * Handles the logic for manager authentication and transitions to the Manager UI.
 */
public class ManagerLoginController implements Initializable {
    /**
     * TextField for the manager's username input.
     */
    @FXML
    public TextField usernameTxt;

    /**
     * PasswordField for the manager's password input.
     */
    @FXML
    public PasswordField passwordTxt;

    /**
     * Database for managing the Manager entities.
     */
    private Database<Manager> managerDatabase = null;

    /**
     * List containing all managers retrieved from the database.
     */
    private List<Manager> allManagers;

    /**
     * FXMLLoader for loading the Manager Main UI.
     */
    public FXMLLoader fxmlLoader;

    /**
     * Stage for displaying the Manager Main UI.
     */
    public Stage stage;

    /**
     * Initializes the Manager Login Controller.
     * Checks if an admin manager exists in the database, and creates one if not.
     *
     * @param location  The location of the FXML file (not used).
     * @param resources The resources used to localize the FXML (not used).
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // if the manager database is empty, create an admin manager
        if (this.managerDatabase == null)
        {
            this.managerDatabase = new Database<>(Manager.class);
        }
        List<Manager> managers = managerDatabase.queryByField("username","admin");
        for (Manager manager: managers) {
            System.out.println(manager.getUsername());
        }
        if (this.managerDatabase.queryByField("username", "admin").isEmpty()) {
            // admin does not exist, create an admin and insert into the database
            Manager manager = new Manager();
            this.managerDatabase.add(manager);
        }
    }

    /**
     * Handles the manager login action.
     * Validates the username and password entered by the manager.
     * Displays a success or failure message and transitions to the Manager UI on successful login.
     *
     * @param e The ActionEvent triggered by the login button.
     * @return true if the login is successful, false otherwise.
     */
    @FXML
    public boolean login(ActionEvent e) {
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
            return true;
        }
        return false;
    }

    /**
     * Displays the Manager Main UI after a successful login.
     *
     * @param eventSource The source of the event (usually the login button).
     */
    public void showManagerUI(Object eventSource)
    {
        if (fxmlLoader == null)
        {
            fxmlLoader = new FXMLLoader(Main.class.getResource("ManagerMainUI.fxml"));
        }
        if (stage == null)
        {
            stage = new Stage();
        }
        stage.setTitle("Hi " + usernameTxt.getText() + ", Welcome to HKUST Examination System");
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
        ((Stage) ((Button) eventSource).getScene().getWindow()).close();
    }

    /**
     * Validates the manager's login credentials.
     * Checks the provided username and password against the database records.
     *
     * @param username The username entered by the manager.
     * @param password The password entered by the manager.
     * @return true if the credentials are valid, false otherwise.
     */
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

