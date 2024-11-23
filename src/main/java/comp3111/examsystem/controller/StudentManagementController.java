package comp3111.examsystem.controller;


import comp3111.examsystem.model.Grade;
import comp3111.examsystem.model.Student;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing students in the HKUST Examination System.
 * Provides functionality for adding, deleting, updating, filtering, and managing student data.
 * Includes integration with the database to ensure persistent storage and validation mechanisms.
 */
public class StudentManagementController {

    /**
     * Database instance for managing student records.
     */
    public Database<Student> studentDatabase;

    /**
     * List containing all student records retrieved from the database.
     */
    public List<Student> allStudents;

    /**
     * Observable list for displaying student records in the TableView.
     */
    public ObservableList<Student> studentList;

    /**
     * Database instance for managing grade records.
     */
    public Database<Grade> gradeDatabase;

    /**
     * List containing all grade records retrieved from the database.
     */
    public List<Grade> allGrades;

    @FXML
    public AnchorPane AnchorWithInputs;

    @FXML
    public Button studentAdd;

    @FXML
    public TableColumn<Student, String> studentAgeCol;

    @FXML
    public TextField studentAgeInput;

    @FXML
    public AnchorPane rootPane;

    @FXML
    public Button studentDelete;

    @FXML
    public TableColumn<Student, String> studentDeptCol;

    @FXML
    public TextField studentDeptFilter;

    @FXML
    public TextField studentDeptInput;

    @FXML
    public Button studentFilter;

    @FXML
    public TableColumn<Student, String> studentGenderCol;

    @FXML
    public ComboBox<String> studentGenderInput;

    @FXML
    public TableColumn<Student, String> studentNameCol;

    @FXML
    public TextField studentNameInput;

    @FXML
    public TableColumn<Student, String> studentPasswordCol;

    @FXML
    public TextField studentPasswordInput;

    @FXML
    public Button studentRefresh;

    @FXML
    public Button studentResetFilter;

    @FXML
    public TableView<Student> studentTable;

    @FXML
    public Button studentUpdate;

    @FXML
    public TableColumn<Student, String> studentUsernameCol;

    @FXML
    public TextField studentUsernameFilter;

    @FXML
    public TextField studentUsernameInput;

    @FXML
    public TextField studentNameFilter;

    /**
     * Initializes the Student Management Controller.
     * Sets up the database connections, loads student data, configures the TableView, and establishes event listeners.
     */
    @FXML
    public void initialize()
    {
        this.studentDatabase = new Database<>(Student.class);
        this.gradeDatabase = new Database<>(Grade.class);
        allStudents = studentDatabase.getAll();

        // initialize the choiceBoxes
        studentGenderInput.getItems().addAll("Male", "Female");

        // load the Student Table
        loadStudentTable();

        // Set up the columns in the table
        studentUsernameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        studentNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        studentAgeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAge()));
        studentGenderCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        studentDeptCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment()));
        studentPasswordCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));

        // Set up the listener for the table selection
        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                studentNameInput.setText(newSelection.getName());
                studentUsernameInput.setText(newSelection.getUsername());
                studentDeptInput.setText(newSelection.getDepartment());
                studentAgeInput.setText(newSelection.getAge());
                studentGenderInput.setValue(newSelection.getGender());
                studentPasswordInput.setText(newSelection.getPassword());
            }
        });

        // Clear the selection when clicking on the table itself with no student selected
        studentTable.setRowFactory(tv -> {
            TableRow<Student> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    studentTable.getSelectionModel().clearSelection();
                    clearFields();
                }
            });
            return row;
        });

        // Add a mouse click event to the rootPane
        rootPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            // Check if the click is outside the TableView
            if (!studentTable.isHover()) {
                if (!AnchorWithInputs.isHover())
                {clearFields();}
            }
        });

        // Populate the table
        studentTable.setItems(studentList);

    }

    /**
     * Adds a new student to the database after validation.
     * Displays a confirmation dialog before adding the student.
     *
     * @param event The event triggered by clicking the "Add" button.
     */
    @FXML
    void addStudent(ActionEvent event) {
        boolean valid = validateAddInput();
        if (valid)
        {
            MsgSender.showConfirm("Add Student", "Are you sure you want to add this student?", () -> addStudentToDatabase());
        }
    }

    /**
     * Deletes a student and their associated grades from the database.
     * Displays a confirmation dialog before performing the deletion.
     *
     * @param actionEvent The event triggered by clicking the "Delete" button.
     */
    @FXML
    public void deleteStudent(ActionEvent actionEvent) {
        // delete student according to selection model
        Student student = studentTable.getSelectionModel().getSelectedItem();
        if (student == null)
        {
            MsgSender.showMsg("Please select a student to delete");
            return;
        }
        String username = student.getUsername();
        MsgSender.showConfirm("Delete Student", "Are you sure you want to delete student with username: " + username + " and all of their grades? ", () -> deleteStudentAndGradesFromDatabase(student));
    }

    /**
     * Filters the students displayed in the TableView based on input criteria.
     *
     * @param actionEvent The event triggered by clicking the "Filter" button.
     */
    @FXML
    public void filterStudent(ActionEvent actionEvent) {
        // filtering functionality is implemented in the loadStudentTable function
        loadStudentTable();
    }

    /**
     * Refreshes the student table by clearing all filters and reloading the data.
     *
     * @param actionEvent The event triggered by clicking the "Refresh" button.
     */
    @FXML
    public void refreshStudent(ActionEvent actionEvent) {
        // this function will reset all the filters, reload the table and clear the input fields
        studentDeptFilter.clear();
        studentNameFilter.clear();
        studentUsernameFilter.clear();

        // clear input fields
        clearFields();

        // reload the teacher Table
        loadStudentTable();
    }

    /**
     * Resets all filters applied to the student table.
     *
     * @param actionEvent The event triggered by clicking the "Reset Filter" button.
     */
    @FXML
    public void resetFilter(ActionEvent actionEvent) {
        studentDeptFilter.clear();
        studentNameFilter.clear();
        studentUsernameFilter.clear();
        loadStudentTable();
    }

    /**
     * Updates an existing student's data in the database.
     * Displays a confirmation dialog for detected changes.
     *
     * @param actionEvent The event triggered by clicking the "Update" button.
     * @return true if the update is successful, false otherwise.
     */
    @FXML
    public boolean updateStudent(ActionEvent actionEvent) {
        Student student = studentTable.getSelectionModel().getSelectedItem();
        if (student == null)
        {
            MsgSender.showMsg("Please select a student to update");
            return false;
        }

        // create a copy of the student object to restore the state if the user cancels the update
        Student originalStudent = new Student(student.getUsername(), student.getName(), student.getGender(), student.getAge(), student.getDepartment(), student.getPassword(), student.getId());

        List<String> changes = new ArrayList<>();

        boolean valid = validateUpdateInput(student, changes);
        if (valid && !changes.isEmpty()) {
            // successfully validated the changes, show a confirmation message
            MsgSender.showUpdateConfirm("Update Student: " + student.getUsername(), changes, () -> updateStudentInDatabase(student), () -> restoreStudentState(student, originalStudent));
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
     * Restores the student's state to the original values if the user cancels the update.
     *
     * @param current The student object to restore.
     * @param original The original student object to restore to.
     */
    private void restoreStudentState(Student current, Student original) {
        current.setUsername(original.getUsername());
        current.setName(original.getName());
        current.setGender(original.getGender());
        current.setAge(original.getAge());
        current.setDepartment(original.getDepartment());
        current.setPassword(original.getPassword());
    }


    /**
     * Updates the selected student's information in the database.
     * Displays a message upon successful update or if an error occurs.
     *
     * @param student The student object with updated information.
     */
    public void updateStudentInDatabase(Student student)
    {
        try
        {
            studentDatabase.update(student);
            MsgSender.showMsg("Student updated successfully");
        }catch (Exception e) {
            MsgSender.showMsg("Error updating student");
            e.printStackTrace();
        }
    }


    /**
     * Deletes a selected student and their associated grades from the database.
     * Displays a message upon successful deletion or if an error occurs.
     *
     * @param student The student object to delete.
     */
    public void deleteStudentAndGradesFromDatabase(Student student)
    {
        // Delete the grades of the student from the database
        allGrades = gradeDatabase.getAll();
        List<Grade> gradesToDelete = new ArrayList<>();
        for (Grade grade : allGrades)
        {
            if (Long.parseLong(grade.getStudentId()) == student.getId())
            {
                gradesToDelete.add(grade);
            }
        }
        try
        {
            for (Grade grade : gradesToDelete)
            {
                gradeDatabase.delByKey(String.valueOf(grade.getID()));
            }
        }
        catch (Exception e)
        {
            MsgSender.showMsg("Error deleting grades");
            e.printStackTrace();
        }

        // Delete the student from the database
        try
        {
            String idString = String.valueOf(student.getId());
            studentDatabase.delByKey(idString);
            MsgSender.showMsg("Student deleted successfully");
        }catch (Exception e)
        {
            MsgSender.showMsg("Error deleting student");
            e.printStackTrace();
        }
    }


    /**
     * Validates the input fields for updating a student.
     * Ensures all required fields are filled, and changes are detected for the selected student.
     *
     * @param student The student object to validate against.
     * @param changes A list to record detected changes in student information.
     * @return true if the input is valid, false otherwise.
     */
    public boolean validateUpdateInput(Student student, List<String>changes)
    {
        // found the student, we now validate the rest of the inputs
        String username = studentUsernameInput.getText();
        String name = studentNameInput.getText();
        String age = studentAgeInput.getText();
        String department = studentDeptInput.getText();
        String password = studentPasswordInput.getText();
        String gender = studentGenderInput.getValue();

        // check that all fields are filled
        if (username.isEmpty() || name.isEmpty() || age.isEmpty() || department.isEmpty() || password.isEmpty() || gender.isEmpty())
        {
            MsgSender.showMsg("Please fill in all fields");
            return false;
        }

        // check that the username is the same
        if (!username.equals(student.getUsername()))
        {
            MsgSender.showMsg("Username must be the same!");
            return false;
        }

        // check the fields that are different, show a confirmation message to verify that
        // if name is not in alphabets, return false
        if (!name.matches("^[a-zA-Z]*$"))
        {
            MsgSender.showMsg("Name must only contain alphabets");
            return false;
        }

        // add name into "changes" array if the name is different
        if (!name.equals(student.getName()))
        {
            changes.add("Name: " + student.getName() + " -> " + name);
        }

        // validate the age
        if (!validateAge(age))
        {
            return false;
        }

        // age is different, add into "changes" array
        if (!age.equals(student.getAge()))
        {
            changes.add("Age: " + student.getAge() + " -> " + age);
        }

        department = department.toUpperCase();
        // validate the department
        if (!validateDepartment(department))
        {
            MsgSender.showMsg("Please input a valid department");
            return false;
        }

        // department is not the same, add into "changes" array
        if (!department.equals(student.getDepartment()))
        {
            changes.add("Department: " + student.getDepartment() + " -> " + department);
        }

        // password is not the same, add into "changes" array
        if (!password.equals(student.getPassword()))
        {
            changes.add("Password: " + student.getPassword() + " -> " + password);
        }

        // gender is not the same, add into "changes" array
        if (!gender.equals(student.getGender()))
        {
            changes.add("Gender: " + student.getGender() + " -> " + gender);
        }

        // successfully validated input, set the corresponding values and return true
        student.setName(name);
        student.setAge(age);
        student.setDepartment(department);
        student.setPassword(password);
        student.setGender(gender);


        return true;
    }

    /**
     * Validates the input fields for adding a new student.
     * Ensures that all fields are filled and contain valid data.
     *
     * @return true if the input is valid, false otherwise.
     */
    public boolean validateAddInput() {
        // check to ensure that all the fields are filled
        String username = studentUsernameInput.getText();
        String name = studentNameInput.getText();
        String age = studentAgeInput.getText();
        String department = studentDeptInput.getText();
        String password = studentPasswordInput.getText();

        if (username.isEmpty() || name.isEmpty() || age.isEmpty() || department.isEmpty() || password.isEmpty()
                || studentGenderInput.getValue() == null) {
            MsgSender.showMsg("Please fill in all fields");
            return false;
        }

        // validate the username
        if (!validateUsername(username)) {
            return false;
        }
        // validate that name only has alphabets
        if (!name.matches("^[a-zA-Z]*$")) {
            MsgSender.showMsg("Name must only contain alphabets");
            return false;
        }

        // validate the age
        if (!validateAge(age)) {
            return false;
        }

        // validate the department
        department = department.toUpperCase();
        if (!validateDepartment(department)) {
            return false;
        }

        // if all previous conditions are met, we have successfully validated the input, return true
        return true;
    }

    /**
     * Validates the department input against a predefined list of valid departments.
     *
     * @param department The department input to validate.
     * @return true if the department is valid, false otherwise.
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
     * Validates the username input for uniqueness and format.
     * Ensures the username is alphanumeric and does not already exist.
     *
     * @param username The username input to validate.
     * @return true if the username is valid, false otherwise.
     */
    public boolean validateUsername(String username)
    {
        // check to ensure that the username is unique
        for (Student student : allStudents)
        {
            if (student.getUsername().equals(username))
            {
                MsgSender.showMsg("Username already exists");
                return false;
            }
        }

        // check to ensure that the username is alphanumeric
        if (!username.matches("^[a-zA-Z0-9]*$"))
        {
            MsgSender.showMsg("Username must be alphanumeric");
            return false;
        }
        return true;
    }

    /**
     * Validates the age input for numeric format and range.
     *
     * @param age The age input to validate.
     * @return true if the age is valid, false otherwise.
     */
    public boolean validateAge(String age)
    {
        // check if age is a number
        int ageNum;
        try
        {
            ageNum = Integer.parseInt(age);
        }catch (NumberFormatException e)
        {
            MsgSender.showMsg("Age must be a number");
            return false;
        }

        // check that age is within a valid range
        if (ageNum < 20 || ageNum > 100)
        {
            MsgSender.showMsg("Age must be between 20 and 100");
            return false;
        }

        return true;
    }

    /**
     * Adds a new student to the database using the input fields.
     * Displays a message upon successful addition or if an error occurs.
     */
    public void addStudentToDatabase()
    {
        String username = studentUsernameInput.getText();
        String name = studentNameInput.getText();
        String age = studentAgeInput.getText();
        String department = studentDeptInput.getText();
        String password = studentPasswordInput.getText();

        Student newStudent = new Student(username, name, studentGenderInput.getValue(), age, department.toUpperCase(), password, 0);
        try
        {
            studentDatabase.add(newStudent);
            MsgSender.showMsg("Student added successfully");
        }catch (Exception e)
        {
            MsgSender.showMsg("Error adding student");
            e.printStackTrace();
        }
    }

    /**
     * Clears all input fields in the student management form.
     */
    public void clearFields()
    {
        studentNameInput.clear();
        studentUsernameInput.clear();
        studentDeptInput.clear();
        studentAgeInput.clear();
        studentGenderInput.setValue(null);
        studentPasswordInput.clear();
    }

    /**
     * Checks if no filters are currently applied to the student table.
     *
     * @return true if no filters are applied, false otherwise.
     */
    public boolean noFilter() {
        return (studentDeptFilter.getText().isEmpty() &&
                studentNameFilter.getText().isEmpty() &&
                studentUsernameFilter.getText().isEmpty());
    }

    /**
     * Loads student data into the TableView.
     * If filters are applied, displays filtered results; otherwise, displays all students.
     *
     * @return A list of filtered students if filters are applied, or an empty list otherwise
     */
    @FXML
    public List<Student> loadStudentTable()
    {
        List<Student> filteredStudents = new ArrayList<>();
        if (noFilter())
        {
            allStudents= studentDatabase.getAll();
            studentList = FXCollections.observableArrayList(allStudents);

        }
        else {
            String usernameFilter = studentUsernameFilter.getText();
            String nameFilter = studentNameFilter.getText();
            String departmentFilter = studentDeptFilter.getText().toUpperCase();

            List<Student> allStudents = studentDatabase.getAll();

            for (Student student : allStudents) {
                if ((usernameFilter.isEmpty() || usernameFilter.equals(student.getUsername())) &&
                        (nameFilter.isEmpty() || nameFilter.equals(student.getName())) &&
                        (departmentFilter.isEmpty() || departmentFilter.equals(student.getDepartment()))) {
                    filteredStudents.add(student);
                }
            }

            studentList = FXCollections.observableArrayList(filteredStudents);
        }
        studentTable.getItems().setAll(studentList);
        return filteredStudents;
    }


}
