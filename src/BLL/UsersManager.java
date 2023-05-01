package BLL;

import BE.Customer;
import BE.User;
import DAL.FacadeDAL;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UsersManager {
    private FacadeDAL facadeDAL;

    public UsersManager() throws Exception {
        facadeDAL = new FacadeDAL();
    }

    public User createUser(User user) throws SQLException {
        return facadeDAL.createUser(user);
    }

    public List<User> getUsers() throws Exception {
        return facadeDAL.getUsers();
    }

    public void deleteUser(User user) throws SQLException {
        facadeDAL.deleteUser(user);
    }

    public void updateUser(User user) throws SQLException {
        facadeDAL.updateUser(user);
    }

}
