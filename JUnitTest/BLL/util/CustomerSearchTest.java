package BLL.util;

import BE.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


class CustomerSearchTest {

    static CustomerSearch customerSearch;
    static List<Customer> customers = new ArrayList<>();

    @BeforeAll
    static void preTest() {
        customerSearch = new CustomerSearch();

        Customer customer1 = new Customer(1, "Firma 1", "kontakt@firma1.dk", "55443322",
                "alfavej 1", "5540", "Ullerslev");

        Customer customer2 = new Customer(2, "Firma 2", "kontakt@Firma2.dk", "88779944",
                "bravovej 2", "6700", "Esbjerg");

        Customer customer3 = new Customer(3, "Firma 3", "kontakt@firma3.dk", "78528486",
                "charlievej 3", "6740", "Bramming");

        Customer customer4 = new Customer(4, "Firma 4", "kontakt@firma4.dk", "68728458",
                "deltavej 4", "9000", "Aalborg");

        Customer customer5 = new Customer(5, "Firma 5", "kontakt@firma5.dk", "45897822",
                "Echovej 5", "8000", "Aarhus C");

        Customer customer6 = new Customer(6, "Firma 6", "kontakt@firma6.dk", "26874128",
                "Foxtrotvej 6", "6800", "Varde");


        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);
        customers.add(customer4);
        customers.add(customer5);
        customers.add(customer6);

    }

    @Test
    void testEmailSearch() {
        // Arrange - set up our test objects etc.
        /* Since everything is set up in pretest, nothing here */

        // Act - do the actual calc or method run
        int actualResult = customerSearch.searchCustomers(customers, "kontakt@").size();
        int expectedResult = 6;

        // Assert - check if actual value is equal to expected value
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void testCitySearch() {
        // Arrange - set up our test objects etc.
        /* Since everything is set up in pretest, nothing here */

        // Act - do the actual calc or method run
        int actualResult = customerSearch.searchCustomers(customers, "Aarhus").size();
        int expectedResult = 1;

        // Assert - check if actual value is equal to expected value
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void testNoResults() {
        // Arrange - set up our test objects etc.
        /* Since everything is set up in pretest, nothing here */

        // Act - do the actual calc or method run
        int actualResult = customerSearch.searchCustomers(customers, "Fimra 1").size();
        int expectedResult = 0;

        // Assert - check if actual value is equal to expected value
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void testStreetName() {
        // Arrange - set up our test objects etc.
        /* Since everything is set up in pretest, nothing here */

        // Act - do the actual calc or method run
        int actualResult = customerSearch.searchCustomers(customers, "Echo").size();
        int expectedResult = 1;

        // Assert - check if actual value is equal to expected value
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void testZipcodeSearch() {
        // Arrange - set up our test objects etc.
        /* Since everything is set up in pretest, nothing here */

        // Act - do the actual calc or method run
        int actualResult = customerSearch.searchCustomers(customers, "5540").size();
        int expectedResult = 1;

        // Assert - check if actual value is equal to expected value
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void testZipcodeSearch2() {
        // Arrange - set up our test objects etc.
        /* Since everything is set up in pretest, nothing here */

        // Act - do the actual calc or method run
        int actualResult = customerSearch.searchCustomers(customers, "67").size();
        int expectedResult = 2;

        // Assert - check if actual value is equal to expected value
        Assertions.assertEquals(expectedResult, actualResult);
    }
}