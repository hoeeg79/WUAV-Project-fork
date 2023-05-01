package BLL;

import BE.Customer;
import BLL.util.CustomerSearch;
import DAL.CustomerDAO;
import DAL.FacadeDAL;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CustomerManager {

    private FacadeDAL facadeDAL;
    private CustomerSearch customerSearch = new CustomerSearch();

    public CustomerManager() throws Exception {
        facadeDAL = new FacadeDAL();
    }

    public Customer createCustomer(Customer customer) throws SQLException {
        return facadeDAL.createCustomer(customer);
    }


    public Map<Integer, List<Customer>> getCustomers()throws Exception {
        return facadeDAL.getCustomers();
    }

    public void deleteCustomer(Customer customer) throws SQLException {
        facadeDAL.deleteCustomer(customer);
    }

    public void updateCustomer(Customer c) throws SQLException{
        facadeDAL.updateCustomer(c);
    }
    public List<Customer> searchCustomer(List<Customer> customers, String searchQuery) throws Exception{
        List<Customer> searchRestult = customerSearch.searchCustomers(customers, searchQuery);
        return searchRestult;
    }
}
