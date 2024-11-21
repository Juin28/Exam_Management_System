package comp3111.examsystem.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import comp3111.examsystem.model.*;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.*;

class QuizViewControllerTest {
    @Mock
    private Database<Grade> mockGradeDatabase;
    @Mock
    private Database<Question> mockQuestionDatabase;
    @Mock
    private Timer mockTimer;
    @Mock
    private Label mockRTime;

    private QuizViewController controller;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        controller = new QuizViewController();

        controller.gradeDatabase = mockGradeDatabase;
        controller.questionDatabase = mockQuestionDatabase;
        controller.questionList = new ListView<>();
        controller.questionIds = new String[]{};
        controller.allQuestionDesc = new ArrayList<>();
        controller.questionDesc = "";
        controller.question = new Label();
        controller.currQuestion = new Question();
        controller.currQNum = new Label();
        controller.ansA = new Label();
        controller.ansB = new Label();
        controller.ansC = new Label();
        controller.ansD = new Label();
        controller.score = 0;
        controller.numOfCorrect = 0;
        controller.startTime = 0;
        controller.totalScore = 0;
        controller.currentQuestionIndex = 0;
        controller.timer = mockTimer;
        controller.rTime = mockRTime;
        controller.timer = new Timer();
        controller.selectedAnswers = new HashMap<>();
        controller.choiceA = new RadioButton();
        controller.choiceB = new RadioButton();
        controller.choiceC = new RadioButton();
        controller.choiceD = new RadioButton();
        controller.curStudent = new Student("bob", "bob", "Male", "18", "cse", "123", "0", 123);
        controller.currentQuiz = new Quiz("QuizTest", "1", "COMP3111", "yes", 321, "11|22");
    }

    @Test
    void testShowQuestions(){
        Question sampleQuestion1 = new Question("Q1", "Q1A", "Q1B", "Q1C", "Q1D", "AB", "20", "Multiple", 11);
        Question sampleQuestion2 = new Question("Q2", "Q2A", "Q2B", "Q2C", "Q2D", "AB", "20", "Multiple", 22);

        when(mockQuestionDatabase.queryByField("id", "11")).thenReturn(List.of(sampleQuestion1));
        when(mockQuestionDatabase.queryByField("id", "22")).thenReturn(List.of(sampleQuestion2));

        controller.showQuestions(controller.currentQuiz);

        assertEquals(2, controller.questionList.getItems().size());
    }

    @Test
    void testSetAnsDescriptions(){
        Question sampleQuestion1 = new Question(
                "Q1",
                "Q1A",
                "Q1B",
                "Q1C",
                "Q1D",
                "AB",
                "20",
                "Multiple",
                11);
        controller.setAnsDescriptions(sampleQuestion1);
        assertEquals("Q1A", controller.ansA.getText());
    }

    @Test
    void testUpdateQuestionUI(){
        Question sampleQuestion1 = new Question(
                "Q1",
                "Q1A",
                "Q1B",
                "Q1C",
                "Q1D",
                "AB",
                "20",
                "Multiple",
                11);
        when(mockQuestionDatabase.queryByField("questionDescription", "Q1")).thenReturn(List.of(sampleQuestion1));
        controller.allQuestionDesc.add(sampleQuestion1.getQuestionDescription());
        controller.updateQuestionUI();
        assertEquals("Question 1", controller.currQNum.getText());
    }

    @Test
    void testHandleNextNotLast(){
        Question sampleQuestion1 = new Question("Q1", "Q1A", "Q1B", "Q1C", "Q1D", "AB", "20", "Multiple", 11);
        Question sampleQuestion2 = new Question("Q2", "Q2A", "Q2B", "Q2C", "Q2D", "AB", "20", "Multiple", 22);
        when(mockQuestionDatabase.queryByField("questionDescription", "Q2")).thenReturn(List.of(sampleQuestion1));
        controller.allQuestionDesc.add(sampleQuestion1.getQuestionDescription());
        controller.allQuestionDesc.add(sampleQuestion2.getQuestionDescription());
        controller.currentQuestionIndex = 0;
        controller.handleNext();
        assertEquals(1, controller.currentQuestionIndex);
    }

    @Test
    void testHandleNextLast(){
        Question sampleQuestion1 = new Question("Q1", "Q1A", "Q1B", "Q1C", "Q1D", "AB", "20", "Multiple", 11);
        controller.allQuestionDesc.add(sampleQuestion1.getQuestionDescription());
        controller.currentQuestionIndex = 0;
        Platform.runLater(() ->{
            try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
                controller.handleNext();
                mockedMsgSender.verify(() -> MsgSender.showMsg("This is the last question."));
            }});
    }

    @Test
    void testCheckAnswer(){
        Question sampleQuestion1 = new Question("Q1", "Q1A", "Q1B", "Q1C", "Q1D", "A", "20", "Multiple", 11);
        controller.questionIds = new String[]{"11"};
        when(mockQuestionDatabase.queryByField("id", "11")).thenReturn(List.of(sampleQuestion1));
        List<String> sampleAnswer = new ArrayList<>();
        sampleAnswer.add("A");
        controller.selectedAnswers.put(Long.valueOf("11"), sampleAnswer);
        controller.checkAnswers();
        assertEquals(20, controller.score);
    }

    @Test
    void testUpdateSelectedAnswersSelectNotEmpty(){
        Question sampleQuestion1 = new Question("Q1", "Q1A", "Q1B", "Q1C", "Q1D", "A", "20", "Multiple", 11);
        controller.currQuestion = sampleQuestion1;
        List<String> sampleAnswer = new ArrayList<>();
        sampleAnswer.add("A");
        controller.selectedAnswers.put(Long.valueOf("11"), sampleAnswer);
        controller.updateSelectedAnswers("A", true);
        assert (controller.selectedAnswers.get(Long.valueOf("11")).contains("A"));
    }

    @Test
    void testUpdateSelectedAnswersSelectEmpty(){
        Question sampleQuestion1 = new Question("Q1", "Q1A", "Q1B", "Q1C", "Q1D", "A", "20", "Multiple", 11);
        controller.currQuestion = sampleQuestion1;
        List<String> sampleAnswer = new ArrayList<>();
        controller.selectedAnswers.put(Long.valueOf("11"), sampleAnswer);
        controller.updateSelectedAnswers("A", true);
        assert (controller.selectedAnswers.get(Long.valueOf("11")).contains("A"));
    }

    @Test
    void testUpdateSelectedAnswersDeselect(){
        Question sampleQuestion1 = new Question("Q1", "Q1A", "Q1B", "Q1C", "Q1D", "A", "20", "Multiple", 11);
        controller.currQuestion = sampleQuestion1;
        List<String> sampleAnswer = new ArrayList<>();
        sampleAnswer.add("A");
        controller.selectedAnswers.put(Long.valueOf("11"), sampleAnswer);
        controller.updateSelectedAnswers("A", false);
        assert (!controller.selectedAnswers.get(Long.valueOf("11")).contains("A"));
    }
}