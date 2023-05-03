package BE;

import java.util.Objects;

public class UserType {
    private int id;
    private String userTypeName;

    public UserType(int id, String userTypeName) {
        this.id = id;
        this.userTypeName = userTypeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserTypeName() {
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    @Override
    public String toString() {
        return userTypeName;
    }
}
