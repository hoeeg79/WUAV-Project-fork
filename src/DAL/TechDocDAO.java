package DAL;

import BE.*;
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
        String extraInfo = techDoc.getExtraInfo();

        String sql = "INSERT INTO TechDoc (setupname, setupDescription, deviceLoginInfo, CustomerID, extraInfo) VALUES (?,?,?,?,?);";

        try (Connection conn = dbc.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, title);
            stmt.setString(2, setupDescription);
            stmt.setString(3, deviceInfo);
            stmt.setInt(4, customerID);
            stmt.setString(5, extraInfo);

            stmt.executeUpdate();

            int id = 0;

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

            TechDoc returnDoc = new TechDoc(id, title, customerID);
            returnDoc.setSetupDescription(setupDescription);
            returnDoc.setDeviceLoginInfo(deviceInfo);
            returnDoc.setExtraInfo(extraInfo);
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
        if (user.getUserType().getId() == 2) {
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
                techDoc.setPictures(getTechPictures(techDoc));
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
            String sql = "";

            if (user.getUserType().getId() == 2) {
                sql = "SELECT * FROM TechDoc WHERE CustomerID = " + customerID + " " +
                        "AND TechDoc.id IN (SELECT TechDocID FROM DocLinkUser WHERE UserID = " + user.getId() + ")";
            } else {
                sql = "SELECT * FROM TechDoc WHERE CustomerID = " + customerID + ";";
            }
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String setupName = rs.getString("setupname");
                String setupDescription = rs.getString("setupDescription");
                String deviceLoginInfo = rs.getString("deviceLoginInfo");
                String extraInfo = rs.getString("extraInfo");

                TechDoc techDoc = new TechDoc(id,setupName,customerID);
                techDoc.setSetupDescription(setupDescription);
                techDoc.setDeviceLoginInfo(deviceLoginInfo);
                techDoc.setExtraInfo(extraInfo);
                techDoc.setPictures(getTechPictures(techDoc));
                techDocs.add(techDoc);
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return techDocs;
    }

    public void updateTechDoc(TechDoc techDoc) throws SQLException {
        String sql = "  UPDATE TechDoc SET setupDescription = ?, deviceLoginInfo = ?, extraInfo = ? WHERE id = ?;";

        try(Connection conn = dbc.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, techDoc.getSetupDescription());
            stmt.setString(2, techDoc.getDeviceLoginInfo());
            stmt.setString(3, techDoc.getExtraInfo());
            stmt.setInt(4,techDoc.getId());

            stmt.executeUpdate();
        } catch (SQLException e){
            throw new SQLException(e);
        }
    }

    public List<Pictures> getTechPictures(TechDoc techDoc) throws SQLException {
        List<Pictures> picturesList = new ArrayList<>();

        String sql = "SELECT * FROM Pictures WHERE techDocID = ?;";

        try (Connection conn = dbc.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, techDoc.getId());
            System.out.println(techDoc.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String filepath = rs.getString("filepath");
                String pictureDescription = rs.getString("pictureDescription");

                Pictures picture = new Pictures(id, filepath);
                picture.setDescription(pictureDescription);

                picturesList.add(picture);
            }

            return picturesList;

        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    public Pictures addTechPictures(Pictures pictures, TechDoc techDoc) throws SQLException {
        String sql = "INSERT INTO Pictures (filepath, pictureDescription, techDocID) VALUES (?,?,?);";

        try(Connection connection = dbc.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            System.out.println("from DAO");
            System.out.println(sql);
            System.out.println(pictures.getFilePath());
            System.out.println(pictures.getDescription());
            preparedStatement.setString(1, pictures.getFilePath());
            preparedStatement.setString(2, pictures.getDescription());
            preparedStatement.setInt(3, techDoc.getId());

            preparedStatement.executeUpdate();

            int id = 0;
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }

            pictures.setId(id);

            return pictures;

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }


    public void deletePicture(Pictures pictures) throws SQLException {
        try (Connection conn = dbc.getConnection()) {

            String sql = "DELETE FROM Pictures WHERE id = ?;";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, pictures.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
