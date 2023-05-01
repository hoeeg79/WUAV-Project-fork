package DAL;

import BE.User;
import DAL.DatabaseConnector.DBConnector;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {
    private DBConnector dbc;

    protected LoginDAO() throws Exception {
        dbc = new DBConnector();
    }

    protected User login(String username) throws SQLServerException {
        String sql = "SELECT * FROM [User] WHERE username=?;";
        try (Connection conn = dbc.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful!");
                return new User(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getInt(5));
            } else {
                System.out.println("Invalid username or password.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
