package comp3111.examsystem.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import comp3111.examsystem.model.Course;
import comp3111.examsystem.model.Question;
import comp3111.examsystem.model.Quiz;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

/**
 * Controller for the Teacher's Exam Management page
 */
public class TeacherExamManagementController {
    public Database<Question> questionDatabase;
    public List<Question> allQuestions;
    public ObservableList<Question> questionList;
    public Database<Quiz> quizDatabase;
    public List<Quiz> allQuizzes;
    public ObservableList<Quiz> quizList;
    public Database<Course> courseDatabase;
    public List<Course> allCourses;
    public ObservableList<Course> courseList;
    public ObservableList<Question> examQuestionList;

    @FXML
    public ResourceBundle resources;
    @FXML
    public URL location;
    @FXML
    public Button addButton;
    @FXML
    public Button addQuestionButton;
    @FXML
    public ChoiceBox<String> courseIDFilterChoiceBox;
    @FXML
    public ChoiceBox<String> courseIDInput;
    @FXML
    public Button deleteButton;
    @FXML
    public TableColumn<Quiz, String> examCourseIDColumn;
    @FXML
    public Button examFilterResetButton;
    @FXML
    public HBox examManagementButtons;
    @FXML
    public HBox examManagementPanel;
    @FXML
    public TableColumn<Quiz, String> examNameColumn;
    @FXML
    public TextField examNameFilterTextField;
    @FXML
    public TextField examNameInput;
    @FXML
    public TableColumn<Quiz, String> examPublishStatusColumn;
    @FXML
    public TableColumn<Question, String> examQuestionColumn;
    @FXML
    public TableColumn<Question, String> examQuestionScoreColumn;
    @FXML
    public TableColumn<Question, String> examQuestionTypeColumn;
    @FXML
    public TableColumn<Quiz, String> examTimeColumn;
    @FXML
    public TextField examTimeInput;
    @FXML
    public HBox filterPanel;
    @FXML
    public VBox optionCInput;
    @FXML
    public VBox optionDInput;
    @FXML
    public ChoiceBox<String> publishInput;
    @FXML
    public ChoiceBox<String> publishStatusFilterChoiceBox;
    @FXML
    public TableColumn<Question, String> questionColumn;
    @FXML
    public Button questionFilterButton;
    @FXML
    public Button questionFilterResetButton;
    @FXML
    public TextField questionFilterTextField;
    @FXML
    public VBox questionInputFields;
    @FXML
    public TableColumn<Question, String> questionScoreColumn;
    @FXML
    public TableView<Quiz> examTable;
    @FXML
    public TableView<Question> examQuestionsTable;
    @FXML
    public TableView<Question> allQuestionsTable;
    @FXML
    public TableColumn<Question, String> questionTypeColumn;
    @FXML
    public Button refreshButton;
    @FXML
    public Button removeQuestionButton;
    @FXML
    public Button examFilterButton;
    @FXML
    public FlowPane rootPane;
    @FXML
    public TextField scoreFilterTextField;
    @FXML
    public ChoiceBox<String> typeFilterChoiceBox;
    @FXML
    public Button updateButton;
    @FXML
    public HBox examFilterPanel;
    @FXML
    public HBox questionFilterPanel;
    @FXML
    public VBox examManagementField;
    @FXML
    public HBox questionTablesField;

    /**
     * Initialize the controller
     */
    @FXML
    public void initialize() {
        this.questionDatabase = new Database<>(Question.class);
        allQuestions = questionDatabase.getAll();
        loadAllQuestions();

        this.quizDatabase = new Database<>(Quiz.class);
        allQuizzes = quizDatabase.getAll();
        loadQuizzes();

        this.courseDatabase = new Database<>(Course.class);
        allCourses = courseDatabase.getAll();

        examQuestionList = FXCollections.observableArrayList();

        // Set up the exam filter options
        for (Quiz quiz : allQuizzes) {
            courseIDFilterChoiceBox.getItems().add(quiz.getCourseID());
        }
        courseIDFilterChoiceBox.getItems().add("");
        courseIDFilterChoiceBox.setValue("");
        publishStatusFilterChoiceBox.getItems().addAll("Yes", "No", "");
        publishStatusFilterChoiceBox.setValue("");

        // Set up the question filter options
        typeFilterChoiceBox.getItems().addAll("Single", "Multiple", "");
        typeFilterChoiceBox.setValue("");

        // Set up exam input filter options
        for (Course course : allCourses) {
            courseIDInput.getItems().add(course.getCourseID());
        }
        publishInput.getItems().addAll("Yes", "No");

        // Handle resize of width of different components when the window is resized
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Handle resize of filter components
            filterPanel.setPrefWidth(newVal.doubleValue());
            examFilterPanel.setPrefHeight(newVal.doubleValue() * 0.5);
            questionFilterPanel.setPrefHeight(newVal.doubleValue() * 0.5);

            // Handle resize of exam management components
            examManagementPanel.setPrefWidth(newVal.doubleValue());

            // Handle resize of exam table
            double examTableWidth = newVal.doubleValue() * 0.34;
            examTable.setPrefWidth(examTableWidth);
            examNameColumn.setPrefWidth(examTableWidth * 0.3);
            examCourseIDColumn.setPrefWidth(examTableWidth * 0.3);
            examTimeColumn.setPrefWidth(examTableWidth * 0.2);
            examPublishStatusColumn.setPrefWidth(examTableWidth * 0.193);

            // Handle resize of exam questions table and all questions table
            double examManagementFieldWidth = newVal.doubleValue() * 0.66;
            examManagementField.setPrefWidth(examManagementFieldWidth);
            double examQuestionsTableWidth = examManagementFieldWidth * 0.5;
            examQuestionsTable.setPrefWidth(examQuestionsTableWidth);
            examQuestionColumn.setPrefWidth(examQuestionsTableWidth * 0.6);
            examQuestionTypeColumn.setPrefWidth(examQuestionsTableWidth * 0.2);
            examQuestionScoreColumn.setPrefWidth(examQuestionsTableWidth * 0.193);
            double allQuestionsTableWidth = examManagementFieldWidth * 0.5;
            allQuestionsTable.setPrefWidth(allQuestionsTableWidth);
            questionColumn.setPrefWidth(allQuestionsTableWidth * 0.6);
            questionTypeColumn.setPrefWidth(allQuestionsTableWidth * 0.2);
            questionScoreColumn.setPrefWidth(allQuestionsTableWidth * 0.193);

            // Handle resize of exam management buttons
            examManagementButtons.setPrefWidth(newVal.doubleValue());
        });

        // Handle resize of height of different components when the window is resized
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            // Handle resize of filter components
            filterPanel.setPrefHeight(newVal.doubleValue() * 0.10);

            // Handle resize of exam management components
            double examManagementPanelHeight = newVal.doubleValue() * 0.75;
            examManagementPanel.setPrefHeight(examManagementPanelHeight);
            examTable.setPrefHeight(examManagementPanelHeight);
            examManagementField.setPrefHeight(examManagementPanelHeight);
            double questionTablesFieldHeight = examManagementPanelHeight * 0.70;
            questionTablesField.setPrefHeight(questionTablesFieldHeight);
            examQuestionsTable.setPrefHeight(questionTablesFieldHeight);
            allQuestionsTable.setPrefHeight(questionTablesFieldHeight);

            // Handle resize of question management buttons
            examManagementButtons.setPrefHeight(newVal.doubleValue() * 0.10);
        });


        // Set up columns to extract data from each Quiz object for examTable
        examNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuizName()));
        examCourseIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseID()));
        examTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuizTime()));
        examPublishStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPublishStatus()));

        // Set up columns to extract data from each Question object for examQuestionsTable
        examQuestionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestionDescription()));
        examQuestionTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestionType()));
        examQuestionScoreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestionScore()));

        // Set up columns to extract data from each Question object for allQuestionsTable
        questionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestionDescription()));
        questionTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestionType()));
        questionScoreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestionScore()));

        // Set up the listener for the table selection
        examTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadExamQuestions(newSelection.getId());

                examNameInput.setText(newSelection.getQuizName());
                examTimeInput.setText(newSelection.getQuizTime());
                courseIDInput.setValue(newSelection.getCourseID());
                publishInput.setValue(newSelection.getPublishStatus());
            }
        });

        // Clear the selection when clicking on the table itself with no quiz selected
        examTable.setRowFactory(tv -> {
            TableRow<Quiz> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    examTable.getSelectionModel().clearSelection();
                    examQuestionList.clear();
                    loadExamQuestions();
                    clearFields();
                }
            });
            return row;
        });

    }

    /**
     * Handle the add exam button action
     * @param event the ActionEvent
     */
    @FXML
    public void addExam(ActionEvent event) {
        String examName = examNameInput.getText();
        String examTime = examTimeInput.getText();
        String courseID = courseIDInput.getValue();
        String publishStatus = publishInput.getValue();
        String questionIDs = getQuestionIDs(examQuestionList);

        if (!validInputs(examName, examTime, courseID, publishStatus, questionIDs)) {
            return;
        }

        // check if the quiz already exists
        for (Quiz quiz : allQuizzes) {
            if (quiz.getQuizName().equals(examName) && quiz.getCourseID().equals(courseID)) {
                MsgSender.showMsg("The exam already exists.");
                return;
            }
        }

        try {
            int quizId = allQuizzes.size() + 1;
            Quiz newQuiz = new Quiz(examName, examTime, courseID, publishStatus, quizId, questionIDs);

            // Store the quiz information in the database
            quizDatabase.add(newQuiz);

            MsgSender.showMsg("Exam added successfully.");
            loadQuizzes();
            examQuestionList.clear();
            loadExamQuestions();
            clearFields();
        } catch (Exception e) {
            MsgSender.showMsg("An error occurred while adding the exam.");
            e.printStackTrace();
        }
    }

    /**
     * Get the question IDs from the exam question list
     * @param examQuestionList list of questions in the exam
     * @return a string of question IDs concatenated by "|"
     */
    public String getQuestionIDs(ObservableList<Question> examQuestionList) {
        return examQuestionList.stream()
                .map(question -> String.valueOf(question.getId()))
                .collect(Collectors.joining("|"));
    }

    /**
     * Check if the inputs for adding or updating an exam are valid
     * @param examName the name of the exam
     * @param examTime the time limit of the exam
     * @param courseID the course ID of the exam
     * @param publishStatus the publish status of the exam
     * @param questionIDs the question IDs of the exam
     * @return true if all inputs are valid, false otherwise
     */
    public boolean validInputs(String examName, String examTime, String courseID, String publishStatus, String questionIDs) {
        // check if all fields are filled
        if (examName.isEmpty() || examTime.isEmpty() || courseID.isEmpty() || publishStatus.isEmpty()) {
            MsgSender.showMsg("All fields must be filled.");
            return false;
        }

        // check if there is at least one question in the exam
        if (questionIDs.isEmpty()) {
            MsgSender.showMsg("At least one question must be added to the exam.");
            return false;
        }

        // check if examTime is a number
        int examTimeInt = 0;
        try {
            examTimeInt = Integer.parseInt(examTime);
        } catch (NumberFormatException e) {
            MsgSender.showMsg("Exam time must be a number.");
            return false;
        }

        // check if examTime is a positive number
        if (examTimeInt <= 0) {
            MsgSender.showMsg("Exam time must be a positive number.");
            return false;
        }

        return true;
    }

    /**
     * Handle the delete exam button action
     * @param event the ActionEvent
     */
    @FXML
    public void deleteExam(ActionEvent event) {
        Quiz selectedQuiz = examTable.getSelectionModel().getSelectedItem();

        if (selectedQuiz == null) {
            MsgSender.showMsg("Please select an exam to delete.");
            return;
        }

        try {
            String idString = String.valueOf(selectedQuiz.getId());
            MsgSender.showConfirm("Exam Delete Confirmation", "Are you sure that you want to delete the exam?", () -> {
                quizDatabase.delByKey(idString);
                MsgSender.showMsg("Exam deleted successfully.");
                examTable.getSelectionModel().clearSelection();
                loadQuizzes();
                examQuestionList.clear();
                loadExamQuestions();
                clearFields();
            });
        } catch (Exception e) {
            MsgSender.showMsg("An error occurred while deleting the exam.");
            e.printStackTrace();
        }
    }

    /**
     * Handle the update exam button action
     * @param event the ActionEvent
     */
    @FXML
    public void updateExam(ActionEvent event) {
        Quiz selectedQuiz = examTable.getSelectionModel().getSelectedItem();

        if (selectedQuiz == null) {
            MsgSender.showMsg("Please select an exam to update.");
            return;
        }

        String oldPublishStatus = selectedQuiz.getPublishStatus();

        // check if the exam is published
        if (oldPublishStatus.equals("Yes") || oldPublishStatus.equals("yes")) {
            MsgSender.showMsg("Cannot update a published exam.");
            return;
        }

        String examName = examNameInput.getText();
        String examTime = examTimeInput.getText();
        String courseID = courseIDInput.getValue();
        String publishStatus = publishInput.getValue();
        String questionIDs = getQuestionIDs(examQuestionList);

        // check valid inputs
        if (!validInputs(examName, examTime, courseID, publishStatus, questionIDs)) {
            return;
        }

        selectedQuiz.setQuizName(examName);
        selectedQuiz.setQuizTime(examTime);
        selectedQuiz.setCourseID(courseID);
        selectedQuiz.setPublishStatus(publishStatus);
        selectedQuiz.setQuestionIDs(questionIDs);

        try {
            MsgSender.showConfirm("Exam Update Confirmation", "Are you sure that you want to update the exam?", () -> {
                quizDatabase.update(selectedQuiz);
                MsgSender.showMsg("Exam updated successfully.");
                examTable.getSelectionModel().clearSelection();
                loadQuizzes();
                examQuestionList.clear();
                loadExamQuestions();
                clearFields();
            });
        } catch (Exception e) {
            MsgSender.showMsg("An error occurred while updating the exam.");
            e.printStackTrace();
        }
    }

    /**
     * Get the list of questions from the database
     * @param questionIDs the string of question IDs concatenated by "|"
     * @return the list of questions
     */
    public List<Question> getExamQuestionList(String questionIDs) {
        List<String> questionIDsList = List.of(questionIDs.split("\\|"));
        List<Question> questions = new ArrayList<>();

        try {
            questions = questionDatabase.queryByKeys(questionIDsList);
        } catch (Exception e) {
            MsgSender.showMsg("An error occurred while loading the exam questions.");
            e.printStackTrace();
        }
        return questions;
    }

    /**
     * Handle the add question button action
     * @param event the ActionEvent
     */
    @FXML
    public void addQuestionToExams(ActionEvent event) {
        Question selectedQuestion = allQuestionsTable.getSelectionModel().getSelectedItem();

        if (selectedQuestion == null) {
            MsgSender.showMsg("Please select a question to add to the exam.");
            return;
        }

        // check if the question is already added to the exam
        for (Question question : examQuestionList) {
            if (question != null && question.getId() == selectedQuestion.getId()) {
                MsgSender.showMsg("Question already added to the exam.");
                return;
            }
        }

        try {
            examQuestionList.add(selectedQuestion);
        } catch (Exception e) {
            MsgSender.showMsg("An error occurred while adding the question to the exam.");
            e.printStackTrace();
        }
        MsgSender.showMsg("Question added to the exam successfully.");
        loadExamQuestions();
        allQuestionsTable.getSelectionModel().clearSelection();
    }

    /**
     * Handle the remove question button action
     * @param event the ActionEvent
     */
    @FXML
    public void removeQuestionFromExam(ActionEvent event) {
        Question selectedQuestion = examQuestionsTable.getSelectionModel().getSelectedItem();

        if (selectedQuestion == null) {
            MsgSender.showMsg("Please select a question to be removed from the exam.");
            return;
        }

        try {
            examQuestionList.remove(selectedQuestion);
        } catch (Exception e) {
            MsgSender.showMsg("An error occurred while removing the question from the exam.");
            e.printStackTrace();
        }
        MsgSender.showMsg("Question removed from the exam successfully.");
        loadExamQuestions();
        examQuestionsTable.getSelectionModel().clearSelection();
    }

    /**
     * Handle the refresh button action
     * @param event the ActionEvent
     */
    @FXML
    public void refreshExamManagementPage(ActionEvent event) {
        if (examTable.getSelectionModel().getSelectedItem() != null || !examTimeInput.getText().isEmpty() ||
                !examNameInput.getText().isEmpty() ||
                (courseIDInput.getValue() != null && !courseIDInput.getValue().isEmpty()) ||
                (publishInput.getValue() != null && !publishInput.getValue().isEmpty())) {
            // ask for confirmation before refreshing
            MsgSender.showConfirm("Refresh Confirmation", "Refreshing will clear all fields and any unsaved changes will be lost. Are you sure you want to refresh?", () -> {
                refreshExamManagementPageConfirmed(event);
            });
        } else {
            refreshExamManagementPageConfirmed(event);
        }
    }

    /**
     * Handle the refresh button action
     * @param event the ActionEvent
     */
    public void refreshExamManagementPageConfirmed(ActionEvent event) {
        // reset all filters
        resetExamFilters(event);
        resetQuestionFilters(event);

        // clear all fields and deselect any selected rows
        clearFields();
        examTable.getSelectionModel().clearSelection();
        examQuestionsTable.getSelectionModel().clearSelection();
        allQuestionsTable.getSelectionModel().clearSelection();

        // reload all quizzes and questions
        loadQuizzes();
        loadAllQuestions();
    }

    /**
     * Handle the reset exam filter button action
     * @param event the ActionEvent
     */
    @FXML
    public void resetExamFilters(ActionEvent event) {
        examNameFilterTextField.clear();
        courseIDFilterChoiceBox.setValue("");
        publishStatusFilterChoiceBox.setValue("");
        loadQuizzes();
    }

    /**
     * Handle the filter exam button action
     * @param event the ActionEvent
     */
    @FXML
    public void filterQuizzes(ActionEvent event) {
        loadQuizzes();
    }

    /**
     * Handle the reset question filter button action
     * @param event the ActionEvent
     */
    @FXML
    public void resetQuestionFilters(ActionEvent event) {
        questionFilterTextField.clear();
        typeFilterChoiceBox.setValue("");
        scoreFilterTextField.clear();
        loadAllQuestions();
    }

    /**
     * Handle the filter question button action
     * @param event the ActionEvent
     */
    @FXML
    public void filterQuestions(ActionEvent event) {
        loadAllQuestions();
    }

    /**
     * Check if there is no filter applied for questions
     * @return true if there is no filter applied, false otherwise
     */
    public boolean noQuestionFilter() {
        return (questionFilterTextField.getText().isEmpty() &&
                (typeFilterChoiceBox.getValue() == null || typeFilterChoiceBox.getValue().isEmpty()) &&
                scoreFilterTextField.getText().isEmpty());
    }

    /**
     * Check if there is no filter applied for exams
     * @return true if there is no filter applied, false otherwise
     */
    public boolean noExamFilter() {
        return (examNameFilterTextField.getText().isEmpty() &&
                (courseIDFilterChoiceBox.getValue() == null || courseIDFilterChoiceBox.getValue().isEmpty()) &&
                (publishStatusFilterChoiceBox.getValue() == null || publishStatusFilterChoiceBox.getValue().isEmpty()));
    }

    /**
     * Load all questions from the database
     */
    public void loadAllQuestions() {
        if (noQuestionFilter()) {
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
                String typeFilter = typeFilterChoiceBox.getValue();
                String scoreFilter = scoreFilterTextField.getText();

                allQuestions = questionDatabase.getAll();
                List<Question> filteredQuestions = new ArrayList<>();

                for (Question question : allQuestions) {
                    if ((questionFilter.isEmpty() || question.getQuestionDescription().contains(questionFilter)) &&
                            (typeFilter.isEmpty() || question.getQuestionType().equals(typeFilter)) &&
                            (scoreFilter.isEmpty() || question.getQuestionScore().equals(scoreFilter))) {
                        filteredQuestions.add(question);
                    }
                }

                questionList = FXCollections.observableArrayList(filteredQuestions);
            } catch (Exception e) {
                MsgSender.showMsg("An error occurred while filtering the questions.");
                e.printStackTrace();
            }
        }

        allQuestionsTable.getItems().setAll(questionList);
    }

    /**
     * Load exam questions from the exam question list
     */
    public void loadExamQuestions() {
        examQuestionsTable.getItems().setAll(examQuestionList);
    }

    /**
     * Load exam questions from the database
     * @param id the id of the exam
     */
    public void loadExamQuestions(long id) {
        try {
            Quiz quiz = quizDatabase.queryByKey(Long.toString(id));
            String questionIDsString = quiz.getQuestionIDs();

            List<String> questionIDs = List.of(questionIDsString.split("\\|"));
            List<Question> questions = new ArrayList<>();

            for (String questionID : questionIDs) {
                questions.add(questionDatabase.queryByKey(questionID));
            }

            examQuestionList.clear();
            examQuestionList = FXCollections.observableArrayList(questions);
            examQuestionsTable.getItems().setAll(examQuestionList);
        } catch (Exception e) {
            MsgSender.showMsg("An error occurred while loading the exam questions.");
            e.printStackTrace();
        }
    }

    /**
     * Load exams from the database
     */
    public void loadQuizzes() {
        Platform.runLater(() -> {
            if (noExamFilter()) {
                try {
                    allQuizzes = quizDatabase.getAll();
                    quizList = FXCollections.observableArrayList(allQuizzes);
                } catch (Exception e) {
                    MsgSender.showMsg("An error occurred while loading the exams.");
                    e.printStackTrace();
                }
            } else {
                try {
                    String examNameFilter = examNameFilterTextField.getText();
                    String courseIDFilter = courseIDFilterChoiceBox.getValue();
                    String publishFilter = publishStatusFilterChoiceBox.getValue();

                    allQuizzes = quizDatabase.getAll();
                    List<Quiz> filteredQuizzes = new ArrayList<>();

                    for (Quiz quiz : allQuizzes) {
                        if ((examNameFilter.isEmpty() || quiz.getQuizName().contains(examNameFilter)) &&
                                (courseIDFilter.isEmpty() || quiz.getCourseID().equals(courseIDFilter)) &&
                                (publishFilter.isEmpty() || quiz.getPublishStatus().equals(publishFilter))) {
                            filteredQuizzes.add(quiz);
                        }
                    }

                    quizList = FXCollections.observableArrayList(filteredQuizzes);
                } catch (Exception e) {
                    MsgSender.showMsg("An error occurred while filtering the exams.");
                    e.printStackTrace();
                }
            }

            examTable.getItems().setAll(quizList);
        });
    }

    /**
     * Clear all fields
     */
    public void clearFields() {
        examNameInput.clear();
        examTimeInput.clear();
        courseIDInput.setValue("");
        publishInput.setValue("");
    }

}
