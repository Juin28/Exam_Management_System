package comp3111.examsystem.model;

public class Manager {
    private final long id;
    private String username;
    private String password;

    public Manager(){
        this.username = "admin";
        this.password = "admin";
        this.id = 0;
    }

    public Manager(String username, String password, long id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }
}
