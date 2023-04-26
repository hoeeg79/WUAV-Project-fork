package BLL;

import BE.Customer;
import DAL.CustomerDAO;
import DAL.FacadeDAL;

import java.sql.SQLException;
import java.util.List;

public class CustomerManager {
    private FacadeDAL facadeDAL;

    public CustomerManager() throws Exception {
        facadeDAL = new FacadeDAL();
    }

    public Customer createCustomer(Customer customer) throws SQLException {
        return facadeDAL.createCustomer(customer);
    }

    public List<Customer> getCustomers()throws Exception {
        return facadeDAL.getCustomers();
    }
}
