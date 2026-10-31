
package za.ac.cput.gui;

/**
 *
 * @author matse
 */
import java.awt.*;
import java.util.List;
import javax.swing.*;
import za.ac.cput.domain.ParkingTicket;
import za.ac.cput.domain.Protocol;
import za.ac.cput.starterclientproject.ParkingClient;


public class ParkingClientGui extends JFrame {

    private final JTextField txtPlate  = new JTextField(15);
    private final JTextField txtDriver = new JTextField(15);
    private final JTextField txtHours  = new JTextField(15);
    private final JTextField txtAmount = new JTextField(15);
    private final JTextArea  txtOutput = new JTextArea(12, 50);

    private final ParkingClient client = new ParkingClient("localhost", Protocol.PORT);

    public ParkingClientGui() {
        super("Parking Ticket Management");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(4, 2, 4, 4));
        form.add(new JLabel("Plate number:")); form.add(txtPlate);
        form.add(new JLabel("Driver name:"));  form.add(txtDriver);
        form.add(new JLabel("Hours parked:")); form.add(txtHours);
        form.add(new JLabel("Amount due (R):")); form.add(txtAmount);

        JButton btnAdd = new JButton("Add Ticket");
        JButton btnRetrieve = new JButton("Retrieve Tickets");
        JButton btnExit = new JButton("Exit");
        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(btnAdd); buttons.add(btnRetrieve); buttons.add(btnExit);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.NORTH);
        top.add(buttons, BorderLayout.SOUTH);

        txtOutput.setEditable(false);
        txtOutput.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(txtOutput), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addTicket());
        btnRetrieve.addActionListener(e -> retrieveTickets());
        btnExit.addActionListener(e -> exit());
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) { exit(); }
        });

        pack();
        setLocationRelativeTo(null);

        try {
            client.connect();
            txtOutput.setText("Connected to server.\n");
        } catch (Exception ex) {
            txtOutput.setText("Could not connect to server: " + ex.getMessage() + "\n");
        }
    }

    private void addTicket() {
        try {
            ParkingTicket t = new ParkingTicket(
                    txtPlate.getText().trim(),
                    txtDriver.getText().trim(),
                    Integer.parseInt(txtHours.getText().trim()),
                    Double.parseDouble(txtAmount.getText().trim()));
            String reply = client.addTicket(t);
            txtOutput.setText("Server reply: " + reply + "\n");
        } catch (NumberFormatException ex) {
            txtOutput.setText("Hours must be a whole number and amount must be a number.\n");
        } catch (Exception ex) {
            txtOutput.setText("Error: " + ex + "\n");
        }
    }

    private void retrieveTickets() {
        try {
            List<ParkingTicket> list = client.retrieveTickets();
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-10s %-20s %-8s %s%n", "Plate", "Driver", "Hours", "Amount"));
            for (ParkingTicket t : list) sb.append(t).append('\n');
            txtOutput.setText(sb.toString());
        } catch (Exception ex) {
            txtOutput.setText("Error: " + ex + "\n");
        }
    }

    private void exit() {
        client.disconnect();
        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ParkingClientGui().setVisible(true));
    }
}
