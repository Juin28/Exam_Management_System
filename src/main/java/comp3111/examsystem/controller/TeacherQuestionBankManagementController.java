package comp3111.examsystem.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import comp3111.examsystem.model.Question;
import comp3111.examsystem.model.Teacher;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controller for the Teacher's Question Bank Management page
 */
public class TeacherQuestionBankManagementController {
    public Database<Question> questionDatabase;
    public List<Question> allQuestions;
    public ObservableList<Question> questionList;

    @FXML
    public ResourceBundle resources;
    @FXML
    public URL location;
    @FXML
    public Button addButton;
    @FXML
    public TableColumn<Question, String> answerColumn;
    @FXML
    public TextField answerTextField;
    @FXML
    public Button deleteButton;
    @FXML
    public Button filterButton;
    @FXML
    public TableColumn<Question, String> optionAColumn;
    @FXML
    public TextField optionATextField;
    @FXML
    public TableColumn<Question, String> optionBColumn;
    @FXML
    public TextField optionBTextField;
    @FXML
    public TableColumn<Question, String> optionCColumn;
    @FXML
    public TextField optionCTextField;
    @FXML
    public TableColumn<Question, String> optionDColumn;
    @FXML
    public TextField optionDTextField;
    @FXML
    public TableColumn<Question, String> questionColumn;
    @FXML
    public TextField questionFilterTextField;
    @FXML
    public TableView<Question> questionTable;
    @FXML
    public TextField questionTextField;
    @FXML
    public Button refreshButton;
    @FXML
    public Button resetButton;
    @FXML
    public TableColumn<Question, String> scoreColumn;
    @FXML
    public TextField scoreFilterTextField;
    @FXML
    public TextField scoreTextField;
    @FXML
    public TableColumn<Question, String> typeColumn;
    @FXML
    public ChoiceBox<String> typeChoiceBox;
    @FXML
    public ChoiceBox<String> typeFilterChoiceBox;
    @FXML
    public Button updateButton;
    @FXML
    public FlowPane rootPane;
    @FXML
    public VBox questionInputFields;
    @FXML
    public HBox filterManagementFields;
    @FXML
    public HBox questionManagementButtons;
    @FXML
    public HBox tableAndQuestionInputFields;
    @FXML
    public VBox questionInput;
    @FXML
    public VBox optionAInput;
    @FXML
    public VBox optionBInput;
    @FXML
    public VBox optionCInput;
    @FXML
    public VBox optionDInput;
    @FXML
    public VBox answerInput;
    @FXML
    public VBox typeInput;
    @FXML
    public VBox scoreInput;

    /**
     * Initialize the controller
     */
    @FXML
    public void initialize() {
        this.questionDatabase = new Database<>(Question.class);
        allQuestions = questionDatabase.getAll();
        loadQuestions();

        typeChoiceBox.getItems().addAll("Single", "Multiple");
        typeFilterChoiceBox.getItems().addAll("Single", "Multiple", "");
        typeFilterChoiceBox.setValue("");

        // Handle resize of width of different components when the window is resized
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Handle resize of filters components
            filterManagementFields.setPrefWidth(newVal.doubleValue());

            // Handle resize of question management components
            tableAndQuestionInputFields.setPrefWidth(newVal.doubleValue());

            // Handle resize of question table
            double tableWidth = newVal.doubleValue() * 0.70;
            questionTable.setPrefWidth(tableWidth);
            questionColumn.setPrefWidth(tableWidth * 0.24);
            optionAColumn.setPrefWidth(tableWidth * 0.12);
            optionBColumn.setPrefWidth(tableWidth * 0.12);
            optionCColumn.setPrefWidth(tableWidth * 0.12);
            optionDColumn.setPrefWidth(tableWidth * 0.12);
            answerColumn.setPrefWidth(tableWidth * 0.09);
            typeColumn.setPrefWidth(tableWidth * 0.1);
            scoreColumn.setPrefWidth(tableWidth * 0.085);

            // Handle resize of question input fields
            double questionInputWidth = newVal.doubleValue() * 0.28;
            questionInputFields.setPrefWidth(questionInputWidth);
            typeChoiceBox.setPrefWidth(questionInputWidth);

            // Handle resize of question management buttons
            questionManagementButtons.setPrefWidth(newVal.doubleValue());
        });

        // Handle resize of height different components when the window is resized
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            // Handle resize of filters components
            filterManagementFields.setPrefHeight(newVal.doubleValue() * 0.10);

            // Handle resize of question table and input fields
            double tableHeight = newVal.doubleValue() * 0.60;
            tableAndQuestionInputFields.setPrefHeight(tableHeight);
            questionInput.setPadding(new Insets(0, 0, tableHeight * 0.03, 0));
            optionAInput.setPadding(new Insets(0, 0, tableHeight * 0.03, 0));
            optionBInput.setPadding(new Insets(0, 0, tableHeight * 0.03, 0));
            optionCInput.setPadding(new Insets(0, 0, tableHeight * 0.03, 0));
            optionDInput.setPadding(new Insets(0, 0, tableHeight * 0.03, 0));
            answerInput.setPadding(new Insets(0, 0, tableHeight * 0.03, 0));
            typeInput.setPadding(new Insets(0, 0, tableHeight * 0.03, 0));

            // Handle resize of question management buttons
            questionManagementButtons.setPrefHeight(newVal.doubleValue() * 0.15);
        });

        // Set up columns to extract data from each Question object
        questionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestionDescription()));
        optionAColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOptionA()));
        optionBColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOptionB()));
        optionCColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOptionC()));
        optionDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOptionD()));
        answerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAnswer()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestionType()));
        scoreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestionScore()));

        // Set up the listener for the table selection
        questionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                questionTextField.setText(newSelection.getQuestionDescription());
                optionATextField.setText(newSelection.getOptionA());
                optionBTextField.setText(newSelection.getOptionB());
                optionCTextField.setText(newSelection.getOptionC());
                optionDTextField.setText(newSelection.getOptionD());
                answerTextField.setText(newSelection.getAnswer());
                scoreTextField.setText(newSelection.getQuestionScore());
                typeChoiceBox.setValue(newSelection.getQuestionType());
            }
        });

        // Clear the selection when clicking on the table itself with no question selected
        questionTable.setRowFactory(tv -> {
            TableRow<Question> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    questionTable.getSelectionModel().clearSelection();
                    clearFields();
                }
            });
            return row;
        });
    }

    /**
     * Add a question to the database
     * @param event the ActionEvent
     */
    @FXML
    public void addQuestion(ActionEvent event) {
        String questionText = questionTextField.getText();
        String optionA = optionATextField.getText();
        String optionB = optionBTextField.getText();
        String optionC = optionCTextField.getText();
        String optionD = optionDTextField.getText();
        String answer = answerTextField.getText().replaceAll("[^A-Za-z]", ""); // Removes non-letter characters
        String score = scoreTextField.getText();
        String type = (String) typeChoiceBox.getValue();

        if (!validInputs(questionText, optionA, optionB, optionC, optionD, answer, score, type) ) {
            return;
        }

        try {
            int questionId = allQuestions.size() + 1;
            Question newQuestion = new Question(questionText, optionA, optionB, optionC, optionD, answer, score, type, questionId);

            // Store the question information in the database
            questionDatabase.add(newQuestion);

        } catch (Exception e) {
            MsgSender.showMsg("An error occurred while adding the question.");
            e.printStackTrace();
        }
        MsgSender.showMsg("Question added successfully.");
        loadQuestions();
        clearFields();
    }


    /**
     * Check if the inputs are valid
     * @param questionText the question text
     * @param optionA the option A
     * @param optionB the option B
     * @param optionC the option C
     * @param optionD the option D
     * @param answer the answer
     * @param score the score
     * @param type the type of question
     * @return true if all inputs are valid, false otherwise
     */
    public boolean validInputs(String questionText, String optionA, String optionB, String optionC, String optionD, String answer, String score, String type) {
        // check if all fields are filled
        if (questionText.isEmpty() || optionA.isEmpty() || optionB.isEmpty() ||
                optionC.isEmpty() || optionD.isEmpty() || answer.isEmpty() ||
                score.isEmpty() || type.isEmpty() ) {
            MsgSender.showMsg("All fields must be filled.");
            return false;
        }

        // check if the score is an integer
        int scoreInt = 0;
        try {
            scoreInt = Integer.parseInt(score);
        } catch (NumberFormatException e) {
            MsgSender.showMsg("Score must be an integer.");
            return false; // If parsing fails, it's not an integer
        }

        // check if the score is a positive integer
        if (scoreInt < 0) {
            MsgSender.showMsg("Score must be a positive integer.");
            return false;
        }

        // check if the number of answer matches the question type
        if (type.equals("Single") && answer.length() > 1) {
            MsgSender.showMsg("Single choice questions can only have one correct answer.");
            return false;
        } else if (type.equals("Multiple") && answer.length() == 1) {
            MsgSender.showMsg("Multiple choice questions must have more than one correct answer.");
            return false;
        }

        // check the answer only have options A, B, C, or D
        for(char c : answer.toCharArray()) {
            if(c != 'A' && c != 'B' && c != 'C' && c != 'D') {
                MsgSender.showMsg("Answer must be either A, B, C, or D. " + c + " is not a valid answer.");
                return false;
            }
        }

        // check if the question already exists
        for (Question question : allQuestions) {
            if (question.getQuestionDescription().equals(questionText) && question.getOptionA().equals(optionA) &&
                    question.getOptionB().equals(optionB) && question.getOptionC().equals(optionC) &&
                    question.getOptionD().equals(optionD) && question.getAnswer().equals(answer) &&
                    question.getQuestionScore().equals(score) && question.getQuestionType().equals(type)) {
                MsgSender.showMsg("Question already exists.");
                return false;
            }
        }

        return true;
    }

    /**
     * Delete a question from the database
     * @param event the ActionEvent
     */
    @FXML
    public void deleteQuestion(ActionEvent event) {
        Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();

        if (selectedQuestion == null) {
            MsgSender.showMsg("Please select a question to delete.");
            return;
        }

        try {
            String idString = String.valueOf(selectedQuestion.getId());
            MsgSender.showConfirm("Question Delete Confirmation", "Are you sure you want to delete the question?", () -> {
                questionDatabase.delByKey(idString);
                MsgSender.showMsg("Question deleted successfully.");
                questionTable.getSelectionModel().clearSelection();
                clearFields();
                loadQuestions();
            });
        } catch (Exception e) {
            MsgSender.showMsg("An error occurred while deleting the question.");
            e.printStackTrace();
        }
    }

    /**
     * Filter the questions based on the filter criteria
     * @param event the ActionEvent
     */
    @FXML
    public void filterQuestions(ActionEvent event) {
        loadQuestions();
    }

    /**
     * Refresh the question table
     * @param event the ActionEvent
     */
    @FXML
    public void refreshQuestion(ActionEvent event) {
        if (!questionTextField.getText().isEmpty() || !optionATextField.getText().isEmpty() ||
                !optionBTextField.getText().isEmpty() || !optionCTextField.getText().isEmpty() ||
                !optionDTextField.getText().isEmpty() || !answerTextField.getText().isEmpty() ||
                !scoreTextField.getText().isEmpty() || typeChoiceBox.getValue() != null) {
            MsgSender.showConfirm("Refresh Confirmation", "Refreshing will clear all fields and any unsaved changes will be lost. Are you sure you want to refresh?", () -> {
                refreshQuestionConfirmed(event);
            });
            return;
        } else {
            refreshQuestionConfirmed(event);
        }
    }

    /**
     * Refresh the question table
     * @param event the ActionEvent
     */
    public void refreshQuestionConfirmed(ActionEvent event) {
        resetFilters(event);
        clearFields();
        questionTable.getSelectionModel().clearSelection();
        loadQuestions();
    }

    /**
     * Reset the filters
     * @param event the ActionEvent
     */
    @FXML
    public void resetFilters(ActionEvent event) {
        questionFilterTextField.clear();
        scoreFilterTextField.clear();
        typeFilterChoiceBox.setValue("");
        loadQuestions();
    }

    /**
     * Update a question in the database
     * @param event the ActionEvent
     */
    @FXML
    public void updateQuestion(ActionEvent event) {
        Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();

        if (selectedQuestion == null) {
            MsgSender.showMsg("Please select a question to update.");
            return;
        }

        String questionText = questionTextField.getText();
        String optionA = optionATextField.getText();
        String optionB = optionBTextField.getText();
        String optionC = optionCTextField.getText();
        String optionD = optionDTextField.getText();
        String answer = answerTextField.getText().replaceAll("[^A-Za-z]", ""); // Removes non-letter characters
        String score = scoreTextField.getText();
        String type = (String) typeChoiceBox.getValue();

        if (!validInputs(questionText, optionA, optionB, optionC, optionD, answer, score, type) ) {
            return;
        }

        selectedQuestion.setQuestionDescription(questionText);
        selectedQuestion.setOptionA(optionA);
        selectedQuestion.setOptionB(optionB);
        selectedQuestion.setOptionC(optionC);
        selectedQuestion.setOptionD(optionD);
        selectedQuestion.setAnswer(answer);
        selectedQuestion.setQuestionScore(score);
        selectedQuestion.setQuestionType(type);

        try {
            MsgSender.showConfirm("Question Update Confirmation", "Are you sure you want to update the question?", () -> {
                questionDatabase.update(selectedQuestion);
                MsgSender.showMsg("Question updated successfully.");
                questionTable.getSelectionModel().clearSelection();
                loadQuestions();
                clearFields();
            });
        } catch (Exception e) {
            MsgSender.showMsg("An error occurred while updating the question.");
            e.printStackTrace();
        }
    }

    /**
     * Check if there is no filter applied
     * @return true if no filter is applied, false otherwise
     */
    public boolean noFilter() {
        return (questionFilterTextField.getText().isEmpty() &&
                scoreFilterTextField.getText().isEmpty() &&
                (typeFilterChoiceBox.getValue() == null || typeFilterChoiceBox.getValue().isEmpty()));
    }

    /**
     * Load the questions from the database
     */
    public void loadQuestions() {
        if (noFilter()) {
            try {
                allQuestions = questionDatabase.getAll();
                questionList = FXCollections.observableArrayList(allQuestions);
            } catch (Exception e) {
                MsgSender.showMsg("An error occurred while loading the questions.");
                e.printStackTrace();
            }
        } else {
            try {
                String questionFilter = questionFilterTextField.getText();
                String scoreFilter = scoreFilterTextField.getText();
                String typeFilter = typeFilterChoiceBox.getValue();

                List<Question> allQuestions = questionDatabase.getAll();
                List<Question> filteredQuestions = new ArrayList<>();

                for (Question question : allQuestions) {
                    if ((questionFilter.isEmpty() || question.getQuestionDescription().contains(questionFilter)) &&
                            (scoreFilter.isEmpty() || question.getQuestionScore().equals(scoreFilter)) &&
                            (typeFilter.isEmpty() || question.getQuestionType().equals(typeFilter))) {
                        filteredQuestions.add(question);
                    }
                }

                questionList = FXCollections.observableArrayList(filteredQuestions);
            } catch (Exception e) {
                MsgSender.showMsg("An error occurred while loading the questions.");
                e.printStackTrace();
            }
        }
        questionTable.getItems().setAll(questionList);
    }

    /**
     * Clear all the input fields
     */
    public void clearFields() {
        questionTextField.clear();
        optionATextField.clear();
        optionBTextField.clear();
        optionCTextField.clear();
        optionDTextField.clear();
        answerTextField.clear();
        scoreTextField.clear();
        typeChoiceBox.setValue("");
    }

}
