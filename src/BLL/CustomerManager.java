package BLL;

import BE.Customer;
import BLL.util.CustomerSearch;
import DAL.FacadeDAL;

import java.sql.SQLException;
import java.util.List;

public class CustomerManager {

    private final FacadeDAL facadeDAL;
    private final CustomerSearch customerSearch = new CustomerSearch();

    /**
     * Constructor for the CustomerManager class. It creates a new instance of FacadeDAL.
     */
    public CustomerManager() throws Exception {
        facadeDAL = new FacadeDAL();
    }

    /**
     * A method that takes in a customer object and uses facadeDAL to create a new customer in the database
     */
    public Customer createCustomer(Customer customer) throws SQLException {
        return facadeDAL.createCustomer(customer);
    }

    /**
     * A method that retrieves a list of customers.
     */
    public List<Customer> getCustomers() throws Exception{
        return facadeDAL.getCustomers();
    }

    /**
     * A method that takes in a customer object and uses facadeDAL to delete a customer in the database
     */
    public void deleteCustomer(Customer customer) throws SQLException {
        facadeDAL.deleteCustomer(customer);
    }

    /**
     *  A method that takes in a customer object and uses facadeDAL to update a customer in the database
     */
    public void updateCustomer(Customer c) throws SQLException{
        facadeDAL.updateCustomer(c);
    }

    /**
     *  A method that takes a list of customers and a search query,
     *  and uses customerSearch to search for customers that match the query
     */
    public List<Customer> searchCustomer(String searchQuery) throws Exception{
        List<Customer> allCustomers = getCustomers();
        List<Customer> searchRestult = customerSearch.searchCustomers(allCustomers ,searchQuery);
        return searchRestult;
    }
}
