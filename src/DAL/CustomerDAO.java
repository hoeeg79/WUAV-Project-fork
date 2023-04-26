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
        String sql = "INSERT INTO User_credentials (username, password, admin) VALUES (?,?,?);";

        try (Connection conn = dbc.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setBoolean(3, isAdmin);

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
