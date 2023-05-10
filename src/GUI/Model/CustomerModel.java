package GUI.Model;

import BE.Customer;
import BLL.CustomerManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;

public class CustomerModel {
    private CustomerManager customerManager;
    private ObservableList<Customer> customerList;

    public CustomerModel() throws Exception {
        customerManager = new CustomerManager();
        customerList = FXCollections.observableArrayList();
    }

    public void createCustomer(Customer customer) throws SQLException {
        customerManager.createCustomer(customer);
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

//    public void customerSearch(String searchQuery) throws Exception {
//        if(searchQuery.isEmpty()){
//            backUpSearch();
//        }else {
//        businessSearch(searchQuery);
//        governmentSearch(searchQuery);
//        privateSearch(searchQuery);
//        }
//    }
//
//    private void businessSearch(String searchQuery) throws Exception{
//        List<Customer> searchResult = customerManager.searchCustomer(businessCustomer, searchQuery);
//            businessCustomer.clear();
//            businessCustomer.addAll(searchResult);
//        }
//
//
//    private void governmentSearch(String searchQuery) throws Exception{
//        List<Customer> searchResult = customerManager.searchCustomer(governmentCustomer, searchQuery);
//            governmentCustomer.clear();
//            governmentCustomer.addAll(searchResult);
//        }
//
//    private void privateSearch(String searchQuery) throws Exception{
//        List<Customer> searchResult = customerManager.searchCustomer(privateCustomer, searchQuery);
//            privateCustomer.clear();
//            privateCustomer.addAll(searchResult);
//    }
//
//    private void backUpSearch() throws Exception{
//        businessCustomer.clear();
//        governmentCustomer.clear();
//        privateCustomer.clear();
//        businessCustomer.addAll(backUpBusiness);
//        governmentCustomer.addAll(backUpGovernment);
//        privateCustomer.addAll(backUpPrivate);
//    }
}
