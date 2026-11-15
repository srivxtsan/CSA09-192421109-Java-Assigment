import java.time.LocalDateTime;

/** Task 4 - constructors, this, constructor chaining, copy constructor. */
class Vehicle {
    private final String regNo;
    private final String type;
    Vehicle() { this("UNKNOWN", "CAR"); }                       // default -> chains
    Vehicle(String regNo) { this(regNo, "CAR"); }               // overloaded -> chains
    Vehicle(String regNo, String type) {                        // the ONE real constructor
        if (regNo == null || regNo.isBlank()) throw new IllegalArgumentException("regNo required");
        this.regNo = regNo.trim().toUpperCase();
        this.type = type;
    }
    Vehicle(Vehicle other) { this(other.regNo, other.type); }   // copy constructor
    String getRegNo() { return regNo; }
    String getType() { return type; }
}

class ParkingTicket {
    private static int counter = 1000;
    private final int ticketNo;
    private final Vehicle vehicle;
    private final LocalDateTime entry;

    ParkingTicket(Vehicle v) { this(v, LocalDateTime.now()); }  // chaining
    ParkingTicket(Vehicle v, LocalDateTime entry) {
        this.ticketNo = ++counter;                              // unique, never reused
        this.vehicle = new Vehicle(v);                          // defensive copy
        this.entry = entry;
    }
    /** Copy constructor: duplicate (re-issued) ticket keeps the SAME ticket number. */
    ParkingTicket(ParkingTicket t) {
        this.ticketNo = t.ticketNo;
        this.vehicle = new Vehicle(t.vehicle);
        this.entry = t.entry;
    }
    @Override public String toString() {
        return "Ticket#" + ticketNo + " " + vehicle.getRegNo() + " @ " + entry;
    }
}

public class TicketDemo {
    public static void main(String[] args) {
        Vehicle v = new Vehicle("tn01ab1234");
        ParkingTicket t1 = new ParkingTicket(v, LocalDateTime.of(2026, 11, 15, 9, 0));
        ParkingTicket t2 = new ParkingTicket(v, LocalDateTime.of(2026, 11, 15, 9, 5));
        ParkingTicket dup = new ParkingTicket(t1);              // re-issue duplicate
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(dup + "  (duplicate of t1)");
        System.out.println("t1 == dup ? " + (t1 == dup) + "  (different heap objects, same data)");
        try { new Vehicle(""); } catch (IllegalArgumentException e) { System.out.println("Rejected: " + e.getMessage()); }
    }
}
