package GUI.Model;

import BE.TechDoc;
import BE.User;
import BLL.UsersManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class UsersModel {

    private final UsersManager usersManager;
    private ObservableList<User> userList;

    public UsersModel() throws Exception {
        usersManager = new UsersManager();

        getObservableUsers();
    }

    /**
     *  A method used to create a new user, and add them to the userList.
     */
    public void createUser(User user) throws SQLException {
        User newUser = usersManager.createUser(user);
        userList.add(newUser);
    }

    /**
     *  A method used delete a user and remove them frmo the userList.
     */
    public void deleteUser(User user) throws SQLException {
        usersManager.deleteUser(user);
        userList.remove(user);
    }

    /**
     *  A method used retrieve a list of users and return the userList.
     */
    public ObservableList<User> getObservableUsers() throws Exception {
        userList = FXCollections.observableArrayList();
        userList.addAll(usersManager.getUsers());

        return userList;
    }

    /**
     *  A method used update users table, and update the userList.
     */
    public void updateUser(User user) throws Exception {
        usersManager.updateUser(user);
        userList.clear();
        userList.addAll(usersManager.getUsers());
    }

    /**
     *  A method used retrieve a list of the users, and see which tech documents they are associated with.
     */
    public List<User> getLinkedUsers(TechDoc techDoc) throws SQLException {
        return usersManager.getLinkedUsers(techDoc);
    }
}


