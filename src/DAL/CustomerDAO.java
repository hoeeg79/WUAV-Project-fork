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
        String tlf = customer.getTlf();
        String picture = "";
        if (customer.getPicture() == null){
            picture = "defaultUser.jpg";
        } else {
            picture = customer.getPicture();
        }
        int customerType = customer.getCustomerType();

        String sql = "INSERT INTO Customer (name, email, tlf, image, customertypeid, softdeleted) VALUES (?,?,?,?,?,0);";

        try (Connection conn = dbc.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, tlf);
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
            String sql = "SELECT * FROM Customer WHERE softDeleted != 1;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //Loop through rows from database result set
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String tlf = rs.getString("tlf");
                String image = rs.getString("image");
                int customertypeid = rs.getInt("customertypeid");

                //Create customer and add to list created in the beginning
                Customer customer = new Customer(id, name, email, tlf, image, customertypeid);

                //Add the customer to the appropriate list based on their type
                List<Customer> customersOfType = customersByType.get(customertypeid);

                if (customersOfType == null){
                    customersOfType = new ArrayList<>();
                    customersByType.put(customertypeid, customersOfType);
                }
                customersByType.get(customertypeid).add(customer);
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return customersByType;
    }

    public void deleteCustomer(Customer customer) throws SQLException {
        try (Connection conn = dbc.getConnection()) {

            String sql = "UPDATE Customer SET softDeleted = 1 WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, customer.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void updateCustomer(Customer customer) throws SQLException{
        try(Connection conn = dbc.getConnection()){

            String sql = "UPDATE Customer SET name=?, email=?, tlf=?, image=?, customertypeid=? WHERE id=?;";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getTlf());
            pstmt.setString(4, customer.getPicture());
            pstmt.setInt(5, customer.getCustomerType());
            pstmt.setInt(6,customer.getId());
            
            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new SQLException("Could not update customer", e);
        }
    }
}
