package comp3111.examsystem.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import comp3111.examsystem.model.Question;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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

class TeacherQuestionBankManagementControllerTest {

    @Mock
    private Database<Question> mockDatabase;

    private TeacherQuestionBankManagementController controller;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/comp3111/examsystem/TeacherQuestionBankManagementUI.fxml"));
        Parent root = loader.load();

        controller = new TeacherQuestionBankManagementController();
        controller.questionTextField = new TextField();
        controller.optionATextField = new TextField();
        controller.optionBTextField = new TextField();
        controller.optionCTextField = new TextField();
        controller.optionDTextField = new TextField();
        controller.answerTextField = new TextField();
        controller.scoreTextField = new TextField();
        controller.typeChoiceBox = new ChoiceBox<>();
        controller.questionFilterTextField = new TextField();
        controller.scoreFilterTextField = new TextField();
        controller.typeFilterChoiceBox = new ChoiceBox<>();
        controller.questionTable = new TableView<>();
        controller.questionDatabase = mockDatabase;
        controller.allQuestions = new ArrayList<>();
        controller.questionList = FXCollections.observableArrayList();
    }

    @Test
    void testAddQuestionValid() {
        controller.questionTextField.setText("What is Java?");
        controller.optionATextField.setText("A programming language");
        controller.optionBTextField.setText("A coffee");
        controller.optionCTextField.setText("An island");
        controller.optionDTextField.setText("A car");
        controller.answerTextField.setText("AB");
        controller.scoreTextField.setText("20");
        controller.typeChoiceBox.setValue("Multiple");

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addQuestion(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("Question added successfully."));
        }
    }

    @Test
    void testAddQuestionInvalidEmptyField() {
        controller.questionTextField.setText("");
        controller.optionATextField.setText("A programming language");
        controller.optionBTextField.setText("A coffee");
        controller.optionCTextField.setText("An island");
        controller.optionDTextField.setText("A car");
        controller.answerTextField.setText("A");
        controller.scoreTextField.setText("10");
        controller.typeChoiceBox.setValue("Single");

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addQuestion(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("All fields must be filled."));
        }
    }

    @Test
    void testAddQuestionInvalidType() {
        controller.questionTextField.setText("What is Java?");
        controller.optionATextField.setText("A programming language");
        controller.optionBTextField.setText("A coffee");
        controller.optionCTextField.setText("An island");
        controller.optionDTextField.setText("A car");
        controller.answerTextField.setText("A");
        controller.scoreTextField.setText("10");
        controller.typeChoiceBox.setValue("Multiple");

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addQuestion(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("Multiple choice questions must have more than one correct answer."));
        }
    }

    @Test
    void testAddQuestionInvalidScore() {
        controller.questionTextField.setText("What is Java?");
        controller.optionATextField.setText("A programming language");
        controller.optionBTextField.setText("A coffee");
        controller.optionCTextField.setText("An island");
        controller.optionDTextField.setText("A car");
        controller.answerTextField.setText("A");
        controller.scoreTextField.setText("ABC");
        controller.typeChoiceBox.setValue("Single");

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addQuestion(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("Score must be an integer."));
        }
    }

    @Test
    void testAddQuestionNegativeScore() {
        controller.questionTextField.setText("What is Java?");
        controller.optionATextField.setText("A programming language");
        controller.optionBTextField.setText("A coffee");
        controller.optionCTextField.setText("An island");
        controller.optionDTextField.setText("A car");
        controller.answerTextField.setText("A");
        controller.scoreTextField.setText("-10");
        controller.typeChoiceBox.setValue("Single");

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addQuestion(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("Score must be a positive integer."));
        }
    }

    @Test
    void testAddQuestionInvalidAnswer() {
        controller.questionTextField.setText("What is Java?");
        controller.optionATextField.setText("A programming language");
        controller.optionBTextField.setText("A coffee");
        controller.optionCTextField.setText("An island");
        controller.optionDTextField.setText("A car");
        controller.answerTextField.setText("E");
        controller.scoreTextField.setText("10");
        controller.typeChoiceBox.setValue("Single");

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addQuestion(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("Answer must be either A, B, C, or D. E is not a valid answer."));
        }
    }

    @Test
    void testAddQuestionRepeatedQuestion() {
        controller.questionTextField.setText("What is Java?");
        controller.optionATextField.setText("A programming language");
        controller.optionBTextField.setText("A coffee");
        controller.optionCTextField.setText("An island");
        controller.optionDTextField.setText("A car");
        controller.answerTextField.setText("A");
        controller.scoreTextField.setText("10");
        controller.typeChoiceBox.setValue("Single");

        Question question = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        controller.allQuestions.add(question);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addQuestion(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("Question already exists."));
        }
    }

    @Test
    void testAddQuestionError() {
        controller.questionTextField.setText("What is Java?");
        controller.optionATextField.setText("A programming language");
        controller.optionBTextField.setText("A coffee");
        controller.optionCTextField.setText("An island");
        controller.optionDTextField.setText("A car");
        controller.answerTextField.setText("A");
        controller.scoreTextField.setText("10");
        controller.typeChoiceBox.setValue("Single");

        doThrow(new RuntimeException()).when(mockDatabase).add(any(Question.class));

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.addQuestion(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("An error occurred while adding the question."));
        }
    }

    @Test
    void testDeleteQuestionValid() {
        // Create a sample question and add it to the questionTable
        Question question = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        controller.questionTable.setItems(FXCollections.observableArrayList(question));
        controller.questionTable.getSelectionModel().select(question);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Mock the showConfirm method to execute the callback
            mockedMsgSender.when(() -> MsgSender.showConfirm(anyString(), anyString(), any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable callback = invocation.getArgument(2);
                        callback.run();
                        return null;
                    });

            // Call the method to be tested
            controller.deleteQuestion(new ActionEvent());

            // Verify that showMsg was called with the correct argument
            mockedMsgSender.verify(() -> MsgSender.showMsg("Question deleted successfully."));
        }
    }

    @Test
    void testDeleteQuestionWithNoSelectedQuestion() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.questionTable.getSelectionModel().clearSelection();

            // Call the method to be tested
            controller.deleteQuestion(new ActionEvent());

            // Verify that showMsg was called
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please select a question to delete."));
        }
    }

    @Test
    void testDeleteQuestionError() {
        // Create a sample question and add it to the questionTable
        Question question = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        controller.questionTable.setItems(FXCollections.observableArrayList(question));
        controller.questionTable.getSelectionModel().select(question);

        doThrow(new RuntimeException()).when(mockDatabase).delByKey(anyString());

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Mock the showConfirm method to execute the callback
            mockedMsgSender.when(() -> MsgSender.showConfirm(anyString(), anyString(), any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable callback = invocation.getArgument(2);
                        callback.run();
                        return null;
                    });

            // Call the method to be tested
            controller.deleteQuestion(new ActionEvent());

            // Verify that showMsg was called with the correct argument
            mockedMsgSender.verify(() -> MsgSender.showMsg("An error occurred while deleting the question."));
        }
    }

    @Test
    void testUpdateQuestionValid() {
        Question question = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Multiple Choice", 123);
        controller.questionTable.setItems(FXCollections.observableArrayList(question));
        controller.questionTable.getSelectionModel().select(question);

        controller.questionTextField.setText("What is Java?");
        controller.optionATextField.setText("A programming language");
        controller.optionBTextField.setText("A coffee");
        controller.optionCTextField.setText("An island");
        controller.optionDTextField.setText("A car");
        controller.answerTextField.setText("A");
        controller.scoreTextField.setText("10");
        controller.typeChoiceBox.setValue("Single");

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Mock the showConfirm method to execute the callback
            mockedMsgSender.when(() -> MsgSender.showConfirm(anyString(), anyString(), any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable callback = invocation.getArgument(2);
                        callback.run();
                        return null;
                    });

            // Call the method to be tested
            controller.updateQuestion(new ActionEvent());

            // Verify that showMsg was called with the correct argument
            mockedMsgSender.verify(() -> MsgSender.showMsg("Question updated successfully."));
        }
    }

    @Test
    void testUpdateQuestionWithNoSelectedQuestion() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.questionTable.getSelectionModel().clearSelection();

            // Call the method to be tested
            controller.updateQuestion(new ActionEvent());

            // Verify that showMsg was called
            mockedMsgSender.verify(() -> MsgSender.showMsg("Please select a question to update."));
        }
    }

    @Test
    void testUpdateQuestionInvalidEmptyField() {
        Question question = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Multiple Choice", 123);
        controller.questionTable.setItems(FXCollections.observableArrayList(question));
        controller.questionTable.getSelectionModel().select(question);

        controller.questionTextField.setText("");
        controller.optionATextField.setText("A programming language");
        controller.optionBTextField.setText("A coffee");
        controller.optionCTextField.setText("An island");
        controller.optionDTextField.setText("A car");
        controller.answerTextField.setText("A");
        controller.scoreTextField.setText("10");
        controller.typeChoiceBox.setValue("Single");

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            controller.updateQuestion(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("All fields must be filled."));
        }
    }

    @Test
    void testUpdateQuestionError() {
        Question question = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Multiple Choice", 123);
        controller.questionTable.setItems(FXCollections.observableArrayList(question));
        controller.questionTable.getSelectionModel().select(question);

        controller.questionTextField.setText("What is Java?");
        controller.optionATextField.setText("A programming language");
        controller.optionBTextField.setText("A coffee");
        controller.optionCTextField.setText("An island");
        controller.optionDTextField.setText("A car");
        controller.answerTextField.setText("A");
        controller.scoreTextField.setText("10");
        controller.typeChoiceBox.setValue("Single");

        doThrow(new RuntimeException()).when(mockDatabase).update(any(Question.class));

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Mock the showConfirm method to execute the callback
            mockedMsgSender.when(() -> MsgSender.showConfirm(anyString(), anyString(), any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable callback = invocation.getArgument(2);
                        callback.run();
                        return null;
                    });

            controller.updateQuestion(new ActionEvent());
            mockedMsgSender.verify(() -> MsgSender.showMsg("An error occurred while updating the question."));
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
    void testRefreshQuestionWithoutUnsavedChanges() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Call the method to be tested
            controller.refreshQuestion(new ActionEvent());

            // Verify that the confirmation message was not shown
            mockedMsgSender.verifyNoInteractions();
        }
    }

    @Test
    void testRefreshQuestionWithUnsavedChanges() {
        // Set up the input fields with unsaved changes
        controller.questionTextField.setText("What is Java?");
        controller.optionATextField.setText("A programming language");

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Call the method to be tested
            controller.refreshQuestion(new ActionEvent());

            // Verify that the confirmation message was shown
            mockedMsgSender.verify(() -> MsgSender.showConfirm(eq("Refresh Confirmation"), eq("Refreshing will clear all fields and any unsaved changes will be lost. Are you sure you want to refresh?"), any(Runnable.class)));
        }
    }

    @Test
    void testRefreshQuestionConfirmed() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            // Mock the showConfirm method to execute the callback
            mockedMsgSender.when(() -> MsgSender.showConfirm(anyString(), anyString(), any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable callback = invocation.getArgument(2);
                        callback.run();
                        return null;
                    });

            // Set up the input fields with unsaved changes
            controller.questionTextField.setText("What is Java?");
            controller.optionATextField.setText("A programming language");

            // Spy on the controller to verify method calls
            TeacherQuestionBankManagementController spyController = spy(controller);

            // Call the method to be tested
            spyController.refreshQuestion(new ActionEvent());

            // Verify that the refreshQuestionConfirmed method was called
            verify(spyController).refreshQuestionConfirmed(any(ActionEvent.class));
        }
    }

    @Test
    void testValidInputValid() {
        assertTrue(controller.validInputs("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single"));
    }

    @Test
    void testValidInputEmptyQuestion() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("All fields must be filled."));
        }
    }

    @Test
    void testValidInputEmptyOptionA() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("What is Java?", "", "A coffee", "An island", "A car", "A", "10", "Single");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("All fields must be filled."));
        }
    }

    @Test
    void testValidInputEmptyOptionB() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("What is Java?", "A programming language", "", "An island", "A car", "A", "10", "Single");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("All fields must be filled."));
        }
    }

    @Test
    void testValidInputEmptyOptionC() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("What is Java?", "A programming language", "A coffee", "", "A car", "A", "10", "Single");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("All fields must be filled."));
        }
    }

    @Test
    void testValidInputEmptyOptionD() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("What is Java?", "A programming language", "A coffee", "An island", "", "A", "10", "Single");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("All fields must be filled."));
        }
    }

    @Test
    void testValidInputEmptyAnswer() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("What is Java?", "A programming language", "A coffee", "An island", "A car", "", "10", "Single");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("All fields must be filled."));
        }
    }

    @Test
    void testValidInputEmptyScore() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "", "Single");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("All fields must be filled."));
        }
    }

    @Test
    void testValidInputEmptyType() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("All fields must be filled."));
        }
    }

    @Test
    void testValidInputInvalidScore() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "ABC", "Single");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Score must be an integer."));
        }
    }

    @Test
    void testValidInputNegativeScore() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "-10", "Single");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Score must be a positive integer."));
        }
    }

    @Test
    void testValidInputInvalidSingleType() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("What is Java?", "A programming language", "A coffee", "An island", "A car", "AB", "10", "Single");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Single choice questions can only have one correct answer."));
        }
    }

    @Test
    void testValidInputInvalidMultipleType() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Multiple");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Multiple choice questions must have more than one correct answer."));
        }
    }

    @Test
    void testValidInputInvalidSingleAnswer() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("What is Java?", "A programming language", "A coffee", "An island", "A car", "E", "10", "Single");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Answer must be either A, B, C, or D. E is not a valid answer."));
        }
    }

    @Test
    void testValidInputInvalidMultipleAnswer() {
        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("What is Java?", "A programming language", "A coffee", "An island", "A car", "AE", "10", "Multiple");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Answer must be either A, B, C, or D. E is not a valid answer."));
        }
    }

    @Test
    void testValidInputRepeatedQuestion() {
        Question question = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single", 123);
        controller.allQuestions.add(question);

        try (MockedStatic<MsgSender> mockedMsgSender = mockStatic(MsgSender.class)) {
            boolean result = controller.validInputs("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "10", "Single");
            assertFalse(result);
            mockedMsgSender.verify(() -> MsgSender.showMsg("Question already exists."));
        }
    }

    @Test
    void testNoFilterWithoutFilter() {
        controller.questionFilterTextField.setText("");
        controller.scoreFilterTextField.setText("");
        controller.typeFilterChoiceBox.setValue("");

        assertTrue(controller.noFilter());
    }

    @Test
    void testNoFilterWithFilter() {
        controller.questionFilterTextField.setText("");
        controller.scoreFilterTextField.setText("10");
        controller.typeFilterChoiceBox.setValue("");

        assertFalse(controller.noFilter());
    }

}