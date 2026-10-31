 
package za.ac.cput.connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;


/**
 *
 * @author matse
 */
public class DBConnection {
    public static Connection derbyConnection() throws SQLException
    {
        String DATABASE_URL = "jdbc:derby://localhost:1527/ProjectDB";
        String username = System.getenv("PARKING_DB_USERNAME");
        String password = System.getenv("PARKING_DB_PASSWORD");
        if (username == null || password == null) {
            throw new SQLException("PARKING_DB_USERNAME and PARKING_DB_PASSWORD must be set");
        }

        Connection connection = DriverManager.getConnection(DATABASE_URL, username, password);
        return connection;
    }    
}
