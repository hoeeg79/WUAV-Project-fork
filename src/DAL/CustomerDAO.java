package DAL;

import BE.Customer;
import DAL.DatabaseConnector.DBConnector;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDAO {
    private final DBConnector dbc;

    public CustomerDAO() throws Exception {
        dbc = new DBConnector();
    }

    public Customer createCustomer(Customer customer) throws SQLException {

        //Prepare variables from customer in parameter
        String name = customer.getName();
        String email = customer.getEmail();
        int tlf = customer.getTlf();
        String picture = "";
        if (customer.getPicture() == null){
            picture = "defaultUser.jpg";
        } else {
            picture = customer.getPicture();
        }
        int customerType = customer.getCustomerType();

        String sql = "INSERT INTO Customer (name, email, tlf, image, customertypeid) VALUES (?,?,?,?,?);";

        try (Connection conn = dbc.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setInt(3, tlf);
            stmt.setString(4, picture);
            stmt.setInt(5, customerType);

            stmt.executeUpdate();

            int id = 0;

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

            return new Customer(id, name, email, tlf, picture, customerType);

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public Map<Integer, List<Customer>> returnCustomersByType() throws Exception {
        //Create a map to store lists of customers by type
        Map<Integer, List<Customer>> customersByType = new HashMap<>();

        //Try with resources to connect to DB
        try(Connection conn = dbc.getConnection()){
            
            //SQL string, selects all customers from db
            String sql = "SELECT * FROM Customer;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //Loop through rows from database result set
            while(rs.next()){
                String name = rs.getString("name");
                String email = rs.getString("email");
                int tlf = rs.getInt("tlf");
                String image = rs.getString("image");
                int customertypeid = rs.getInt("customertypeid");

                //Create customer and add to list created in the beginning
                Customer customer = new Customer(name, email, tlf, image, customertypeid);

                //Add the customer to the appropriate list based on their type
                List<Customer> customersOfType = customersByType.get(customertypeid);

                if (customersOfType == null){
                    customersOfType = new ArrayList<>();
                    customersByType.put(customertypeid, customersOfType);
                }
                customersByType.get(customertypeid).add(customer);
            }

        } catch (Exception e) {
            throw new Exception(e);
        }
        return customersByType;
    }
}
