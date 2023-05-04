package BE;

import java.util.Objects;

public class User {
    private int id;
    private String username;
    private String password;
    private String name;
    private UserType userType;
    private int userTypeID;
    private String picture;


    public User(int id, String username, String password, String name, UserType userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.userType = userType;
    }

    public User(String username, String password, String name, UserType userType) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.userType = userType;
    }

    public User(String username, String password, String name, int userTypeID) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.userTypeID = userTypeID;
    }

    public int getUserTypeID() {
        return userTypeID;
    }

    public void setUserTypeID(int userTypeID) {
        this.userTypeID = userTypeID;
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

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void add(User user) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name;
    }
}
