package GUI.Model;

import BE.Customer;
import BLL.CustomerManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.sql.SQLException;

public class CustomerModel {
    private CustomerManager customerManager;
    private ObservableList<Customer> customerList;

    public CustomerModel() throws Exception {
        customerManager = new CustomerManager();
        customerList = FXCollections.observableArrayList();
    }

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

    public void deleteCustomer(Customer customer) throws SQLException {
        customerManager.deleteCustomer(customer);
    }

    public void updateCustomer(Customer c) throws SQLException {
        customerManager.updateCustomer(c);
    }

    public ObservableList<Customer> getObservableCustomers() throws Exception{
        customerList.clear();
        customerList.addAll(customerManager.getCustomers());
        return customerList;
    }

    public void searchCustomer(String query) throws Exception{
        List<Customer> searchResults = customerManager.searchCustomer(query);
        customerList.clear();
        customerList.addAll(searchResults);
    }

    public boolean checkCustomerForDocs(){

        for (Customer customer : customerList) {
            if (customer.isDocReadyForApproval()) {
                return true;
            }
        }

        return false;
    }
}
