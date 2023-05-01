package BLL;

import BE.Customer;
import BE.User;
import DAL.FacadeDAL;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UsersManager {
    private final FacadeDAL facadeDAL;

    /**
     * Constructor for the UsersManager class. It creates a new instance of FacadeDAL.
     */
    public UsersManager() throws Exception {
        facadeDAL = new FacadeDAL();
    }

    /**
     * A method that takes in a user object and users facadeDAL to create a new user in the database
     */
    public User createUser(User user) throws SQLException {
        return facadeDAL.createUser(user);
    }

    /**
     * A method that uses facadeDAL to retrieve all users from our database, and return them as a list.
     */
    public List<User> getUsers() throws Exception {
        return facadeDAL.getUsers();
    }

    /**
     * A method that takes in a user object and users facadeDAL to delete a user in the database
     */
    public void deleteUser(User user) throws SQLException {
        facadeDAL.deleteUser(user);
    }

    public void updateUser(User user) throws SQLException {
        facadeDAL.updateUser(user);
    }

}
