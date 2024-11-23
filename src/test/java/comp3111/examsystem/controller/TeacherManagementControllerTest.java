package comp3111.examsystem.controller;
import comp3111.examsystem.model.Teacher;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.JavaFXInitializer;
import comp3111.examsystem.service.MsgSender;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.jupiter.api.*;
import org.mockito.*;
import java.io.IOException;
import java.util.List;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import org.testfx.util.WaitForAsyncUtils;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherManagementControllerTest {

    @InjectMocks
    private TeacherManagementController controller;

    @Mock
    private Database<Teacher> mockDatabase;

    @Mock
    private TableView<Teacher> mockTeachTable;

    @Mock
    private TableColumn<Teacher, String> teachUsernameCol, teachNameCol, teachAgeCol, teachGenderCol, teachDeptCol, teachPasswordCol, teachPositionCol;

    @Mock
    private TextField teachUsernameInput, teachNameInput, teachAgeInput, teachDeptInput, teachPasswordInput, teachUsernameFilter, teachNameFilter, teachDeptFilter;

    @Mock
    private ComboBox<String> teachGenderInput, teachPosInput;

    @Mock
    private Button teachAdd, teachDelete, teachFilter, teachRefresh, teachResetFilter, teachUpdate;

    @Mock
    private AnchorPane rootPane, AnchorWithInputs;
    @BeforeAll
    public static void initJavaFx() throws InterruptedException {
        JavaFXInitializer.initToolkit();
    }


    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/TeacherManagementUI.fxml"));
        Parent root = loader.load();

        controller.teachGenderInput = new ComboBox<>();
        controller.teachPosInput = new ComboBox<>();
        controller.teachTable = new TableView<>();
        controller.teachUsernameCol = new TableColumn<>();
        controller.teachNameCol = new TableColumn<>();
        controller.teachAgeCol = new TableColumn<>();
        controller.teachGenderCol = new TableColumn<>();
        controller.teachDeptCol = new TableColumn<>();
        controller.teachPasswordCol = new TableColumn<>();
        controller.teachPositionCol = new TableColumn<>();
        controller.teachNameInput = new TextField();
        controller.teachUsernameInput = new TextField();
        controller.teachDeptInput = new TextField();
        controller.teachAgeInput = new TextField();
        controller.teachPasswordInput = new TextField();
        controller.teachPosInput = new ComboBox<>();
        controller.teachGenderInput = new ComboBox<>();
        controller.rootPane = new AnchorPane();
        controller.AnchorWithInputs = new AnchorPane();
        controller.teachDeptFilter = new TextField();
        controller.teachNameFilter = new TextField();
        controller.teachUsernameFilter = new TextField();
        controller.teacherDatabase = mockDatabase;
        controller.allTeachers = new ArrayList<>();
        controller.teacherList = FXCollections.observableArrayList();
    }


    @Test
    void testNoFilterTrue()
    {
        controller.teachNameFilter.setText("");
        controller.teachUsernameFilter.setText("");
        controller.teachDeptFilter.setText("");
        boolean expected = true;
        boolean actual = controller.noFilter();
        assertEquals(expected, actual);
    }

    @Test
    void testNoFilterFalse()
    {
        controller.teachNameFilter.setText("name");
        controller.teachUsernameFilter.setText("username");
        controller.teachDeptFilter.setText("CSE");
        boolean expected = false;
        boolean actual = controller.noFilter();
        assertEquals(expected, actual);
    }

    @Test
    void testLoadTeacherTableNoFilter()
    {
        // set up no filter
        controller.teachNameFilter.setText("");
        controller.teachUsernameFilter.setText("");
        controller.teachDeptFilter.setText("");

        // set up mock database
        Teacher teacher1 = new Teacher("John123", "John", "Male", "21", "CSE", "password", "Professor",0);
        Teacher teacher2 = new Teacher("Amy123", "Amy", "Female", "21", "SUST", "password", "Professor",0);
        Teacher teacher3 = new Teacher("Cindy123", "Cindy", "Female", "21", "CSE", "password", "Professor",0);

        List<Teacher> allTeachers = List.of(teacher1, teacher2, teacher3);
        when(mockDatabase.getAll()).thenReturn(allTeachers);

        // load teacher table
        controller.loadTeacherTable();

        // verify that the table is loaded correctly, where getAll is called
        verify(mockDatabase).getAll();

    }

    @Test
    void testLoadTeacherTableWithFilter()
    {
        // set up the filter
        controller.teachNameFilter.setText("John");
        controller.teachUsernameFilter.setText("");
        controller.teachDeptFilter.setText("");

        // set up mock database
        Teacher teacher1 = new Teacher("John123", "John", "Male", "21", "CSE", "password", "Professor",0);
        Teacher teacher2 = new Teacher("Amy123", "Amy", "Female", "21", "SUST", "password", "Professor",0);
        Teacher teacher3 = new Teacher("Cindy123", "Cindy", "Female", "21", "CSE", "password", "Professor",0);


        List<Teacher> allTeachers = List.of(teacher1, teacher2, teacher3);
        when(mockDatabase.getAll()).thenReturn(allTeachers);

        // call the method
        List<Teacher> filteredTeachers = controller.loadTeacherTable();

        // Verify that filteredTeachers is not null after the function call, with a size of 1
        assertNotNull(filteredTeachers);
        assertThat(filteredTeachers.size()).isEqualTo(1);

    }


    @Test
    void resetFilter() {
        // test that the reset filter function works
        controller.teachNameFilter.setText("John");
        controller.teachUsernameFilter.setText("John123");
        controller.teachDeptFilter.setText("CSE");

        // assert that these fields are not null
        assertNotNull(controller.teachNameFilter);
        assertNotNull(controller.teachUsernameFilter);
        assertNotNull(controller.teachDeptFilter);

        // next, call the resetFilter function
        ActionEvent actionEvent = mock(ActionEvent.class);
        controller.resetFilter(actionEvent);

        // assert that the fields are now empty
        assertEquals("", controller.teachNameFilter.getText());
        assertEquals("", controller.teachUsernameFilter.getText());
        assertEquals("", controller.teachDeptFilter.getText());

    }

    @Test
    void filterTeacher() {
        // check that filterTeacher works as expected
        ActionEvent actionEvent = mock(ActionEvent.class);

        // call the function
        controller.filterTeacher(actionEvent);

    }


    @Test
    void refreshTeacher() {
        // set up the fields
        controller.teachNameInput.setText("John");
        controller.teachUsernameInput.setText("John123");
        controller.teachDeptInput.setText("CSE");
        controller.teachAgeInput.setText("21");
        controller.teachPasswordInput.setText("password");
        controller.teachGenderInput.setValue("Male");
        controller.teachPosInput.setValue("Professor");
        controller.teachNameFilter.setText("John");
        controller.teachUsernameFilter.setText("John123");
        controller.teachDeptFilter.setText("CSE");

        // assert not null
        assertNotNull(controller.teachNameInput);
        assertNotNull(controller.teachUsernameInput);
        assertNotNull(controller.teachDeptInput);
        assertNotNull(controller.teachAgeInput);
        assertNotNull(controller.teachPasswordInput);
        assertNotNull(controller.teachGenderInput);
        assertNotNull(controller.teachPosInput);
        assertNotNull(controller.teachNameFilter);
        assertNotNull(controller.teachUsernameFilter);
        assertNotNull(controller.teachDeptFilter);

        // refresh the teacher
        ActionEvent actionEvent = mock(ActionEvent.class);
        controller.refreshTeacher(actionEvent);

        // assert that the fields are now empty
        assertEquals("", controller.teachNameInput.getText());
        assertEquals("", controller.teachUsernameInput.getText());
        assertEquals("", controller.teachDeptInput.getText());
        assertEquals("", controller.teachAgeInput.getText());
        assertEquals("", controller.teachPasswordInput.getText());
        assertNull(controller.teachGenderInput.getValue());
        assertNull(controller.teachPosInput.getValue());
        assertEquals("", controller.teachNameFilter.getText());
        assertEquals("", controller.teachUsernameFilter.getText());
        assertEquals("", controller.teachDeptFilter.getText());
    }

    @Test
    void testClearFields()
    {
        // set up the fields
        controller.teachNameInput.setText("John");
        controller.teachUsernameInput.setText("John123");
        controller.teachDeptInput.setText("CSE");
        controller.teachAgeInput.setText("21");
        controller.teachPasswordInput.setText("password");
        controller.teachGenderInput.setValue("Male");
        controller.teachPosInput.setValue("Professor");

        // assert not null
        assertNotNull(controller.teachNameInput);
        assertNotNull(controller.teachUsernameInput);
        assertNotNull(controller.teachDeptInput);
        assertNotNull(controller.teachAgeInput);
        assertNotNull(controller.teachPasswordInput);
        assertNotNull(controller.teachGenderInput);
        assertNotNull(controller.teachPosInput);

        // clear fields
        controller.clearFields();

        // assert that the fields are now empty
        assertEquals("", controller.teachNameInput.getText());
        assertEquals("", controller.teachUsernameInput.getText());
        assertEquals("", controller.teachDeptInput.getText());
        assertEquals("", controller.teachAgeInput.getText());
        assertEquals("", controller.teachPasswordInput.getText());
        assertNull(controller.teachGenderInput.getValue());
        assertNull(controller.teachPosInput.getValue());

    }

    @Test
    void testDeleteTeacherFromDatabase_Success()
    {
        Teacher teacher = new Teacher();
        // convert teacherID to string
        String teacherID = String.valueOf(teacher.getId());
        doNothing().when(mockDatabase).delByKey(anyString());

        Platform.runLater(()->{
            controller.deleteTeacherFromDatabase(teacher);

            // verify that the default username was passed into the delByKey function
            verify(mockDatabase).delByKey(teacherID);
        });

        WaitForAsyncUtils.waitForFxEvents();

    }

    @Test
    void testDeleteTeacherFromDatabase_Failed()
    {
        Teacher teacher = new Teacher();
        // convert teacherID to string
        String teacherID = String.valueOf(teacher.getId());

        doThrow(new RuntimeException()).when(mockDatabase).delByKey(anyString());

        Platform.runLater(() -> {
            assertThrows(RuntimeException.class, () -> controller.deleteTeacherFromDatabase(teacher));
        });
        WaitForAsyncUtils.waitForFxEvents(); // Wait for JavaFX operations to complete.

    }

    @Test
    void testValidateUsername_existingUsername()
    {
        // set up the mock database
        // set up mock database
        Teacher teacher1 = new Teacher("John123", "John", "Male", "21", "CSE", "password", "Professor",0);
        Teacher teacher2 = new Teacher("Amy123", "Amy", "Female", "21", "SUST", "password", "Professor",0);
        Teacher teacher3 = new Teacher("Cindy123", "Cindy", "Female", "21", "CSE", "password", "Professor",0);

        List<Teacher> allTeachers = List.of(teacher1, teacher2, teacher3);
        controller.allTeachers = allTeachers;

        // set up teacher with an existing Username
        Teacher teacher = new Teacher("John123", "Jimmy", "Male", "21", "CSE", "password", "Professor",0);
        boolean expected = false;
        Platform.runLater(()-> {
            boolean actual = controller.validateUsername(teacher.getUsername());
            assertEquals(expected, actual);
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testValidateUsername_nonAlphanumeric()
    {
        // set up the mock database
        Teacher teacher1 = new Teacher("John123", "John", "Male", "21", "CSE", "password", "Professor",0);
        Teacher teacher2 = new Teacher("Amy123", "Amy", "Female", "21", "SUST", "password", "Professor",0);
        Teacher teacher3 = new Teacher("Cindy123", "Cindy", "Female", "21", "CSE", "password", "Professor",0);

        List<Teacher> allTeachers = List.of(teacher1, teacher2, teacher3);
        controller.allTeachers = allTeachers;

        // set up teacher with a non-alphanumeric Username
        String nonAlphanumeric = "@#%&*!";
        boolean expected = false;
        Platform.runLater(()-> {
            boolean actual = controller.validateUsername(nonAlphanumeric);
            assertEquals(expected, actual);
        });
        WaitForAsyncUtils.waitForFxEvents();

    }

    @Test
    void testUsernameExists()
    {
        Teacher teacher = new Teacher();
        when(mockDatabase.getAll()).thenReturn(List.of(teacher));

        boolean expected = false;

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean actual = controller.validateUsername(teacher.getUsername());
            assertEquals(expected, actual);
            // verify that the message sender sends the correct message
            mockedMsgSender.verify(() -> MsgSender.showMsg("Username already exists"));

        }

    }


    @Test
    void testValidUsername()
    {
        // set up the mock database
        Teacher teacher1 = new Teacher("John123", "John", "Male", "21", "CSE", "password", "Professor",0);
        Teacher teacher2 = new Teacher("Amy123", "Amy", "Female", "21", "SUST", "password", "Professor",0);
        Teacher teacher3 = new Teacher("Cindy123", "Cindy", "Female", "21", "CSE", "password", "Professor",0);

        List<Teacher> allTeachers = List.of(teacher1, teacher2, teacher3);
        controller.allTeachers = allTeachers;

        // unique username
        String uniqueUsername = "uniqueUsername";
        boolean expected = true;
        Platform.runLater(()-> {
            boolean actual = controller.validateUsername(uniqueUsername);
            assertEquals(expected, actual);
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testValidateAge_correct()
    {
        // valid age
        String validAge = "21";
        boolean expected = true;
        Platform.runLater(()-> {
            boolean actual = controller.validateAge(validAge);
            assertEquals(expected, actual);
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testValidateAge_lowerBound()
    {
        // invalid age
        String invalidAge = "19";
        boolean expected = false;
        Platform.runLater(()-> {
            boolean actual = controller.validateAge(invalidAge);
            assertEquals(expected, actual);
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testValidateAge_upperBound()
    {
        // invalid age
        String invalidAge = "101";
        boolean expected = false;
        Platform.runLater(()-> {
            boolean actual = controller.validateAge(invalidAge);
            assertEquals(expected, actual);
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testValidateAge_notANumber()
    {
        // invalid age
        String invalidAge = "notANumber";
        boolean expected = false;
        Platform.runLater(()-> {
            boolean actual = controller.validateAge(invalidAge);
            assertEquals(expected, actual);
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testUpdateTeacherInDatabase_correct()
    {
        Teacher newTeacher = new Teacher();
        doNothing().when(mockDatabase).update(Mockito.any(Teacher.class));

        Platform.runLater(()-> {
            controller.updateTeacherInDatabase(newTeacher);
            verify(mockDatabase).update(newTeacher);
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testUpdateTeacherInDatabase_throwsException()
    {
        Teacher newTeacher = new Teacher();
        doThrow(new RuntimeException()).when(mockDatabase).update(Mockito.any(Teacher.class));

        Platform.runLater(()-> {
            assertThrows(RuntimeException.class, () -> controller.updateTeacherInDatabase(newTeacher));
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void addTeacherToDatabase_success()
    {
        // set up the input fields
        controller.teachNameInput.setText("John");
        controller.teachUsernameInput.setText("John123");
        controller.teachDeptInput.setText("CSE");
        controller.teachAgeInput.setText("21");
        controller.teachPasswordInput.setText("password");
        controller.teachGenderInput.setValue("Male");
        controller.teachPosInput.setValue("Professor");

        doNothing().when(mockDatabase).add(Mockito.any(Teacher.class));

        Platform.runLater(()-> {
            controller.addTeacherToDatabase();
            verify(mockDatabase).add(Mockito.any(Teacher.class));
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void addTeacherToDatabase_throwsException() {
        // set up the input fields
        controller.teachNameInput.setText("John");
        controller.teachUsernameInput.setText("John123");
        controller.teachDeptInput.setText("CSE");
        controller.teachAgeInput.setText("21");
        controller.teachPasswordInput.setText("password");
        controller.teachGenderInput.setValue("Male");
        controller.teachPosInput.setValue("Professor");

        doThrow(new RuntimeException()).when(mockDatabase).add(Mockito.any(Teacher.class));
        Platform.runLater(() -> {
            assertThrows(RuntimeException.class, () -> controller.addTeacherToDatabase());

        });
        WaitForAsyncUtils.waitForFxEvents();

    }

    @Test
    void testValidateDepartment_success()
    {
        // valid department
        String validDept = "CSE";
        boolean expected = true;
        Platform.runLater(()-> {
            boolean actual = controller.validateDepartment(validDept);
            assertEquals(expected, actual);
        });
        WaitForAsyncUtils.waitForFxEvents();

    }

    @Test
    void testValidateDepartment_invalid()
    {
        // invalid department
        String invalidDept = "invalidDept";
        boolean expected = false;
        Platform.runLater(()-> {
            boolean actual = controller.validateDepartment(invalidDept);
            assertEquals(expected, actual);
        });
        WaitForAsyncUtils.waitForFxEvents();

    }

    @Test
    void testValidateAddInput_success()
    {
        // set up the input fields
        controller.teachNameInput.setText("MrUnique");
        controller.teachUsernameInput.setText("UniqueUser");
        controller.teachDeptInput.setText("CSE");
        controller.teachAgeInput.setText("21");
        controller.teachPasswordInput.setText("password");
        controller.teachGenderInput.setValue("Female");
        controller.teachPosInput.setValue("Professor");

        boolean expected = true;
        Platform.runLater(()-> {
            boolean actual = controller.validateAddInput();
            assertEquals(expected, actual);
        });
        WaitForAsyncUtils.waitForFxEvents();

    }

    @Test
    void testValidateAddInput_withEmptyValues()
    {
        // set up the input fields
        controller.teachNameInput.setText("");
        controller.teachUsernameInput.setText("");
        controller.teachDeptInput.setText("");
        controller.teachAgeInput.setText("");
        controller.teachPasswordInput.setText("");

        boolean expected = false;
        Platform.runLater(()-> {
            boolean actual = controller.validateAddInput();
            assertEquals(expected, actual);
        });
        WaitForAsyncUtils.waitForFxEvents();

    }

    @Test
    void testValidateUpdateInput_withCorrectValues()
    {
        // set up the input fields, with different name and department
        controller.teachNameInput.setText("UniqueSir");
        controller.teachUsernameInput.setText("UniqueUser");
        controller.teachDeptInput.setText("SUST");
        controller.teachAgeInput.setText("21");
        controller.teachPasswordInput.setText("password");
        controller.teachGenderInput.setValue("Male");
        controller.teachPosInput.setValue("Professor");

        Teacher updatedTeacher = new Teacher("UniqueUser", "MrUnique", "Male", "21", "CSE", "password", "Professor",0);

        boolean expected = true;

        Platform.runLater(()-> {
            List<String> changes = new ArrayList<>();
            boolean actual = controller.validateUpdateInput(updatedTeacher, changes);
            assertEquals(expected, actual);
            // check that changes has 2 values, name and department
            assertEquals(2, changes.size());
        });
        WaitForAsyncUtils.waitForFxEvents();

    }

    @Test
    void testValidateUpdateInput_withSameValues()
    {
        // set up the input fields, with different name and department
        controller.teachNameInput.setText("MrUnique");
        controller.teachUsernameInput.setText("UniqueUser");
        controller.teachDeptInput.setText("CSE");
        controller.teachAgeInput.setText("21");
        controller.teachPasswordInput.setText("password");
        controller.teachGenderInput.setValue("Male");
        controller.teachPosInput.setValue("Professor");

        Teacher updatedTeacher = new Teacher("UniqueUser", "MrUnique", "Male", "21", "CSE", "password", "Professor",0);

        boolean expected = true;

        Platform.runLater(()-> {
            List<String> changes = new ArrayList<>();
            boolean actual = controller.validateUpdateInput(updatedTeacher, changes);
            assertEquals(expected, actual);
            // check that changes has 2 values, name and department
            assertEquals(0, changes.size());
        });
        WaitForAsyncUtils.waitForFxEvents();

    }

    @Test
    void testValidateUpdateInput_withEmptyValues()
    {
        // set up the input fields, with different name and department
        controller.teachNameInput.setText("");
        controller.teachUsernameInput.setText("");
        controller.teachDeptInput.setText("");
        controller.teachAgeInput.setText("");
        controller.teachPasswordInput.setText("");
        controller.teachGenderInput.setValue("");
        controller.teachPosInput.setValue("");

        Teacher updatedTeacher = new Teacher("UniqueUser", "MrUnique", "Male", "21", "CSE", "password", "Professor",0);

        boolean expected = false;

        Platform.runLater(()-> {
            List<String> changes = new ArrayList<>();
            boolean actual = controller.validateUpdateInput(updatedTeacher, changes);
            assertEquals(expected, actual);
            // check that changes has nothing inside
            assertEquals(0, changes.size());
        });
        WaitForAsyncUtils.waitForFxEvents();

    }

    @Test
    void testValidateUpdateInput_withChangedUsername()
    {
        // set up the input fields, with different name and department
        controller.teachNameInput.setText("UserNotUnique");
        controller.teachUsernameInput.setText("MrUnique");
        controller.teachDeptInput.setText("CSE");
        controller.teachAgeInput.setText("21");
        controller.teachPasswordInput.setText("password");
        controller.teachGenderInput.setValue("Male");
        controller.teachPosInput.setValue("Professor");

        Teacher updatedTeacher = new Teacher("UniqueUser", "MrUnique", "Male", "21", "CSE", "password", "Professor",0);

        boolean expected = false;

        Platform.runLater(()-> {
            List<String> changes = new ArrayList<>();
            boolean actual = controller.validateUpdateInput(updatedTeacher, changes);
            assertEquals(expected, actual);
            // check that changes has nothing inside
            assertEquals(0, changes.size());
        });
        WaitForAsyncUtils.waitForFxEvents();

    }

    @Test
    void testValidateUpdateInput_withNonAlphanumericName()
    {
        // set up the input fields, with different name and department
        controller.teachNameInput.setText("Unique^&%#");
        controller.teachUsernameInput.setText("MrUnique");
        controller.teachDeptInput.setText("CSE");
        controller.teachAgeInput.setText("21");
        controller.teachPasswordInput.setText("password");
        controller.teachGenderInput.setValue("Male");
        controller.teachPosInput.setValue("Professor");

        Teacher updatedTeacher = new Teacher("UniqueUser", "MrUnique", "Male", "21", "CSE", "password", "Professor",0);

        boolean expected = false;

        Platform.runLater(()-> {
            List<String> changes = new ArrayList<>();
            boolean actual = controller.validateUpdateInput(updatedTeacher, changes);
            assertEquals(expected, actual);
            // check that changes has nothing inside
            assertEquals(0, changes.size());

        });
        WaitForAsyncUtils.waitForFxEvents();

    }

    @Test
    void testValidateUpdateInput_withInvalidAge()
    {
        // set up the input fields, with different name and department
        controller.teachNameInput.setText("Unique");
        controller.teachUsernameInput.setText("MrUnique");
        controller.teachDeptInput.setText("CSE");
        controller.teachAgeInput.setText("101");
        controller.teachPasswordInput.setText("password");
        controller.teachGenderInput.setValue("Male");
        controller.teachPosInput.setValue("Professor");

        Teacher updatedTeacher = new Teacher("UniqueUser", "MrUnique", "Male", "21", "CSE", "password", "Professor",0);

        boolean expected = false;

        Platform.runLater(()-> {
            List<String> changes = new ArrayList<>();
            boolean actual = controller.validateUpdateInput(updatedTeacher, changes);
            assertEquals(expected, actual);
            // check that changes has nothing inside
            assertEquals(0, changes.size());

        });
        WaitForAsyncUtils.waitForFxEvents();

    }

    @Test
    void testValidateUpdateInput_withInvalidDept()
    {
        // set up the input fields, with different name and department
        controller.teachNameInput.setText("Unique");
        controller.teachUsernameInput.setText("UniqueUser");
        controller.teachDeptInput.setText("UnknownDept");
        controller.teachAgeInput.setText("21");
        controller.teachPasswordInput.setText("password");
        controller.teachGenderInput.setValue("Male");
        controller.teachPosInput.setValue("Professor");

        Teacher updatedTeacher = new Teacher("UniqueUser", "MrUnique", "Male", "21", "CSE", "password", "Professor",0);

        boolean expected = false;

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            List<String> changes = new ArrayList<>();
            boolean actual = controller.validateUpdateInput(updatedTeacher, changes);
            assertEquals(expected, actual);
            // verify that the message sender sends the correct message
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please input a valid department"));

        }
    }

    @Test
    void testValidateUpdateInput_withChangedPasswordGenderPosition() {
        // set up the input fields, with different name and department
        controller.teachNameInput.setText("MrUnique");
        controller.teachUsernameInput.setText("UniqueUser");
        controller.teachDeptInput.setText("CSE");
        controller.teachAgeInput.setText("21");
        controller.teachPasswordInput.setText("NEWpassword");
        controller.teachGenderInput.setValue("Female");
        controller.teachPosInput.setValue("Associate Professor");

        Teacher updatedTeacher = new Teacher("UniqueUser", "MrUnique", "Male", "21", "CSE", "password", "Professor",0);

        boolean expected = true;

        Platform.runLater(()-> {
            List<String> changes = new ArrayList<>();
            boolean actual = controller.validateUpdateInput(updatedTeacher, changes);
            assertEquals(expected, actual);
            // check that changes have the expected size
            assertEquals(3, changes.size(), "Expected 3 changes, but got: " + changes.size());

        });
        WaitForAsyncUtils.waitForFxEvents();


    }

    @Test
    void testValidateUpdateInput_WithInvalidName() {
        // set up the input fields, with different name and department
        controller.teachNameInput.setText("MrUnique@");
        controller.teachUsernameInput.setText("UniqueUser");
        controller.teachDeptInput.setText("CSE");
        controller.teachAgeInput.setText("21");
        controller.teachPasswordInput.setText("password");
        controller.teachGenderInput.setValue("Male");
        controller.teachPosInput.setValue("Professor");

        Teacher existingTeacher = new Teacher("UniqueUser", "MrUnique", "Male", "21", "CSE", "password", "Professor", 0);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            List<String> changes = new ArrayList<>();
            boolean result = controller.validateUpdateInput(existingTeacher, changes);
            // assert that the result returned is false
            assertFalse(result);

            // verify that the correct message is passed back
            mockedMsgSender.verify(() -> MsgSender.showMsg("Name must only contain alphabets"));

        }
    }

    @Test
    void testAddTeacher_validInput()
    {
        // set up the input fields
        controller.teachNameInput.setText("MrUnique");
        controller.teachUsernameInput.setText("UniqueUser");
        controller.teachDeptInput.setText("CSE");
        controller.teachAgeInput.setText("21");
        controller.teachPasswordInput.setText("password");
        controller.teachGenderInput.setValue("Male");
        controller.teachPosInput.setValue("Professor");

        // set up existing teachers (none)
        List<Teacher> allTeachers = List.of();
        controller.allTeachers = allTeachers;
        ActionEvent actionEvent = mock(ActionEvent.class);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            mockedMsgSender.when(() -> MsgSender.showConfirm(eq("Add Teacher"), eq("Are you sure you want to add this teacher?"), any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable callback = invocation.getArgument(2);
                        callback.run();
                        return null;
                    });
            controller.addTeacher(actionEvent);
            // verify that the teacher was added to the database
            mockedMsgSender.verify(() -> MsgSender.showConfirm(anyString(), anyString(), any(Runnable.class)));
        }

    }

    @Test
    void testUpdateTeacher_withNullTeacher()
    {
        controller.teachTable.getSelectionModel().clearSelection();

        ActionEvent actionEvent = mock(ActionEvent.class);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.updateTeacher(actionEvent);

            // assert that the result is false
            assertFalse(result);
            // verify that the correct message was sent
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please select a teacher to update"));
        }
    }

    @Test
    void testUpdateTeacher_withChanges()
    {
        // set up the input fields, with different name and department
        controller.teachNameInput.setText("MrUnique");
        controller.teachUsernameInput.setText("MrUnique");
        controller.teachDeptInput.setText("CSE");
        controller.teachAgeInput.setText("21");
        controller.teachPasswordInput.setText("password");
        controller.teachGenderInput.setValue("Male");
        controller.teachPosInput.setValue("Professor");

        // set up the selected teacher
        Teacher newTeacher = new Teacher("MrUnique", "User","Male","21","SUST","password","Professor",0);
        controller.teachTable.setItems(FXCollections.observableArrayList(newTeacher));
        controller.teachTable.getSelectionModel().select(newTeacher);


        ActionEvent actionEvent = mock(ActionEvent.class);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.updateTeacher(actionEvent);

            // assert that the result is true
            assertTrue(result);
            // verify that the correct message was sent
            mockedMsgSender.verify(() -> MsgSender.showUpdateConfirm(anyString(), anyList(), any(Runnable.class), any(Runnable.class)));
        }

    }

    @Test
    void testUpdateTeacher_noChanges()
    {
        // set up the input fields, with different name and department
        controller.teachNameInput.setText("MrUnique");
        controller.teachUsernameInput.setText("MrUnique");
        controller.teachDeptInput.setText("CSE");
        controller.teachAgeInput.setText("21");
        controller.teachPasswordInput.setText("password");
        controller.teachGenderInput.setValue("Male");
        controller.teachPosInput.setValue("Professor");

        // set up the selected teacher
        Teacher newTeacher = new Teacher("MrUnique", "MrUnique","Male","21","CSE","password","Professor",0);
        controller.teachTable.setItems(FXCollections.observableArrayList(newTeacher));
        controller.teachTable.getSelectionModel().select(newTeacher);


        ActionEvent actionEvent = mock(ActionEvent.class);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.updateTeacher(actionEvent);

            // assert that the result is false, since no changes
            assertFalse(result);
            // verify that the correct message was sent
            mockedMsgSender.verify(() -> MsgSender.showMsg("No changes detected"));
        }
    }

    @Test
    void testUpdateTeacher_InvalidInput()
    {
        // set up the input fields, with different name and department
        controller.teachNameInput.setText("MrUnique");
        controller.teachUsernameInput.setText("MrUnique");
        controller.teachDeptInput.setText("CSE");
        // set age to 1000
        controller.teachAgeInput.setText("1000");
        controller.teachPasswordInput.setText("password");
        controller.teachGenderInput.setValue("Male");
        controller.teachPosInput.setValue("Professor");

        // set up the selected teacher
        Teacher newTeacher = new Teacher("MrUnique", "MrUnique","Male","21","CSE","password","Professor",0);
        controller.teachTable.setItems(FXCollections.observableArrayList(newTeacher));
        controller.teachTable.getSelectionModel().select(newTeacher);


        ActionEvent actionEvent = mock(ActionEvent.class);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.updateTeacher(actionEvent);

            // assert that the result is false, since no changes
            assertFalse(result);
            // verify that the correct message was sent
            mockedMsgSender.verify(() -> MsgSender.showMsg("Age must be between 20 and 100"));
        }
    }
}