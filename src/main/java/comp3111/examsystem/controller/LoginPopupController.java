package comp3111.examsystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LoginPopupController {
    @FXML
    private Label messageLabel;

    private ManagerLoginController managerLoginController;
    private boolean loginStatus;
    private Object eventSource;

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setManagerLoginController(ManagerLoginController managerLoginController, boolean loginStatus, Object eventSource) {
        this.managerLoginController = managerLoginController;
        this.loginStatus = loginStatus;
        this.eventSource = eventSource;
    }

    @FXML
    public void closePopup() {
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.close();
        if (loginStatus) {
            managerLoginController.showManagerUI(eventSource);
        }
    }
}