package comp3111.examsystem.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import comp3111.examsystem.model.*;
import comp3111.examsystem.service.Database;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@ExtendWith(MockitoExtension.class)
class TeacherGradeStatisticControllerTest {
    @InjectMocks
    private TeacherGradeStatisticController controller;

    @Mock
    private Database<Grade> gradeDatabase;
    @Mock
    private Database<Quiz> quizDatabase;
    @Mock
    private Database<Student> studentDatabase;
    @Mock
    private Database<Question> questionDatabase;
    @Mock
    private Database<Course> courseDatabase;


    @FXML
    BarChart<String, Number> barChart = mock(BarChart.class);
    @FXML
    CategoryAxis categoryAxisBar = mock(CategoryAxis.class);
    @FXML
    NumberAxis numberAxisBar = mock(NumberAxis.class);
    @FXML
    LineChart<String, Number> lineChart = mock(LineChart.class);
    @FXML
    CategoryAxis categoryAxisLine = mock(CategoryAxis.class);
    @FXML
    NumberAxis numberAxisLine = mock(NumberAxis.class);
    @FXML
    PieChart pieChart = mock(PieChart.class);

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown); // Initialize JavaFX
        latch.await();
    }

    @AfterAll
    static void tearDownJavaFX() {
        Platform.exit();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        // Mock databases
        controller.gradeDatabase = gradeDatabase;
        controller.quizDatabase = quizDatabase;
        controller.studentDatabase = studentDatabase;
        controller.questionDatabase = questionDatabase;
        controller.courseDatabase = courseDatabase;
        controller.gradeTable = new TableView<>();
        controller.gradeListObservable = FXCollections.observableArrayList();


        // Mock data
        Grade mockGrade = new Grade("1", "quiz1", "90", "60");
        Quiz mockQuiz = new Quiz("quiz1", "60", "comp3111", "Yes", 0, "1|2");
        Student mockStudent = new Student("aliceyao", "Alice","Female","21","CSE","password",0);
        Question mockQuestion1 = new Question("description1", "optionA1", "optionB1", "optionC1", "optionD1", "A", "10", "Single", 1);
        Question mockQuestion2 = new Question("description2", "optionA2", "optionB2", "optionC2", "optionD2", "B", "20", "Single", 2);

    }


    @Test
    void testIsDuplicate()
    {
        TeacherGradeStatisticController.GradeStatisticClass grade1 = new TeacherGradeStatisticController.GradeStatisticClass("John", "COMP1111", "Test 1", "10","90", "10");
        TeacherGradeStatisticController.GradeStatisticClass grade2 = new TeacherGradeStatisticController.GradeStatisticClass("John", "COMP1331", "Test 1", "10","90", "10");
        TeacherGradeStatisticController.GradeStatisticClass grade3 = new TeacherGradeStatisticController.GradeStatisticClass("John", "COMP1111", "Test 2", "10","90", "10");
        List <TeacherGradeStatisticController.GradeStatisticClass> grades = Arrays.asList(grade1, grade2, grade3);

        // same grade, duplicate
        assertTrue(controller.isDuplicate(grade1, grades));

        // different student, not a duplicate
        assertFalse(controller.isDuplicate(new TeacherGradeStatisticController.GradeStatisticClass("Tim", "COMP1111", "Test 2", "10","90", "10"), grades));
    }

    @Test
    void testLoadList()
    {
        TeacherGradeStatisticController.GradeStatisticClass grade1 = new TeacherGradeStatisticController.GradeStatisticClass("John", "COMP1111", "Test 1", "10","90", "10");
        TeacherGradeStatisticController.GradeStatisticClass grade2 = new TeacherGradeStatisticController.GradeStatisticClass("John", "COMP1331", "Test 1", "10","90", "10");
        TeacherGradeStatisticController.GradeStatisticClass grade3 = new TeacherGradeStatisticController.GradeStatisticClass("John", "COMP1111", "Test 2", "10","90", "10");
        List <TeacherGradeStatisticController.GradeStatisticClass> grades = Arrays.asList(grade1, grade2, grade3);

        controller.loadList(grades);

        assertEquals(3, controller.gradeListObservable.size());
    }

    @Test
    void testLoadChart()
    {
        TeacherGradeStatisticController.GradeStatisticClass grade1 = new TeacherGradeStatisticController.GradeStatisticClass("John", "COMP1111", "Test 1", "10","90", "10");
        TeacherGradeStatisticController.GradeStatisticClass grade2 = new TeacherGradeStatisticController.GradeStatisticClass("John", "COMP1331", "Test 1", "10","90", "10");
        TeacherGradeStatisticController.GradeStatisticClass grade3 = new TeacherGradeStatisticController.GradeStatisticClass("John", "COMP1111", "Test 2", "10","90", "10");
        List <TeacherGradeStatisticController.GradeStatisticClass> grades = Arrays.asList(grade1, grade2, grade3);

        ObservableList<XYChart.Series<String, Number>> mockBarChartData = mock(ObservableList.class);
        ObservableList<XYChart.Series<String, Number>> mockLineChartData = mock(ObservableList.class);
        ObservableList<PieChart.Data> mockPieChartData = mock(ObservableList.class);

        when(barChart.getData()).thenReturn(mockBarChartData);
        when(lineChart.getData()).thenReturn(mockLineChartData);
        when(pieChart.getData()).thenReturn(mockPieChartData);

        // Mock the add behavior
        when(mockBarChartData.add(any())).thenReturn(true);
        when(mockLineChartData.add(any())).thenReturn(true);
        when(mockPieChartData.add(any())).thenReturn(true);


        controller.loadChart(grades);

        // assert that all the charts have been populated
        verify(barChart.getData(), times(1)).add(any());
        verify(lineChart.getData(), times(1)).add(any());
        verify(pieChart.getData(), times(1)).add(any());
    }

    @Test
    void testPopulateChoiceBoxesWithUniqueValues() {
        // Mock gradeStatisticList with unique values
        // Mock the ChoiceBoxes
        controller.courseCombox = mock(ChoiceBox.class);
        controller.examCombox = mock(ChoiceBox.class);
        controller.studentCombox = mock(ChoiceBox.class);

        // Use real observable lists for items
        ObservableList<String> courseItems = FXCollections.observableArrayList();
        ObservableList<String> examItems = FXCollections.observableArrayList();
        ObservableList<String> studentItems = FXCollections.observableArrayList();

        when(controller.courseCombox.getItems()).thenReturn(courseItems);
        when(controller.examCombox.getItems()).thenReturn(examItems);
        when(controller.studentCombox.getItems()).thenReturn(studentItems);


        controller.gradeStatisticList = Arrays.asList(
                new TeacherGradeStatisticController.GradeStatisticClass("Alice", "COMP3111", "Final", "90", "100", "60"),
                new TeacherGradeStatisticController.GradeStatisticClass("Bob", "COMP3112", "Midterm", "80", "100", "50")
        );

        // Call the method
        controller.populateChoiceBoxes();

        // Verify that unique values are added
        assertEquals(2, controller.courseCombox.getItems().size());
        assertTrue(controller.courseCombox.getItems().containsAll(Arrays.asList("COMP3111", "COMP3112")));

        assertEquals(2, controller.examCombox.getItems().size());
        assertTrue(controller.examCombox.getItems().containsAll(Arrays.asList("Final", "Midterm")));

        assertEquals(2, controller.studentCombox.getItems().size());
        assertTrue(controller.studentCombox.getItems().containsAll(Arrays.asList("Alice", "Bob")));
    }

    @Test
    void testPopulateChoiceBoxesWithDuplicates() {
        // Mock the ChoiceBoxes
        controller.courseCombox = mock(ChoiceBox.class);
        controller.examCombox = mock(ChoiceBox.class);
        controller.studentCombox = mock(ChoiceBox.class);

        // Use real observable lists for items
        ObservableList<String> courseItems = FXCollections.observableArrayList();
        ObservableList<String> examItems = FXCollections.observableArrayList();
        ObservableList<String> studentItems = FXCollections.observableArrayList();

        when(controller.courseCombox.getItems()).thenReturn(courseItems);
        when(controller.examCombox.getItems()).thenReturn(examItems);
        when(controller.studentCombox.getItems()).thenReturn(studentItems);


        // Mock gradeStatisticList with duplicate values
        controller.gradeStatisticList = Arrays.asList(
                new TeacherGradeStatisticController.GradeStatisticClass("Alice", "COMP3111", "Final", "90", "100", "60"),
                new TeacherGradeStatisticController.GradeStatisticClass("Alice", "COMP3111", "Final", "90", "100", "60"),
                new TeacherGradeStatisticController.GradeStatisticClass("Bob", "COMP3111", "Final", "80", "100", "50")
        );

        // Call the method
        controller.populateChoiceBoxes();

        // Verify that duplicates are not added
        assertEquals(1, controller.courseCombox.getItems().size());
        assertTrue(controller.courseCombox.getItems().contains("COMP3111"));

        assertEquals(1, controller.examCombox.getItems().size());
        assertTrue(controller.examCombox.getItems().contains("Final"));

        assertEquals(2, controller.studentCombox.getItems().size());
        assertTrue(controller.studentCombox.getItems().containsAll(Arrays.asList("Alice", "Bob")));
    }

    @Test
    void testPopulateChoiceBoxesWithEmptyList() {
        // Mock the ChoiceBoxes
        controller.courseCombox = mock(ChoiceBox.class);
        controller.examCombox = mock(ChoiceBox.class);
        controller.studentCombox = mock(ChoiceBox.class);

        // Use real observable lists for items
        ObservableList<String> courseItems = FXCollections.observableArrayList();
        ObservableList<String> examItems = FXCollections.observableArrayList();
        ObservableList<String> studentItems = FXCollections.observableArrayList();

        when(controller.courseCombox.getItems()).thenReturn(courseItems);
        when(controller.examCombox.getItems()).thenReturn(examItems);
        when(controller.studentCombox.getItems()).thenReturn(studentItems);


        // Mock an empty gradeStatisticList
        controller.gradeStatisticList = new ArrayList<>();

        // Call the method
        controller.populateChoiceBoxes();

        // Verify that choice boxes remain empty
        assertEquals(0, controller.courseCombox.getItems().size());
        assertEquals(0, controller.examCombox.getItems().size());
        assertEquals(0, controller.studentCombox.getItems().size());
    }

    @Test
    void testPopulateChoiceBoxesWithMixedData() {
        // Mock the ChoiceBoxes
        controller.courseCombox = mock(ChoiceBox.class);
        controller.examCombox = mock(ChoiceBox.class);
        controller.studentCombox = mock(ChoiceBox.class);

        // Use real observable lists for items
        ObservableList<String> courseItems = FXCollections.observableArrayList();
        ObservableList<String> examItems = FXCollections.observableArrayList();
        ObservableList<String> studentItems = FXCollections.observableArrayList();

        when(controller.courseCombox.getItems()).thenReturn(courseItems);
        when(controller.examCombox.getItems()).thenReturn(examItems);
        when(controller.studentCombox.getItems()).thenReturn(studentItems);


        // Mock gradeStatisticList with null and unusual data
        controller.gradeStatisticList = Arrays.asList(
                new TeacherGradeStatisticController.GradeStatisticClass("Alice", "COMP3111", "Final", "90", "100", "60"),
                new TeacherGradeStatisticController.GradeStatisticClass(null, "COMP3112", "Midterm", "80", "100", "50"),
                new TeacherGradeStatisticController.GradeStatisticClass("Bob", null, "Midterm", "70", "100", "40"),
                new TeacherGradeStatisticController.GradeStatisticClass("Charlie", "COMP3111", null, "60", "100", "30")
        );

        // Call the method
        controller.populateChoiceBoxes();

        // Verify that null values do not cause exceptions and valid data is added
        assertTrue(controller.courseCombox.getItems().containsAll(Arrays.asList("COMP3111", "COMP3112")));
        assertTrue(controller.examCombox.getItems().contains("Final"));
        assertTrue(controller.examCombox.getItems().contains("Midterm"));
        assertTrue(controller.studentCombox.getItems().containsAll(Arrays.asList("Alice", "Bob", "Charlie")));
    }

    @Test
    void testPopulateGradeStatisticListWithUniqueData() {
        // Mock data
        Grade mockGrade = new Grade("1", "0", "30", "60");
        Quiz mockQuiz = new Quiz("quiz1", "60", "COMP3111", "Yes", 0, "1|2");
        Student mockStudent = new Student("aliceyao", "Alice","Female","21","CSE","password",0);
        Question mockQuestion1 = new Question("description1", "optionA1", "optionB1", "optionC1", "optionD1", "A", "10", "Single", 1);
        Question mockQuestion2 = new Question("description2", "optionA2", "optionB2", "optionC2", "optionD2", "B", "20", "Single", 2);

        // Stub database methods
        when(controller.gradeDatabase.getAll()).thenReturn(List.of(mockGrade));
        when(controller.quizDatabase.getAll()).thenReturn(List.of(mockQuiz));
        when(controller.studentDatabase.getAll()).thenReturn(List.of(mockStudent));
        when(controller.courseDatabase.getAll()).thenReturn(Collections.emptyList());
        when(controller.questionDatabase.queryByKey("1")).thenReturn(mockQuestion1);
        when(controller.questionDatabase.queryByKey("2")).thenReturn(mockQuestion2);
        when(controller.quizDatabase.queryByKey("0")).thenReturn(mockQuiz);
        when(controller.studentDatabase.queryByKey(anyString())).thenReturn(mockStudent);

        controller.gradeStatisticList = new ArrayList<>();

        // Execute the method
        List <TeacherGradeStatisticController.GradeStatisticClass> gradeStatisticList = controller.populateGradeStatisticList();
        TeacherGradeStatisticController.GradeStatisticClass gradeStatistic = gradeStatisticList.get(0);

        // Assertions
        assertEquals(1, gradeStatisticList.size());
        assertEquals("Alice", gradeStatistic.getStudent());
        assertEquals("COMP3111", gradeStatistic.getCourse());
        assertEquals("quiz1", gradeStatistic.getExam());
        assertEquals("30", gradeStatistic.getFullScore());
        assertEquals("30", gradeStatistic.getScore());
        assertEquals("60", gradeStatistic.getTimeSpend());

        // Verify observable list is updated
        assertEquals(1, controller.gradeListObservable.size());
    }

}