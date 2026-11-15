# Task 12 – Re-evaluate and Adapt the Design (Decision: **MODIFY**, not redesign)
Source: `src/parking/...` (copy of Task 11 + changes). Compile & run:
```
javac -d out $(find src -name '*.java')
java -cp out parking.app.TestRunner12     # 25 tests -> tests_output.txt
```
## A. Impact analysis
**What changed:** (1) new vehicle categories – e-bike (flat Rs 20) and delivery van (2 × car rate for hours starting 09:00–17:00); (2) visitors may buy a one-day pass; (3) facility grows from 3 to 5 floors with *different* slot counts.
| Area | Affected? |
|---|---|
| `Vehicle`, `TwoWheeler`, `Car`, `ElectricCar`, `Chargeable`, `UserCategory` | **No** – not touched |
| Fee computation | Van fee depends on **time of day**, but the original already had the overload `calculateFee(LocalDateTime in, LocalDateTime out)`, which `exit()` already called → the van just overrides it |
| `ParkingTicket` | modified: `boolean monthlyPass` is too weak (cannot express "valid for one day only") → `Pass` object |
| `ParkingFacility.enter()` | modified: takes a `Pass` instead of a `boolean` |
| `VehicleFactory` | modified: two new `case` lines |
| Floors/slots | Original already uses a **jagged array** `String[][]`, so `{20, 30, 40, 50, 60}` works with no code change. A rectangular `new String[5][S]` or a 1-D formula `f × S + s` would break because it assumes equal floor sizes (wrong indices / wasted or missing slots). |
**Modified rather than extended:** `ParkingTicket`, `ParkingFacility`, `VehicleFactory` (3 classes).

## B. Evaluation of the original design
- New vehicle types are added by **writing new subclasses** (`EBike`, `DeliveryVan`) – no existing vehicle class edited. ✔
- Pricing is **spread across classes** (each subclass owns its rate/rule). That is good for per-type rules (flat fee, peak hours) but means a *global* rule such as "festival discount" would touch every class. This is the main weakness; a `PricingRule` abstraction would fix it if such rules appear.
- Existing tested classes: 6 of 9 carried-over classes are byte-identical (verified with `cmp`), and the 4 regression tests still pass. ✔
- Weak spot found: the pass was a boolean → modified.

## C. Revised design
Decision: **modify** (small, targeted changes); redesigning everything was unnecessary because the abstractions (abstract `Vehicle`, polymorphic fee, jagged array, HashMap of tickets) already absorb the change.
```
interface Pass { boolean covers(UserCategory, LocalDate) }      NEW
  ├─ MonthlyPass(from,to)   STUDENT/STAFF within date range      NEW
  └─ OneDayPass(day)        VISITOR on that exact day            NEW
Vehicle (unchanged) ◄─ EBike (flat Rs 20)                        NEW
                    ◄─ DeliveryVan (peak ×2, overrides calculateFee(in,out))  NEW
ParkingTicket: Pass pass instead of boolean        MODIFIED
ParkingFacility.enter(Vehicle, UserCategory, Pass, time)   MODIFIED
VehicleFactory: + "EBIKE", "VAN"                   MODIFIED
```
Rule choices (documented assumptions): the van's rate is decided by the **start time of each started hour** (08:30–10:00 = 08:30 off-peak + 09:30 peak = 30 + 60 = 90); 17:00 is off-peak; the van has no daily maximum; a pass waives the *parking* fee only (EV charging is still charged).

## D. Evidence-based defence
| Evidence | Result |
|---|---|
| Existing classes changed | **3** (ParkingTicket, ParkingFacility, VehicleFactory); **6 unchanged** (Vehicle, TwoWheeler, Car, ElectricCar, Chargeable, UserCategory) |
| New classes | 5 (EBike, DeliveryVan, Pass, MonthlyPass, OneDayPass) |
| Adding a further vehicle type later | 1 new subclass + 1 factory line (the factory `switch` is the only central place; a registry map would make it 0 edits) |
| Tests | `tests_output.txt`: 25/25 pass – 4 regression, 3 e-bike, 6 van incl. 17:00 and 09:00 boundaries, 6 pass cases, 3 factory, 2 jagged-floor, 1 mixed-day (hand total 20 + 90 + 0 + 120 = 230 → program 230.0) |
| Trade-offs | Per-subclass pricing keeps types independent but a global rule touches all subclasses; `Pass` interface adds 3 types but removes the boolean's limits; `instanceof Chargeable` in `exit()` remains. |
**Conclusion:** the original object-oriented design absorbed the new requirements with small, localised changes, so MODIFY is better than RETAIN (the pass could not be expressed) and better than REDESIGN (it would throw away 6 tested classes).
