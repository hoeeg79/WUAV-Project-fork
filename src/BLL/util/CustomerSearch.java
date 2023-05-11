package BLL.util;

import BE.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerSearch {

    /**
     * A method that takes a list of customers and a search query.
     * It then loops through customers and search if the query matches the name or the phone number
     * It then returns the matching customers.
     */
    public List<Customer> searchCustomers(List<Customer> search, String searchQuery){
        List<Customer> matchingCustomers = new ArrayList<>();

        for(Customer customer: search){

            if(compareName(searchQuery, customer) || compareTlf(searchQuery, customer) || compareEmail(searchQuery, customer)){
                matchingCustomers.add(customer);
        }
    }
        return matchingCustomers;
    }

    /**
     *  A method that takes in a search query and a customer, and returns a boolean value indicating whether the name matches the search
     */
    private boolean compareName(String searchQuery, Customer customer){
        return customer.getName().toLowerCase().contains(searchQuery.toLowerCase());
    }

    /**
    * A method that takes in a search query and a customer, and returns a boolean value indicating whether phone number matches the search
     */
    private boolean compareTlf(String searchQuery, Customer customer){
        return customer.getTlf().toLowerCase().contains(searchQuery.toLowerCase());
    }

    private boolean compareEmail(String searchQuery, Customer customer){
        return customer.getEmail().toLowerCase().contains(searchQuery.toLowerCase());
    }

//    private boolean compareZipCode(String searchQuery, Customer customer){
//        return customer.getZipCode.toLowerCase().contains(searchQuery.toLowerCase());
//    }

//        private boolean compareAddress(String searchQuery, Customer customer){
//        return customer.getAddress.toLowerCase().contains(searchQuery.toLowerCase());
//    }
}
