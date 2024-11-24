package comp3111.examsystem.controller;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.*;

import comp3111.examsystem.service.JavaFXInitializer;
import comp3111.examsystem.service.MsgSender;
import comp3111.examsystem.model.Teacher;
import comp3111.examsystem.service.Database;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class TeacherRegisterControllerTest {

    @Mock
    private TextField usernameTxt;
    @Mock
    private TextField nameTxt;
    @Mock
    private ChoiceBox<String> genderChoice;
    @Mock
    private TextField ageTxt;
    @Mock
    private ChoiceBox<String> positionChoice;
    @Mock
    private TextField departmentTxt;
    @Mock
    private PasswordField passwordTxt;
    @Mock
    private PasswordField passwordConfirmTxt;
    @Mock
    private Database<Teacher> mockDatabase;
    @Mock
    private Scene mockScene;
    @Mock
    private Stage mockStage;

    private TeacherRegisterController controller;

    @BeforeAll
    static void initToolkit() {
        JavaFXInitializer.initToolkit();
    }

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/TeacherRegisterUI.fxml"));
        Parent root = loader.load();

        controller = new TeacherRegisterController();
        controller.usernameTxt = usernameTxt;
        controller.nameTxt = nameTxt;
        controller.genderChoice = genderChoice;
        controller.ageTxt = ageTxt;
        controller.positionChoice = positionChoice;
        controller.departmentTxt = departmentTxt;
        controller.passwordTxt = passwordTxt;
        controller.passwordConfirmTxt = passwordConfirmTxt;
        controller.teacherDatabase = mockDatabase;
        controller.allTeachers = new ArrayList<>();

        when(usernameTxt.getScene()).thenReturn(mockScene);
        when(mockScene.getWindow()).thenReturn(mockStage);
    }

    @Test
    void testRegisterValid() {
        when(usernameTxt.getText()).thenReturn("teacher1");
        when(nameTxt.getText()).thenReturn("Teacher One");
        when(genderChoice.getValue()).thenReturn("Male");
        when(ageTxt.getText()).thenReturn("30");
        when(positionChoice.getValue()).thenReturn("Professor");
        when(departmentTxt.getText()).thenReturn("CSE");
        when(passwordTxt.getText()).thenReturn("password");
        when(passwordConfirmTxt.getText()).thenReturn("password");
        when(mockDatabase.queryByField("username", "teacher1")).thenReturn(new ArrayList<>());

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.register();
            mockedMsgSender.verify(() -> MsgSender.showMsg("Registration successful! You can now log in."));
        }
    }

    @Test
    void testRegisterInvalid() {
        when(usernameTxt.getText()).thenReturn("");
        when(nameTxt.getText()).thenReturn("Teacher One");
        when(genderChoice.getValue()).thenReturn("Male");
        when(ageTxt.getText()).thenReturn("30");
        when(positionChoice.getValue()).thenReturn("Professor");
        when(departmentTxt.getText()).thenReturn("CSE");
        when(passwordTxt.getText()).thenReturn("password");
        when(passwordConfirmTxt.getText()).thenReturn("password");

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.register();
            mockedMsgSender.verify(() -> MsgSender.showMsg("An error occurred while registering the teacher."));
        }
    }

    @Test
    void testHandleTeacherRegistrationValid() {
        when(mockDatabase.queryByField("username", "teacher1")).thenReturn(new ArrayList<>());

        boolean result = controller.handleTeacherRegistration("teacher1", "Teacher One", "Male", "30", "Professor", "CSE", "password", "password");
        assertTrue(result);
        verify(mockDatabase, times(1)).add(any(Teacher.class));
    }

    @Test
    void testHandleTeacherRegistrationInvalid() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.handleTeacherRegistration("", "Teacher One", "Male", "30", "Professor", "CSE", "password", "password");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please fill in all the fields."));
        }
    }

    @Test
    void testValidInputsEmptyFields() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("", "Teacher One", "Male", "30", "Professor", "CSE", "password", "password");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please fill in all the fields."));
        }
    }

    @Test
    void testValidInputsInvalidAge() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("teacher1", "Teacher One", "Male", "abc", "Professor", "CSE", "password", "password");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("The age must be a valid number. Please try again."));
        }
    }

    @Test
    void testValidInputsNegativeAge() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("teacher1", "Teacher One", "Male", "-1", "Professor", "CSE", "password", "password");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("The age must be a positive number. Please try again."));
        }
    }

    @Test
    void testValidInputsPasswordMismatch() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("teacher1", "Teacher One", "Male", "30", "Professor", "CSE", "password", "differentPassword");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("The passwords do not match. Please try again."));
        }
    }

    @Test
    void testValidInputsDuplicateUsername() {
        List<Teacher> existingTeachers = new ArrayList<>();
        existingTeachers.add(new Teacher("teacher1", "Teacher One", "Male", "30", "CSE", "password", "Professor", 1));
        when(mockDatabase.queryByField("username", "teacher1")).thenReturn(existingTeachers);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("teacher1", "Teacher One", "Male", "30", "Professor", "CSE", "password", "password");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("The username already exists. Please choose a different username."));
        }
    }

    @Test
    void testValidInputsAllValid() {
        when(mockDatabase.queryByField("username", "teacher1")).thenReturn(new ArrayList<>());

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("teacher1", "Teacher One", "Male", "30", "Professor", "CSE", "password", "password");
            assertTrue(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg(anyString()), never());
        }
    }

    @Test
    void testStoreTeacherCredentialsError() {
        Teacher sampleTeacher = new Teacher("teacher1", "Teacher One", "Male", "30", "CSE", "password", "Professor", 1);

        doThrow(new RuntimeException()).when(mockDatabase).add(any(Teacher.class));

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.storeTeacherCredentials("teacher1", "Teacher One", "Male", "30", "Professor", "CSE", "password");
            assertFalse(result);
        }
    }

    @Test
    void testCloseWindow() {
        controller.closeWindow();
        verify(mockStage, times(1)).close();
    }

    @Test
    void testInitialize() {
        controller.initialize();
        assertNotNull(controller.teacherDatabase);
        assertNotNull(controller.allTeachers);
    }
}