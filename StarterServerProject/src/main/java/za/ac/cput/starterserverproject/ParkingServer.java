
package za.ac.cput.starterserverproject;

/**
 *
 * @author matse
 */
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import za.ac.cput.dao.TicketDAO;
import za.ac.cput.domain.ParkingTicket;
import za.ac.cput.domain.Protocol;


public class ParkingServer {
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    ServerSocket serverSocket;
    Socket client;
    private TicketDAO dao;



public ParkingServer(){

    try {
        serverSocket = new ServerSocket(Protocol.PORT);
        System.out.println("Server is listening on port " + Protocol.PORT);

        
        client = serverSocket.accept();
        System.out.println("Client connection has been accepted");

        
        dao = new TicketDAO();
    }
    catch (IOException ioException) {
        System.out.println("IO Exception: " + ioException.getMessage());
    }

}

public void getStreams(){
    
    try {
        out = new ObjectOutputStream(client.getOutputStream());
        out.flush();
        in = new ObjectInputStream(client.getInputStream());
        System.out.println("Streams created");
    }
    catch (IOException ioException) {
        System.out.println("IO Exception: " + ioException.getMessage());
    }

}


    public void processClient() {
        boolean exit = false;
        try {
            while (!exit) {
                
                String command = (String) in.readObject();
                System.out.println("From CLIENT>> " + command);

                if (command.equals(Protocol.ADD)) {
                    
                    ParkingTicket ticket = (ParkingTicket) in.readObject();
                    String reply;
                    try {
                        dao.insertTicket(ticket);
                        reply = Protocol.OK;
                    }
                    catch (SQLException sqlException) {
                        if ("23505".equals(sqlException.getSQLState())) {
                            reply = Protocol.ERROR + "Duplicate plate number";
                        } else {
                            reply = Protocol.ERROR + sqlException.getMessage();
                        }
                    }
                    System.out.println("To CLIENT>> " + reply);
                    out.writeObject(reply);
                    out.flush();
                }
                else if (command.equals(Protocol.RETRIEVE)) {
                    
                    ArrayList<ParkingTicket> tickets = new ArrayList<>();
                    try {
                        tickets.addAll(dao.getAllTickets());
                    }
                    catch (SQLException sqlException) {
                        System.out.println("SQL Exception: " + sqlException.getMessage());
                    }
                    System.out.println("To CLIENT>> list of " + tickets.size() + " tickets");
                    out.writeObject(tickets);
                    out.flush();
                }
                else if (command.equals(Protocol.EXIT)) {
                    
                    exit = true;
                }
            }
        }
        catch (EOFException eofException) {
            
            System.out.println("Client disconnected");
        }
        catch (IOException ioException) {
            System.out.println("IO Exception: " + ioException.getMessage());
        }
        catch (ClassNotFoundException classNotFoundException) {
            System.out.println("Class not found: " + classNotFoundException.getMessage());
        }
    }


    private void closeConnection() {
        try {
            if (dao != null)
               dao.closeConnection();
            if (out != null)
                out.close();
            if (in != null)
                in.close();
            if (client != null)
                client.close();
            if (serverSocket != null)
                serverSocket.close();
            System.out.println("All connections closed");
        }
        catch (IOException ioException) {
            System.out.println("IO Exception: " + ioException.getMessage());
        }
        System.exit(0);
    }




    public static void main(String[] args) {
        ParkingServer server = new ParkingServer();
        server.getStreams();
        server.processClient();
        server.closeConnection();
    }

}
