package comp3111.examsystem.controller;

import static org.mockito.Mockito.*;

import comp3111.examsystem.model.*;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


class StudentRegisterControllerTest {
    @Mock
    private TextField usernameTxt;
    @Mock
    private PasswordField passwordTxt;
    @Mock
    private PasswordField passwordConfirmTxt;
    @Mock
    private PasswordField ageTxt;
    @Mock
    private TextField departmentTxt;
    @Mock
    private TextField nameTxt;
    @Mock
    private ChoiceBox<String> genderChoice;
    @Mock
    private Database<Student> mockDatabase;
//    @Mock
//    private Integer studentNum;
    @Mock
    private Scene mockScene;
    @Mock
    private Stage mockStage;
    @Mock
    private MsgSender msgSender;
    @Mock
    private List<Student> mockList;

    private StudentRegisterController controller;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {
        });
    }

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        controller = new StudentRegisterController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/StudentRegisterUI.fxml"));
        Parent root = loader.load();

        controller = new StudentRegisterController();
        controller.usernameTxt = usernameTxt;
        controller.passwordTxt = passwordTxt;
        controller.passwordConfirmTxt = passwordConfirmTxt;
        controller.studentDatabase = mockDatabase;
        controller.genderChoice = genderChoice;
        controller.nameTxt = nameTxt;
        controller.ageTxt = ageTxt;
        controller.departmentTxt = departmentTxt;
        when(usernameTxt.getScene()).thenReturn(mockScene);
        when(mockScene.getWindow()).thenReturn(mockStage);
    }

    @Test
    void testRegisterValid() {
        when(usernameTxt.getText()).thenReturn("Student1");
        when(nameTxt.getText()).thenReturn("Student One");
        when(genderChoice.getValue()).thenReturn("Male");
        when(ageTxt.getText()).thenReturn("18");
        when(departmentTxt.getText()).thenReturn("CSE");
        when(passwordTxt.getText()).thenReturn("password");
        when(passwordConfirmTxt.getText()).thenReturn("password");
        when(controller.studentDatabase.queryByField("username", usernameTxt.getText())).thenReturn(new ArrayList<>());

        Platform.runLater(() ->{
            try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
                controller.register(new ActionEvent());
                mockedMsgSender.verify(() -> MsgSender.showMsg("Registration Successful! You can now log in."));
            }});
    }

    @Test
    void testRegisterInvalidUsername() {
        Student sampleStudent = new Student("Student1", "Student One", "Male", "18", "CSE", "password", "0", 0);
        when(usernameTxt.getText()).thenReturn("Student1");
        when(nameTxt.getText()).thenReturn("Student One");
        when(genderChoice.getValue()).thenReturn("Male");
        when(ageTxt.getText()).thenReturn("18");
        when(departmentTxt.getText()).thenReturn("CSE");
        when(passwordTxt.getText()).thenReturn("password");
        when(passwordConfirmTxt.getText()).thenReturn("password");
        when(controller.studentDatabase.getAll()).thenReturn(List.of(sampleStudent));
        when(controller.studentDatabase.queryByField("username", usernameTxt.getText())).thenReturn(List.of(sampleStudent));
        Platform.runLater(() ->{
            try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
                controller.register(new ActionEvent());
                mockedMsgSender.verify(() -> MsgSender.showMsg("This student already exists."));
            }
        });
    }

    @Test
    void testEmptyField() {
        Student sampleStudent = new Student("Student1", "Student One", "Male", "18", "CSE", "password", "0", 0);
        when(usernameTxt.getText()).thenReturn("");
        when(nameTxt.getText()).thenReturn("Student One");
        when(genderChoice.getValue()).thenReturn("Male");
        when(ageTxt.getText()).thenReturn("18");
        when(departmentTxt.getText()).thenReturn("CSE");
        when(passwordTxt.getText()).thenReturn("password");
        when(passwordConfirmTxt.getText()).thenReturn("password");
        when(controller.studentDatabase.queryByField("username", usernameTxt.getText())).thenReturn(List.of(sampleStudent));
        Platform.runLater(() ->{
            try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
                controller.register(new ActionEvent());
                mockedMsgSender.verify(() -> MsgSender.showMsg("All fields should be filled."));
            }
        });
    }

    @Test
    void testAgeNumber() {
        when(usernameTxt.getText()).thenReturn("Student1");
        when(nameTxt.getText()).thenReturn("Student One");
        when(genderChoice.getValue()).thenReturn("Male");
        when(ageTxt.getText()).thenReturn("abc");
        when(departmentTxt.getText()).thenReturn("CSE");
        when(passwordTxt.getText()).thenReturn("password");
        when(passwordConfirmTxt.getText()).thenReturn("password");
        Platform.runLater(() ->{
            try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
                controller.register(new ActionEvent());
                mockedMsgSender.verify(() -> MsgSender.showMsg("Age must be a number."));
            }
        });
    }

    @Test
    void testAgeLowInvalid() {
        when(usernameTxt.getText()).thenReturn("Student1");
        when(nameTxt.getText()).thenReturn("Student One");
        when(genderChoice.getValue()).thenReturn("Male");
        when(ageTxt.getText()).thenReturn("9");
        when(departmentTxt.getText()).thenReturn("CSE");
        when(passwordTxt.getText()).thenReturn("password");
        when(passwordConfirmTxt.getText()).thenReturn("password");
        Platform.runLater(() ->{
            try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
                controller.register(new ActionEvent());
                mockedMsgSender.verify(() -> MsgSender.showMsg("Must be a valid age."));
            }
        });
    }

    @Test
    void testAgeHighInvalid() {
        when(usernameTxt.getText()).thenReturn("Student1");
        when(nameTxt.getText()).thenReturn("Student One");
        when(genderChoice.getValue()).thenReturn("Male");
        when(ageTxt.getText()).thenReturn("101");
        when(departmentTxt.getText()).thenReturn("CSE");
        when(passwordTxt.getText()).thenReturn("password");
        when(passwordConfirmTxt.getText()).thenReturn("password");
        Platform.runLater(() ->{
            try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
                controller.register(new ActionEvent());
                mockedMsgSender.verify(() -> MsgSender.showMsg("Must be a valid age."));
            }
        });
    }

    @Test
    void testPasswordConfirm() throws InterruptedException {
        when(usernameTxt.getText()).thenReturn("Student1");
        when(nameTxt.getText()).thenReturn("Student One");
        when(genderChoice.getValue()).thenReturn("Male");
        when(ageTxt.getText()).thenReturn("18");
        when(departmentTxt.getText()).thenReturn("CSE");
        when(passwordTxt.getText()).thenReturn("password");
        when(passwordConfirmTxt.getText()).thenReturn("wrongpassword");
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() ->{
            try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
                controller.register(new ActionEvent());
                mockedMsgSender.verify(() -> MsgSender.showMsg("Passwords do not match."));
            } finally {
                latch.countDown();
            }
        });
        latch.await();
    }
}