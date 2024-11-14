package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagerMainController implements Initializable {
    @FXML
    private VBox mainbox;

    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void openStudentManageUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentManagementUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("StudentManagementUI");
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
//        ((Stage) ((Button) eventSource).getScene().getWindow()).close();
    }

    @FXML
    public void openTeacherManageUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherManagementUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("TeacherManagementUI");
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
    }

    @FXML
    public void openCourseManageUI() {
    }

    @FXML
    public void exit() {
        System.exit(0);
    }
}
