package GUI.Model;

import BE.User;
import BLL.UsersManager;

import java.sql.SQLException;

public class UsersModel {

    private UsersManager usersManager;

    public UsersModel(UsersManager usersManager) {
        this.usersManager = usersManager;
    }
    public void createUser(User user) {
        createUser(user);
    }
    public void deleteUser(User user) throws SQLException {
        usersManager.deleteUser(user);
    }



}
