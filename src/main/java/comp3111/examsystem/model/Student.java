package comp3111.examsystem.model;

public class Student extends User {
    private int studentId;

    public Student(String username, String name, String gender, int age, String department, String password, String studentScore, int studentId) {
        super(username, name, gender, age, department, password);
        this.studentId = studentId;
    }

    public int getStudentId() {
        return studentId;
    }
}
