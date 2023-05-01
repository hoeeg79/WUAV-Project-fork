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

        userList = FXCollections.observableArrayList();
        userList.addAll(usersManager.getUsers());
    }
    public void createUser(User user) throws SQLException {
        usersManager.createUser(user);
        userList.add(user);
    }
    public void deleteUser(User user) throws SQLException {
        usersManager.deleteUser(user);
        userList.remove(user);
    }
    public ObservableList<User> getObservableUsers() {
        return userList;
    }

    public void updateUser(User user) throws SQLException {
        usersManager.updateUser(user);
    }

}


