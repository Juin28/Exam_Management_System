package comp3111.examsystem.controller;

import comp3111.examsystem.model.*;
import comp3111.examsystem.service.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class TeacherGradeStatisticController implements Initializable {
    public static class GradeExampleClass {
        public String getStudentName() {
            return "student";
        }
        public String getCourseNum() {
            return "comp3111";
        }
        public String getExamName() {
            return "final";
        }
        public String getScore() {
            return "100";
        }
        public String getFullScore() {
            return "100";
        }
        public String getTimeSpend() {
            return "60";
        }
    }

    public static class GradeStatisticClass{
        public String course;
        public String exam;
        public String student;
        public String score;
        public String fullScore;
        public String timeSpend;
        public GradeStatisticClass(String student, String course, String exam, String score, String fullScore, String timeSpend){
            this.course = course;
            this.exam = exam;
            this.student = student;
            this.score = score;
            this.fullScore = fullScore;
            this.timeSpend = timeSpend;
        }
        public String getStudent() {
            return student;
        }
        public String getCourse() {
            return course;
        }
        public String getExam() {
            return exam;
        }
        public String getScore() {
            return score;
        }
        public String getFullScore() {
            return fullScore;
        }
        public String getTimeSpend() {
            return timeSpend;
        }
    }

    @FXML
    public ChoiceBox<String> courseCombox;
    @FXML
    public ChoiceBox<String> examCombox;
    @FXML
    public ChoiceBox<String> studentCombox;
    @FXML
    public TableView<GradeStatisticClass> gradeTable;
    @FXML
    private TableColumn<GradeExampleClass, String> studentColumn;
    @FXML
    private TableColumn<GradeExampleClass, String> courseColumn;
    @FXML
    private TableColumn<GradeExampleClass, String> examColumn;
    @FXML
    private TableColumn<GradeExampleClass, String> scoreColumn;
    @FXML
    private TableColumn<GradeExampleClass, String> fullScoreColumn;
    @FXML
    private TableColumn<GradeExampleClass, String> timeSpendColumn;
    @FXML
    BarChart<String, Number> barChart;
    @FXML
    CategoryAxis categoryAxisBar;
    @FXML
    NumberAxis numberAxisBar;
    @FXML
    LineChart<String, Number> lineChart;
    @FXML
    CategoryAxis categoryAxisLine;
    @FXML
    NumberAxis numberAxisLine;
    @FXML
    PieChart pieChart;

    public ObservableList<GradeStatisticClass> gradeListObservable = FXCollections.observableArrayList();
    public Database<Grade> gradeDatabase;
    public Database<Quiz> quizDatabase;
    public Database<Student> studentDatabase;
    public Database<Course> courseDatabase;
    public Database<Question> questionDatabase;
    public List<Grade> gradeList;
    public List<Quiz> quizList;
    public List<Student> studentList;
    public List<Course> courseList;
    public List<GradeStatisticClass> gradeStatisticList;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gradeDatabase = new Database<>(Grade.class);
        quizDatabase = new Database<>(Quiz.class);
        studentDatabase = new Database<>(Student.class);
        courseDatabase = new Database<>(Course.class);
        questionDatabase = new Database<>(Question.class);

        gradeStatisticList = new ArrayList<>();

        // populate the gradeStatisticList
        populateGradeStatisticList();

        // populate the ChoiceBoxes
        populateChoiceBoxes();

        barChart.setLegendVisible(false);
        categoryAxisBar.setLabel("Course");
        numberAxisBar.setLabel("Avg. Score");
        pieChart.setLegendVisible(false);
        pieChart.setTitle("Student Scores");
        lineChart.setLegendVisible(false);
        categoryAxisLine.setLabel("Exam");
        numberAxisLine.setLabel("Avg. Score");

        studentColumn.setCellValueFactory(new PropertyValueFactory<>("student"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("exam"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        fullScoreColumn.setCellValueFactory(new PropertyValueFactory<>("fullScore"));
        timeSpendColumn.setCellValueFactory(new PropertyValueFactory<>("timeSpend"));

        refresh();
        loadChart(gradeStatisticList);


    }

    public void populateChoiceBoxes()
    {

        List<String> courseNames = new ArrayList<>();
        List<String> examNames = new ArrayList<>();
        List<String> studentNames = new ArrayList<>();
        for(GradeStatisticClass grade: gradeStatisticList)
        {
            if(!courseNames.contains(grade.course))
            {
                courseNames.add(grade.course);
            }
            if(!examNames.contains(grade.exam))
            {
                examNames.add(grade.exam);
            }
            if(!studentNames.contains(grade.student))
            {
                studentNames.add(grade.student);
            }
        }

        courseCombox.getItems().addAll(courseNames);
        examCombox.getItems().addAll(examNames);
        studentCombox.getItems().addAll(studentNames);
    }

    public List<GradeStatisticClass> populateGradeStatisticList()
    {
        gradeList = gradeDatabase.getAll();
        quizList = quizDatabase.getAll();
        studentList = studentDatabase.getAll();
        courseList = courseDatabase.getAll();

        Map<String, String> quizTotal = new HashMap<>();

        for (Quiz quiz : quizList) {
            String[] questionIDs = quiz.getQuestionIDs().split("\\|");
            int totalScore = 0;
            for (String questionID : questionIDs) {
                Question question = questionDatabase.queryByKey(questionID);
                totalScore += Integer.parseInt(question.getQuestionScore());
            }
            quizTotal.put(Long.toString(quiz.getId()), Integer.toString(totalScore));
        }

        // make the grade statistic list
        for (Grade grade : gradeList)
        {
            // find the courseID of the quiz
            Quiz quizInGrade = quizDatabase.queryByKey(grade.getQuestionId());
            String courseID = quizInGrade.getCourseID();
            String quizName = quizInGrade.getQuizName();
            String studentName = studentDatabase.queryByKey(grade.getStudentId()).getName();
            GradeStatisticClass gradeStatistic = new GradeStatisticClass (
                    studentName,
                    courseID,
                    quizName,
                    grade.getStudentScore(),
                    quizTotal.get(grade.getQuestionId()),
                    grade.getTimeSpent()
            );

            // if the course is not in the list, add it
            if (!isDuplicate(gradeStatistic, gradeStatisticList))
            {
                gradeStatisticList.add(gradeStatistic);
                gradeListObservable.add(gradeStatistic);
            }
        }

        // make the grade statistic list
        gradeTable.setItems(gradeListObservable);
        return gradeStatisticList;


    }

    @FXML
    public void refresh() {
        // clear the combox boxes and load the data
        courseCombox.getItems().clear();
        examCombox.getItems().clear();
        studentCombox.getItems().clear();

        // populate the combox boxes
        populateChoiceBoxes();

        // load the data
        populateGradeStatisticList();

        loadChart(gradeStatisticList);
        loadList(gradeStatisticList);
    }

    public void loadChart(List<GradeStatisticClass> gradeStatisticList) {

        loadBarChart(gradeStatisticList);
        loadPieChart(gradeStatisticList);
        loadLineChart(gradeStatisticList);

    }

    public void loadBarChart(List<GradeStatisticClass> gradeStatisticList)
    {
        XYChart.Series<String, Number> seriesBar = new XYChart.Series<>();
        seriesBar.getData().clear();
        barChart.getData().clear();
        // bar chart shows the grade average of each course
        // in the list, the first entry represents the total score, and the second represents the number of grades
        HashMap <String, List<Integer>> courseGradeSum = new HashMap<>();
        for (GradeStatisticClass grade : gradeStatisticList) {
            if (courseGradeSum.containsKey(grade.course)) {
                List<Integer> sumAndCount = courseGradeSum.get(grade.course);
                sumAndCount.set(0, sumAndCount.get(0) + Integer.parseInt(grade.score));
                sumAndCount.set(1, sumAndCount.get(1) + 1);
            } else {
                List<Integer> sumAndCount = new ArrayList<>(Arrays.asList(Integer.parseInt(grade.score), 1));
                courseGradeSum.put(grade.course, sumAndCount);
            }
        }
        for (Map.Entry<String, List<Integer>> entry : courseGradeSum.entrySet()) {
            seriesBar.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue().get(0) / entry.getValue().get(1)));
        }

//        seriesBar.getData().add(new XYChart.Data<>("COMP" + i, 50));

        barChart.getData().add(seriesBar);
    }

    public void loadPieChart(List<GradeStatisticClass> gradeStatisticList)
    {
        pieChart.getData().clear();

        // Load the pie chart according to the scores of individual students
        HashMap <String, List<Integer>> studentGradeSum = new HashMap<>();
        for (GradeStatisticClass grade : gradeStatisticList) {
            if (studentGradeSum.containsKey(grade.student)) {
                List<Integer> sumAndCount = studentGradeSum.get(grade.student);
                sumAndCount.set(0, sumAndCount.get(0) + Integer.parseInt(grade.score));
                sumAndCount.set(1, sumAndCount.get(1) + 1);
            } else {
                List<Integer> sumAndCount = new ArrayList<>(Arrays.asList(Integer.parseInt(grade.score), 1));
                studentGradeSum.put(grade.student, sumAndCount);
            }
        }
        for (Map.Entry<String, List<Integer>> entry : studentGradeSum.entrySet()) {
            pieChart.getData().add(new PieChart.Data(entry.getKey(), (double) entry.getValue().get(0) / entry.getValue().get(1)));
        }

    }

    public void loadLineChart(List<GradeStatisticClass> gradeStatisticList)
    {
        XYChart.Series<String, Number> seriesLine = new XYChart.Series<>();
        seriesLine.getData().clear();
        lineChart.getData().clear();

        HashMap <String, List<Integer>> quizGradeSum = new HashMap<>();
        for (GradeStatisticClass grade : gradeStatisticList) {
            String quizName = grade.getCourse() + "-" + grade.getExam();
            if (quizGradeSum.containsKey(quizName)) {
                List<Integer> sumAndCount = quizGradeSum.get(quizName);
                sumAndCount.set(0, sumAndCount.get(0) + Integer.parseInt(grade.score));
                sumAndCount.set(1, sumAndCount.get(1) + 1);
            } else {
                List<Integer> sumAndCount = new ArrayList<>(Arrays.asList(Integer.parseInt(grade.score), 1));
                quizGradeSum.put(quizName, sumAndCount);
            }
        }
        for (Map.Entry<String, List<Integer>> entry : quizGradeSum.entrySet()) {
            seriesLine.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue().get(0) / entry.getValue().get(1)));
        }

//        for (int i = 0;  i < 6; i++) {
//            seriesLine.getData().add(new XYChart.Data<>("COMP3111" + "-" + "quiz" + i, 70));
//        }
        lineChart.getData().add(seriesLine);
        lineChart.setAnimated(false); // Ensure animation is disabled to avoid rendering issues

    }

    // reset filters
    @FXML
    public void reset() {
        // clear the combox boxes and load the data
        courseCombox.getItems().clear();
        examCombox.getItems().clear();
        studentCombox.getItems().clear();

        // populate the combox boxes
        populateChoiceBoxes();

        loadChart(gradeStatisticList);
        loadList(gradeStatisticList);
    }


    // apply filters
    @FXML
    public void query() {
        String course = courseCombox.getValue();
        String exam = examCombox.getValue();
        String student = studentCombox.getValue();
        List<GradeStatisticClass> filteredList = new ArrayList<>();
        for(GradeStatisticClass grade: gradeStatisticList)
        {
            if((course == null || grade.course.equals(course)) && (exam == null || grade.exam.equals(exam)) && (student == null || grade.student.equals(student)))
            {
                filteredList.add(grade);
            }
        }
        loadChart(filteredList);
        loadList(filteredList);
    }

    public void loadList(List<GradeStatisticClass> gradeStatisticList)
    {
        gradeListObservable.clear();
        gradeListObservable.addAll(gradeStatisticList);

        // make the grade statistic list
        gradeTable.setItems(gradeListObservable);

    }

    // Utility method to check if a similar object exists in the list
    public boolean isDuplicate(GradeStatisticClass newGrade, List<GradeStatisticClass> gradeStatisticList) {
        for (GradeStatisticClass existingGrade : gradeStatisticList) {
            if (existingGrade.getCourse().equals(newGrade.getCourse()) &&
                    existingGrade.getExam().equals(newGrade.getExam()) &&
                    existingGrade.getStudent().equals(newGrade.getStudent()) &&
                    existingGrade.getScore().equals(newGrade.getScore()) &&
                    existingGrade.getFullScore().equals(newGrade.getFullScore()) &&
                    existingGrade.getTimeSpend().equals(newGrade.getTimeSpend())) {
                return true; // Duplicate found
            }
        }
        return false; // No duplicate
    }
}
