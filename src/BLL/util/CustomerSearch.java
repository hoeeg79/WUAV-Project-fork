package BLL.util;

import BE.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerSearch {

    public List<Customer> searchCustomers(List<Customer> search, String searchQuery){
        List<Customer> matchingCustomers = new ArrayList<>();

        for(Customer customer: search){

            if(compareName(searchQuery, customer) || compareEmail(searchQuery, customer) || compareTlf(searchQuery, customer)){
                matchingCustomers.add(customer);
        }
    }
        return matchingCustomers;
    }

    private boolean compareName(String searchQuery, Customer customer){
        return customer.getName().toLowerCase().contains(searchQuery.toLowerCase());
    }

    private boolean compareEmail(String searchQuery, Customer customer){
        return customer.getEmail().toLowerCase().contains(searchQuery.toLowerCase());
    }

    private boolean compareTlf(String searchQuery, Customer customer){
        return customer.getTlf().toLowerCase().contains(searchQuery.toLowerCase());
    }
}
