
package za.ac.cput.dao;

/**
 *
 * @author matse
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import za.ac.cput.connection.DBConnection;
import za.ac.cput.domain.ParkingTicket;



public class TicketDAO {

    private Connection conn;

    
    public TicketDAO() {
       try {
            conn = DBConnection.derbyConnection();
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE TABLE PARKING_TICKET ("
                        + "PLATE_NUMBER VARCHAR(10) PRIMARY KEY, "
                        + "DRIVER_NAME VARCHAR(50) NOT NULL, "
                        + "HOURS_PARKED INT NOT NULL, "
                        + "AMOUNT_DUE DECIMAL(8,2) NOT NULL)");
            } catch (SQLException e) {
                
                if (!"X0Y32".equals(e.getSQLState())) throw e;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection error: " + e.getMessage());
        }
    }
    
    public void insertTicket(ParkingTicket ticket) throws SQLException {
        if (conn == null) {
            throw new SQLException("No database connection");
        }

        
        String sql = "INSERT INTO PARKING_TICKET (PLATE_NUMBER, DRIVER_NAME, HOURS_PARKED, AMOUNT_DUE) "
                + "VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ticket.getPlateNumber());
            pstmt.setString(2, ticket.getDriverName());
            pstmt.setInt(3, ticket.getHoursParked());
            pstmt.setDouble(4, ticket.getAmountDue());
            pstmt.executeUpdate();
        } finally {
            
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    
    public List<ParkingTicket> getAllTickets() throws SQLException {
        List<ParkingTicket> tickets = new ArrayList<>();
        if (conn == null) {
            throw new SQLException("No database connection");
        }

        String sql = "SELECT PLATE_NUMBER, DRIVER_NAME, HOURS_PARKED, AMOUNT_DUE "
                + "FROM PARKING_TICKET ORDER BY PLATE_NUMBER";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                
                tickets.add(new ParkingTicket(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getDouble(4)));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return tickets;
    }

    
    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException sqlException) {
            System.out.println("SQL Exception: " + sqlException.getMessage());
        }
    }

}
