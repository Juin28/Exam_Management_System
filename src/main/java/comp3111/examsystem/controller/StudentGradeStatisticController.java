package comp3111.examsystem.controller;

import comp3111.examsystem.model.*;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class for displaying student grade statistics.
 */
public class StudentGradeStatisticController implements Initializable {
    public static class GradeTableRow {
        private final String courseName;
        private final String examName;
        private final String score;
        private final String fullScore;
        private final String timeSpent;

        /**
         * Inner class representing a row in the grade table.
         */
        public GradeTableRow(String courseName, String examName, String score, String fullScore, String timeSpent) {
            this.courseName = courseName;
            this.examName = examName;
            this.score = score;
            this.fullScore = fullScore;
            this.timeSpent = timeSpent;
        }

        public String getCourseName() {
            return courseName;
        }

        public String getExamName() {
            return examName;
        }

        public String getScore() {
            return score;
        }

        public String getFullScore() {
            return fullScore;
        }

        public String getTimeSpent() {
            return timeSpent;
        }
    }

    public Database<Course> courseDatabase;
    public Database<Grade> gradeDatabase;
    public Database<Quiz> quizDatabase;
    public Database<Question> questionDatabase;

    public Student currStudent;
    public List<String> coursesList;
    public String[] questionIds;

    @FXML
    public BarChart<String, Number> barChart;

    @FXML
    public TableColumn<?, ?> courseColumn;

    @FXML
    public ChoiceBox<String> courseCombox;

    @FXML
    public TableColumn<?, ?> examColumn;

    @FXML
    public Button filterBtn;

    @FXML
    public TableColumn<?, ?> fullScoreColumn;

    @FXML
    public TableView<GradeTableRow> gradeTable;

    @FXML
    public Button resetBtn;

    @FXML
    public TableColumn<?, ?> scoreColumn;

    @FXML
    public TableColumn<?, ?> timeSpendColumn;

    @FXML
    public CategoryAxis xAxis;

    @FXML
    public NumberAxis yAxis;

    public ObservableList<GradeTableRow> gradeRows = FXCollections.observableArrayList();

    /**
     * Initializes the controller.
     *
     * @param url the URL to initialize
     * @param resourceBundle the ResourceBundle to initialize
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        courseDatabase = new Database<>(Course.class);
        gradeDatabase = new Database<>(Grade.class);
        quizDatabase = new Database<>(Quiz.class);
        questionDatabase = new Database<>(Question.class);

        currStudent = StudentLoginController.loggedInStudent;
        coursesList = new ArrayList<>();

        barChart.setLegendVisible(false);
        yAxis.setLabel("Score");
        xAxis.setLabel("Exam");
        showCourses();
        initTable();
        showBarChart();
    }

    /**
     * Shows the bar chart with the student's grades.
     */
    public void showBarChart() {
        barChart.getData().clear();

        try {
            // Create a series for the chart
            XYChart.Series<String, Number> series = new XYChart.Series<>();

            // Populate data for the current student
            for (GradeTableRow row : gradeRows) {
                String examName = row.getExamName();
                Number score = Integer.parseInt(row.getScore());
                series.getData().add(new XYChart.Data<>(examName, score));
            }

            // Add the series to the chart
            barChart.getData().add(series);
        } catch (Exception e) {
            MsgSender.showMsg("An error occurred while displaying the bar chart.");
            e.printStackTrace();
        }
    }

    /**
     * Shows the courses in the courseCombox.
     */
    public void showCourses() {
        try {
            // Iterate through all grades in the database
            for (int i = 0; i < gradeDatabase.getAll().size(); ++i) {
                Grade grade = gradeDatabase.getAll().get(i);
                // Check if the grade belongs to the current student
                if (grade.getStudentId().equals(String.valueOf(currStudent.getId()))) {
                    // Query the database for quizzes associated with the grade
                    List<Quiz> quizzes = quizDatabase.queryByField("id", grade.getQuestionId());
                    if (quizzes.isEmpty()) {
                        continue; // Skip this grade if no quiz is found
                    }
                    Quiz q = quizzes.getFirst();
                    // Iterate through all courses in the database
                    for (Course c : courseDatabase.getAll()) {
                        // Check if the course is associated with the quiz
                        if (c.getCourseID().equals(q.getCourseID())) {
                            coursesList.add(c.getCourseName());
                        }
                    }
                }
            }
            // Remove duplicate course names
            List<String> toShow = new ArrayList<>();
            for (String c : coursesList) {
                if (!toShow.contains(c)) {
                    toShow.add(c);
                }
            }
            // Add the course names to the courseCombox
            courseCombox.getItems().addAll(toShow);
        } catch (Exception e) {
            // Show an error message if an exception occurs
            MsgSender.showMsg("An error occurred while showing courses.");
            e.printStackTrace();
        }
    }

    /**
     * Initializes the table with the student's grades.
     */
    public void initTable() {
        try {
            // Retrieve all grades for the current student
            List<Grade> studentGrades = new ArrayList<>();
            for (Grade grade : gradeDatabase.getAll()) {
                if (grade.getStudentId().equals(Long.toString(currStudent.getId()))) {
                    studentGrades.add(grade);
                }
            }

            // Create an ObservableList for the table
            for (Grade grade : studentGrades) {
                // Get the quiz associated with the grade
                List<Quiz> quizzes = quizDatabase.queryByField("id", grade.getQuestionId());
                if (quizzes.isEmpty()) {
                    continue; // Skip this grade if the quiz doesn't exist
                }
                Quiz quiz = quizzes.get(0);

                // Get the total score by summing the questions in the quiz
                int total = 0;
                questionIds = StudentMainController.splitByPipe(quiz.getQuestionIDs());
                for (String questionId : questionIds) {
                    List<Question> questions = questionDatabase.queryByField("id", questionId);
                    if (questions.isEmpty()) {
                        continue; // Skip this question if it doesn't exist
                    }
                    total += Integer.parseInt(questions.getFirst().getQuestionScore());
                }

                // Get the course associated with the quiz
                List<Course> courses = courseDatabase.queryByField("courseID", quiz.getCourseID());
                if (courses.isEmpty()) {
                    continue; // Skip this grade if the course doesn't exist
                }
                Course course = courses.getFirst();

                // Add a row for each grade if the table is not full. Table should have the capacity of the number of courses
                if (gradeRows.size() < coursesList.size()) {
                    gradeRows.add(new GradeTableRow(
                            course.getCourseName(),
                            quiz.getQuizName(),
                            grade.getStudentScore(),
                            String.valueOf(total),
                            grade.getTimeSpent()
                    ));
                }
            }

            // Set the table items
            gradeTable.setItems(gradeRows);
            // Bind columns to the GradeTableRow properties
            courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
            examColumn.setCellValueFactory(new PropertyValueFactory<>("examName"));
            scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
            fullScoreColumn.setCellValueFactory(new PropertyValueFactory<>("fullScore"));
            timeSpendColumn.setCellValueFactory(new PropertyValueFactory<>("timeSpent"));
        } catch (Exception e) {
            // Show an error message if an exception occurs
            MsgSender.showMsg("An error occurred while initializing the table.");
            e.printStackTrace();
        }
    }

    /**
     * Event handler for querying student grades.
     *
     * @param event the ActionEvent triggering the query
     */
    @FXML
    void query(ActionEvent event) {
        barChart.getData().clear();
        ObservableList<GradeTableRow> tmp = FXCollections.observableArrayList();
        try {
            for (GradeTableRow row : gradeRows) {
                // Check if the course name matches the selected value in the courseCombox
                if (row.getCourseName().equals(courseCombox.getValue())) {
                    // Create a series for the chart
                    XYChart.Series<String, Number> series = new XYChart.Series<>();

                    // Populate data for the current student
                    String examName = row.getExamName();
                    Number score = Integer.parseInt(row.getScore());
                    series.getData().add(new XYChart.Data<>(examName, score));

                    // Add the series to the chart
                    tmp.add(new GradeTableRow(
                            row.getCourseName(),
                            row.getExamName(),
                            row.getScore(),
                            row.getFullScore(),
                            row.getTimeSpent()
                    ));
                    barChart.getData().add(series);
                    gradeTable.setItems(tmp);
                }
            }
        } catch (Exception e) {
            // Show an error message if an exception occurs
            MsgSender.showMsg("An error occurred while querying student grades.");
            e.printStackTrace();
        }
    }

    /**
     * Event handler for resetting filters.
     *
     * @param event the ActionEvent triggering the reset
     */
    @FXML
    void reset(ActionEvent event) {
        // Reset filters and show all data
        courseCombox.setValue(null);
        gradeTable.setItems(gradeRows);
        showBarChart();
    }
}
