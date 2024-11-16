package comp3111.examsystem.model;

public class Student extends User {
    public long id;

    // Default Constructor For Student
    public Student() {
        super("username", "name", "gender", "age", "department", "password");
        this.id = 0;
    }

    // Constructor For Student
    public Student(String username, String name, String gender, String age, String department, String password, String studentScore, long id) {
        super(username, name, gender, age, department, password);
        this.id = id;
    }

    // Constructor For Student
    public Student(String username, String name, String gender, String age, String department, String password, long id) {
        super(username, name, gender, age, department, password);
        this.id = id;
    }

    // Getter For Id
    public long getId() {
        return id;
    }

    // Setter For Id
    public void setId(long id) {
        this.id = id;
    }
}
