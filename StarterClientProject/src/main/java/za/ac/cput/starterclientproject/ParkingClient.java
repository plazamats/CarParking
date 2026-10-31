
package za.ac.cput.starterclientproject;

/**
 *
 * @author matse
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import za.ac.cput.domain.ParkingTicket;
import za.ac.cput.domain.Protocol;


public class ParkingClient {

    private final String host;
    private final int port;

    
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ParkingClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    
    public void connect() throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());
    }




    public String addTicket(ParkingTicket ticket) throws IOException, ClassNotFoundException {
        out.writeObject(Protocol.ADD);
        out.writeObject(ticket);
        out.flush();
        return (String) in.readObject();
    }




    @SuppressWarnings("unchecked")
    public List<ParkingTicket> retrieveTickets() throws IOException, ClassNotFoundException {
        out.writeObject(Protocol.RETRIEVE);
        out.flush();
        return (List<ParkingTicket>) in.readObject();
    }


    public void disconnect() {
        try {
            
            if (out != null) {
                out.writeObject(Protocol.EXIT);
                out.flush();
                out.close();
            }
            if (in != null)
                in.close();
            if (socket != null)
                socket.close();
        }
        catch (IOException ioException) {
            System.out.println("IO Exception: " + ioException.getMessage());
        }
    }

}
