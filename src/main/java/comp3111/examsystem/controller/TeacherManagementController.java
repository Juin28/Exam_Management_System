package comp3111.examsystem.controller;

import comp3111.examsystem.model.Teacher;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;


import java.util.ArrayList;
import java.util.List;

public class TeacherManagementController {

    @FXML
    public void initialize()
    {
        this.teacherDatabase = new Database<>(Teacher.class);
        allTeachers = teacherDatabase.getAll();

        // initialize the choiceBoxes
        teachGenderInput.getItems().addAll("Male", "Female");
        teachPosInput.getItems().addAll("Head", "Associate Head", "Chair Professor", "Professor", "Associate Professor","Assistant Professor",
        "Senior Lecturer", "Lecturer");

        // load the Teacher Table
        loadTeacherTable();

        // Set up the columns in the table
        teachUsernameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        teachNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        teachAgeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAge()));
        teachGenderCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        teachDeptCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment()));
        teachPasswordCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
        teachPositionCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosition()));

        // Set up the listener for the table selection
        teachTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                teachNameInput.setText(newSelection.getName());
                teachUsernameInput.setText(newSelection.getUsername());
                teachDeptInput.setText(newSelection.getDepartment());
                teachAgeInput.setText(newSelection.getAge());
                teachPosInput.setValue(newSelection.getPosition());
                teachGenderInput.setValue(newSelection.getGender());
                teachPasswordInput.setText(newSelection.getPassword());
            }
        });

        // Clear the selection when clicking on the table itself with no teacher selected
        teachTable.setRowFactory(tv -> {
            TableRow<Teacher> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    teachTable.getSelectionModel().clearSelection();
                    clearFields();
                }
            });
            return row;
        });

        // Add a mouse click event to the rootPane
        rootPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            // Check if the click is outside the TableView
            if (!teachTable.isHover()) {
                if (!AnchorWithInputs.isHover())
                {clearFields();}
            }
        });

        // Populate the table
        teachTable.setItems(teacherList);
    }

    @FXML
    public List<Teacher> loadTeacherTable()
    {
        List<Teacher> filteredTeachers = new ArrayList<>();
        if (noFilter())
        {
            allTeachers = teacherDatabase.getAll();
            teacherList = FXCollections.observableArrayList(allTeachers);
        }
        else {
            String usernameFilter = teachUsernameFilter.getText();
            String nameFilter = teachNameFilter.getText();
            String departmentFilter = teachDeptFilter.getText().toUpperCase();

            List<Teacher> allTeachers = teacherDatabase.getAll();

            for (Teacher teacher : allTeachers) {
                if ((usernameFilter.isEmpty() || usernameFilter.equals(teacher.getUsername())) &&
                        (nameFilter.isEmpty() || nameFilter.equals(teacher.getName())) &&
                        (departmentFilter.isEmpty() || departmentFilter.equals(teacher.getDepartment()))) {
                    filteredTeachers.add(teacher);
                }
            }

            teacherList = FXCollections.observableArrayList(filteredTeachers);
        }
        teachTable.getItems().setAll(teacherList);
        return filteredTeachers;
    }

    public boolean noFilter() {
        return (teachDeptFilter.getText().isEmpty() &&
                teachNameFilter.getText().isEmpty() &&
                teachUsernameFilter.getText().isEmpty());
    }

    @FXML
    public boolean updateTeacher(ActionEvent actionEvent) {
        Teacher teacher = teachTable.getSelectionModel().getSelectedItem();
        if (teacher == null)
        {
            MsgSender.showMsg("Please select a teacher to update");
            return false;
        }
        List<String> changes = new ArrayList<>();

        boolean valid = validateUpdateInput(teacher, changes);
        if (valid && !changes.isEmpty()) {
            // successfully validated the changes, show a confirmation message
            MsgSender.showUpdateConfirm("Update Teacher: " + teacher.getUsername(), changes, () -> updateTeacherInDatabase(teacher));
            return true;
        }
        // no changes detected
        if (valid) {
            MsgSender.showMsg("No changes detected");
            return false;
        }

        return false;
    }

    @FXML
    public void resetFilter(ActionEvent actionEvent) {
        teachDeptFilter.clear();
        teachNameFilter.clear();
        teachUsernameFilter.clear();
        loadTeacherTable();
    }

    @FXML
    public void filterTeacher(ActionEvent actionEvent) {
        // filtering functionality is implemented in the loadTeacherTable function
        loadTeacherTable();
    }

    @FXML
    public boolean deleteTeacher(ActionEvent actionEvent) {
        // delete teacher according to selection model
        Teacher teacher = teachTable.getSelectionModel().getSelectedItem();
        if (teacher == null)
        {
            MsgSender.showMsg("Please select a teacher to delete");
            return false;
        }
        String username = teacher.getUsername();
        MsgSender.showConfirm("Delete Teacher", "Are you sure you want to delete teacher with username: " + username + " ?", () -> deleteTeacherFromDatabase(teacher));
        return true;
    }

    public void deleteTeacherFromDatabase(Teacher teacher)
    {
        try
        {
            String idString = String.valueOf(teacher.getId());
            teacherDatabase.delByKey(idString);
            MsgSender.showMsg("Teacher deleted successfully");
        }catch (Exception e)
        {
            MsgSender.showMsg("Error deleting teacher");
            e.printStackTrace();
        }
    }

    @FXML
    public void clearFields() {
        teachUsernameInput.clear();
        teachNameInput.clear();
        teachAgeInput.clear();
        teachDeptInput.clear();
        teachPasswordInput.clear();
        teachGenderInput.setValue(null);
        teachPosInput.setValue(null);
    }

    @FXML
    public void refreshTeacher(ActionEvent actionEvent) {
        // this function will reset all the filters, reload the table and clear the input fields
        teachDeptFilter.clear();
        teachNameFilter.clear();
        teachUsernameFilter.clear();

        // clear input fields
        clearFields();

        // reload the teacher Table
        loadTeacherTable();
    }

    @FXML
    public void addTeacher(ActionEvent actionEvent) {
        boolean valid = validateAddInput();
        if (valid)
        {
            MsgSender.showConfirm("Add Teacher", "Are you sure you want to add this teacher?", () -> addTeacherToDatabase());
        }
    }

    /**
     * This function validates the input fields for updating a teacher
     *
     * @return Teacher     returns the updated Teacher object
     */
    public boolean validateUpdateInput(Teacher teacher, List<String>changes)
    {
        // found the teacher, we now validate the rest of the inputs
        String username = teachUsernameInput.getText();
        String name = teachNameInput.getText();
        String age = teachAgeInput.getText();
        String department = teachDeptInput.getText();
        String password = teachPasswordInput.getText();
        String gender = teachGenderInput.getValue();
        String position = teachPosInput.getValue();

        // check that all fields are filled
        if (username.isEmpty() || name.isEmpty() || age.isEmpty() || department.isEmpty() || password.isEmpty() || gender.isEmpty() || position.isEmpty())
        {
            MsgSender.showMsg("Please fill in all fields");
            return false;
        }

        // check that the username is the same
        if (!username.equals(teacher.getUsername()))
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
        if (!name.equals(teacher.getName()))
        {
            changes.add("Name: " + teacher.getName() + " -> " + name);
        }

        // validate the age
        if (!validateAge(age))
        {
            return false;
        }

        // age is different, add into "changes" array
        if (!age.equals(teacher.getAge()))
        {
            changes.add("Age: " + teacher.getAge() + " -> " + age);
        }

        department = department.toUpperCase();
        // validate the department
        if (!validateDepartment(department))
        {
            MsgSender.showMsg("Please input a valid department");
            return false;
        }

        // department is not the same, add into "changes" array
        if (!department.equals(teacher.getDepartment()))
        {
            changes.add("Department: " + teacher.getDepartment() + " -> " + department);
        }

        // password is not the same, add into "changes" array
        if (!password.equals(teacher.getPassword()))
        {
            changes.add("Password: " + teacher.getPassword() + " -> " + password);
        }

        // gender is not the same, add into "changes" array
        if (!gender.equals(teacher.getGender()))
        {
            changes.add("Gender: " + teacher.getGender() + " -> " + gender);
        }

        // position is not the same, add into "changes" array
        if (!position.equals(teacher.getPosition()))
        {
            changes.add("Position: " + teacher.getPosition() + " -> " + position);
        }

        // successfully validated input, set the corresponding values and return true
        teacher.setName(name);
        teacher.setAge(age);
        teacher.setDepartment(department);
        teacher.setPassword(password);
        teacher.setGender(gender);
        teacher.setPosition(teachPosInput.getValue());

        return true;
    }

    /**
     * This function validates the input fields while adding a teacher
     *
     * @return boolean     True if the input is valid, false otherwise
     */
    public boolean validateAddInput()
    {
        // check to ensure that all the fields are filled
        String username = teachUsernameInput.getText();
        String name = teachNameInput.getText();
        String age = teachAgeInput.getText();
        String department = teachDeptInput.getText();
        String password = teachPasswordInput.getText();

        if (username.isEmpty() || name.isEmpty() || age.isEmpty() || department.isEmpty() || password.isEmpty()
        || teachGenderInput.getValue() == null || teachPosInput.getValue() == null)
        {
            MsgSender.showMsg("Please fill in all fields");
            return false;
        }

        // validate the username
        if(!validateUsername(username)){return false;}

        // validate that name only has alphabets
        if (!name.matches("^[a-zA-Z]*$"))
        {
            MsgSender.showMsg("Name must only contain alphabets");
            return false;
        }

        // validate the age
        if(!validateAge(age)){return false;}

        // validate the department
        department = department.toUpperCase();
        if(!validateDepartment(department)){return false;}

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
        for (Teacher teacher : allTeachers)
        {
            if (teacher.getUsername().equals(username))
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

    public void updateTeacherInDatabase(Teacher teacher)
    {
        try
        {
            teacherDatabase.update(teacher);
            MsgSender.showMsg("Teacher updated successfully");
        }catch (Exception e) {
            MsgSender.showMsg("Error updating teacher");
            e.printStackTrace();
        }
    }

    /**
     * This function adds a teacher to the database
     */
    public void addTeacherToDatabase()
    {
        String username = teachUsernameInput.getText();
        String name = teachNameInput.getText();
        String age = teachAgeInput.getText();
        String department = teachDeptInput.getText();
        String password = teachPasswordInput.getText();

        Teacher newTeacher = new Teacher(username, name, teachGenderInput.getValue(), age, department.toUpperCase(), password, teachPosInput.getValue(), 0);
        try
        {
            teacherDatabase.add(newTeacher);
            MsgSender.showMsg("Teacher added successfully");
        }catch (Exception e)
        {
            MsgSender.showMsg("Error adding teacher");
            e.printStackTrace();
        }
    }

    public Database<Teacher> teacherDatabase;
    public List<Teacher> allTeachers;
    public ObservableList<Teacher> teacherList;

    @FXML
    public Button teachAdd;

    @FXML
    public TableColumn<Teacher, String> teachAgeCol;

    @FXML
    public TextField teachAgeInput;

    @FXML
    public Button teachDelete;

    @FXML
    public TableColumn<Teacher, String> teachDeptCol;

    @FXML
    public TableView<Teacher> teachTable;

    @FXML
    public TextField teachDeptFilter;

    @FXML
    public TextField teachDeptInput;

    @FXML
    public Button teachFilter;

    @FXML
    public TableColumn<Teacher, String> teachGenderCol;

    @FXML
    public ComboBox<String> teachGenderInput;

    @FXML
    public TableColumn<Teacher, String> teachNameCol;

    @FXML
    public TextField teachNameFilter;

    @FXML
    public TextField teachNameInput;

    @FXML
    public TableColumn<Teacher, String> teachPasswordCol;

    @FXML
    public TextField teachPasswordInput;

    @FXML
    public ComboBox<String> teachPosInput;

    @FXML
    public Button teachRefresh;

    @FXML
    public Button teachResetFilter;

    @FXML
    public Button teachUpdate;

    @FXML
    public TableColumn<Teacher, String> teachUsernameCol;

    @FXML
    public TextField teachUsernameFilter;

    @FXML
    public TextField teachUsernameInput;

    @FXML
    public TableColumn<Teacher, String> teachPositionCol;

    @FXML
    public AnchorPane rootPane;

    @FXML
    public AnchorPane AnchorWithInputs;

}
