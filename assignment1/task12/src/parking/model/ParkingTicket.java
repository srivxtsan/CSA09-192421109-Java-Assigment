package parking.model;

import java.time.LocalDateTime;

public class ParkingTicket {
    private static int counter = 1000;          // shared, never reused
    private final int ticketNo;
    private final Vehicle vehicle;
    private final UserCategory category;
    private final Pass pass;          // CHANGED: was boolean monthlyPass
    private final int floor, slot;
    private final LocalDateTime entry;

    public ParkingTicket(Vehicle v, UserCategory c, Pass pass, int floor, int slot, LocalDateTime entry) {
        if (v == null || c == null || entry == null) throw new IllegalArgumentException("null argument");
        if (pass != null && !pass.covers(c, entry.toLocalDate())) throw new IllegalArgumentException("Pass is not valid for this user/date");
        this.ticketNo = ++counter;
        this.vehicle = v; this.category = c; this.pass = pass;
        this.floor = floor; this.slot = slot; this.entry = entry;
    }
    public ParkingTicket(Vehicle v, int floor, int slot, LocalDateTime entry) {   // chaining: visitor, no pass
        this(v, UserCategory.VISITOR, null, floor, slot, entry);
    }
    public ParkingTicket(ParkingTicket t) {                                       // copy: duplicate ticket, same number
        this.ticketNo = t.ticketNo; this.vehicle = t.vehicle; this.category = t.category;
        this.pass = t.pass; this.floor = t.floor; this.slot = t.slot; this.entry = t.entry;
    }
    public int getTicketNo() { return ticketNo; }
    public Vehicle getVehicle() { return vehicle; }
    public UserCategory getCategory() { return category; }
    public boolean isFree() { return pass != null && pass.covers(category, entry.toLocalDate()); }
    public int getFloor() { return floor; }
    public int getSlot() { return slot; }
    public LocalDateTime getEntry() { return entry; }
    @Override public String toString() { return "Ticket#" + ticketNo + " " + vehicle + " slot[" + floor + "," + slot + "] " + entry; }
}
