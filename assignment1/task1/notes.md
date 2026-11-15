# Task 1 – Understand the Problem and Identify the Objects

## 1. Inputs and outputs
| Inputs | Outputs |
|---|---|
| Facility layout (floors × slots per floor) | Allocated slot `{floor, slot}` |
| Entry request: vehicle type, registration no., user category (student/staff/visitor), pass flag, entry time | Parking ticket with unique ticket number and entry time |
| Exit request: registration no., exit time, kWh charged (EV) | Released slot, parking fee, charging fee, daily collection |
| | Errors: "parking full", "duplicate registration", "unknown vehicle type", "invalid slot" |

## 2. Nouns → candidate classes, verbs → methods
| Noun (class) | Why it is needed | Responsibility | Verbs (methods) |
|---|---|---|---|
| `Vehicle` (abstract) + `TwoWheeler`, `Car`, `ElectricCar` | Vehicle types differ in rate; common data (regNo) is shared | Hold registration no.; know its hourly rate; compute its own fee | `ratePerHour()`, `calculateFee()` |
| `Chargeable` (interface) | Only EVs can charge – a capability, not a vehicle kind | Price of charging | `chargingFee(kWh)` |
| `UserCategory` (enum) | Student/Staff/Visitor is a fixed set of values | Type-safe category | – |
| `ParkingTicket` | Proof of entry; links vehicle, slot, time | Unique ticket no., entry time, pass status | `getTicketNo()`, `isFree()` |
| `ParkingFacility` | Owns floors/slots and the active tickets | Allocate/release slots, issue tickets, process exit, total collection | `allocate()`, `release()`, `enter()`, `exit()` |
| `VehicleFactory` | Converts input text ("CAR") into an object; rejects unknown types | Object creation | `create(type, regNo)` |

A `Slot` is *not* a class in the base design: a slot has only one piece of state (which vehicle is in it), so a `String[][]` is enough (YAGNI). It becomes a class in Task 10 where slots need validation.

## 3. Business rules
1. A slot cannot hold two vehicles.
2. A ticket number is unique and never reused (static counter only increases).
3. A registration number can have only one active ticket (duplicate entry rejected).
4. A vehicle can be parked only if a free slot exists; otherwise "parking full".
5. Fee = every started hour charged, with a daily maximum; 0 minutes = no fee.
6. Students/staff with a valid monthly pass pay no parking fee; visitors cannot hold a monthly pass.
7. EV charging is charged separately (also for pass holders).
8. Exit time cannot be before entry time; an unknown ticket cannot be exited; a slot can only be released if occupied.

## 4. Assumptions
- Slots are interchangeable (first free slot is acceptable); no reserved EV-only slots.
- Slot numbers start at 0 on each floor; floors start at 0.
- Rates: two-wheeler Rs 10/h, car Rs 30/h, EV = car rate + Rs 12/kWh; daily max = 8 hours' charge per 24 h.
- Time is supplied by the caller (makes testing deterministic).
- Single-threaded, one entry gate (concurrency not handled).

## 5. Alternative concept – parallel arrays
```java
String[] regNo = new String[N]; String[] type = new String[N]; long[] entryTime = new long[N];
int count = 0;
int find(String r) { for (int i = 0; i < count; i++) if (regNo[i].equals(r)) return i; return -1; }
// adding an attribute (e.g. owner): create String[] owner AND edit every add/remove/shift/copy routine
```
| | Object design (`Vehicle[]`) | Parallel arrays |
|---|---|---|
| Find by regNo | O(n) linear scan (O(1) with a `HashMap<String,Vehicle>`) | O(n) linear scan; no way to index without extra structures |
| Add new attribute | Add one field to one class; other code untouched | New array + change every place that keeps arrays in step (insert/delete/sort) – easy to desynchronise |
| Space per vehicle | one object header (~12–16 B) + fields + 1 reference | no header, but 3+ separate arrays; type stored as a `String` reference (8 B) instead of a class |
| Behaviour (fee) | Lives with the data (polymorphism) | Separate `if/switch` on `type[i]` |
Both are O(n) for a plain scan and O(n) total space (O(1) per vehicle); the object design costs a small constant (object header) more but gains safety and extensibility.
