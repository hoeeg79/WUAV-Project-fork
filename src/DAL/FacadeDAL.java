package DAL;

import BE.Customer;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class FacadeDAL {
    private CustomerDAO customerDAO;

    public FacadeDAL() throws Exception {
        customerDAO = new CustomerDAO();
    }

    public Customer createCustomer(Customer customer) throws SQLException {
        return customerDAO.createCustomer(customer);
    }

    public Map<Integer, List<Customer>> getCustomers() throws Exception{
        return customerDAO.returnCustomersByType();
    }
}
