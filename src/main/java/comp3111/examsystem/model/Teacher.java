package comp3111.examsystem.model;

public class Teacher extends User {
    public String position;
    public long id;

    // Default constructor
    public Teacher() {
        super("username", "name", "male", "20", "department", "password");
        this.position = "position";
        this.id = 0;
    }

    public Teacher(String username, String name, String gender, String age, String department, String password, String position, long id) {
        super(username, name, gender, age, department, password);
        this.position = position;
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public long getId() {
        return id;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
