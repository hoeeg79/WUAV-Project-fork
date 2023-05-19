package BLL;

import BE.TechDoc;
import BE.User;
import DAL.FacadeDAL;

import java.sql.SQLException;
import java.util.List;

public class UsersManager {
    private final FacadeDAL facadeDAL;

    /**
     * Constructor for the UsersManager class. It creates a new instance of FacadeDAL.
     */
    public UsersManager() throws Exception {
        facadeDAL = new FacadeDAL();
    }

    /**
     * A method that creates a user object in the database.
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
     * A method that deletes a user object in the database.
     */
    public void deleteUser(User user) throws SQLException {
        facadeDAL.deleteUser(user);
    }

    /**
     * A method that updates a user object in the database.
     */
    public void updateUser(User user) throws SQLException {
        facadeDAL.updateUser(user);
    }

    /**
     * A method that gets a list of linked users associated with a techDoc.
     */
    public List<User> getLinkedUsers(TechDoc techDoc) throws SQLException {
        return facadeDAL.getLinkedUsers(techDoc);
    }

}
