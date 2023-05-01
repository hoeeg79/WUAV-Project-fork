package DAL;

import BE.Customer;
import BE.TechDoc;
import BE.User;
import DAL.DatabaseConnector.DBConnector;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;

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

        String sql = "INSERT INTO TechDoc (setupname, setupDescription, deviceLoginInfo) VALUES (?,?,?);";

        try (Connection conn = dbc.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, title);
            stmt.setString(2, setupDescription);
            stmt.setString(3, deviceInfo);

            stmt.executeUpdate();

            int id = 0;

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

            TechDoc returnDoc = new TechDoc(id, title);
            returnDoc.setSetupDescription(setupDescription);
            returnDoc.setDeviceLoginInfo(deviceInfo);
            return returnDoc;

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void addTech(TechDoc techDoc, User user) throws SQLException {
        String sql = "INSERT INTO TechDoc VALUES(?,?);";

        try(Connection conn = dbc.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, user.getId());
            stmt.setInt(2, techDoc.getId());

            stmt.executeUpdate();
        } catch (SQLException e){
            throw new SQLException(e);
        }
    }
}
