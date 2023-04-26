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
        String name = customer.getName();
        String email = customer.getEmail();
        int tlf = customer.getTlf();
        String sql = "INSERT INTO User_credentials (username, password, admin) VALUES (?,?,?);";

        try (Connection conn = dbc.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setInt(3, tlf);

            stmt.executeUpdate();

            int id = 0;

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

            return new User(username, id, isAdmin, password);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
