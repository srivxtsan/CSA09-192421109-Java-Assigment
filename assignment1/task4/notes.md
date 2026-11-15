# Task 4 – Design Classes, Objects and Constructors
Code: `TicketDemo.java` (output in `output.txt`). The full classes are in Task 11.

## 1. Fields and methods
| Class | Fields | Methods |
|---|---|---|
| `Vehicle` | `regNo` | `getRegNo`, `ratePerHour` (abstract), `calculateFee` |
| `ParkingTicket` | static `counter`, `ticketNo`, `vehicle`, `category`, `monthlyPass`, `floor`, `slot`, `entry` | getters, `isFree`, `toString` |
| `ParkingFacility` | `String[][] slots`, `Map<String,ParkingTicket> active`, `totalCollection` | `allocate`, `release`, `enter`, `exit` |

## 2. Constructors
- **Default** `Vehicle()` – creates a placeholder with defaults (used in the demo; the real hierarchy has none because a vehicle without a registration number is invalid).
- **Parameterised** `Vehicle(regNo, type)` – the one constructor that validates and assigns.
- **Overloaded** `Vehicle(regNo)`, `ParkingTicket(v, entry)` – convenient shorter forms with defaults.
- **Constructor chaining with `this(...)`** – all overloaded constructors delegate to the single full constructor, so validation exists in exactly one place.
- **`this`** also disambiguates `this.regNo = regNo`.
- **Copy constructor** `ParkingTicket(ParkingTicket t)` – needed to re-issue a duplicate ticket: it copies the same ticket number (the `counter` is NOT incremented, so uniqueness of *distinct* tickets is kept). `Vehicle(Vehicle)` makes a defensive copy so later changes to the original don't affect the ticket.

## 3. Memory
`ParkingTicket t = new ParkingTicket(v);` – the reference variable `t` lives on the **stack** (in the method's frame); the object lives on the **heap**; `static counter` lives in the class's static area (metaspace). `t2 = t` copies the reference, not the object, so both point to the same heap object; the copy constructor creates a second heap object (`t1 == dup` is `false`).

## 4. Alternative concept – default constructor + setters, or static factory
```java
ParkingTicket t = new ParkingTicket(); t.setVehicle(v); t.setEntry(time);   // may be forgotten -> half-built object
static ParkingTicket ParkingTicket.issue(Vehicle v, LocalDateTime e) { ... validate ...; return new ParkingTicket(v, e); }
```
| | Chained constructors | Default + setters | Static factory |
|---|---|---|---|
| Valid object guaranteed? | Yes – cannot be created without valid data; fields can be `final` | No – object exists in an invalid state until all setters run; cannot use `final` | Yes – private constructor + validation in the factory; can return a cached/subclass object, have descriptive names |
| Time to create n tickets | O(n) | O(n) (n × k setter calls, still O(1) each) | O(n) |
| Space | O(n) objects | O(n) | O(n) (same object size; factory adds no per-object cost) |
All three are O(n) time and O(n) space; the difference is **correctness**, not speed. Constructors/factories are preferred; setters only for fields that really change.
