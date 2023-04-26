package BLL;

import BE.Customer;
import DAL.CustomerDAO;
import DAL.FacadeDAL;

import java.sql.SQLException;

public class CustomerManager {
    private FacadeDAL facadeDAL;

    public CustomerManager() throws Exception {
        facadeDAL = new FacadeDAL();
    }

    public Customer createCustomer(Customer customer) throws SQLException {
        return facadeDAL.createCustomer(customer);
    }
}
