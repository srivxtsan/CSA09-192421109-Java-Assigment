package parking.model;

import java.time.LocalDateTime;

public class ParkingTicket {
    private static int counter = 1000;          // shared, never reused
    private final int ticketNo;
    private final Vehicle vehicle;
    private final UserCategory category;
    private final boolean monthlyPass;
    private final int floor, slot;
    private final LocalDateTime entry;

    public ParkingTicket(Vehicle v, UserCategory c, boolean monthlyPass, int floor, int slot, LocalDateTime entry) {
        if (v == null || c == null || entry == null) throw new IllegalArgumentException("null argument");
        if (monthlyPass && c == UserCategory.VISITOR) throw new IllegalArgumentException("Visitors cannot hold a monthly pass");
        this.ticketNo = ++counter;
        this.vehicle = v; this.category = c; this.monthlyPass = monthlyPass;
        this.floor = floor; this.slot = slot; this.entry = entry;
    }
    public ParkingTicket(Vehicle v, int floor, int slot, LocalDateTime entry) {   // chaining: visitor, no pass
        this(v, UserCategory.VISITOR, false, floor, slot, entry);
    }
    public ParkingTicket(ParkingTicket t) {                                       // copy: duplicate ticket, same number
        this.ticketNo = t.ticketNo; this.vehicle = t.vehicle; this.category = t.category;
        this.monthlyPass = t.monthlyPass; this.floor = t.floor; this.slot = t.slot; this.entry = t.entry;
    }
    public int getTicketNo() { return ticketNo; }
    public Vehicle getVehicle() { return vehicle; }
    public UserCategory getCategory() { return category; }
    public boolean isFree() { return monthlyPass; }
    public int getFloor() { return floor; }
    public int getSlot() { return slot; }
    public LocalDateTime getEntry() { return entry; }
    @Override public String toString() { return "Ticket#" + ticketNo + " " + vehicle + " slot[" + floor + "," + slot + "] " + entry; }
}
