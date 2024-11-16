package comp3111.examsystem.model;

public class Manager {
    private final long id;
    private final String username;
    private final String password;

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
