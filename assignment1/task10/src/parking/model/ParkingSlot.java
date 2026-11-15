package parking.model;

public class ParkingSlot {
    private final int floor;                 // private: only this class touches the state
    private final int number;
    private String vehicleNo;                // null = free
    private int minutesParked;

    public ParkingSlot(int floor, int number) {
        if (floor < 0 || number < 0) throw new IllegalArgumentException("Invalid slot number " + floor + "/" + number);
        this.floor = floor;
        this.number = number;
    }

    public int getFloor() { return floor; }
    public int getNumber() { return number; }
    public String getVehicleNo() { return vehicleNo; }
    public boolean isFree() { return vehicleNo == null; }

    public void setMinutesParked(int minutes) {           // validating setter
        if (minutes < 0) throw new IllegalArgumentException("Duration cannot be negative: " + minutes);
        this.minutesParked = minutes;
    }
    public int getMinutesParked() { return minutesParked; }

    protected void assign(String regNo) {                 // protected: subclasses (e.g. an EV slot) may extend
        if (!isFree()) throw new IllegalStateException("Slot already occupied");
        this.vehicleNo = regNo;
    }
    void clear() { vehicleNo = null; minutesParked = 0; } // package-private: only parking.model classes

    @Override public String toString() {
        return "Slot[" + floor + "," + number + (isFree() ? ",free]" : "," + vehicleNo + "]");
    }
}
