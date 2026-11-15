# Task 11 – Implement, Test and Defend the Design
Source: `src/parking/{model,service,app}`. Compile & run:
```
javac -d out $(find src -name '*.java')
java -cp out parking.app.TestRunner       # 23 tests  -> tests_output.txt
java -cp out parking.app.Main < sample_input.txt   # console app -> sample_output.txt
java -cp out parking.app.Benchmark        # alternative-structure timing -> benchmark_output.txt
```
## 1. Test table (actual results from `tests_output.txt` – all 23 PASS)
| # | Category | Test | Expected | Actual |
|---|---|---|---|---|
| 1 | Normal | TwoWheeler 130 min | 30.0 | 30.0 |
| 2 | Normal | Car 130 min | 90.0 | 90.0 |
| 3 | Normal | EV 130 min + 10 kWh | 210.0 | 210.0 |
| 4 | Boundary | zero minutes | 0.0 | 0.0 |
| 5 | Boundary | exactly 60 min | 30.0 | 30.0 |
| 6 | Boundary | 61 min | 60.0 | 60.0 |
| 7 | Boundary | 700 min (daily max) | 240.0 | 240.0 |
| 8 | Boundary | 1440 min | 240.0 | 240.0 |
| 9 | Boundary | 1500 min | 270.0 | 270.0 |
| 10 | Boundary | last free slot taken, next entry | IllegalStateException | IllegalStateException |
| 11 | Boundary | slot reused after exit | [0, 0] | [0, 0] |
| 12 | Invalid | unknown vehicle type | IllegalArgumentException | IllegalArgumentException |
| 13 | Invalid | duplicate registration | IllegalStateException | IllegalStateException |
| 14 | Invalid | release(5,0) | false | false |
| 15 | Invalid | release(0,-1) | false | false |
| 16 | Invalid | visitor with monthly pass | IllegalArgumentException | IllegalArgumentException |
| 17 | Invalid | exit before entry | IllegalArgumentException | IllegalArgumentException |
| 18 | Invalid | exit of unknown vehicle | IllegalStateException | IllegalStateException |
| 19 | Invalid | negative minutes | IllegalArgumentException | IllegalArgumentException |
| 20 | Design | Car equals EV with same regNo | true | true |
| 21–23 | Scenario | day total / totalCollection / all slots free | 520.0 / 520.0 / 0 | 520.0 / 520.0 / 0 |

## 2. Hand-calculated day (all entries 09:00, rates 10 / 30 / 30 + 12 per kWh, max 8 h/day)
| Vehicle | Type / user | Exit | Minutes | Hours | Calculation | Fee |
|---|---|---|---|---|---|---|
| D1 | two-wheeler, visitor | 09:45 | 45 | 1 | 1 × 10 | 10 |
| D2 | car, visitor | 11:10 | 130 | 3 | 3 × 30 | 90 |
| D3 | EV, visitor, 10 kWh | 10:10 | 70 | 2 | 2 × 30 + 10 × 12 | 180 |
| D4 | car, student with pass | 14:00 | 300 | – | free | 0 |
| D5 | car, visitor | 20:40 | 700 | 12 → capped 8 | 8 × 30 | 240 |
| | | | | | **Total** | **520** |
Program output: `520.0`.

## 3. Evaluation of the design
- **Encapsulation** – all fields private; invariants (non-blank regNo, no negative minutes, unique tickets) enforced in constructors/methods.
- **Inheritance** – `Car → ElectricCar` reuses the fee logic; only the differing bit is added.
- **Polymorphism** – `exit()` calls `vehicle.calculateFee(...)` without checking the type; the test run proves each type prices itself.
- **Abstraction** – `Vehicle` abstract, `Chargeable` interface.
- **Evidence:** adding a vehicle type required only a new subclass + one `case` in `VehicleFactory` (shown in Task 12).
- **Limitation:** `exit()` uses `instanceof Chargeable` (a capability check, acceptable but a form of type test), the pass is a boolean (cannot expire or be day-specific – fixed in Task 12), slots are `String` placeholders (no slot types such as EV-only), and the facility is not thread-safe.

## 4. Alternative concept – ArrayList / HashMap for active tickets
| Structure | Exit-processing logic | Time to find ticket | Space for n tickets |
|---|---|---|---|
| array `ParkingTicket[]` | scan comparing regNo, then null the element | O(n) | n references (fixed size) |
| `ArrayList<ParkingTicket>` | scan, `remove(i)` also shifts → O(n) | O(n) | n refs + spare capacity (≤ 1.5 n) |
| `HashMap<String,ParkingTicket>` (used) | `map.remove(regNo)` | O(1) average | n entries (~32+ B each) + table (load 0.75) → still O(n), constant ≈ 3–5× larger |
**Measured** (2000 random lookups, my machine, `benchmark_output.txt`; your numbers will differ but the trend will not):
| n | array (ns/lookup) | ArrayList | HashMap |
|---|---|---|---|
| 1 000 | 3 471 | 1 314 | 71 |
| 10 000 | 12 673 | 13 530 | 196 |
| 100 000 | 133 400 | 166 548 | 406 |
Linear structures grow ~10× when n grows 10×; the HashMap stays almost flat. For a 5×100 = 500-vehicle facility the difference is small in absolute time, but HashMap also makes the **duplicate-registration check** O(1). Costs: extra memory and no ordering (use `LinkedHashMap` if entry order matters).
