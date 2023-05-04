package DAL;

import BE.Customer;
import BE.TechDoc;
import BE.User;
import BE.UserType;
import DAL.DatabaseConnector.DBConnector;
import javafx.collections.FXCollections;

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
        int userTypeId = user.getUserType().getId();

        String sqlCreateUser = "INSERT INTO [User] (username, password, name, usertypeID, softDeleted) VALUES (?,?,?,?,0);";

        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sqlCreateUser, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setInt(4, userTypeId);
            //stmt.setString(5, picture);

            stmt.executeUpdate();

            int id = 0;

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

            UserType userType = getUserType(conn, userTypeId);

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
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String name = rs.getString("name");
                int userTypeID = rs.getInt("userTypeID");

                UserType userType = getUserType(conn, userTypeID);

                User user = new User(id, username, password, name, userType);
                allUsers.add(user);
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return allUsers;
    }

    public void updateUser(User user) throws SQLException{
        try(Connection conn = dbConnector.getConnection()){

            String sql = "UPDATE [User] SET password=?, name=?, usertypeID=? WHERE id=?;";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setInt(3, user.getUserType().getId());
            pstmt.setInt(4, user.getId());

            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new SQLException("Could not update user", e);
        }
    }

    public List<User> getLinkedUsers(TechDoc techdoc) throws SQLException{
        String sql = "SELECT u.* FROM DocLinkUser dl INNER JOIN [User] u ON u.id = dl.UserID WHERE dl.TechDocID = ?;";

        List<User> links = FXCollections.observableArrayList();

        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, techdoc.getId());

            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String name = rs.getString("name");
                int userTypeID = rs.getInt("userTypeID");

                UserType userType = getUserType(conn, userTypeID);

                User user = new User(id, username, password, name, userType);

                links.add(user);
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return links;
    }

    private UserType getUserType(Connection conn, int userTypeId) throws SQLException {
        try {
            String sqlGetUserType = "SELECT * FROM Usertype WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sqlGetUserType, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, userTypeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String userTypeName = rs.getString("usertypename");
                return new UserType(userTypeId, userTypeName);
            }
            return null;
        } catch (SQLException e){
            throw new SQLException("Could not retrieve user type", e);
        }
    }
}
