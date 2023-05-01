package DAL;

import BE.Customer;
import BE.User;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class FacadeDAL {
    private CustomerDAO customerDAO;
    private UsersDAO usersDAO;
    private LoginDAO loginDAO;

    public FacadeDAL() throws Exception {
        customerDAO = new CustomerDAO();
        usersDAO = new UsersDAO();
        loginDAO = new LoginDAO();
    }

    public Customer createCustomer(Customer customer) throws SQLException {
        return customerDAO.createCustomer(customer);
    }

    public void deleteCustomer(Customer customer) throws SQLException {
        customerDAO.deleteCustomer(customer);
    }

    public Map<Integer, List<Customer>> getCustomers() throws Exception{
        return customerDAO.returnCustomersByType();
    }

    public User createUser(User user) throws SQLException {
        return usersDAO.createUser(user);
    }

    public void deleteUser(User user) throws SQLException {
        usersDAO.deleteUser(user);
    }

    public List<User> getUsers() throws SQLException {
        return usersDAO.returnUsers();
    }

    public void updateCustomer(Customer c) throws SQLException{
        customerDAO.updateCustomer(c);
    }

    public User login(String username) throws SQLServerException {
        return loginDAO.login(username);
    }
}
