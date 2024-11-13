package comp3111.examsystem.model;

public class User {
    private String username;
    private String name;
    private String gender;
    private int age;
    private String department;
    private String password;

    public User(String username, String name, String gender, int age, String department, String password) {
        this.username = username;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.department = department;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getDepartment() {
        return department;
    }

    public String getPassword() {
        return password;
    }

}
