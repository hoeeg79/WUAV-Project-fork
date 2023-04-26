package DAL;

import BE.Customer;

import java.sql.SQLException;

public class FacadeDAL {
    private CustomerDAO customerDAO;

    public FacadeDAL() throws Exception {
        customerDAO = new CustomerDAO();
    }

    public Customer createCustomer(Customer customer) throws SQLException {
        return customerDAO.createCustomer(customer);
    }
}
