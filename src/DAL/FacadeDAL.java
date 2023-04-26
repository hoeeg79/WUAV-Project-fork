package DAL;

import BE.Customer;

import java.sql.SQLException;
import java.util.List;

public class FacadeDAL {
    private CustomerDAO customerDAO;

    public FacadeDAL() throws Exception {
        customerDAO = new CustomerDAO();
    }

    public Customer createCustomer(Customer customer) throws SQLException {
        return customerDAO.createCustomer(customer);
    }

    public List<Customer> getCustomers() throws SQLException{
        return customerDAO.returnCustomers();
    }
    public void deleteCustomer(Customer customer) throws SQLException {
        customerDAO.deleteCustomer(customer);
    }
}
