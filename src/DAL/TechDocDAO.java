package DAL;

import BE.Customer;
import BE.TechDoc;
import BE.User;
import DAL.DatabaseConnector.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TechDocDAO {
    private final DBConnector dbc;

    public TechDocDAO() throws Exception {
        dbc = new DBConnector();
    }

    public TechDoc createTechDoc(TechDoc techDoc) throws SQLException {
        //Prepare variables from customer in parameter
        String title = techDoc.getSetupName();
        String setupDescription = techDoc.getSetupDescription();
        String deviceInfo = techDoc.getDeviceLoginInfo();
        int customerID = techDoc.getCustomerID();

        String sql = "INSERT INTO TechDoc (setupname, setupDescription, deviceLoginInfo, CustomerID) VALUES (?,?,?,?);";

        try (Connection conn = dbc.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, title);
            stmt.setString(2, setupDescription);
            stmt.setString(3, deviceInfo);
            stmt.setInt(4, customerID);

            stmt.executeUpdate();

            int id = 0;

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

            TechDoc returnDoc = new TechDoc(id, title, customerID);
            returnDoc.setSetupDescription(setupDescription);
            returnDoc.setDeviceLoginInfo(deviceInfo);
            return returnDoc;

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void addTech(TechDoc techDoc, User user) throws SQLException {
        String sql = "INSERT INTO DocLinkUser VALUES(?,?);";

        try(Connection conn = dbc.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, user.getId());
            stmt.setInt(2, techDoc.getId());

            stmt.executeUpdate();
        } catch (SQLException e){
            throw new SQLException(e);
        }
    }

    public List<TechDoc> getTechDocs(Customer customer, User user) throws SQLException {
        if (user.getUserType() == 2) {
            return getSpecificTechDocs(customer,user);
        } else {
            return getAllTechDocs(customer);
        }
    }

    private List<TechDoc> getAllTechDocs(Customer customer) throws SQLException {
        ArrayList<TechDoc> techDocs = new ArrayList<>();

        try (Connection conn = dbc.getConnection()) {
            int customerID = customer.getId();
            String sql =   "SELECT * FROM TechDoc WHERE CustomerID = " + customerID + ";";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String setupName = rs.getString("setupname");
                String setupDescription = rs.getString("setupDescription");
                String deviceLoginInfo = rs.getString("deviceLoginInfo");

                TechDoc techDoc = new TechDoc(id,setupName,customerID);
                techDoc.setSetupDescription(setupDescription);
                techDoc.setDeviceLoginInfo(deviceLoginInfo);
                techDocs.add(techDoc);
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return techDocs;
    }

    private List<TechDoc> getSpecificTechDocs(Customer customer, User user) throws SQLException {
        ArrayList<TechDoc> techDocs = new ArrayList<>();

        try (Connection conn = dbc.getConnection()) {
            int customerID = customer.getId();
            String sql =   "SELECT * FROM TechDoc WHERE CustomerID = " + customerID + " " +
                    "AND TechDoc.id IN (SELECT TechDocID FROM DocLinkUser WHERE UserID = "+ user.getId() +")";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String setupName = rs.getString("setupname");
                String setupDescription = rs.getString("setupDescription");
                String deviceLoginInfo = rs.getString("deviceLoginInfo");

                TechDoc techDoc = new TechDoc(id,setupName,customerID);
                techDoc.setSetupDescription(setupDescription);
                techDoc.setDeviceLoginInfo(deviceLoginInfo);
                techDocs.add(techDoc);
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return techDocs;
    }

    public void updateTechDoc(TechDoc techDoc) throws SQLException {
        String sql = "  UPDATE TechDoc SET setupDescription = ?, deviceLoginInfo = ? WHERE id = ?;";

        try(Connection conn = dbc.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, techDoc.getSetupDescription());
            stmt.setString(2, techDoc.getDeviceLoginInfo());
            stmt.setInt(3,techDoc.getId());

            stmt.executeUpdate();
        } catch (SQLException e){
            throw new SQLException(e);
        }
    }
}
