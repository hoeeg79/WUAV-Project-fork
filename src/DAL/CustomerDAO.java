package DAL;

import BE.Customer;
import DAL.DatabaseConnector.DBConnector;

import java.sql.*;

public class CustomerDAO {
    private final DBConnector dbc;

    public CustomerDAO(DBConnector dbc) {
        this.dbc = dbc;
    }

    public Customer createCustomer(Customer customer) {

        //Prepare variables from customer in parameter
        String name = customer.getName();
        String email = customer.getEmail();
        int tlf = customer.getTlf();
        String picture = "";
        if (customer.getPicture() == null){
            picture = "skriv sti til default billede her";
        } else {
            picture = customer.getPicture();
        }

        String sql = "INSERT INTO Customer (name, email, tlf, image) VALUES (?,?,?,?);";

        try (Connection conn = dbc.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setInt(3, tlf);
            stmt.setString(4, picture);

            stmt.executeUpdate();

            int id = 0;

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

            return new Customer(id, name, email, tlf, picture);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
