package comp3111.examsystem.controller;

import comp3111.examsystem.model.Grade;
import comp3111.examsystem.model.Student;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentManagementControllerTest {

    @InjectMocks
    private StudentManagementController controller;

    @Mock
    private Database<Student> mockDatabase;

    @Mock
    private Database<Grade> mockGradeDatabase;

    @Mock
    private TableView<Student> mockStudentTable;

    @Mock
    private TableColumn<Student, String> studentUsernameCol, studentNameCol, studentAgeCol, studentGenderCol, studentDeptCol, studentPasswordCol;

    @Mock
    private TextField studentUsernameInput, studentNameInput, studentAgeInput, studentDeptInput, studentPasswordInput, studentUsernameFilter, studentNameFilter, studentDeptFilter;

    @Mock
    private ComboBox<String> studentGenderInput;

    @Mock
    private Button studentAdd, studentDelete, studentFilter, studentRefresh, studentResetFilter, studentUpdate;

    @Mock
    private AnchorPane rootPane, AnchorWithInputs;

    @BeforeAll
    public static void initJavaFx() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock fields
        controller.studentTable = new TableView<>();
        controller.studentUsernameCol = new TableColumn<>();
        controller.studentNameCol = new TableColumn<>();
        controller.studentAgeCol = new TableColumn<>();
        controller.studentGenderCol = new TableColumn<>();
        controller.studentDeptCol = new TableColumn<>();
        controller.studentPasswordCol = new TableColumn<>();
        controller.studentNameInput = new TextField();
        controller.studentUsernameInput = new TextField();
        controller.studentDeptInput = new TextField();
        controller.studentAgeInput = new TextField();
        controller.studentPasswordInput = new TextField();
        controller.studentGenderInput = new ComboBox<>();
        controller.rootPane = new AnchorPane();
        controller.AnchorWithInputs = new AnchorPane();
        controller.studentDeptFilter = new TextField();
        controller.studentNameFilter = new TextField();
        controller.studentUsernameFilter = new TextField();
        controller.studentDatabase = mockDatabase;
        controller.allStudents = new ArrayList<>();
        controller.studentList = FXCollections.observableArrayList();
    }

    @AfterAll
    public static void tearDownJavaFx() {
        Platform.exit();
    }

    // --- Test cases (converted from TeacherManagementControllerTest) ---

    @Test
    void testNoFilterTrue() {
        controller.studentNameFilter.setText("");
        controller.studentUsernameFilter.setText("");
        controller.studentDeptFilter.setText("");
        assertTrue(controller.noFilter());
    }

    @Test
    void testNoFilterFalse() {
        controller.studentNameFilter.setText("John");
        controller.studentUsernameFilter.setText("john123");
        controller.studentDeptFilter.setText("CSE");
        assertFalse(controller.noFilter());
    }

    @Test
    void testLoadStudentTableNoFilter() {
        controller.studentNameFilter.setText("");
        controller.studentUsernameFilter.setText("");
        controller.studentDeptFilter.setText("");

        List<Student> allStudents = List.of(
                new Student("John123", "John", "Male", "21", "CSE", "password", 0),
                new Student("Amy123", "Amy", "Female", "22", "SUST", "password", 0)
        );
        when(mockDatabase.getAll()).thenReturn(allStudents);

        controller.loadStudentTable();

        verify(mockDatabase).getAll();
    }

    @Test
    void testLoadStudentTableWithFilter() {
        controller.studentNameFilter.setText("John");
        controller.studentUsernameFilter.setText("");
        controller.studentDeptFilter.setText("");

        List<Student> allStudents = List.of(
                new Student("John123", "John", "Male", "21", "CSE", "password", 0),
                new Student("Amy123", "Amy", "Female", "22", "SUST", "password", 0)
        );
        when(mockDatabase.getAll()).thenReturn(allStudents);

        List<Student> filteredStudents = controller.loadStudentTable();

        assertNotNull(filteredStudents);
        assertThat(filteredStudents.size()).isEqualTo(1);
    }

    @Test
    void testClearFields() {
        controller.studentNameInput.setText("John");
        controller.studentUsernameInput.setText("john123");
        controller.studentDeptInput.setText("CSE");
        controller.studentAgeInput.setText("21");
        controller.studentPasswordInput.setText("password");
        controller.studentGenderInput.setValue("Male");

        controller.clearFields();

        assertEquals("", controller.studentNameInput.getText());
        assertEquals("", controller.studentUsernameInput.getText());
        assertEquals("", controller.studentDeptInput.getText());
        assertEquals("", controller.studentAgeInput.getText());
        assertEquals("", controller.studentPasswordInput.getText());
        assertNull(controller.studentGenderInput.getValue());
    }

    @Test
    void testValidateAddInput_Valid() {
        controller.studentNameInput.setText("John");
        controller.studentUsernameInput.setText("john123");
        controller.studentDeptInput.setText("CSE");
        controller.studentAgeInput.setText("21");
        controller.studentPasswordInput.setText("password");
        controller.studentGenderInput.setValue("Male");

        assertTrue(controller.validateAddInput());
    }

    @Test
    void testValidateAddInput_nullFields() {
        controller.studentNameInput.setText("");
        controller.studentUsernameInput.setText("");
        controller.studentDeptInput.setText("");
        controller.studentAgeInput.setText("");
        controller.studentPasswordInput.setText("");

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validateAddInput();

            // assert that the result is false
            assertFalse(result);

            mockedMsgSender.verify(() -> MsgSender.showMsg("Please fill in all fields"));
        }
    }

    @Test
    void testAddStudentToDatabase_Success() {
        controller.studentNameInput.setText("John");
        controller.studentUsernameInput.setText("john123");
        controller.studentDeptInput.setText("CSE");
        controller.studentAgeInput.setText("21");
        controller.studentPasswordInput.setText("password");
        controller.studentGenderInput.setValue("Male");

        doNothing().when(mockDatabase).add(any(Student.class));

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addStudentToDatabase();

            mockedMsgSender.verify(() -> MsgSender.showMsg("Student added successfully"));
        }

        verify(mockDatabase).add(any(Student.class));
    }

    @Test
    void testAddStudentToDatabase_Exception() {
        controller.studentNameInput.setText("John");
        controller.studentUsernameInput.setText("john123");
        controller.studentDeptInput.setText("CSE");
        controller.studentAgeInput.setText("21");
        controller.studentPasswordInput.setText("password");
        controller.studentGenderInput.setValue("Male");

        doThrow(new RuntimeException()).when(mockDatabase).add(any(Student.class));

        assertThrows(RuntimeException.class, controller::addStudentToDatabase);
    }

    @Test
    void testValidateUsername_Valid() {
        List<Student> students = List.of(
                new Student("amy123", "Amy", "Female", "22", "CSE", "password", 0),
                new Student("cindy123", "Cindy", "Female", "21", "ECE", "password", 0)
        );
        controller.allStudents = students;

        assertTrue(controller.validateUsername("uniqueUsername"));
    }

    @Test
    void testValidateUsername_ExistingUser() {
        List<Student> students = List.of(
                new Student("john123", "John", "Male", "21", "CSE", "password", 0)
        );
        controller.allStudents = students;

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validateUsername("john123");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Username already exists"));
        }
    }

    @Test
    void testUpdateStudentWithChanges() {
        controller.studentNameInput.setText("JohnUpdated");
        controller.studentUsernameInput.setText("john123");
        controller.studentDeptInput.setText("SUST");
        controller.studentAgeInput.setText("21");
        controller.studentPasswordInput.setText("password");
        controller.studentGenderInput.setValue("Male");

        Student selectedStudent = new Student("john123", "John", "Male", "21", "CSE", "password", 0);
        controller.studentTable.setItems(FXCollections.observableArrayList(selectedStudent));
        controller.studentTable.getSelectionModel().select(selectedStudent);

        ActionEvent actionEvent = mock(ActionEvent.class);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.updateStudent(actionEvent);
            assertTrue(result);

            mockedMsgSender.verify(() -> MsgSender.showUpdateConfirm(
                    anyString(), anyList(), any(Runnable.class)
            ));
        }
    }

    @Test
    void testUpdateStudentWithNoChanges() {
        controller.studentNameInput.setText("John");
        controller.studentUsernameInput.setText("john123");
        controller.studentDeptInput.setText("CSE");
        controller.studentAgeInput.setText("21");
        controller.studentPasswordInput.setText("password");
        controller.studentGenderInput.setValue("Male");

        Student selectedStudent = new Student("john123", "John", "Male", "21", "CSE", "password", 0);
        controller.studentTable.setItems(FXCollections.observableArrayList(selectedStudent));
        controller.studentTable.getSelectionModel().select(selectedStudent);

        ActionEvent actionEvent = mock(ActionEvent.class);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.updateStudent(actionEvent);
            assertFalse(result);

            mockedMsgSender.verify(() -> MsgSender.showMsg("No changes detected"));
        }
    }

    @Test
    void testUpdateStudent_InvalidInput() {
        controller.studentNameInput.setText("John");
        controller.studentUsernameInput.setText("john123");
        controller.studentDeptInput.setText("CSE");
        controller.studentAgeInput.setText("200");
        controller.studentPasswordInput.setText("password");
        controller.studentGenderInput.setValue("Male");

        Student selectedStudent = new Student("john123", "John", "Male", "21", "CSE", "password", 0);
        controller.studentTable.setItems(FXCollections.observableArrayList(selectedStudent));
        controller.studentTable.getSelectionModel().select(selectedStudent);

        ActionEvent actionEvent = mock(ActionEvent.class);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.updateStudent(actionEvent);
            assertFalse(result);

            mockedMsgSender.verify(() -> MsgSender.showMsg("Age must be between 20 and 100"));
        }
    }

    @Test
    void testDeleteStudent() {
        Student studentToDelete = new Student("amy123", "Amy", "Female", "22", "CSE", "password", 0);
        controller.studentTable.setItems(FXCollections.observableArrayList(studentToDelete));
        controller.studentTable.getSelectionModel().select(studentToDelete);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {

            controller.deleteStudent(mock(ActionEvent.class));
            mockedMsgSender.verify(() -> MsgSender.showConfirm(anyString(),anyString(), any(Runnable.class)));

        }
    }

    @Test
    void testValidateAge_NotANumber()
    {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {

            boolean result = controller.validateAge("Hello");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Age must be a number"));

        }
    }

    @Test
    void testValidateDepartment_False()
    {
        boolean result = controller.validateDepartment("ISMO");
        assertFalse(result);
    }

    @Test
    void testDeleteStudentAndGradesFromDatabase_valid()
    {
        // set up mock grade database with 1 grade
        Grade newGrade = new Grade("0", "question1", "100");
        when((mockGradeDatabase).getAll()).thenReturn(List.of(newGrade));

        // set up the student to delete
        Student studentToDelete = new Student("john123", "john", "Male", "21", "CSE", "password", 0);

        // configure the remaining mocks
        doNothing().when(mockGradeDatabase).delByKey(anyString());
        doNothing().when(mockDatabase).delByKey(anyString());

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {

            controller.deleteStudentAndGradesFromDatabase(studentToDelete);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Student deleted successfully"));

        }
    }

    @Test
    void testDeleteStudentAndGradesFromDatabase_exceptionDelByKeyGrade()
    {
        // set up mock grade database with 1 grade
        Grade newGrade = new Grade("0", "question1", "100");
        when((mockGradeDatabase).getAll()).thenReturn(List.of(newGrade));

        // set up the student to delete
        Student studentToDelete = new Student("john123", "john", "Male", "21", "CSE", "password", 0);

        // configure the mock to throw an exception
        doThrow(new RuntimeException()).when(mockGradeDatabase).delByKey(anyString());

        assertThrows(RuntimeException.class, () -> controller.deleteStudentAndGradesFromDatabase(studentToDelete));

    }

    @Test
    void testUpdateStudentinDatabase_valid() {
        // set up the student to update
        Student studentToUpdate = new Student("john123", "john", "Male", "21", "CSE", "password", 0);

        // configure the remaining mocks
        doNothing().when(mockDatabase).update(any(Student.class));

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {

            controller.updateStudentInDatabase(studentToUpdate);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Student updated successfully"));

        }
    }

    @Test
    void testUpdateStudentInDatabase_fail()
    {
        // set up the student to update
        Student studentToUpdate = new Student("john123", "john", "Male", "21", "CSE", "password", 0);

        // configure the mock to throw an exception
        doThrow(new RuntimeException()).when(mockDatabase).update(any(Student.class));

        assertThrows(RuntimeException.class, ()->controller.updateStudentInDatabase(studentToUpdate));
    }

    @Test
    void testResetFilter()
    {
        controller.studentNameFilter.setText("John");
        controller.studentUsernameFilter.setText("john123");
        controller.studentDeptFilter.setText("CSE");

        ActionEvent actionEvent = mock(ActionEvent.class);
        controller.resetFilter(actionEvent);

        assertEquals("", controller.studentNameFilter.getText());
        assertEquals("", controller.studentUsernameFilter.getText());
        assertEquals("", controller.studentDeptFilter.getText());
    }

    @Test
    void testRefreshStudent()
    {
        // initialize all fields including input fields
        controller.studentNameFilter.setText("John");
        controller.studentUsernameFilter.setText("john123");
        controller.studentDeptFilter.setText("CSE");
        controller.studentNameInput.setText("John");
        controller.studentUsernameInput.setText("john123");
        controller.studentDeptInput.setText("CSE");
        controller.studentAgeInput.setText("21");
        controller.studentPasswordInput.setText("password");
        controller.studentGenderInput.setValue("Male");

        ActionEvent actionEvent = mock(ActionEvent.class);
        controller.refreshStudent(actionEvent);

        // assert that all fields are cleared
        assertEquals("", controller.studentNameFilter.getText());
        assertEquals("", controller.studentUsernameFilter.getText());
        assertEquals("", controller.studentDeptFilter.getText());
        assertEquals("", controller.studentNameInput.getText());
        assertEquals("", controller.studentUsernameInput.getText());
        assertEquals("", controller.studentDeptInput.getText());
        assertEquals("", controller.studentAgeInput.getText());
        assertEquals("", controller.studentPasswordInput.getText());
        assertNull(controller.studentGenderInput.getValue());


    }

    @Test
    void testDeleteNullStudent()
    {
        // set the selected student to null
        controller.studentTable.getSelectionModel().select(null);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.deleteStudent(mock(ActionEvent.class));
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please select a student to delete"));
        }
    }

    @Test
    void testUpdateNullStudent()
    {
        // set the selected student to null
        controller.studentTable.getSelectionModel().select(null);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.updateStudent(mock(ActionEvent.class));
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please select a student to update"));
        }
    }

    @Test
    void testValidateUpdateInput_nullValues()
    {
        // set the input fields to null
        controller.studentNameInput.setText("");
        controller.studentUsernameInput.setText("");
        controller.studentDeptInput.setText("");
        controller.studentAgeInput.setText("");
        controller.studentPasswordInput.setText("");
        controller.studentGenderInput.setValue("Male");

        Student studentToUpdate = new Student("john123", "john", "Male", "21", "CSE", "password", 0);
        List<String> changes = new ArrayList<>();

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validateUpdateInput(studentToUpdate,changes );
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please fill in all fields"));
        }
    }

    @Test
    void testValidateUpdateInput_differentUsername()
    {
        // set the input fields to null
        controller.studentNameInput.setText("jonny");
        controller.studentUsernameInput.setText("john321");
        controller.studentDeptInput.setText("CSE");
        controller.studentAgeInput.setText("22");
        controller.studentPasswordInput.setText("password");
        controller.studentGenderInput.setValue("Male");

        Student studentToUpdate = new Student("john123", "john", "Male", "21", "CSE", "password", 0);
        List<String> changes = new ArrayList<>();

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validateUpdateInput(studentToUpdate,changes );
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Username must be the same!"));
        }
    }

    @Test
    void testValidateUpdateInput_invalidUsername()
    {
        // set the input fields to null
        controller.studentNameInput.setText("jonny%^$&");
        controller.studentUsernameInput.setText("john123");
        controller.studentDeptInput.setText("CSE");
        controller.studentAgeInput.setText("22");
        controller.studentPasswordInput.setText("password");
        controller.studentGenderInput.setValue("Male");

        Student studentToUpdate = new Student("john123", "john", "Male", "21", "CSE", "password", 0);
        List<String> changes = new ArrayList<>();

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validateUpdateInput(studentToUpdate,changes );
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Name must only contain alphabets"));
        }
    }

    @Test
    void testValidateUpdateInput_invalidDept()
    {
        // set the input fields to null
        controller.studentNameInput.setText("jonny");
        controller.studentUsernameInput.setText("john123");
        controller.studentDeptInput.setText("SCESC");
        controller.studentAgeInput.setText("22");
        controller.studentPasswordInput.setText("password");
        controller.studentGenderInput.setValue("Male");

        Student studentToUpdate = new Student("john123", "john", "Male", "21", "CSE", "password", 0);
        List<String> changes = new ArrayList<>();

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validateUpdateInput(studentToUpdate,changes );
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please input a valid department"));
        }
    }


}
