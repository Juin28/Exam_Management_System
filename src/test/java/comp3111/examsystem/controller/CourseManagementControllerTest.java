package comp3111.examsystem.controller;

import static org.junit.jupiter.api.Assertions.*;
import comp3111.examsystem.model.Course;
import comp3111.examsystem.model.Grade;
import comp3111.examsystem.model.Question;
import comp3111.examsystem.model.Quiz;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseManagementControllerTest {

    @InjectMocks
    private CourseManagementController controller;

    @Mock
    private Database<Course> courseDatabase;

    @Mock
    private Database<Quiz> quizDatabase;

    @Mock
    private Database<Question> questionDatabase;

    @Mock
    private Database<Grade> gradeDatabase;

    @Mock
    private TableView<Course> courseTable;

    @Mock
    private TableColumn<Course, String> courseNameCol, courseIDCol, courseDeptCol;

    @Mock
    private AnchorPane rootPane, AnchorWithInputs;

    @Mock
    private ObservableList<Course> courseList;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown); // Initialize JavaFX
        latch.await();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inject mock values
        controller.courseTable = new TableView<>();
        controller.courseNameInput = new TextField();
        controller.courseIDInput = new TextField();
        controller.courseDeptInput = new TextField();
        controller.courseNameFilter = new TextField();
        controller.courseIDFilter = new TextField();
        controller.courseDeptFilter = new TextField();
        controller.courseList = FXCollections.observableArrayList();

        controller.courseDatabase = courseDatabase;
        controller.quizDatabase = quizDatabase;
        controller.gradeDatabase = gradeDatabase;

    }

    @AfterAll
    static void tearDownJavaFX() {
        Platform.exit();
    }

    @Test
    void testNoFilter()
    {
        controller.courseDeptFilter.setText("");
        controller.courseIDFilter.setText("");
        controller.courseNameFilter.setText("");

        assertTrue(controller.noFilter());
    }

    @Test
    void testNoFilterWithFilter()
    {
        controller.courseDeptFilter.setText("CSE");
        controller.courseIDFilter.setText("");
        controller.courseNameFilter.setText("");

        assertFalse(controller.noFilter());
    }

    @Test
    void testLoadCourseTable_noFilter()
    {
        controller.courseDeptFilter.setText("");
        controller.courseIDFilter.setText("");
        controller.courseNameFilter.setText("");

        List<Course> filteredCourses = controller.loadCourseTable();

        // assert that there are no filtered courses
        assertTrue(filteredCourses.isEmpty());
    }

    @Test
    void testLoadCourseTable_withFilter()
    {
        controller.courseDeptFilter.setText("CSE");
        controller.courseIDFilter.setText("");
        controller.courseNameFilter.setText("");

        // generate some dummy courses
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Intro to CS", "COMP1111", "CSE", 0));
        courses.add(new Course("Advanced Algorithms", "COMP2001", "CSE", 0));
        courses.add(new Course("Physics Basics", "PHYS1001", "PHYS", 0));

        when(courseDatabase.getAll()).thenReturn(courses);

        List<Course> filteredCourses = controller.loadCourseTable();

        // assert that the size of filtered courses is 2
        assertEquals(2, filteredCourses.size());
    }

    @Test
    void testValidateDepartment_valid()
    {
        // pass in all the valid departments and check if they assert as true
        assertTrue(controller.validateDepartment("IEDA"));
        assertTrue(controller.validateDepartment("CSE"));
        assertTrue(controller.validateDepartment("ECE"));
        assertTrue(controller.validateDepartment("MAE"));
        assertTrue(controller.validateDepartment("BBA"));
        assertTrue(controller.validateDepartment("CBE"));
        assertTrue(controller.validateDepartment("CIVL"));
        assertTrue(controller.validateDepartment("PHYS"));
        assertTrue(controller.validateDepartment("MATH"));
        assertTrue(controller.validateDepartment("HUMA"));
        assertTrue(controller.validateDepartment("LANG"));
        assertTrue(controller.validateDepartment("ACCT"));
        assertTrue(controller.validateDepartment("OCES"));
        assertTrue(controller.validateDepartment("ISOM"));
        assertTrue(controller.validateDepartment("FINA"));
        assertTrue(controller.validateDepartment("MARK"));
        assertTrue(controller.validateDepartment("GBUS"));
        assertTrue(controller.validateDepartment("LIFS"));
        assertTrue(controller.validateDepartment("BIEN"));
        assertTrue(controller.validateDepartment("CHEM"));
        assertTrue(controller.validateDepartment("ENVR"));
        assertTrue(controller.validateDepartment("HUMA"));
        assertTrue(controller.validateDepartment("SOSC"));
        assertTrue(controller.validateDepartment("SHSS"));
        assertTrue(controller.validateDepartment("SUST"));
        assertTrue(controller.validateDepartment("ISD"));

        // assert false for a wrong department
        assertFalse(controller.validateDepartment("RandomDept"));

    }

    // validateID
    @Test
    void testValidateID_Correct()
    {
        // make an ID that has 4 chars + 4 numbers, 4 chars as uppercase
        String id = "PHYS1001";

        // make allCourses return no courses
        controller.allCourses = List.of();

        // no msg sender sent, so just validate true
        assertTrue(controller.validateID(id));

    }

    @Test
    void testValidateID_incorrectFormat()
    {
        String id = "COMP100001";

        // make allCourses return no courses
        controller.allCourses = List.of();

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validateID(id);

            assertFalse(result);
            // Verify message
            mockedMsgSender.verify(() -> MsgSender.showMsg(eq("Course name must be 8 characters long")));
        }
    }

    @Test
    void testValidateID_incorrectFormat2()
    {
        String id = "ComP1001";

        // make allCourses return no courses
        controller.allCourses = List.of();

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validateID(id);

            assertFalse(result);
            // Verify message
            mockedMsgSender.verify(() -> MsgSender.showMsg(eq("First 4 characters of Course ID must be uppercase letters")));
        }
    }

    @Test
    void testValidateID_incorrectFormat3()
    {
        String id = "ComPP101";

        // make allCourses return no courses
        controller.allCourses = List.of();

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validateID(id);

            assertFalse(result);
            // Verify message
            mockedMsgSender.verify(() -> MsgSender.showMsg(eq("Last 4 characters of Course ID must be numbers")));
        }
    }

    @Test
    void testValidateID_courseExists()
    {
        String id = "COMP1001";

        // make allCourses return no courses
        Course courseExisting = new Course("Intro to CS", "COMP1001", "CSE", 0);
        controller.allCourses = List.of(courseExisting);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validateID(id);

            assertFalse(result);
            // Verify message
            mockedMsgSender.verify(() -> MsgSender.showMsg(eq("Course ID already exists")));
        }
    }

    @Test
    void testAddCourseToDatabase()
    {
        // set the required fields
        controller.courseNameInput.setText("Intro to CS");
        controller.courseIDInput.setText("COMP1001");
        controller.courseDeptInput.setText("CSE");

        doNothing().when(courseDatabase).add(any(Course.class));

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addCourseToDatabase();
            // Verify message
            mockedMsgSender.verify(() -> MsgSender.showMsg(eq("Course added successfully")));
            // verify that courseDatabase.add was called
            verify(courseDatabase).add(any(Course.class));
        }
    }

    @Test
    void testAddCourseToDatabase_fail()
    {
        // set the required fields
        controller.courseNameInput.setText("Intro to CS");
        controller.courseIDInput.setText("COMP1001");
        controller.courseDeptInput.setText("CSE");

        doThrow(new RuntimeException()).when(courseDatabase).add(any(Course.class));

        assertThrows(RuntimeException.class, () -> {
            controller.addCourseToDatabase();
        });
    }

    @Test
    void testDeleteCourseFromDatabase()
    {
        // set up the course
        Course courseToDelete = new Course("Intro to CS", "COMP1001", "CSE", 0);
        doNothing().when(courseDatabase).delByKey(anyString());

        // verify correct message is sent
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.deleteCourseFromDatabase(courseToDelete);
            // Verify message
            mockedMsgSender.verify(() -> MsgSender.showMsg(eq("Course deleted successfully")));
            // verify that courseDatabase.delByKey was called
            verify(courseDatabase).delByKey(anyString());
        }
    }

    @Test
    void testDeleteCourseFromDatabase_fail()
    {
        // set up the course
        Course courseToDelete = new Course("Intro to CS", "COMP1001", "CSE", 0);
        doThrow(new RuntimeException()).when(courseDatabase).delByKey(anyString());

        assertThrows(RuntimeException.class, () -> {
            controller.deleteCourseFromDatabase(courseToDelete);
        });
    }

    @Test
    void testDeleteCourseAndQuizzes()
    {
        // in this case, we need to delete the grade associated with the quiz, then delete
        // the quiz and courses

        Course courseToDelete = new Course("Intro to CS", "COMP1001", "CSE", 0);
        Quiz quizToDelete = new Quiz("Quiz 1", "60", "COMP1001", "no", 0, "1|2");
        // set up a grade associated with the quiz
        Grade gradeToDelete = new Grade("student1", String.valueOf(quizToDelete.getId()), "50");

        controller.allGrades = List.of(gradeToDelete);

        // call the method and ensure that the grade, quizToDelete, and the course are all deleted
        doNothing().when(gradeDatabase).delByKey(anyString());
        doNothing().when(quizDatabase).delByKey(anyString());
        doNothing().when(courseDatabase).delByKey(anyString());

        // verify correct message is sent
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.deleteCourseAndQuizzes(courseToDelete, List.of(quizToDelete));
            // Verify message
            mockedMsgSender.verify(() -> MsgSender.showMsg(eq("Course deleted successfully")));
            // verify that courseDatabase.delByKey was called
            verify(courseDatabase).delByKey(String.valueOf(courseToDelete.getId()));
            verify(quizDatabase).delByKey(String.valueOf(quizToDelete.getId()));
            verify(gradeDatabase).delByKey(String.valueOf(gradeToDelete.getID()));

        }
    }

    @Test
    void testDeleteCourseAndQuizzes_errorDeletingGrade()
    {
        // in this case, we need to delete the grade associated with the quiz, then delete
        // the quiz and courses

        Course courseToDelete = new Course("Intro to CS", "COMP1001", "CSE", 0);
        Quiz quizToDelete = new Quiz("Quiz 1", "60", "COMP1001", "no", 0, "1|2");
        // set up a grade associated with the quiz
        Grade gradeToDelete = new Grade("student1", String.valueOf(quizToDelete.getId()), "50");

        controller.allGrades = List.of(gradeToDelete);

        doThrow(RuntimeException.class).when(gradeDatabase).delByKey(anyString());

        assertThrows(RuntimeException.class, () -> {
            controller.deleteCourseAndQuizzes(courseToDelete, List.of(quizToDelete));
        });
    }

    @Test
    void testDeleteCourseAndQuizzes_errorDeletingQuizzes()
    {
        // in this case, we need to delete the grade associated with the quiz, then delete
        // the quiz and courses

        Course courseToDelete = new Course("Intro to CS", "COMP1001", "CSE", 0);
        Quiz quizToDelete = new Quiz("Quiz 1", "60", "COMP1001", "no", 0, "1|2");
        // set up a grade associated with the quiz
        Grade gradeToDelete = new Grade("student1", String.valueOf(quizToDelete.getId()), "50");

        controller.allGrades = List.of(gradeToDelete);

        doNothing().when(gradeDatabase).delByKey(anyString());
        doThrow(RuntimeException.class).when(quizDatabase).delByKey(anyString());

        assertThrows(RuntimeException.class, () -> {
            controller.deleteCourseAndQuizzes(courseToDelete, List.of(quizToDelete));
        });
    }

    @Test
    void testValidateUpdateInput_existingCourse()
    {
        // set the required fields
        controller.courseNameInput.setText("Intro to Algorithms");
        controller.courseIDInput.setText("COMP1001");
        controller.courseDeptInput.setText("CSE");

        // make allCourses return one existing course
        Course courseExisting = new Course("Intro to CS", "COMP1001", "CSE", 0);
        controller.allCourses = List.of(courseExisting);

        List<String> changes = new ArrayList<>();

        boolean result = controller.validateUpdateInput(courseExisting, changes);

        assertTrue(result);

    }

    @Test
    void testValidateUpdateInput_differentCourseID()
    {
        // set the required fields
        controller.courseNameInput.setText("Intro to Algorithms");
        controller.courseIDInput.setText("COMP1021");
        controller.courseDeptInput.setText("CSE");

        // make allCourses return one existing course
        Course courseExisting = new Course("Intro to CS", "COMP1001", "CSE", 0);
        controller.allCourses = List.of(courseExisting);

        List<String> changes = new ArrayList<>();

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            assertFalse(controller.validateUpdateInput(courseExisting, changes));
            // Verify message
            mockedMsgSender.verify(() -> MsgSender.showMsg(eq("Course ID cannot be changed")));
        }
    }

    @Test
    void testValidateUpdateInput_notFilledFields()
    {
        // set the required fields
        controller.courseNameInput.setText("");
        controller.courseIDInput.setText("COMP1001");
        controller.courseDeptInput.setText("CSE");

        // make allCourses return one existing course
        Course courseExisting = new Course("Intro to CS", "COMP1001", "CSE", 0);
        controller.allCourses = List.of(courseExisting);

        List<String> changes = new ArrayList<>();

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            assertFalse(controller.validateUpdateInput(courseExisting, changes));
            // Verify message
            mockedMsgSender.verify(() -> MsgSender.showMsg(eq("Please fill in all the fields")));
        }
    }

    @Test
    void testValidateUpdateInput_invalidDept()
    {
        // set the required fields
        controller.courseNameInput.setText("Intro to Algorithms");
        controller.courseIDInput.setText("COMP1001");
        controller.courseDeptInput.setText("SCCE");

        // make allCourses return one existing course
        Course courseExisting = new Course("Intro to CS", "COMP1001", "CSE", 0);
        controller.allCourses = List.of(courseExisting);

        List<String> changes = new ArrayList<>();

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            assertFalse(controller.validateUpdateInput(courseExisting, changes));
            // Verify message
            mockedMsgSender.verify(() -> MsgSender.showMsg(eq("Please enter a valid department")));
        }
    }

    @Test
    void testUpdateCourseInDatabase()
    {

        Course courseToUpdate = new Course();
        doNothing().when(courseDatabase).update(courseToUpdate);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            assertTrue(controller.updateCourseInDatabase(courseToUpdate));
            // Verify message
            mockedMsgSender.verify(() -> MsgSender.showMsg(eq("Course updated successfully")));
        }
    }

    @Test
    void testUpdateCourseInDatabase_fail()
    {

        Course courseToUpdate = new Course();
        doThrow(RuntimeException.class).when(courseDatabase).update(courseToUpdate);

        assertThrows(RuntimeException.class, () -> {
            controller.updateCourseInDatabase(courseToUpdate);
        });
    }

    @Test
    void testUpdateCourse()
    {
        controller.courseTable.getSelectionModel().select(new Course("Intro to CS", "COMP1001", "CSE", 0));
        // set up the input fields
        controller.courseNameInput.setText("Intro to Algorithms");
        controller.courseIDInput.setText("COMP1001");
        controller.courseDeptInput.setText("CSE");

        ActionEvent actionEvent = mock(ActionEvent.class);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            assertTrue(controller.updateCourse(actionEvent));
            // Verify message
            mockedMsgSender.verify(() -> MsgSender.showUpdateConfirm(anyString(), any(List.class), any(Runnable.class)));
        }

    }

    @Test
    void testUpdateCourse_noChanges()
    {
        controller.courseTable.getSelectionModel().select(new Course("Intro to CS", "COMP1001", "CSE", 0));
        // set up the input fields
        controller.courseNameInput.setText("Intro to CS");
        controller.courseIDInput.setText("COMP1001");
        controller.courseDeptInput.setText("CSE");

        ActionEvent actionEvent = mock(ActionEvent.class);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            assertFalse(controller.updateCourse(actionEvent));
            // Verify message
            mockedMsgSender.verify(() -> MsgSender.showMsg("No changes detected"));
        }
    }

    @Test
    void testUpdateCourse_invalidUpdate()
    {
        controller.courseTable.getSelectionModel().select(new Course("Intro to CS", "COMP1001", "CSE", 0));
        // set up the input fields
        controller.courseNameInput.setText("Intro to CS");
        controller.courseIDInput.setText("1001COMP");
        controller.courseDeptInput.setText("CSE");

        ActionEvent actionEvent = mock(ActionEvent.class);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            assertFalse(controller.updateCourse(actionEvent));
            // Verify message
            mockedMsgSender.verify(() -> MsgSender.showMsg("Course ID cannot be changed"));
        }
    }

    @Test
    void testUpdateCourse_nullSelection()
    {
        controller.courseTable.getSelectionModel().select(null);
        // set up the input fields
        controller.courseNameInput.setText("Intro to CS");
        controller.courseIDInput.setText("1001COMP");
        controller.courseDeptInput.setText("CSE");

        ActionEvent actionEvent = mock(ActionEvent.class);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            assertFalse(controller.updateCourse(actionEvent));
            // Verify message
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please select a course to update"));
        }
    }


    @Test
    void testAddCourse_ValidInput() {


        // Set input fields
        controller.courseNameInput.setText("Intro to CS");
        controller.courseIDInput.setText("COMP1111");
        controller.courseDeptInput.setText("CSE");

        controller.allCourses = List.of();
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            mockedMsgSender.when(() -> MsgSender.showConfirm(anyString(), anyString(), any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable callback = invocation.getArgument(2);
                        callback.run();
                        return null;
                    });

            // Call the method
            controller.addCourse(new ActionEvent());

            // Verify interaction with database
            verify(courseDatabase).add(any(Course.class));
            mockedMsgSender.verify(() -> MsgSender.showConfirm(eq("Add Course"), anyString(), any(Runnable.class)));
        }
    }

    @Test
    void testAddCourse_InvalidInput() {

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Call the method
            controller.addCourse(new ActionEvent());

            // Verify MsgSender was called
            mockedMsgSender.verify(() -> MsgSender.showMsg(eq("Please fill in all fields")));
        }
    }

    @Test
    void testDeleteCourse_NoSelection() {
        controller.courseTable.getSelectionModel().select(null);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.deleteCourse(new ActionEvent());

            // Verify message
            mockedMsgSender.verify(() -> MsgSender.showMsg(eq("Please select a course to delete")));
        }
    }

    @Test
    void testDeleteCourse_WithSelection() {
        // Mock selected course
        Course courseToDelete = new Course("Intro to CS", "COMP1111", "CSE", 0);
        controller.courseTable.getSelectionModel().select(courseToDelete);
        controller.allQuizzes = List.of(new Quiz("Quiz 1", "60", "COMP1111", "no", 0, "1|2"));
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {

            // Call the method
            controller.deleteCourse(new ActionEvent());

            // Verify the msg sender sends the correct message
            mockedMsgSender.verify(() -> MsgSender.showDeleteConfirm(eq(courseToDelete),anyString(), any(List.class), any(Runnable.class)));

        }
    }

    @Test
    void testDeleteCourse_WithSelectionNoQuizzes() {
        // Mock selected course
        Course courseToDelete = new Course("Intro to CS", "COMP1111", "CSE", 0);
        controller.courseTable.getSelectionModel().select(courseToDelete);

        // create quiz not associated with the course
        controller.allQuizzes = List.of(new Quiz("Quiz 1", "60", "COMP1221", "no", 0, "1|2"));
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {

            // Call the method
            controller.deleteCourse(new ActionEvent());

            // Verify the msg sender sends the correct message
            mockedMsgSender.verify(() -> MsgSender.showConfirm(anyString(), anyString(), any(Runnable.class)));

        }
    }
}

//    @Test
//    void testRefreshCourse() {
//        // Call refreshCourse
//        controller.refreshCourse(new ActionEvent());
//
//        // Verify input fields are cleared
//        verify(mockCourseNameInput).clear();
//        verify(mockCourseIDInput).clear();
//        verify(mockCourseDeptInput).clear();
//
//        // Verify filters are cleared
//        verify(mockCourseNameFilter).clear();
//        verify(mockCourseIDFilter).clear();
//        verify(mockCourseDeptFilter).clear();
//
//        // Verify the course table is reloaded
//        verify(mockCourseDatabase).getAll();
//    }
//}
