package comp3111.examsystem.controller;

import static junit.framework.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import comp3111.examsystem.model.Course;
import comp3111.examsystem.model.Question;
import comp3111.examsystem.model.Quiz;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class TeacherExamManagementControllerTest {

    @Mock
    private Database<Quiz> mockQuizDatabase;
    @Mock
    private Database<Question> mockQuestionDatabase;
    @Mock
    private Database<Course> mockCourseDatabase;

    private TeacherExamManagementController controller;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() throws Exception{
        MockitoAnnotations.openMocks(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/TeacherExamManagementUI.fxml"));
        Parent root = loader.load();

        controller = new TeacherExamManagementController();

        controller.quizDatabase = mockQuizDatabase;
        controller.questionDatabase = mockQuestionDatabase;
        controller.courseDatabase = mockCourseDatabase;
        controller.examTable = new TableView<>();
        controller.examQuestionsTable = new TableView<>();
        controller.allQuestionsTable = new TableView<>();
        controller.examNameInput = new TextField();
        controller.examTimeInput = new TextField();
        controller.courseIDInput = new ChoiceBox<>();
        controller.publishInput = new ChoiceBox<>();
        controller.examNameFilterTextField = new TextField();
        controller.courseIDFilterChoiceBox = new ChoiceBox<>();
        controller.publishStatusFilterChoiceBox = new ChoiceBox<>();
        controller.questionFilterTextField = new TextField();
        controller.typeFilterChoiceBox = new ChoiceBox<>();
        controller.scoreFilterTextField = new TextField();
        controller.allQuestions = new ArrayList<>();
        controller.questionList = FXCollections.observableArrayList();
        controller.allQuizzes = new ArrayList<>();
        controller.quizList = FXCollections.observableArrayList();
        controller.allCourses = new ArrayList<>();
        controller.courseList = FXCollections.observableArrayList();
        controller.examQuestionList = FXCollections.observableArrayList();

    }

    @Test
    void testAddExamValid() {
        controller.examNameInput.setText("Sample Exam");
        controller.examTimeInput.setText("60");
        controller.courseIDInput.setValue("COMP3111");
        controller.publishInput.setValue("Yes");

        Question sampleQuestion = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        controller.examQuestionList.add(sampleQuestion);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addExam(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("Exam added successfully."));
        }
    }

    @Test
    void testAddExamInvalidEmptyField() {
        controller.examNameInput.setText("");
        controller.examTimeInput.setText("60");
        controller.courseIDInput.setValue("COMP3111");
        controller.publishInput.setValue("Yes");

        Question sampleQuestion = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        controller.examQuestionList.add(sampleQuestion);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addExam(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("All fields must be filled."));
        }
    }

    @Test
    void testAddExamInvalidNoQuestion() {
        controller.examNameInput.setText("Sample Exam");
        controller.examTimeInput.setText("60");
        controller.courseIDInput.setValue("COMP3111");
        controller.publishInput.setValue("Yes");

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addExam(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("At least one question must be added to the exam."));
        }
    }

    @Test
    void testAddExamInvalidTime() {
        controller.examNameInput.setText("Sample Exam");
        controller.examTimeInput.setText("ABC");
        controller.courseIDInput.setValue("COMP3111");
        controller.publishInput.setValue("Yes");

        Question sampleQuestion = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        controller.examQuestionList.add(sampleQuestion);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addExam(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("Exam time must be a number."));
        }
    }

    @Test
    void testAddExamNegativeTime() {
        controller.examNameInput.setText("Sample Exam");
        controller.examTimeInput.setText("-60");
        controller.courseIDInput.setValue("COMP3111");
        controller.publishInput.setValue("Yes");

        Question sampleQuestion = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        controller.examQuestionList.add(sampleQuestion);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addExam(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("Exam time must be a positive number."));
        }
    }

    @Test
    void testAddExamRepeatedExam() {
        controller.examNameInput.setText("Sample Exam");
        controller.examTimeInput.setText("60");
        controller.courseIDInput.setValue("COMP3111");
        controller.publishInput.setValue("Yes");

        Question sampleQuestion = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        controller.examQuestionList.add(sampleQuestion);
        Quiz sampleQuiz = new Quiz("Sample Exam", "60", "COMP3111", "Yes", 123, "123|");
        controller.allQuizzes.add(sampleQuiz);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addExam(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("The exam already exists."));
        }
    }

    @Test
    void testAddExamError() {
        controller.examNameInput.setText("Sample Exam");
        controller.examTimeInput.setText("60");
        controller.courseIDInput.setValue("COMP3111");
        controller.publishInput.setValue("Yes");

        Question sampleQuestion = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        controller.examQuestionList.add(sampleQuestion);

        doThrow(new RuntimeException()).when(mockQuizDatabase).add(any(Quiz.class));

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addExam(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("An error occurred while adding the exam."));
        }
    }

    @Test
    void testGetQuestionIDs() {
        Question sampleQuestion1 = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "AB", "20", "Multiple", 123);
        Question sampleQuestion2 = new Question("What is C++?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 456);
        controller.examQuestionList.add(sampleQuestion1);
        controller.examQuestionList.add(sampleQuestion2);

        String questionIDs = controller.getQuestionIDs(controller.examQuestionList);
        assertEquals("123|456", questionIDs);
    }

    @Test
    void testDeleteExamValid() {
        // Create a sample quiz and add it to the examTable
        Quiz quiz = new Quiz("Sample Exam", "60", "COMP3111", "Yes", 123, "123|");
        controller.examTable.setItems(FXCollections.observableArrayList(quiz));
        controller.examTable.getSelectionModel().select(quiz);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Mock the showConfirm method to execute the callback
            mockedMsgSender.when(() -> MsgSender.showConfirm(anyString(), anyString(), any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable callback = invocation.getArgument(2);
                        callback.run();
                        return null;
                    });

            // Call the method to be tested
            controller.deleteExam(new ActionEvent());

            // Verify that showMsg was called with the correct argument
            mockedMsgSender.verify(() -> MsgSender.showMsg("Exam deleted successfully."));
        }
    }

    @Test
    void testDeleteExamWithNoExamQuestion() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.examTable.getSelectionModel().clearSelection();

            // Call the method to be tested
            controller.deleteExam(new ActionEvent());

            // Verify that showMsg was called
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please select an exam to delete."));
        }
    }

    @Test
    void testDeleteExamError() {
        // Create a sample quiz and add it to the examTable
        Quiz quiz = new Quiz("Sample Exam", "60", "COMP3111", "Yes", 123, "123|");
        controller.examTable.setItems(FXCollections.observableArrayList(quiz));
        controller.examTable.getSelectionModel().select(quiz);

        doThrow(new RuntimeException()).when(mockQuizDatabase).delByKey(anyString());

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Mock the showConfirm method to execute the callback
            mockedMsgSender.when(() -> MsgSender.showConfirm(anyString(), anyString(), any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable callback = invocation.getArgument(2);
                        callback.run();
                        return null;
                    });

            // Call the method to be tested
            controller.deleteExam(new ActionEvent());

            // Verify that showMsg was called with the correct argument
            mockedMsgSender.verify(() -> MsgSender.showMsg("An error occurred while deleting the exam."));
        }
    }

    @Test
    void testUpdateExamValid() {
        // Create a sample quiz and add it to the examTable
        Quiz quiz = new Quiz("Sample Exam", "60", "COMP3111", "No", 123, "123|");
        controller.examTable.setItems(FXCollections.observableArrayList(quiz));
        controller.examTable.getSelectionModel().select(quiz);

        controller.examNameInput.setText("Sample Exam");
        controller.examTimeInput.setText("60");
        controller.courseIDInput.setValue("COMP3111");
        controller.publishInput.setValue("Yes");

        Question sampleQuestion = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        controller.examQuestionList.add(sampleQuestion);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Mock the showConfirm method to execute the callback
            mockedMsgSender.when(() -> MsgSender.showConfirm(anyString(), anyString(), any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable callback = invocation.getArgument(2);
                        callback.run();
                        return null;
                    });

            // Call the method to be tested
            controller.updateExam(new ActionEvent());

            // Verify that showMsg was called with the correct argument
            mockedMsgSender.verify(() -> MsgSender.showMsg("Exam updated successfully."));
        }
    }

    @Test
    void testUpdatePublishedExam() {
        // Create a sample quiz and add it to the examTable
        Quiz quiz = new Quiz("Sample Exam", "60", "COMP3111", "Yes", 123, "123|");
        controller.examTable.setItems(FXCollections.observableArrayList(quiz));
        controller.examTable.getSelectionModel().select(quiz);

        controller.examNameInput.setText("Sample Exam");
        controller.examTimeInput.setText("60");
        controller.courseIDInput.setValue("COMP3111");
        controller.publishInput.setValue("No");

        Question sampleQuestion = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        controller.examQuestionList.add(sampleQuestion);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Mock the showConfirm method to execute the callback
            mockedMsgSender.when(() -> MsgSender.showConfirm(anyString(), anyString(), any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable callback = invocation.getArgument(2);
                        callback.run();
                        return null;
                    });

            // Call the method to be tested
            controller.updateExam(new ActionEvent());

            // Verify that showMsg was called with the correct argument
            mockedMsgSender.verify(() -> MsgSender.showMsg("Cannot update a published exam."));
        }
    }

    @Test
    void testUpdateExamWithNoSelectedExam() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.examTable.getSelectionModel().clearSelection();

            // Call the method to be tested
            controller.updateExam(new ActionEvent());

            // Verify that showMsg was called with the correct argument
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please select an exam to update."));
        }
    }

    @Test
    void testUpdateExamInvalidEmptyField() {
        // Create a sample quiz and add it to the examTable
        Quiz quiz = new Quiz("Sample Exam", "60", "COMP3111", "No", 123, "123|");
        controller.examTable.setItems(FXCollections.observableArrayList(quiz));
        controller.examTable.getSelectionModel().select(quiz);

        controller.examNameInput.setText("");
        controller.examTimeInput.setText("60");
        controller.courseIDInput.setValue("COMP3111");
        controller.publishInput.setValue("No");

        Question sampleQuestion = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        controller.examQuestionList.add(sampleQuestion);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Call the method to be tested
            controller.updateExam(new ActionEvent());

            // Verify that showMsg was called with the correct argument
            mockedMsgSender.verify(() -> MsgSender.showMsg("All fields must be filled."));
        }
    }

    @Test
    void testUpdateExamError() {
        // Create a sample quiz and add it to the examTable
        Quiz quiz = new Quiz("Sample Exam", "60", "COMP3111", "No", 123, "123|");
        controller.examTable.setItems(FXCollections.observableArrayList(quiz));
        controller.examTable.getSelectionModel().select(quiz);

        controller.examNameInput.setText("Sample Exam");
        controller.examTimeInput.setText("60");
        controller.courseIDInput.setValue("COMP3111");
        controller.publishInput.setValue("No");

        Question sampleQuestion = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        controller.examQuestionList.add(sampleQuestion);

        doThrow(new RuntimeException()).when(mockQuizDatabase).update(any(Quiz.class));

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Mock the showConfirm method to execute the callback
            mockedMsgSender.when(() -> MsgSender.showConfirm(anyString(), anyString(), any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable callback = invocation.getArgument(2);
                        callback.run();
                        return null;
                    });

            // Call the method to be tested
            controller.updateExam(new ActionEvent());

            // Verify that showMsg was called with the correct argument
            mockedMsgSender.verify(() -> MsgSender.showMsg("An error occurred while updating the exam."));
        }
    }

    @Test
    void testFilterQuestions() {
        controller.questionFilterTextField.setText("Java");
        controller.scoreFilterTextField.setText("10");
        controller.typeFilterChoiceBox.setValue("Multiple Choice");

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.filterQuestions(new ActionEvent());
            mockedMsgSender.verifyNoInteractions();
        }
    }

    @Test
    void testFilterQuizzes() {
        controller.examNameFilterTextField.setText("Sample Quiz");
        controller.courseIDFilterChoiceBox.setValue("COMP3111");
        controller.publishStatusFilterChoiceBox.setValue("Yes");

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.filterQuizzes(new ActionEvent());
            mockedMsgSender.verifyNoInteractions();
        }
    }

    @Test
    void testRefreshExamManagementPageWithUnsavedChanges() {
        // Set up the input fields with unsaved changes
        controller.examNameInput.setText("Sample Exam");
        controller.examTimeInput.setText("60");

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Call the method to be tested
            controller.refreshExamManagementPage(new ActionEvent());

            // Verify that the showConfirm method was called
            mockedMsgSender.verify(() -> MsgSender.showConfirm(eq("Refresh Confirmation"), eq("Refreshing will clear all fields and any unsaved changes will be lost. Are you sure you want to refresh?"), any(Runnable.class)));
        }
    }

    @Test
    void testRefreshExamManagementPageWithoutUnsavedChanges() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Call the method to be tested
            controller.refreshExamManagementPage(new ActionEvent());

            // Verify that the confirmation message was not shown
            mockedMsgSender.verifyNoInteractions();
        }
    }

    @Test
    void testRefreshExamConfirmed() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Mock the showConfirm method to execute the callback
            mockedMsgSender.when(() -> MsgSender.showConfirm(anyString(), anyString(), any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable callback = invocation.getArgument(2);
                        callback.run();
                        return null;
                    });

            // Set up the input fields with unsaved changes
            controller.examNameInput.setText("Sample Quiz");
            controller.examTimeInput.setText("60");

            // Spy on the controller to verify method calls
            TeacherExamManagementController spyController = spy(controller);

            // Call the method to be tested
            spyController.refreshExamManagementPage(new ActionEvent());

            // Verify that the refreshQuestionConfirmed method was called
            verify(spyController).refreshExamManagementPageConfirmed(any(ActionEvent.class));
        }
    }

    @Test
    void testLoadAllQuestionsWithNoFilter() {
        // Create a list of questions to be returned by the mock
        List<Question> mockQuestions = new ArrayList<>();
        mockQuestions.add(new Question("Sample Question 1", "A", "B", "C", "D", "A", "10", "Single", 123));
        mockQuestions.add(new Question("Sample Question 2", "A", "B", "C", "D", "A", "10", "Single", 123));

        // Stub the getAll method to return the mockQuestions list
        when(mockQuestionDatabase.getAll()).thenReturn(mockQuestions);

        // Call the method to be tested
        controller.loadAllQuestions();

        // Verify that the getAll method was called on the mock
        verify(mockQuestionDatabase).getAll();

        // Assert that the questionList contains the mockQuestions
        assertEquals(mockQuestions, controller.questionList);
    }

    @Test
    void testLoadAllQuestionsWithFilter() {
        // Create a list of questions to be returned by the mock
        List<Question> allQuestions = new ArrayList<>();
        allQuestions.add(new Question("Sample Question 1", "A", "B", "C", "D", "A", "10", "Single", 123));
        allQuestions.add(new Question("Sample Question 2", "A", "B", "C", "D", "A", "10", "Multiple", 124));

        // Stub the getAll method to return the allQuestions list
        when(mockQuestionDatabase.getAll()).thenReturn(allQuestions);

        // Set up the filter criteria
        controller.typeFilterChoiceBox.setValue("Single");

        // Call the method to be tested
        controller.loadAllQuestions();

        // Verify that the getAll method was called on the mock
        verify(mockQuestionDatabase).getAll();

        // Create the expected filtered list
        List<Question> filteredQuestions = allQuestions.stream()
                .filter(q -> "Single".equals(q.getQuestionType()))
                .collect(Collectors.toList());

        // Assert that the questionList contains the filteredQuestions
        assertEquals(filteredQuestions, controller.questionList);
    }

    @Test
    void testLoadQuizzesWithNoFilter() {
        // Create a list of quizzes to be returned by the mock
        List<Quiz> allQuizzes = new ArrayList<>();
        allQuizzes.add(new Quiz("Sample Quiz 1", "60", "COMP3111", "Yes", 1, "123|"));
        allQuizzes.add(new Quiz("Sample Quiz 2", "90", "COMP3112", "No", 2, "124|"));

        // Stub the getAll method to return the allQuizzes list
        when(mockQuizDatabase.getAll()).thenReturn(allQuizzes);

        // Run the loadQuizzes method on the JavaFX Application Thread
        Platform.runLater(() -> {
            // Call the method to be tested
            controller.loadQuizzes();

            // Assert that the quizList contains allQuizzes
            assertEquals(allQuizzes, controller.quizList);
        });

        // Wait for the JavaFX Application Thread to process the runLater task
        try {
            Thread.sleep(1000); // Adjust the sleep time if necessary
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testLoadQuizzesWithFilter() {
        // Create a list of quizzes to be returned by the mock
        List<Quiz> allQuizzes = new ArrayList<>();
        allQuizzes.add(new Quiz("Sample Quiz 1", "60", "COMP3111", "Yes", 1, "123|"));
        allQuizzes.add(new Quiz("Sample Quiz 2", "90", "COMP3112", "No", 2, "124|"));

        // Stub the getAll method to return the allQuizzes list
        when(mockQuizDatabase.getAll()).thenReturn(allQuizzes);

        // Set up the filter criteria
        controller.courseIDFilterChoiceBox.setValue("COMP3111");

        // Run the loadQuizzes method on the JavaFX Application Thread
        Platform.runLater(() -> {
            // Call the method to be tested
            controller.loadQuizzes();

            // Create the expected filtered list
            List<Quiz> filteredQuizzes = allQuizzes.stream()
                    .filter(q -> "COMP3111".equals(q.getCourseID()))
                    .collect(Collectors.toList());

            // Assert that the quizList contains the filteredQuizzes
            assertEquals(filteredQuizzes, controller.quizList);
        });

        // Wait for the JavaFX Application Thread to process the runLater task
        try {
            Thread.sleep(1000); // Adjust the sleep time if necessary
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testValidInputsEmptyExamName() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("", "60", "COMP3111", "Yes", "123|");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("All fields must be filled."));
        }
    }

    @Test
    void testValidInputsEmptyExamTime() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("Sample Exam", "", "COMP3111", "Yes", "123|");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("All fields must be filled."));
        }
    }

    @Test
    void testValidInputsEmptyCourseID() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("Sample Exam", "60", "", "Yes", "123|");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("All fields must be filled."));
        }
    }

    @Test
    void testValidInputsEmptyPublishStatus() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("Sample Exam", "60", "COMP3111", "", "123|");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("All fields must be filled."));
        }
    }

    @Test
    void testValidInputsEmptyQuestionIDs() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("Sample Exam", "60", "COMP3111", "Yes", "");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("At least one question must be added to the exam."));
        }
    }

    @Test
    void testExamQuestionList() {
        // Create sample questions
        Question sampleQuestion1 = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "AB", "20", "Multiple", 123);
        Question sampleQuestion2 = new Question("What is C++?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 456);

        // Add sample questions to the controller's examQuestionList
        controller.examQuestionList.add(sampleQuestion1);
        controller.examQuestionList.add(sampleQuestion2);

        // Mock the questionDatabase and stub the queryByKeys method
        List<String> questionIDsList = List.of("123", "456");
        when(mockQuestionDatabase.queryByKeys(questionIDsList)).thenReturn(List.of(sampleQuestion1, sampleQuestion2));

        // Call the method to be tested
        List<Question> result = controller.getExamQuestionList("123|456|");

        // Verify the result
        assertEquals(2, result.size());
        assertEquals(List.of(sampleQuestion1, sampleQuestion2), result);
    }

    @Test
    void testAddQuestionToExams() {
        // Create a sample question
        Question sampleQuestion = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);

        // Mock the selection model of the allQuestionsTable
        TableView<Question> mockAllQuestionsTable = mock(TableView.class);
        TableView.TableViewSelectionModel<Question> mockSelectionModel = mock(TableView.TableViewSelectionModel.class);
        when(mockAllQuestionsTable.getSelectionModel()).thenReturn(mockSelectionModel);
        when(mockSelectionModel.getSelectedItem()).thenReturn(sampleQuestion);

        // Set the mock table in the controller
        controller.allQuestionsTable = mockAllQuestionsTable;

        // Call the method to be tested
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addQuestionToExams(new ActionEvent());

            // Verify that the question was added to the examQuestionList
            assertTrue(controller.examQuestionList.contains(sampleQuestion));

            // Verify that the success message was shown
            mockedMsgSender.verify(() -> MsgSender.showMsg("Question added to the exam successfully."));
        }
    }

    @Test
    void testAddQuestionToExamsNoQuestionSelected() {
        // Create a sample question
        Question sampleQuestion = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);

        // Mock the selection model of the allQuestionsTable
        TableView<Question> mockAllQuestionsTable = mock(TableView.class);
        TableView.TableViewSelectionModel<Question> mockSelectionModel = mock(TableView.TableViewSelectionModel.class);
        when(mockAllQuestionsTable.getSelectionModel()).thenReturn(mockSelectionModel);
        when(mockSelectionModel.getSelectedItem()).thenReturn(null);

        // Set the mock table in the controller
        controller.allQuestionsTable = mockAllQuestionsTable;

        // Call the method to be tested
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addQuestionToExams(new ActionEvent());

            // Verify that the question was added to the examQuestionList
            assertFalse(controller.examQuestionList.contains(sampleQuestion));

            // Verify that the success message was shown
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please select a question to add to the exam."));
        }
    }

    @Test
    void testAddQuestionToExamsRepeatedQuestions() {
        // Create a sample question
        Question sampleQuestion = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);

        // Mock the selection model of the allQuestionsTable
        TableView<Question> mockAllQuestionsTable = mock(TableView.class);
        TableView.TableViewSelectionModel<Question> mockSelectionModel = mock(TableView.TableViewSelectionModel.class);
        when(mockAllQuestionsTable.getSelectionModel()).thenReturn(mockSelectionModel);
        when(mockSelectionModel.getSelectedItem()).thenReturn(sampleQuestion);

        // Set the mock table in the controller
        controller.allQuestionsTable = mockAllQuestionsTable;

        controller.examQuestionList.add(sampleQuestion);

        // Call the method to be tested
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addQuestionToExams(new ActionEvent());

            // Verify that the success message was shown
            mockedMsgSender.verify(() -> MsgSender.showMsg("Question already added to the exam."));
        }
    }

    @Test
    void testAddQuestionToExamsError() {
        // Create a sample question
        Question sampleQuestion = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);

        // Create a real ObservableList
        ObservableList<Question> questionList = FXCollections.observableArrayList();
        questionList.add(sampleQuestion);
        ObservableList<Question> examQuestionList = FXCollections.observableArrayList();

        // Set the real list in your controller
        controller.questionList = questionList;
        controller.examQuestionList.clear();

        // Create a mock TableView and set it up
        TableView<Question> mockAllQuestionsTable = mock(TableView.class);
        TableView.TableViewSelectionModel<Question> mockSelectionModel = mock(TableView.TableViewSelectionModel.class);
        when(mockAllQuestionsTable.getSelectionModel()).thenReturn(mockSelectionModel);
        when(mockAllQuestionsTable.getItems()).thenReturn(questionList);
        when(mockSelectionModel.getSelectedItem()).thenReturn(sampleQuestion);

        // Set the mock table in the controller
        controller.allQuestionsTable = mockAllQuestionsTable;

        // Simulate an error when adding the question
        // You can spy on the real list if needed
        ObservableList<Question> spyExamQuestionList = Mockito.spy(examQuestionList);
        controller.examQuestionList = spyExamQuestionList;

        // Stubbing the remove method to throw an exception
        doThrow(new RuntimeException()).when(spyExamQuestionList).add(any(Question.class));

        // Call the method to be tested
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addQuestionToExams(new ActionEvent());

            // Verify that the error message was shown
            mockedMsgSender.verify(() -> MsgSender.showMsg("An error occurred while adding the question to the exam."));
        }
    }

    @Test
    void testRemoveQuestionFromExam() {
        // Create a sample question
        Question sampleQuestion1 = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        Question sampleQuestion2 = new Question("What is C++?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 456);

        // Add the sample question to the examQuestionList
        controller.examQuestionList.add(sampleQuestion1);
        controller.examQuestionList.add(sampleQuestion2);

        // Create an ObservableList of questions to be returned by the examQuestionsTable
        ObservableList<Question> examQuestionList = FXCollections.observableArrayList();
        examQuestionList.add(sampleQuestion1);
        examQuestionList.add(sampleQuestion2);

        // Mock the selection model of the examQuestionsTable
        TableView<Question> mockExamQuestionsTable = mock(TableView.class);
        TableView.TableViewSelectionModel<Question> mockSelectionModel = mock(TableView.TableViewSelectionModel.class);
        when(mockExamQuestionsTable.getSelectionModel()).thenReturn(mockSelectionModel);
        when(mockExamQuestionsTable.getItems()).thenReturn(examQuestionList);
        when(mockSelectionModel.getSelectedItem()).thenReturn(sampleQuestion1);

        // Set the mock table in the controller
        controller.examQuestionsTable = mockExamQuestionsTable;

        // Call the method to be tested
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.removeQuestionFromExam(new ActionEvent());

            // Verify that the question was removed from the examQuestionList
            assertFalse(controller.examQuestionList.contains(sampleQuestion1));

            // Verify that the success message was shown
            mockedMsgSender.verify(() -> MsgSender.showMsg("Question removed from the exam successfully."));
        }
    }

    @Test
    void testRemoveQuestionFromExamNoQuestionSelected() {
        // Mock the selection model of the examQuestionsTable
        TableView<Question> mockExamQuestionsTable = mock(TableView.class);
        TableView.TableViewSelectionModel<Question> mockSelectionModel = mock(TableView.TableViewSelectionModel.class);
        when(mockExamQuestionsTable.getSelectionModel()).thenReturn(mockSelectionModel);
        when(mockSelectionModel.getSelectedItem()).thenReturn(null);

        // Set the mock table in the controller
        controller.examQuestionsTable = mockExamQuestionsTable;

        // Call the method to be tested
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.removeQuestionFromExam(new ActionEvent());

            // Verify that the message was shown
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please select a question to be removed from the exam."));
        }
    }

    @Test
    void testRemoveQuestionFromExamError() {
        // Create a sample question
        Question sampleQuestion = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);

        // Create a real ObservableList
        ObservableList<Question> examQuestionList = FXCollections.observableArrayList();
        examQuestionList.add(sampleQuestion);

        // Set the real list in your controller
        controller.examQuestionList = examQuestionList;

        // Create a mock TableView and set it up
        TableView<Question> mockExamQuestionsTable = mock(TableView.class);
        TableView.TableViewSelectionModel<Question> mockSelectionModel = mock(TableView.TableViewSelectionModel.class);
        when(mockExamQuestionsTable.getSelectionModel()).thenReturn(mockSelectionModel);
        when(mockExamQuestionsTable.getItems()).thenReturn(examQuestionList);
        when(mockSelectionModel.getSelectedItem()).thenReturn(sampleQuestion);

        // Set the mock table in the controller
        controller.examQuestionsTable = mockExamQuestionsTable;

        // Simulate an error when removing the question
        // You can spy on the real list if needed
        ObservableList<Question> spyExamQuestionList = Mockito.spy(examQuestionList);
        controller.examQuestionList = spyExamQuestionList;

        // Stubbing the remove method to throw an exception
        doThrow(new RuntimeException()).when(spyExamQuestionList).remove(any(Question.class));

        // Call the method to be tested
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.removeQuestionFromExam(new ActionEvent());

            // Verify that the error message was shown
            mockedMsgSender.verify(() -> MsgSender.showMsg("An error occurred while removing the question from the exam."));
        }
    }

    @Test
    void testResetExamFilters() {
        // Set up initial filter values
        controller.examNameFilterTextField.setText("Sample Exam");
        controller.courseIDFilterChoiceBox.setValue("COMP3111");
        controller.publishStatusFilterChoiceBox.setValue("Yes");

        // Call the method to be tested
        controller.resetExamFilters(new ActionEvent());

        // Verify that the filters are reset
        assertEquals("", controller.examNameFilterTextField.getText());
        assertEquals("", controller.courseIDFilterChoiceBox.getValue());
        assertEquals("", controller.publishStatusFilterChoiceBox.getValue());

        // Verify that loadQuizzes is called
        // You can use a spy to verify method calls
        TeacherExamManagementController spyController = spy(controller);
        spyController.resetExamFilters(new ActionEvent());
        verify(spyController).loadQuizzes();
    }

    @Test
    void testResetQuestionFilters() {
        // Set up initial filter values
        controller.questionFilterTextField.setText("Question1");
        controller.typeFilterChoiceBox.setValue("Single");
        controller.scoreFilterTextField.setText("10");

        // Call the method to be tested
        controller.resetQuestionFilters(new ActionEvent());

        // Verify that the filters are reset
        assertEquals("", controller.questionFilterTextField.getText());
        assertEquals("", controller.typeFilterChoiceBox.getValue());
        assertEquals("", controller.scoreFilterTextField.getText());

        // Verify that loadAllQuestions is called
        // You can use a spy to verify method calls
        TeacherExamManagementController spyController = spy(controller);
        spyController.resetQuestionFilters(new ActionEvent());
        verify(spyController).loadAllQuestions();
    }

    @Test
    void testClearFields() {
        // Set some initial values
        controller.examNameInput.setText("Sample Exam");
        controller.examTimeInput.setText("60");
        controller.courseIDInput.setValue("COMP3111");
        controller.publishInput.setValue("Yes");

        // Call the method to be tested
        controller.clearFields();

        // Verify that the fields are cleared
        assertEquals("", controller.examNameInput.getText());
        assertEquals("", controller.examTimeInput.getText());
        assertEquals("", controller.courseIDInput.getValue());
        assertEquals("", controller.publishInput.getValue());
    }

    @Test
    void testLoadExamQuestions() {
        // Create a sample quiz with question IDs
        Quiz sampleQuiz = new Quiz("Sample Exam", "60", "COMP3111", "Yes", 123, "123|456|");

        // Mock the quizDatabase to return the sample quiz
        when(mockQuizDatabase.queryByKey("123")).thenReturn(sampleQuiz);

        // Create sample questions
        Question sampleQuestion1 = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        Question sampleQuestion2 = new Question("What is C++?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 456);

        // Mock the questionDatabase to return the sample questions
        when(mockQuestionDatabase.queryByKey("123")).thenReturn(sampleQuestion1);
        when(mockQuestionDatabase.queryByKey("456")).thenReturn(sampleQuestion2);

        // Call the method to be tested
        controller.loadExamQuestions(123);

        // Verify that the examQuestionList contains the sample questions
        assertEquals(2, controller.examQuestionList.size());
        assertTrue(controller.examQuestionList.contains(sampleQuestion1));
        assertTrue(controller.examQuestionList.contains(sampleQuestion2));

        // Verify that the examQuestionsTable is updated with the sample questions
        assertEquals(2, controller.examQuestionsTable.getItems().size());
        assertTrue(controller.examQuestionsTable.getItems().contains(sampleQuestion1));
        assertTrue(controller.examQuestionsTable.getItems().contains(sampleQuestion2));
    }

    @Test
    void testLoadExamQuestionsError() {
        // Create a sample quiz with question IDs
        Quiz sampleQuiz = new Quiz("Sample Exam", "60", "COMP3111", "Yes", 123, "123|456|");

        // Mock the quizDatabase to return the sample quiz
        when(mockQuizDatabase.queryByKey("123")).thenReturn(sampleQuiz);

        // Create sample questions
        Question sampleQuestion1 = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        Question sampleQuestion2 = new Question("What is C++?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 456);

        // Mock the questionDatabase to return the sample questions
        when(mockQuestionDatabase.queryByKey("123")).thenReturn(sampleQuestion1);
        when(mockQuestionDatabase.queryByKey("456")).thenReturn(sampleQuestion2);

        // Simulate an error when adding the question
        // You can spy on the real list if needed
        ObservableList<Question> spyExamQuestionList = Mockito.spy(controller.examQuestionList);
        controller.examQuestionList = spyExamQuestionList;

        // Stubbing the add method to throw an exception
        doThrow(new RuntimeException()).when(mockQuizDatabase).queryByKey(anyString());

        // Call the method to be tested
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.loadExamQuestions(123);
            // Verify that the error message was shown
            mockedMsgSender.verify(() -> MsgSender.showMsg("An error occurred while loading the exam questions."));
        }
    }

    @Test
    void testLoadAllQuestionsErrorWithoutFilter() {
        // Create a list of questions to be returned by the mock
        List<Question> allQuestions = new ArrayList<>();
        allQuestions.add(new Question("Sample Question 1", "A", "B", "C", "D", "A", "10", "Single", 123));
        allQuestions.add(new Question("Sample Question 2", "A", "B", "C", "D", "A", "10", "Multiple", 124));

        // Stub the getAll method to return the allQuestions list
        when(mockQuestionDatabase.getAll()).thenReturn(allQuestions);

        // Test with no filter
        controller.questionFilterTextField.setText("");
        controller.typeFilterChoiceBox.setValue("");
        controller.scoreFilterTextField.setText("");

        // Test with error
        doThrow(new RuntimeException()).when(mockQuestionDatabase).getAll();

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.loadAllQuestions();
            mockedMsgSender.verify(() -> MsgSender.showMsg("An error occurred while loading the questions."));
        }
    }

    @Test
    void testLoadAllQuestionsErrorWithFilter() {
        // Create a list of questions to be returned by the mock
        List<Question> allQuestions = new ArrayList<>();
        allQuestions.add(new Question("Sample Question 1", "A", "B", "C", "D", "A", "10", "Single", 123));
        allQuestions.add(new Question("Sample Question 2", "A", "B", "C", "D", "A", "10", "Multiple", 124));

        // Stub the getAll method to return the allQuestions list
        when(mockQuestionDatabase.getAll()).thenReturn(allQuestions);

        // Test with no filter
        controller.questionFilterTextField.setText("");
        controller.typeFilterChoiceBox.setValue("Single");
        controller.scoreFilterTextField.setText("");

        // Test with error
        doThrow(new RuntimeException()).when(mockQuestionDatabase).getAll();

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.loadAllQuestions();
            mockedMsgSender.verify(() -> MsgSender.showMsg("An error occurred while filtering the questions."));
        }
    }

    @Test
    void testLoadQuizzesErrorWithoutFilter() {
        // Set the mock database in the controller
        controller.quizDatabase = mockQuizDatabase;

        // Test with no filter
        controller.examNameFilterTextField.setText("");
        controller.courseIDFilterChoiceBox.setValue("");
        controller.publishStatusFilterChoiceBox.setValue("");

        // Test with error: stub the getAll method to throw an exception
        doThrow(new RuntimeException()).when(mockQuizDatabase).getAll();

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Call loadQuizzes on JavaFX Application Thread
            Platform.runLater(() -> controller.loadQuizzes());

            // Process all pending JavaFX events
            Platform.runLater(() -> {
                // Verify that the error message is shown
                mockedMsgSender.verify(() -> MsgSender.showMsg("An error occurred while loading the exams."));
            });

            // Wait for the JavaFX Application Thread to process the runLater task
            // You may want to use a more robust synchronization mechanism here
            try {
                Thread.sleep(100); // Short sleep to allow the UI to update, adjust as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void testLoadQuizzesErrorWithFilter() {
        // Test with no filter
        controller.examNameFilterTextField.setText("");
        controller.courseIDFilterChoiceBox.setValue("COMP3111");
        controller.publishStatusFilterChoiceBox.setValue("");

        // Test with error: stub the getAll method to throw an exception
        doThrow(new RuntimeException()).when(mockQuizDatabase).getAll();

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Call loadQuizzes on JavaFX Application Thread
            Platform.runLater(() -> controller.loadQuizzes());

            // Process all pending JavaFX events
            Platform.runLater(() -> {
                // Verify that the error message is shown
                mockedMsgSender.verify(() -> MsgSender.showMsg("An error occurred while loading the exams."));
            });

            // Wait for the JavaFX Application Thread to process the runLater task
            // You may want to use a more robust synchronization mechanism here
            try {
                Thread.sleep(100); // Short sleep to allow the UI to update, adjust as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void testNoQuestionFilterNoFilter() {
        // Set up the filter criteria
        controller.questionFilterTextField.setText("");
        controller.typeFilterChoiceBox.setValue("");
        controller.scoreFilterTextField.setText("");

        // Call the method to be tested
        boolean result = controller.noQuestionFilter();

        // Verify the result
        assertTrue(result);
    }

    @Test
    void testNoQuestionFilterQuestionDescriptionFilter() {
        // Set up the filter criteria
        controller.questionFilterTextField.setText("Question 1");
        controller.typeFilterChoiceBox.setValue("");
        controller.scoreFilterTextField.setText("");

        // Call the method to be tested
        boolean result = controller.noQuestionFilter();

        // Verify the result
        assertFalse(result);
    }

    @Test
    void testNoQuestionFilterTypeFilter() {
        // Set up the filter criteria
        controller.questionFilterTextField.setText("");
        controller.typeFilterChoiceBox.setValue("Single");
        controller.scoreFilterTextField.setText("");

        // Call the method to be tested
        boolean result = controller.noQuestionFilter();

        // Verify the result
        assertFalse(result);
    }

    @Test
    void testNoQuestionFilterScoreFilter() {
        // Set up the filter criteria
        controller.questionFilterTextField.setText("");
        controller.typeFilterChoiceBox.setValue("");
        controller.scoreFilterTextField.setText("10");

        // Call the method to be tested
        boolean result = controller.noQuestionFilter();

        // Verify the result
        assertFalse(result);
    }

    @Test
    void testNoExamFilterNoFilter() {
        // Set up the filter criteria
        controller.examNameFilterTextField.setText("");
        controller.courseIDFilterChoiceBox.setValue("");
        controller.publishStatusFilterChoiceBox.setValue("");

        // Call the method to be tested
        boolean result = controller.noExamFilter();

        // Verify the result
        assertTrue(result);
    }

    @Test
    void testNoExamFilterExamNameFilter() {
        // Set up the filter criteria
        controller.examNameFilterTextField.setText("Sample Exam");
        controller.courseIDFilterChoiceBox.setValue("");
        controller.publishStatusFilterChoiceBox.setValue("");

        // Call the method to be tested
        boolean result = controller.noExamFilter();

        // Verify the result
        assertFalse(result);
    }

    @Test
    void testNoExamFilterCourseIDFilter() {
        // Set up the filter criteria
        controller.examNameFilterTextField.setText("");
        controller.courseIDFilterChoiceBox.setValue("COMP3111");
        controller.publishStatusFilterChoiceBox.setValue("");

        // Call the method to be tested
        boolean result = controller.noExamFilter();

        // Verify the result
        assertFalse(result);
    }

    @Test
    void testNoExamFilterPublishStatusFilter() {
        // Set up the filter criteria
        controller.examNameFilterTextField.setText("");
        controller.courseIDFilterChoiceBox.setValue("");
        controller.publishStatusFilterChoiceBox.setValue("Yes");

        // Call the method to be tested
        boolean result = controller.noExamFilter();

        // Verify the result
        assertFalse(result);
    }
}