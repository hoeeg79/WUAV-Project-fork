package DAL;

import BE.User;
import BE.UserType;
import DAL.DatabaseConnector.DBConnector;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;

public class LoginDAO {
    private DBConnector dbc;

    protected LoginDAO() throws Exception {
        dbc = new DBConnector();
    }

    /**
     * A method that uses an SQL query to retrieve us users with a specified username, that are not softdeleted.
     * @param username
     * @return
     * @throws Exception
     */
    protected User login(String username) throws Exception {
        String sql = "SELECT * FROM [User] WHERE username=? AND softDeleted != 1;";
        try (Connection conn = dbc.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                UserType userType = getUserType(conn, rs.getInt(5));
                System.out.println("Login successful!");
                return new User(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), userType);
            } else {
                System.out.println("Invalid username or password.");
                throw new Exception("Invalid username or password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    /**
     * A method that uses an SQL query to get the usertypes on a specified id.
     * @param conn
     * @param userTypeId
     * @return
     * @throws SQLException
     */
    private UserType getUserType(Connection conn, int userTypeId) throws SQLException {
        try {
            String sqlGetUserType = "SELECT * FROM Usertype WHERE id = ?;";

            PreparedStatement stmt = conn.prepareStatement(sqlGetUserType, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, userTypeId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                String userTypeName = rs.getString("usertypename");
                return new UserType(userTypeId, userTypeName);
            }
            return null;
        } catch (SQLException e){
            throw new SQLException("Could not retrieve user type", e);
        }
    }

}
