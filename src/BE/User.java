package BE;

public class User {
    private int id;
    private String username;
    private String password;
    private String name;
    private String userType;

    public User(int id, String username, String password, String name, String userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.userType = userType;
    }

    public User(String username, String password, String name, String userType) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.userType = userType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
