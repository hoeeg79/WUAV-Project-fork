package DAL;

import BE.User;
import DAL.DatabaseConnector.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersDAO {

    private final DBConnector dbConnector;

    public UsersDAO() throws Exception {
        dbConnector = new DBConnector();
    }

    public User createUser(User user) throws SQLException {

        String username = user.getUsername();
        String password = user.getPassword();
        String name = user.getName();
        /*String picture = "";
        if (user.getPicture() == null) {
            picture = "defaultUser.jpg";
        } else {
            picture = user.getPicture();
        }*/
        int userType = user.getUserType();

        String sql = "INSERT INTO [User] (username, password, name, usertypeID, softDeleted) VALUES (?,?,?,?,0);";

        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setInt(4, userType);
            //stmt.setString(5, picture);

            stmt.executeUpdate();

            int id = 0;

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

            return new User(id, username, password, name, userType);

        } catch (SQLException e) {
            throw new SQLException(e);
        }

    }

    public void deleteUser(User user) throws SQLException {
        try (Connection conn = dbConnector.getConnection()) {

            String sql = "UPDATE [User] SET softDeleted = 1 WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, user.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }

    }

    public List<User> returnUsers() throws SQLException {
        ArrayList<User> allUsers = new ArrayList<>();

        try (Connection conn = dbConnector.getConnection()) {

            String sql = "SELECT * FROM [User] WHERE softDeleted != 1;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

                String username = rs.getString("username");
                String password = rs.getString("password");
                String name = rs.getString("name");
                int userType = rs.getInt("userTypeID");

                User user = new User(username, password, name, userType);
                allUsers.add(user);
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return allUsers;
    }
}
