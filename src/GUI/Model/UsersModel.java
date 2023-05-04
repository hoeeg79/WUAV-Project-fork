package GUI.Model;

import BE.Customer;
import BE.User;
import BLL.UsersManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class UsersModel {

    private UsersManager usersManager;
    private ObservableList<User> userList;

    public UsersModel() throws Exception {
        usersManager = new UsersManager();

        getObservableUsers();
    }

    public void createUser(User user) throws SQLException {
        User newUser = usersManager.createUser(user);
        userList.add(newUser);
    }

    public void deleteUser(User user) throws SQLException {
        usersManager.deleteUser(user);
        userList.remove(user);
    }

    public ObservableList<User> getObservableUsers() throws Exception {
        userList = FXCollections.observableArrayList();
        userList.addAll(usersManager.getUsers());

        return userList;
    }

    public void updateUser(User user) throws Exception {
        usersManager.updateUser(user);
        userList.clear();
        userList.addAll(usersManager.getUsers());
    }
}


