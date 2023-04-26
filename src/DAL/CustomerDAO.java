package DAL;

import BE.Customer;
import DAL.DatabaseConnector.DBConnector;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Customer> returnCustomers() throws SQLException {
        //Makes a list called allCustomers to store customers in, returns in the end
        ArrayList<Customer> allCustomers = new ArrayList<>();

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
                String picture = rs.getString("picture");
                int customerType = rs.getInt("customerType");

                //Create event and add to list created in the beginning
                Customer customer = new Customer(name, email, tlf, picture, customerType);
                allCustomers.add(customer);
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return allCustomers;
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
}
