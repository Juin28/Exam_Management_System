package comp3111.examsystem.controller;

import comp3111.examsystem.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing courses in the HKUST Examination System.
 * Provides functionality to add, delete, update, filter, and manage course-related data
 * in the system. Interacts with the database to retrieve, modify, and save data.
 *
 * The class also supports managing associated quizzes and grades for a course, ensuring
 * database consistency during operations like deletion.
 */
public class CourseManagementController {

    /**
     * Database instance for managing courses.
     */
    public Database<Course> courseDatabase;

    /**
     * Database instance for managing quizzes.
     */
    public Database<Quiz> quizDatabase;

    /**
     * Database instance for managing grades.
     */
    public Database<Grade> gradeDatabase;

    /**
     * List of all courses retrieved from the database.
     */
    public List<Course> allCourses;

    /**
     * List of all quizzes retrieved from the database.
     */
    public List<Quiz> allQuizzes;

    /**
     * List of all grades retrieved from the database.
     */
    public List<Grade> allGrades;

    /**
     * ObservableList for displaying courses in the TableView.
     */
    public ObservableList<Course> courseList;

    @FXML
    public AnchorPane AnchorWithInputs;

    @FXML
    private Button courseAdd;

    @FXML
    private Button courseDelete;

    @FXML
    public TableColumn<Course, String> courseDeptCol;

    @FXML
    public TextField courseDeptFilter;

    @FXML
    public TextField courseDeptInput;

    @FXML
    private Button courseFilter;

    @FXML
    public TableColumn<Course, String> courseIDCol;

    @FXML
    public TextField courseIDFilter;

    @FXML
    public TextField courseIDInput;

    @FXML
    public TableColumn<Course, String> courseNameCol;

    @FXML
    public TextField courseNameFilter;

    @FXML
    public TextField courseNameInput;

    @FXML
    private Button courseRefresh;

    @FXML
    private Button courseResetFilter;

    @FXML
    public TableView<Course> courseTable;

    @FXML
    private Button courseUpdate;

    @FXML
    public AnchorPane rootPane;

    /**
     * Initializes the Course Management Controller.
     * Sets up the database connections, loads the course data, configures the TableView,
     * and establishes event listeners for managing user interactions.
     */
    @FXML
    public void initialize()
    {
        // initialize the database
        this.courseDatabase = new Database<>(Course.class);
        allCourses = courseDatabase.getAll();
        this.gradeDatabase = new Database<>(Grade.class);
        allGrades = gradeDatabase.getAll();
        this.quizDatabase = new Database<>(Quiz.class);
        allQuizzes = quizDatabase.getAll();

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

    /**
     * Clear the input fields in the course management page
     */
    private void clearFields() {
        courseNameInput.clear();
        courseIDInput.clear();
        courseDeptInput.clear();
    }

    /**
     * Adds a course to the database.
     * Prompts the user for confirmation before performing the operation.
     *
     * @param event The event triggered by clicking the "Add" button.
     */
    @FXML
    public void addCourse(ActionEvent event)
    {
        boolean valid = validateAddInput();
        if (valid)
        {
            MsgSender.showConfirm("Add Course", "Are you sure you want to add this course?", () -> addCourseToDatabase());
        }
    }

    /**
     * Deletes a course from the database.
     * If the course has associated quizzes, those are also deleted after user confirmation.
     *
     * @param event The event triggered by clicking the "Delete" button.
     */
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

    /**
     * Filters the courses displayed in the TableView based on user input.
     *
     * @param event The event triggered by clicking the "Filter" button.
     */
    @FXML
    void filterCourse(ActionEvent event)
    {
        loadCourseTable();
    }


    /**
     * Validates the input fields for adding or updating a course.
     * Ensures all fields are filled and contain valid data.
     *
     * @return true if the input is valid, false otherwise.
     */
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

    /**
     * Refreshes the course table by clearing filters and reloading all courses.
     *
     * @param event The event triggered by clicking the "Refresh" button.
     */
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

    /**
     * Resets all filters in the course table and reloads all courses.
     *
     * @param event The event triggered by clicking the "Reset" button.
     */
    @FXML
    void resetFilter(ActionEvent event)
    {
        // clear the input fields for filter and reload the courseTable
        courseDeptFilter.clear();
        courseIDFilter.clear();
        courseNameFilter.clear();
        loadCourseTable();
    }

    /**
     * Updates an existing course in the database.
     * Prompts the user for confirmation if changes are detected.
     *
     * @param event The event triggered by clicking the "Update" button.
     * @return true if the course is updated successfully, false otherwise.
     */
    @FXML
    public boolean updateCourse(ActionEvent event)
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

        // create a copy of the selected course in case the user cancels the update
        Course oldCourse = new Course(selectedCourse.getCourseName(), selectedCourse.getCourseID(), selectedCourse.getDepartment(), selectedCourse.getId());

        boolean valid = validateUpdateInput(selectedCourse, changes);
        if (valid && !changes.isEmpty()) {
            // successfully validated the changes, show a confirmation message
            MsgSender.showUpdateConfirm("Update Course: " + selectedCourse.getCourseID(), changes, () -> updateCourseInDatabase(selectedCourse), () -> restoreCourseState(selectedCourse, oldCourse));
            return true;
        }
        // no changes detected
        if (valid) {
            MsgSender.showMsg("No changes detected");
            return false;
        }

        return false;

    }

    /**
     * Restores the course's state to the original values if the user cancels the update.
     *
     * @param current The course object to restore.
     * @param original The original course object to restore to.
     */
    private void restoreCourseState(Course current, Course original) {
        current.setCourseName(original.getCourseName());
        current.setCourseID(original.getCourseID());
        current.setDepartment(original.getDepartment());
    }

    /**
     * Update the course in the database
     *
     * @param course The course to update
     * @return true if the course is updated successfully, false otherwise
     */
    public boolean updateCourseInDatabase(Course course)
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

    /**
     * Validate the input for updating a course
     *
     * @param course The course to update
     * @param changes The list of changes to make
     * @return true if the input is valid, false otherwise
     */
    public boolean validateUpdateInput(Course course, List<String> changes)
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

    /**
     * Delete a course and its quizzes from the database
     *
     * @param course The course to delete
     * @param quizzesToDelete The quizzes to delete
     */
    public void deleteCourseAndQuizzes(Course course, List<Quiz> quizzesToDelete)
    {
        // delete the grades associated with the quizzes first

        for (Grade grade: allGrades)
        {
            for (Quiz quiz : quizzesToDelete)
            {
                if (Long.parseLong(grade.getQuestionId()) == quiz.getId())
                {
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


    /**
     * Delete a course from the database
     *
     * @param course The course to delete
     */
    public void deleteCourseFromDatabase(Course course)
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

    /**
     * Add a course to the database
     */
    public void addCourseToDatabase()
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


    /**
     * Validate the course ID.
     * Ensures the ID follows the format: 4 uppercase letters followed by 4 digits.
     * Additionally, checks for uniqueness among existing courses.
     *
     * @param id The course ID to validate
     * @return true if the course ID is valid, false otherwise
     */
    public boolean validateID(String id)
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
        allCourses = courseDatabase.getAll();
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

    /**
     * Validate the department name.
     * Ensures the department exists within a predefined list of valid departments.
     *
     * @param department The department to validate
     * @return true if the department is valid, false otherwise
     */
    public boolean validateDepartment(String department)
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

    /**
     * Load the course table with data from the database.
     * If filters are applied, the table is populated with filtered results.
     * Otherwise, all courses are loaded.
     *
     * @return a list of filtered courses if filters are applied. otherwise empty list
     */
    public List<Course> loadCourseTable()
    {
        List<Course> filteredCourses = new ArrayList<>();
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
        return filteredCourses;
    }

    /**
     * Check if no filters are currently applied to the course table.
     * This is used to determine whether to load all courses or apply filtering logic.
     *
     * @return true if no filters are applied, false otherwise
     */
    public boolean noFilter() {
        return (courseDeptFilter.getText().isEmpty() &&
                courseNameFilter.getText().isEmpty() &&
                courseIDFilter.getText().isEmpty());
    }


}
