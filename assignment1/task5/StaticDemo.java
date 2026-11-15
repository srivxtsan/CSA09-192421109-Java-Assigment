import java.util.ArrayList;
import java.util.List;

/** Task 5 - static counter, static block, static method; object life cycle; instance-counter bug. */
class Ticket {
    private static int counter;               // shared by ALL tickets of ALL facilities
    private static int activeTickets;
    static { counter = 1000; System.out.println("[static block] counter initialised to 1000"); }
    private final int no;
    Ticket() { no = ++counter; activeTickets++; }
    int getNo() { return no; }
    static int getActiveTickets() { return activeTickets; }
    static void closed() { activeTickets--; }
}

/** WRONG: instance counter - two facilities both start at 1000 -> duplicate ticket numbers. */
class BadFacility {
    private int counter = 1000;
    int issue() { return ++counter; }
}

class Facility {
    private final String name;
    private final List<Ticket> active = new ArrayList<>();
    Facility(String name) { this.name = name; }
    Ticket enter() { Ticket t = new Ticket(); active.add(t); return t; }   // object returned
    void exit(Ticket t) {                                                  // reference passed
        active.remove(t);
        Ticket.closed();     // after this, t becomes unreachable once caller drops it -> GC-eligible
    }
}

public class StaticDemo {
    public static void main(String[] args) {
        Facility north = new Facility("North"), south = new Facility("South");
        Ticket a = north.enter(), b = south.enter(), c = north.enter();
        System.out.println("Tickets: " + a.getNo() + ", " + b.getNo() + ", " + c.getNo());
        System.out.println("Active : " + Ticket.getActiveTickets());
        north.exit(a);
        a = null;                                   // no reference left -> eligible for GC
        System.out.println("Active after exit: " + Ticket.getActiveTickets());

        BadFacility n = new BadFacility(), s = new BadFacility();
        System.out.println("Instance counter bug -> north " + n.issue() + ", south " + s.issue()
                + "  (DUPLICATE!)");
    }
}
