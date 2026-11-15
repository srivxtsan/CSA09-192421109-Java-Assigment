# Task 10 – Packages and Encapsulation
Code: `src/parking/...` (compile: `javac -d out $(find src -name '*.java')`, run: `java -cp out parking.app.Main`; output in `output.txt`).

## 1. Package structure
```
parking.model    – Vehicle, TwoWheeler, Car, ElectricCar, Chargeable, ParkingTicket, ParkingSlot  (data + rules of one object)
parking.service  – ParkingFacility, VehicleFactory, SlotService                                  (use-cases, coordinate objects)
parking.app      – Main, TestRunner                                                              (user interface / entry point)
```
Dependencies point one way: app → service → model. Model never depends on the UI.

## 2. Access modifiers
| Member | Modifier | Why |
|---|---|---|
| fields (`regNo`, `slots`, `counter`) | `private` | only the class may change its state |
| `getRegNo()`, `calculateFee()`, constructors used by other packages | `public` | the API |
| `ratePerHour()` in Vehicle | `protected` | subclasses override it; callers outside the hierarchy don't need it |
| `ParkingSlot.assign()` | `protected` | extension point for subclasses/same package |
| `ParkingSlot.clear()` | default (package-private) | only classes in `parking.model` may free a slot (see `SlotAssigner`) |
Commented lines in `Main` show the compile errors if `vehicleNo` or `clear()` are accessed from `parking.app`.

## 3. Validating getters/setters
`setMinutesParked(int)` rejects negatives; `ParkingSlot` constructor rejects negative floor/slot; `SlotService.get` rejects out-of-range floor/slot; `Vehicle` rejects blank registration numbers. Getters expose values (never the internal arrays) so state can't be altered from outside.

## 4. Wrapper classes and autoboxing
`Integer.parseInt("45")` converts input text; `Integer`/`Double` are needed in collections (`Map<String,Integer>`). Risk: `==` on two `Integer` objects compares references – values −128…127 are cached (`127 == 127` → true) but `1000 == 1000` → **false**; use `equals()`. Other risks: unboxing a `null` throws `NullPointerException`; boxing in a loop creates garbage.

## 5. Alternative concept – public fields, validation by callers
```java
slot.minutesParked = -5;           // nothing stops it
```
What can go wrong: any code can put the object in an invalid state; a rule change (e.g. max 24 h) must be edited in every caller; some callers forget the check → corrupt fee; fields can't be made read-only or changed without breaking all users.
| | Validating setter | Public field + caller checks |
|---|---|---|
| Cost per update | O(1) one comparison | O(1) per check, but done at every call site |
| Places where the check is repeated | 1 (in the setter) | c = number of calling places (grows with the code base) |

Encapsulation costs one method call (inlined by the JIT), so there is essentially no performance penalty.
