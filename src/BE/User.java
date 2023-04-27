package BE;

public class User {
    private int id;
    private String username;
    private String password;
    private String name;
    private int userType;
    private String picture;


    public User(int id, String username, String password, String name, int userType, String picture) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.userType = userType;
        this.picture = picture;
    }

    public User(String username, String password, String name, int userType, String picture) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.userType = userType;
        this.picture = picture;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void add(User user) {
    }


}
