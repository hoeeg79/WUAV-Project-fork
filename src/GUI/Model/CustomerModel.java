package GUI.Model;

import BE.Customer;
import BLL.CustomerManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

import java.sql.SQLException;

public class CustomerModel {
    private final CustomerManager customerManager;
    private final ObservableList<Customer> customerList;

    /**
     * A constructor of the CustomerModel. Used to instantiate CustomerManager and declare what customerList is.
     */
    public CustomerModel() throws Exception {
        customerManager = new CustomerManager();
        customerList = FXCollections.observableArrayList();
    }

    /**
     * A method used to Create a new customer if the specified information does not exist.
     */
    public boolean createCustomer(Customer customer) throws SQLException {
        Customer newCustomer = customerManager.createCustomer(customer);
        if (newCustomer == null) {
            return false;
        }
        else {
            customerList.add(newCustomer);
            return true;
        }
    }

    /**
     * A method used to delete a customer.
     */
    public void deleteCustomer(Customer customer) throws SQLException {
        customerManager.deleteCustomer(customer);
    }

    /**
     * A method used to update a customer.
     */
    public void updateCustomer(Customer c) throws SQLException {
        customerManager.updateCustomer(c);
    }

    /**
     * A method that clears a list of customers, and adds them back using information from the customerManager.
     */
    public ObservableList<Customer> getObservableCustomers() throws Exception{
        customerList.clear();
        customerList.addAll(customerManager.getCustomers());
        return customerList;
    }

    /**
     * A method used to search for customers.
     */
    public void searchCustomer(String query) throws Exception{
        List<Customer> searchResults = customerManager.searchCustomer(query);
        customerList.clear();
        customerList.addAll(searchResults);
    }

    /**
     * A method used to check if there is a document ready for approval for a specific customer.
     */
    public boolean checkCustomerForDocs(){

        for (Customer customer : customerList) {
            if (customer.isDocReadyForApproval()) {
                return true;
            }
        }

        return false;
    }
}
