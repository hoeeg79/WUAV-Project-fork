package DAL;

import BE.Customer;
import DAL.DatabaseConnector.DBConnector;

import java.sql.*;

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
}
