package comp3111.examsystem.controller;

import static org.mockito.Mockito.*;

import comp3111.examsystem.model.*;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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


class StudentLoginControllerTest {
    @Mock
    private TextField usernameTxt;
    @Mock
    private PasswordField passwordTxt;
    @Mock
    private Database<Student> mockDatabase;
    @Mock
    private Scene mockScene;
    @Mock
    private Stage mockStage;
    @Mock
    private MsgSender msgSender;
    @Mock
    private List<Student> mockList;

    private StudentLoginController controller;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        controller = new StudentLoginController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/StudentLoginUI.fxml"));
        Parent root = loader.load();

        controller = new StudentLoginController();
        controller.usernameTxt = usernameTxt;
        controller.passwordTxt = passwordTxt;
        controller.studentDatabase = mockDatabase;
        controller.studentList = new ArrayList<>();
        when(usernameTxt.getScene()).thenReturn(mockScene);
        when(mockScene.getWindow()).thenReturn(mockStage);
    }

    @Test
    void testLoginValid() {
        Student sampleStudent = new Student("bob", "bob", "Male", "20", "cse", "123", "0", 123);
        when(usernameTxt.getText()).thenReturn("bob");
        when(passwordTxt.getText()).thenReturn("123");
        when(mockDatabase.queryByField("username", usernameTxt.getText())).thenReturn(List.of(sampleStudent));
        Platform.runLater(() ->{
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.login(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("Login Successful"));
        }});
    }

    @Test
    void testUserDoesNotExist() {
        when(usernameTxt.getText()).thenReturn("bob");
        when(passwordTxt.getText()).thenReturn("123");
        when(mockDatabase.queryByField("username", usernameTxt.getText())).thenReturn(null);
        Platform.runLater(() ->{
            try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
                controller.login(new ActionEvent());
                mockedMsgSender.verify(() -> MsgSender.showMsg("This user does not exist. Please register for an account."));
            }
        });
    }

    @Test
    void testIncorrectPassword() throws InterruptedException {
        // Arrange
        Student sampleStudent = new Student("bob", "bob", "Male", "20", "cse", "123", "0", 123);
        when(usernameTxt.getText()).thenReturn("bob");
        when(passwordTxt.getText()).thenReturn("wrongpassword");
        when(mockDatabase.queryByField("username", "bob")).thenReturn(List.of(sampleStudent)); // Mock the database response

        // Act
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
                controller.login(new ActionEvent()); // Perform the login action

                // Assert
                mockedMsgSender.verify(() -> MsgSender.showMsg("Incorrect password. Please re-enter password."));
                latch.countDown();
            }
        });
        latch.await(); // Wait for the Platform.runLater to complete
    }


}