package GUI.Model;

import BE.Customer;
import BLL.CustomerManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class CustomerModel {
    private CustomerManager customerManager;

    private ObservableList<Customer> customerList;

    public CustomerModel() throws Exception {
        customerManager = new CustomerManager();
        customerList = FXCollections.observableArrayList();
    }

    public void createCustomer(Customer customer) {
        createCustomer(customer);
    }

    public ObservableList<Customer> getCustomerList() throws SQLException{
        customerList.clear();
        customerList.addAll(customerManager.getCustomers());
        return customerList;
    }

    public void deleteCustomer(Customer customer) throws SQLException {
        customerManager.deleteCustomer(customer);
    }
}
