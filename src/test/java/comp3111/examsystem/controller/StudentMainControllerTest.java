package comp3111.examsystem.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import comp3111.examsystem.model.*;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.JavaFXInitializer;
import comp3111.examsystem.service.MsgSender;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.*;
class StudentMainControllerTest {
    @Mock
    private Database<Quiz> mockQuizDatabase;
    @Mock
    private Database<Grade> mockGradeDatabase;
    private StudentMainController controller;

    @BeforeAll
    static void initToolkit() {
        JavaFXInitializer.initToolkit();

    }

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        controller = new StudentMainController();

        controller.examCombox = new ComboBox<>();
        controller.quizDatabase = mockQuizDatabase;
        controller.gradeDatabase = mockGradeDatabase;
        controller.quizzes = new ArrayList<>();
        controller.studentGrades = new ArrayList<>();
        controller.student = new Student("StudentTest", "Student", "Male", "18", "cse", "password", "0", 1);
    }

    @Test
    void testLoadStudentGrades(){
        Grade sampleGrade = new Grade("1", "1", "100", "1");
        when(mockGradeDatabase.getAll()).thenReturn(List.of(sampleGrade));
        controller.loadStudentGrades();
    }

    @Test
    void testFormatQuizInfoNotPublised(){
        Quiz sampleQuiz = new Quiz("QuizTest", "1", "COMP3111", "no", 1, "1");
        controller.formatQuizInfo(false, false, sampleQuiz);
    }

    @Test
    void testFormatQuizInfoTaken(){
        Quiz sampleQuiz = new Quiz("QuizTest", "1", "COMP3111", "yes", 1, "1");
        controller.formatQuizInfo(true, true, sampleQuiz);
    }

    @Test
    void testFormatQuizInfoNotTakenPublished(){
        Quiz sampleQuiz = new Quiz("QuizTest", "1", "COMP3111", "yes", 1, "1");
        controller.formatQuizInfo(false, true, sampleQuiz);
        assert controller.quizzes.contains("COMP3111 | QuizTest");
    }

    @Test
    void testCheckQuizValidityTaken(){
        Quiz sampleQuiz = new Quiz("QuizTest", "1", "COMP3111", "yes", 1, "1");
        Grade sampleGrade = new Grade("1", "1", "100", "1");
        controller.studentGrades.add(sampleGrade);
        when(mockQuizDatabase.getAll()).thenReturn(List.of(sampleQuiz));
        controller.checkQuizValidity();
        assert !controller.quizzes.contains("COMP3111 | QuizTest");
    }

    @Test
    void testCheckQuizValidityNotPublished(){
        Quiz sampleQuiz = new Quiz("QuizTest", "1", "COMP3111", "no", 1, "1");
        Grade sampleGrade = new Grade("1", "1", "100", "1");
        controller.studentGrades.add(sampleGrade);
        when(mockQuizDatabase.getAll()).thenReturn(List.of(sampleQuiz));
        controller.checkQuizValidity();
        assert !controller.quizzes.contains("COMP3111 | QuizTest");
    }

    @Test
    void testCheckQuizValidityNotTaken(){
        Quiz sampleQuiz = new Quiz("QuizTest", "1", "COMP3111", "yes", 1, "1");
        when(mockQuizDatabase.getAll()).thenReturn(List.of(sampleQuiz));
        controller.checkQuizValidity();
        assert controller.quizzes.contains("COMP3111 | QuizTest");
    }

    @Test
    void testFormatString(){
        String sampleString = "COMP3111 | QuizTest";
        String[] result = StudentMainController.formatString(sampleString);
        assertEquals("COMP3111", result[0]);
    }

    @Test
    void testFormatStringEmpty(){
        String[] result = StudentMainController.formatString(null);
        assertNull(result);
    }

    @Test
    void testOpenExamUI(){
        controller.examCombox.getItems().add(null);
        Platform.runLater(() ->{
            try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
                controller.openExamUI(new ActionEvent());
                mockedMsgSender.verify(() -> MsgSender.showMsg("Please select a quiz to start."));
            }});
    }

    @Test
    void testOpenExamUI2() {
        // Mock the MsgSender
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Test when no quiz is selected
            controller.examCombox.setValue(null);
            controller.openExamUI(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please select a quiz to start."));

            // Test when a quiz is selected
            Quiz sampleQuiz = new Quiz("QuizTest", "1", "COMP3111", "yes", 1, "1");
            when(mockQuizDatabase.queryByField("courseID", "COMP3111")).thenReturn(List.of(sampleQuiz));
            controller.examCombox.setValue("COMP3111 | QuizTest");
            Platform.runLater(() -> {
                controller.openExamUI(new ActionEvent());
                assertEquals(sampleQuiz, StudentMainController.chosenQuiz);
            });
        }
    }

}