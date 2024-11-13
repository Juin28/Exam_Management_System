package comp3111.examsystem.model;

public class Student extends User {
    public long id;

    public Student(String username, String name, String gender, String age, String department, String password, String studentScore, long id) {
        super(username, name, gender, age, department, password);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
