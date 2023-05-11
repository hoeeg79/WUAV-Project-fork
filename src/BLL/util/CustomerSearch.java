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

            if(compareName(searchQuery, customer) || compareTlf(searchQuery, customer) || compareEmail(searchQuery, customer) || compareZipCode(searchQuery, customer) || compareStreetName(searchQuery, customer) || compareCity(searchQuery, customer)){
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
    /**
     * A method that takes in a search query and a customer, and returns a boolean value indicating whether email matches the search
     */
    private boolean compareEmail(String searchQuery, Customer customer){
        return customer.getEmail().toLowerCase().contains(searchQuery.toLowerCase());
    }
    /**
     * A method that takes in a search query and a customer, and returns a boolean value indicating whether zipcode matches the search
     */
    private boolean compareZipCode(String searchQuery, Customer customer){
        return customer.getZipcode().toLowerCase().contains(searchQuery.toLowerCase());
    }

    /**
     * A method that takes in a search query and a customer, and returns a boolean value indicating whether street name matches the search
     */
    private boolean compareStreetName(String searchQuery, Customer customer){
        return customer.getStreetName().toLowerCase().contains(searchQuery.toLowerCase());
    }

    /**
     * A method that takes in a search query and a customer, and returns a boolean value indicating whether city matches the search
     */
    private boolean compareCity(String searchQuery, Customer customer){
        return customer.getCity().toLowerCase().contains(searchQuery.toLowerCase());
    }
}
