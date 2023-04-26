package GUI.Model;

import BE.Customer;
import BLL.CustomerManager;

public class CustomerModel {
    private CustomerManager customerManager;

    public CustomerModel() throws Exception {
        customerManager = new CustomerManager();
    }

    public void createCustomer(Customer customer) {
        createCustomer(customer);
    }
}
