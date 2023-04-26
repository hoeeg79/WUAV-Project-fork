package DAL.DatabaseConnector;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;

public class DBConnector {

    private static final String PROP_FILE = "config/database.settings";
    private SQLServerDataSource ds;

    /**
     * Constructor of the DBConnector class, used to declare the database information.
     */
    public DBConnector() throws Exception{
        Properties databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream(new File(PROP_FILE)));

        String server = databaseProperties.getProperty("Server");
        String database = databaseProperties.getProperty("Database");
        String user = databaseProperties.getProperty("User");
        String password = databaseProperties.getProperty("Password");

        ds = new SQLServerDataSource();
        ds.setServerName(server);
        ds.setDatabaseName(database);
        ds.setUser(user);
        ds.setPassword(password);
        ds.setTrustServerCertificate(true);
    }

    /**
     * Method used to get connection to the database.
     */
    public Connection getConnection() throws SQLServerException {
        return ds.getConnection();
    }
}
