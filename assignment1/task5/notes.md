# Task 5 – static Members and the Object Life Cycle
Code: `StaticDemo.java` (output in `output.txt`).

## 1. Where `static` is needed
- **`Ticket.counter`** – one counter shared by all tickets. An instance variable would restart at 1000 in every object and give duplicate ticket numbers (the demo prints `1001` for both facilities in `BadFacility`).
- **`activeTickets` / total occupancy** – a property of the class as a whole, not of one ticket.
- **Static method** `Ticket.getActiveTickets()`, `VehicleFactory.create()` – need no object state (utility/factory methods).
- **Static block** – runs once when the class loads; used to initialise the counter (e.g. read last used number from a file). A static method cannot use `this` or instance fields.
- `static final` constants: `MAX_BILLABLE_HOURS_PER_DAY`, `RATE_PER_KWH`.

## 2. Life cycle of a `ParkingTicket`
Created at entry (`new`) → stored in the `active` map → on exit removed from the map and slot released → once no reference remains (local variable out of scope or set to `null`) it is **eligible** for garbage collection; the JVM frees it at an unspecified later time (`finalize` is not used). Ticket **numbers** stay unique because the counter is never decreased, even though the object is gone.

## 3. Passing and returning objects
Java passes **references by value**: `facility.exit(t)` hands over a copy of the reference – the method can change the object's state but cannot make the caller's variable point elsewhere. `enter()` returns the new `ParkingTicket` reference to the caller. Primitives (`int minutes`) are copied.

## 4. Alternative concept – counter as an instance variable
Two facilities (north, south) each with `private int counter = 1000` both issue ticket 1001 → duplicates across the campus.
**Solutions:**
1. Keep one `static` counter shared by all facilities (simplest; used here).
2. Pass a shared `TicketNumberGenerator` object into each facility (better testability, no global state, can be reset in tests).
3. Include the facility code in the number (`N-1001`, `S-1001`) with per-facility instance counters.
| | Space |
|---|---|
| static counter | O(1) – one int for all k facilities and n tickets (+ n × 4 B for each ticket's own `ticketNo`) |
| instance counter | O(k) – one int per facility; same O(n) ticket numbers |
Time is O(1) per ticket in both. (A static counter is not thread-safe; use `AtomicInteger` if gates run in parallel.)
