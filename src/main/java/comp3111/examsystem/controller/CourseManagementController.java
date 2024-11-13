package comp3111.examsystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class CourseManagementController {

    @FXML
    private TableColumn<?, ?> ageCol;

    @FXML
    private TextField ageInput;

    @FXML
    private TextField departmentFilter;

    @FXML
    private TextField departmentInput;

    @FXML
    private TableColumn<?, ?> deptCol;

    @FXML
    private Button filter;

    @FXML
    private TableColumn<?, ?> genderCol;

    @FXML
    private ComboBox<?> genderInput;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TextField nameFilter;

    @FXML
    private TableColumn<?, ?> passwordCol;

    @FXML
    private TextField passwordInput;

    @FXML
    private Button reset;

    @FXML
    private TableColumn<?, ?> usernameCol;

    @FXML
    private TextField usernameFilter;

    @FXML
    private TextField usernameInput;

}
