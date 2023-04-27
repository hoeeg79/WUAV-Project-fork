package GUI.Model;

import BE.User;
import BLL.UsersManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class UsersModel {

    private UsersManager usersManager;

    private ObservableList<User> userList;


    public UsersModel(UsersManager usersManager) throws Exception {
        this.usersManager = usersManager;

        userList = FXCollections.observableArrayList();
        userList.addAll(usersManager.getUsers());
    }
    public void createUser(User user) {
        createUser(user);
        userList.add(user);
    }
    public void deleteUser(User user) throws SQLException {
        usersManager.deleteUser(user);
        userList.remove(user);
    }
    public ObservableList<User> getObservableUsers() {
        return userList;
    }
}

