package comp3111.examsystem.model;

public class User {
    public String username;
    public String name;
    public String gender;
    public String age;
    public String department;
    public String password;

    public User(String username, String name, String gender, String age, String department, String password) {
        this.username = username;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.department = department;
        this.password = password;
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

    public String getAge() {
        return age;
    }

    public String getDepartment() {
        return department;
    }

    public String getPassword() {
        return password;
    }
}
