
package za.ac.cput.domain;

/**
 *
 * @author matse
 */
import java.io.Serializable;


public class ParkingTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    private String plateNumber;
    private String driverName;
    private int hoursParked;
    private double amountDue;

    public ParkingTicket(String plateNumber, String driverName, int hoursParked, double amountDue) {
        this.plateNumber = plateNumber;
        this.driverName = driverName;
        this.hoursParked = hoursParked;
        this.amountDue = amountDue;
    }

    public String getPlateNumber() { return plateNumber; }
    public String getDriverName()  { return driverName; }
    public int getHoursParked()    { return hoursParked; }
    public double getAmountDue()   { return amountDue; }

    @Override
    public String toString() {
        return String.format("%-10s %-20s %3d hrs   R%9.2f",
                plateNumber, driverName, hoursParked, amountDue);
    }
}
