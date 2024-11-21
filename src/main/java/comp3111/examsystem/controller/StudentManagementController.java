package comp3111.examsystem.controller;


import comp3111.examsystem.model.Grade;
import comp3111.examsystem.model.Student;
import comp3111.examsystem.model.Teacher;
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

public class StudentManagementController {

    public Database<Student> studentDatabase;
    public List<Student> allStudents;
    public ObservableList<Student> studentList;
    public Database<Grade> gradeDatabase;
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

    @FXML
    void addStudent(ActionEvent event) {
        boolean valid = validateAddInput();
        if (valid)
        {
            MsgSender.showConfirm("Add Student", "Are you sure you want to add this student?", () -> addStudentToDatabase());
        }
    }

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

    @FXML
    public void filterStudent(ActionEvent actionEvent) {
        // filtering functionality is implemented in the loadStudentTable function
        loadStudentTable();
    }

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

    @FXML
    public void resetFilter(ActionEvent actionEvent) {
        studentDeptFilter.clear();
        studentNameFilter.clear();
        studentUsernameFilter.clear();
        loadStudentTable();
    }

    @FXML
    public boolean updateStudent(ActionEvent actionEvent) {
        Student student = studentTable.getSelectionModel().getSelectedItem();
        if (student == null)
        {
            MsgSender.showMsg("Please select a student to update");
            return false;
        }
        List<String> changes = new ArrayList<>();

        boolean valid = validateUpdateInput(student, changes);
        if (valid && !changes.isEmpty()) {
            // successfully validated the changes, show a confirmation message
            MsgSender.showUpdateConfirm("Update Student: " + student.getUsername(), changes, () -> updateStudentInDatabase(student));
            return true;
        }
        // no changes detected
        if (valid) {
            MsgSender.showMsg("No changes detected");
            return false;
        }

        return false;
    }

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
     * This function validates the input fields for updating a student
     *
     * @return Student     returns the updated Student object
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
     * This function validates the department input
     *
     * @param   department  The department input as a String
     * @return  boolean     True if the department is valid, false otherwise
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
     * This function validates the username input
     *
     * @param   username    The username input as a String
     * @return  boolean     True if the username is valid, false otherwise
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
     * This function validates the age input
     *
     * @param   age        The age input as a String
     * @return  boolean    True if the age is valid, false otherwise
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
     * This function adds a student to the database
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

    public void clearFields()
    {
        studentNameInput.clear();
        studentUsernameInput.clear();
        studentDeptInput.clear();
        studentAgeInput.clear();
        studentGenderInput.setValue(null);
        studentPasswordInput.clear();
    }

    public boolean noFilter() {
        return (studentDeptFilter.getText().isEmpty() &&
                studentNameFilter.getText().isEmpty() &&
                studentUsernameFilter.getText().isEmpty());
    }


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
