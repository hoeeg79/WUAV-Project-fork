package DAL;

import BE.Customer;
import DAL.DatabaseConnector.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private final DBConnector dbc;

    public CustomerDAO() throws Exception {
        dbc = new DBConnector();
    }

    /**
     * A method that uses an SQL string to create a new user in our database.
     */
    public Customer createCustomer(Customer customer) throws SQLException {

        //Prepare variables from customer in parameter
        String name = customer.getName();
        String email = customer.getEmail();
        String tlf = customer.getTlf();
        String streetName = customer.getStreetName();
        String zipcode = customer.getZipcode();

        String sql = "INSERT INTO Customer (name, email, tlf, streetName, zipcode, softdeleted) VALUES (?,?,?,?,?,0);";

        try (Connection conn = dbc.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, tlf);
            stmt.setString(4, streetName);
            stmt.setString(5, zipcode);

            stmt.executeUpdate();

            int id = 0;

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

            return new Customer(id, name, email, tlf, streetName, zipcode);

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public List<Customer> returnCustomers() throws Exception{
        ArrayList<Customer> allCustomers = new ArrayList<>();

        try(Connection connection = dbc.getConnection()){

            String sql = "SELECT * FROM Customer WHERE softDeleted != 1;;";

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("tlf");
                String streetName = rs.getString("streetName");
                String zipcode = rs.getString("zipcode");


                Customer customer = new Customer(id, name, email, phoneNumber, streetName, zipcode);
                allCustomers.add(customer);
            }
        }catch(Exception e){
            throw new Exception("Could not get Customers from database", e);
        }
        return allCustomers;
    }

    /**
     * A method that uses an SQL string to update a user from our database, changing the bit value from 0 to 1.
     * Making the user not show up in our view anymore.
     */
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

    /**
     * A method that uses an SQL string to update the specified data of our specified user.
     */
    public void updateCustomer(Customer customer) throws SQLException{
        try(Connection conn = dbc.getConnection()){

            String sql = "UPDATE Customer SET name=?, email=?, tlf=?, streetName=?, zipcode=? WHERE id=?;";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getTlf());
            pstmt.setString(4, customer.getStreetName());
            pstmt.setString(5, customer.getZipcode());
            pstmt.setInt(6,customer.getId());
            
            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new SQLException("Could not update customer", e);
        }
    }
    public Customer zipCheck(String city, String zipcode) throws SQLException {
        try(Connection conn = dbc.getConnection()){
            String sql = "SELECT COUNT(*) FROM Customer Where zipcode = ?;";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, zipcode);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next() && rs.getString(1) > 0){
                return null;
            }
        }catch (SQLException e){
            throw new SQLException();
        }
        zipInsert(city, zipcode);
    }

    public void zipInsert(String city, String zipcode) throws SQLException{
        try(Connection conn = dbc.getConnection()){
            String sql = "INSERT INTO City (city, zipcode) VALUES (?,?);";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, city);
            pstmt.setString(2, zipcode);

            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new SQLException(e);
        }
    }
}
