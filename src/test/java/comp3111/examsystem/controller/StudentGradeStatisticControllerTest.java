package comp3111.examsystem.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import comp3111.examsystem.model.*;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.JavaFXInitializer;
import comp3111.examsystem.service.MsgSender;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.*;

class StudentGradeStatisticControllerTest {
    @Mock
    private Database<Grade> mockGradeDatabase;
    @Mock
    private Database<Course> mockCourseDatabase;
    @Mock
    private Database<Quiz> mockQuizDatabase;
    @Mock
    private Database<Question> mockQuestionDatabase;
    private StudentGradeStatisticController controller;

    @BeforeAll
    static void initToolkit() {
        JavaFXInitializer.initToolkit();

    }

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        controller = new StudentGradeStatisticController();

        controller.gradeDatabase = mockGradeDatabase;
        controller.courseDatabase = mockCourseDatabase;
        controller.quizDatabase = mockQuizDatabase;
        controller.questionDatabase = mockQuestionDatabase;
        controller.currStudent = new Student(
                "bob",
                "bob",
                "Male",
                "18",
                "cse",
                "123",
                "0",
                123);
        controller.courseCombox = new ChoiceBox<>();
        controller.coursesList = new ArrayList<>();
        controller.questionIds = new String[]{};
        controller.courseColumn = new TableColumn<>();
        controller.examColumn = new TableColumn<>();
        controller.scoreColumn = new TableColumn<>();
        controller.fullScoreColumn = new TableColumn<>();
        controller.timeSpendColumn = new TableColumn<>();
        controller.gradeTable = new TableView<>();
        controller.gradeRows = FXCollections.observableArrayList();
        controller.barChart = new BarChart<String, Number>(new CategoryAxis(), new NumberAxis());
    }

    @Test
    void testShowCourses(){
        Grade sampleGrade = new Grade("123", "1", "100", "1");
        Quiz sampleQuiz = new Quiz("QuizTest", "1", "COMP3111", "yes", 321, "11|22");
        Course sampleCourse = new Course("Course1", "COMP3111", "cse", 213);
        when(mockGradeDatabase.getAll()).thenReturn(List.of(sampleGrade));
        when(mockQuizDatabase.queryByField("id", sampleGrade.getQuestionId())).thenReturn(List.of(sampleQuiz));
        when(mockCourseDatabase.getAll()).thenReturn(List.of(sampleCourse));
        controller.showCourses();
        assert controller.coursesList.contains("Course1");
    }

    @Test
    void testShowCoursesQuizzesEmpty(){
        Grade sampleGrade = new Grade("123", "1", "100", "1");
        Course sampleCourse = new Course("Course1", "COMP3111", "cse", 213);
        when(mockGradeDatabase.getAll()).thenReturn(List.of(sampleGrade));
        when(mockQuizDatabase.queryByField("id", sampleGrade.getQuestionId())).thenReturn(new ArrayList<>());
        when(mockCourseDatabase.getAll()).thenReturn(List.of(sampleCourse));
        controller.showCourses();
        assert !controller.coursesList.contains("Course1");
    }

    @Test
    void testInitTable(){
        Grade sampleGrade = new Grade("123", "1", "100", "1");
        Quiz sampleQuiz = new Quiz("QuizTest", "1", "COMP3111", "yes", 321, "11");
        Question sampleQuestion = new Question("Q1", "Q1A", "Q1B", "Q1C", "Q1D", "AB", "20", "Multiple", 11);
        Course sampleCourse = new Course("Course1", "COMP3111", "cse", 213);
        controller.coursesList.add("Course1");
        when(mockGradeDatabase.getAll()).thenReturn(List.of(sampleGrade));
        when(mockQuizDatabase.queryByField("id", sampleGrade.getQuestionId())).thenReturn(List.of(sampleQuiz));
        when(mockQuestionDatabase.queryByField("id", "11")).thenReturn(List.of(sampleQuestion));
        when(mockCourseDatabase.queryByField("courseID", sampleQuiz.getCourseID())).thenReturn(List.of(sampleCourse));
        controller.initTable();
        assert controller.gradeRows.size() == 1;
    }

    @Test
    void testQuery(){
        StudentGradeStatisticController.GradeTableRow sampleRow = new StudentGradeStatisticController.GradeTableRow(
                "Course1",
                "QuizTest",
                "100",
                "100",
                "1");
        controller.gradeRows.add(sampleRow);
        controller.courseCombox.setValue("Course1");
        controller.query(new ActionEvent());
        assert controller.gradeTable.getItems().size() == 1;
        assert controller.gradeTable.getItems().getFirst().getCourseName().equals("Course1");
    }

    @Test
    void testShowBarChart(){
        StudentGradeStatisticController.GradeTableRow sampleRow = new StudentGradeStatisticController.GradeTableRow(
                "Course1",
                "QuizTest",
                "100",
                "100",
                "1");
        controller.gradeRows.add(sampleRow);
        controller.showBarChart();
        assert controller.barChart.getData().getFirst().getData().getFirst().getXValue().equals("QuizTest");
    }


}