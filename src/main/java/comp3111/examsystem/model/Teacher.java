package comp3111.examsystem.model;

public class Teacher extends User {
    private String position;
    private int teacherId;

    public Teacher(String username, String name, String gender, int age, String department, String password, String position, int teacherId) {
        super(username, name, gender, age, department, password);
        this.position = position;
        this.teacherId = teacherId;
    }

    public String getPosition() {
        return position;
    }

    public int getTeacherId() {
        return teacherId;
    }
}
