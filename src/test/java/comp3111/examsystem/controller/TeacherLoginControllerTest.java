package comp3111.examsystem.controller;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.*;

import comp3111.examsystem.service.MsgSender;
import comp3111.examsystem.model.Teacher;
import comp3111.examsystem.service.Database;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.List;

class TeacherLoginControllerTest {

    @Mock
    private TextField usernameTxt;
    @Mock
    private PasswordField passwordTxt;
    @Mock
    private Database<Teacher> mockDatabase;
    @Mock
    private Scene mockScene;
    @Mock
    private Stage mockStage;

    private TeacherLoginController controller;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new TeacherLoginController();
        controller.usernameTxt = usernameTxt;
        controller.passwordTxt = passwordTxt;
        controller.teacherDatabase = mockDatabase;
        controller.allTeachers = new ArrayList<>();

        when(usernameTxt.getScene()).thenReturn(mockScene);
        when(mockScene.getWindow()).thenReturn(mockStage);
    }

    @Test
    void testLoginValid() {
        when(usernameTxt.getText()).thenReturn("Teacher1");
        when(passwordTxt.getText()).thenReturn("123");

        Teacher sampleTeacher = new Teacher("Teacher1", "Teacher1", "Male", "20", "Department", "123", "Position", 123);
        when(mockDatabase.getAll()).thenReturn(List.of(sampleTeacher));

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.login(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("Login successful"));
        }
    }

    @Test
    void testLoginInvalid() {
        when(usernameTxt.getText()).thenReturn("Teacher1");
        when(passwordTxt.getText()).thenReturn("wrongpassword");

        Teacher sampleTeacher = new Teacher("Teacher1", "Teacher1", "Male", "20", "Department", "123", "Position", 123);
        when(mockDatabase.getAll()).thenReturn(List.of(sampleTeacher));

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.login(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("Login failed, please try again"));
        }
    }

    @Test
    void checkTeacherCredentialsValidCredentials() {
        Teacher sampleTeacher = new Teacher("Teacher1", "Teacher1", "Male", "20", "Department", "123", "Position", 123);
        when(mockDatabase.getAll()).thenReturn(List.of(sampleTeacher));

        boolean result = controller.checkTeacherCredentials("Teacher1", "123");
        assertTrue(result);
    }

    @Test
    void checkTeacherCredentialsInvalidCredentials() {
        Teacher sampleTeacher = new Teacher("Teacher1", "Teacher1", "Male", "20", "Department", "123", "Position", 123);
        when(mockDatabase.getAll()).thenReturn(List.of(sampleTeacher));

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.checkTeacherCredentials("Teacher1", "wrongpassword");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Invalid username or password"));
        }
    }

    @Test
    void testHandleTeacherLoginValid() {
        boolean result = controller.handleTeacherLogin("Teacher1", "123");
        assertTrue(result);
    }

    @Test
    void testHandleTeacherLoginInvalidCredentials() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.handleTeacherLogin("Teacher1", "wrongpassword");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Invalid username or password"));
        }
    }

    @Test
    void testHandleTeacherLoginInvalidInput() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.handleTeacherLogin("", "wrongpassword");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please fill in both fields"));
        }
    }

    @Test
    void testValidInput() {
        assertTrue(controller.validInput("teacher1", "password"));
    }

    @Test
    void TestInvalidInput() {
        assertFalse(controller.validInput("", "password"));
        assertFalse(controller.validInput("teacher1", ""));
        assertFalse(controller.validInput("", ""));
    }

    @Test
    void testCheckTeacherCredentialsValid() {
        boolean result = controller.checkTeacherCredentials("Teacher1", "123");
        assertTrue(result);
    }

    @Test
    void testCheckTeacherCredentialsInvalid() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.checkTeacherCredentials("Teacher1", "wrongpassword");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Invalid username or password"));
        }
    }
}