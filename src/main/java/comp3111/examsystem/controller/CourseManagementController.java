package comp3111.examsystem.controller;

import comp3111.examsystem.model.Grade;
import comp3111.examsystem.model.Quiz;
import comp3111.examsystem.model.Teacher;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import comp3111.examsystem.model.Course;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;

import java.util.ArrayList;
import java.util.List;

public class CourseManagementController {
    private Database<Course> courseDatabase;
    private Database<Quiz> quizDatabase;
    private Database<Grade> gradeDatabase;
    private List<Course> allCourses;
    private List<Quiz> allQuizzes;
    private List<Grade> allGrades;
    private ObservableList<Course> courseList;

    @FXML
    private AnchorPane AnchorWithInputs;

    @FXML
    private Button courseAdd;

    @FXML
    private Button courseDelete;

    @FXML
    private TableColumn<Course, String> courseDeptCol;

    @FXML
    private TextField courseDeptFilter;

    @FXML
    private TextField courseDeptInput;

    @FXML
    private Button courseFilter;

    @FXML
    private TableColumn<Course, String> courseIDCol;

    @FXML
    private TextField courseIDFilter;

    @FXML
    private TextField courseIDInput;

    @FXML
    private TableColumn<Course, String> courseNameCol;

    @FXML
    private TextField courseNameFilter;

    @FXML
    private TextField courseNameInput;

    @FXML
    private Button courseRefresh;

    @FXML
    private Button courseResetFilter;

    @FXML
    private TableView<Course> courseTable;

    @FXML
    private Button courseUpdate;

    @FXML
    private AnchorPane rootPane;

    @FXML
    public void initialize()
    {
        // initialize the database
        this.courseDatabase = new Database<>(Course.class);
        allCourses = courseDatabase.getAll();

        // load the course table
        loadCourseTable();

        // set up the columns
        courseNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseName()));
        courseIDCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseID()));
        courseDeptCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment()));

        // set up the listener
        // Set up the listener for the table selection
        courseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                courseNameInput.setText(newSelection.getCourseName());
                courseIDInput.setText(newSelection.getCourseID());
                courseDeptInput.setText(newSelection.getDepartment());
            }
        });

        // Clear the selection when clicking on the table itself with no course selected
        courseTable.setRowFactory(tv -> {
            TableRow<Course> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    courseTable.getSelectionModel().clearSelection();
                    clearFields();
                }
            });
            return row;
        });

        // Add a mouse click event to the rootPane
        rootPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            // Check if the click is outside the TableView
            if (!courseTable.isHover()) {
                if (!AnchorWithInputs.isHover())
                {clearFields();}
            }
        });

        // Populate the table
        courseTable.setItems(courseList);

    }

    private void clearFields() {
        courseNameInput.clear();
        courseIDInput.clear();
        courseDeptInput.clear();
    }

    @FXML
    public void addCourse(ActionEvent event)
    {
        boolean valid = validateAddInput();
        if (valid)
        {
            MsgSender.showConfirm("Add Course", "Are you sure you want to add this course?", () -> addCourseToDatabase());
        }
    }

    @FXML
    public void deleteCourse(ActionEvent event)
    {
        // after clicking on a course, the manager can press "delete" to delete a course
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null)
        {
            MsgSender.showMsg("Please select a course to delete");
            return;
        }

        // check if there are quizzes associated with the course
        this.quizDatabase = new Database<>(Quiz.class);
        allQuizzes = quizDatabase.getAll();

        List<Quiz> quizzesToDelete = new ArrayList<>();
        for (Quiz quiz : allQuizzes)
        {
            if (quiz.getCourseID().equals(selectedCourse.getCourseID()))
            {
                quizzesToDelete.add(quiz);
            }
        }

        // confirm the deletion
        if(!quizzesToDelete.isEmpty())
        {
            MsgSender.showDeleteConfirm(selectedCourse, "Delete Course", quizzesToDelete, () -> deleteCourseAndQuizzes(selectedCourse, quizzesToDelete));
        }
        else
        {
            MsgSender.showConfirm("Delete Course", "Are you sure you want to delete course with Course ID: "+ selectedCourse.getCourseID(), () -> deleteCourseFromDatabase(selectedCourse));
        }
    }

    @FXML
    void filterCourse(ActionEvent event)
    {
        loadCourseTable();
    }

    private boolean validateAddInput()
    {
        // check to ensure that all the fields are filled
        String name = courseNameInput.getText();
        String department = courseDeptInput.getText();
        String id = courseIDInput.getText();

        if (name.isEmpty() || department.isEmpty() || id.isEmpty())
        {
            MsgSender.showMsg("Please fill in all fields");
            return false;
        }

        // validate the courseID
        if(!validateID(id)){return false;}

        // validate the department
        department = department.toUpperCase();
        if(!validateDepartment(department)){return false;}

        // if all previous conditions are met, we have successfully validated the input, return true
        return true;
    }

    @FXML
    void refreshCourse(ActionEvent event)
    {
        // clear all input fields
        clearFields();

        // clear the filters and reload the table
        courseDeptFilter.clear();
        courseIDFilter.clear();
        courseNameFilter.clear();
        loadCourseTable();
    }

    @FXML
    void resetFilter(ActionEvent event)
    {
        // clear the input fields for filter and reload the courseTable
        courseDeptFilter.clear();
        courseIDFilter.clear();
        courseNameFilter.clear();
        loadCourseTable();
    }

    @FXML
    private boolean updateCourse(ActionEvent event)
    {
        // Check if a course is selected
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null)
        {
            MsgSender.showMsg("Please select a course to update");
            return false;
        }

        // Check if the input is valid
        List<String> changes = new ArrayList<>();

        boolean valid = validateUpdateInput(selectedCourse, changes);
        if (valid && !changes.isEmpty()) {
            // successfully validated the changes, show a confirmation message
            MsgSender.showUpdateConfirm("Update Course: " + selectedCourse.getCourseID(), changes, () -> updateCourseInDatabase(selectedCourse));
            return true;
        }
        // no changes detected
        if (valid) {
            MsgSender.showMsg("No changes detected");
            return false;
        }

        return false;

    }

    private boolean updateCourseInDatabase(Course course)
    {
        try
        {
            courseDatabase.update(course);
            MsgSender.showMsg("Course updated successfully");
            return true;
        }catch (Exception e)
        {
            MsgSender.showMsg("Error updating course");
            e.printStackTrace();
            return false;
        }
    }

    private boolean validateUpdateInput(Course course, List<String> changes)
    {
        // check to ensure that all the fields are filled
        String name = courseNameInput.getText();
        String department = courseDeptInput.getText();
        String id = courseIDInput.getText();

        if (name.isEmpty() || department.isEmpty() || id.isEmpty())
        {
            MsgSender.showMsg("Please fill in all the fields");
            return false;
        }

        // ensure that courseID is not changed
        if (!id.equals(course.getCourseID()))
        {
            MsgSender.showMsg("Course ID cannot be changed");
            return false;
        }

        // validate the department
        department = department.toUpperCase();
        if(!validateDepartment(department))
        {
            MsgSender.showMsg("Please enter a valid department");
            return false;
        }
        if (!department.equals(course.getDepartment()))
        {
            changes.add("Department: " + course.getDepartment() + " -> " + department);
        }

        if(!name.equals(course.getCourseName()))
        {
            changes.add("Course Name: " + course.getCourseName() + " -> " + name);
        }

        // set the course object to the validated input values
        course.setCourseName(name);
        course.setDepartment(department);
        course.setCourseID(id);

        // if all previous conditions are met, we have successfully validated the input, return true
        return true;
    }

    private void deleteCourseAndQuizzes(Course course, List<Quiz> quizzesToDelete)
    {
        // delete the grades associated with the quizzes first
        this.gradeDatabase = new Database<>(Grade.class);
        allGrades = gradeDatabase.getAll();
        for (Grade grade: allGrades)
        {
            for (Quiz quiz : quizzesToDelete)
            {
                if (Long.parseLong(grade.getQuestionId()) == quiz.getId())
                {
                    // TODO: implement grade deletion
                    try
                    {
                        String idString = String.valueOf(grade.getID());
                        gradeDatabase.delByKey(idString);
                    }catch (Exception e)
                    {
                        MsgSender.showMsg("Error deleting grades");
                        e.printStackTrace();
                    }
                }
            }
        }


        // next, try to delete the quizzes
        for (Quiz quiz : quizzesToDelete)
        {
            try
            {
                String idString = String.valueOf(quiz.getId());
                quizDatabase.delByKey(idString);
            }catch (Exception e)
            {
                MsgSender.showMsg("Error deleting quizzes");
                e.printStackTrace();
            }
        }
        // then delete the course
        deleteCourseFromDatabase(course);

    }

    private void deleteCourseFromDatabase(Course course)
    {
        try
        {
            String idString = String.valueOf(course.getId());
            courseDatabase.delByKey(idString);
            MsgSender.showMsg("Course deleted successfully");
        }catch (Exception e)
        {
            MsgSender.showMsg("Error deleting course");
            e.printStackTrace();
        }
    }

    private void addCourseToDatabase()
    {
        String name = courseNameInput.getText();
        String department = courseDeptInput.getText().toUpperCase();
        String id = courseIDInput.getText();

        Course newCourse = new Course(name, id, department, 0);
        try
        {
            courseDatabase.add(newCourse);
            MsgSender.showMsg("Course added successfully");}
        catch (Exception e)
        {
            MsgSender.showMsg("An error occurred while adding the course");
            e.printStackTrace();
        }
    }

    private boolean validateID(String id)
    {
        // check that the name is 4 chars + 4 numbers, with the 4 chars as uppercase
        if (id.length() != 8)
        {
            MsgSender.showMsg("Course name must be 8 characters long");
            return false;
        }
        String chars = id.substring(0, 4);
        String nums = id.substring(4, 8);
        for (int i = 0; i < 4; i++)
        {
            if (!Character.isLetter(chars.charAt(i)) || !Character.isUpperCase(chars.charAt(i)))
            {
                MsgSender.showMsg("First 4 characters of Course ID must be uppercase letters");
                return false;
            }
            if (!Character.isDigit(nums.charAt(i)))
            {
                MsgSender.showMsg("Last 4 characters of Course ID must be numbers");
                return false;
            }
        }

        // check that the ID does not already exist
        for (Course course : allCourses)
        {
            if (course.getCourseID().equals(id))
            {
                MsgSender.showMsg("Course ID already exists");
                return false;
            }
        }
        return true;

    }
    private boolean validateDepartment(String department)
    {
        // check to ensure that the department is within HKUST's list of departments
        String[] validDepartments = {"IEDA","CSE", "ECE", "MAE", "BBA", "CBE", "CIVL", "PHYS", "MATH", "HUMA", "LANG", "ACCT", "OCES", "ISOM", "FINA", "MARK", "GBUS", "LIFS", "BIEN", "CHEM", "ENVR", "HUMA", "SOSC", "SHSS", "SUST", "ISD"};

        for (String validDepartment : validDepartments)
        {
            if (department.equals(validDepartment))
            {
                return true;
            }
        }
        return false;
    }

    private void loadCourseTable()
    {
        if (noFilter())
        {
            allCourses = courseDatabase.getAll();
            courseList = FXCollections.observableArrayList(allCourses);

        }
        else {
            String idFilter = courseIDFilter.getText();
            String nameFilter = courseNameFilter.getText();
            String departmentFilter = courseDeptFilter.getText().toUpperCase();

            List<Course> allCourses = courseDatabase.getAll();
            List<Course> filteredCourses = new ArrayList<>();

            for (Course course : allCourses) {
                if ((nameFilter.isEmpty() || nameFilter.equals(course.getCourseName())) &&
                        (idFilter.isEmpty() || idFilter.equals(course.getCourseID())) &&
                        (departmentFilter.isEmpty() || departmentFilter.equals(course.getDepartment())))
                {
                    filteredCourses.add(course);
                }
            }

            courseList = FXCollections.observableArrayList(filteredCourses);
        }
        courseTable.getItems().setAll(courseList);
    }

    private boolean noFilter() {
        return (courseDeptFilter.getText().isEmpty() &&
                courseNameFilter.getText().isEmpty() &&
                courseIDFilter.getText().isEmpty());
    }


}
