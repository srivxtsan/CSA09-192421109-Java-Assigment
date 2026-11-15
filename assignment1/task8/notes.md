# Task 8 – Polymorphism
Code: `FeeDemo.java` (completed TODOs – output in `output.txt` equals the expected output) and `overloading/OverloadDemo.java`.

## 1. Compile-time polymorphism (overloading)
`calculateFee(int minutes)`, `calculateFee(int hours, int extraMinutes)`, `calculateFee(LocalTime in, LocalTime out)`. The **compiler** chooses by the number/types of the arguments (static type), e.g. two `LocalTime` arguments → the time version. It does not look at the object's class.

## 2. Upcasting
`Vehicle[] parked = { new TwoWheeler(..), new Car(..), new ElectricCar(..) };` – one array, three different objects. A subclass object is-a `Vehicle`, so the upcast is implicit and safe.

## 3. Runtime polymorphism – trace of `v.calculateFee(130)` for `v = parked[2]`
| Step | Detail |
|---|---|
| Declared (static) type | `Vehicle` – the compiler checks `calculateFee(int)` exists in `Vehicle` |
| Actual (runtime) type | `ElectricCar` (found via the object's class pointer / vtable) |
| Method executed | `Vehicle.calculateFee(int)` (inherited, not overridden) |
| Inside it, `ratePerHour()` | dispatched again at runtime → `Car.ratePerHour()` (inherited by ElectricCar) = 30 |
| Result | 3 started hours × 30 = 90; then `instanceof Chargeable` → `+ 10 × 12 = 120` → **Rs 210** |
`ratePerHour()` is the overridden method whose body is selected by the actual type – this is dynamic method dispatch.

## 4. Why no if–else chains
Without polymorphism the loop would need `if (v instanceof TwoWheeler) … else if (v instanceof Car) …`. With dispatch the loop is one line, and a new vehicle type needs no change to the loop.

## 5. Why does the last line print `true`?
`new Car("TN02B2").equals(new ElectricCar("TN02B2"))` → `equals` is inherited from `Vehicle`: `o instanceof Vehicle` is true for an `ElectricCar` and the registration numbers are identical. **Is it correct?** Yes for this system: the registration number identifies one physical vehicle, so the same number can't be two different vehicles. If a Car and an ElectricCar have the same number, it is a data-entry error, and the `HashMap` duplicate check on entry rejects the second one. (If equality required the same class, `getClass() == o.getClass()` would be used, but then a vehicle re-registered as an EV could park twice.)

## 6. Alternative concept – `instanceof` and casts
```java
double fee;
if (v instanceof TwoWheeler) fee = hours * 10;
else if (v instanceof ElectricCar) fee = hours * 30 + ((ElectricCar) v).chargingFee(10);
else if (v instanceof Car) fee = hours * 30;     // ORDER MATTERS: Car must come after ElectricCar!
```
| | Dynamic dispatch | `instanceof` chain |
|---|---|---|
| Time per call (k types) | O(1) | up to O(k) checks |
| Code size | each type in its own class | one long block, grows with k |
| New type | add a class | edit the chain in every place it appears; ordering bugs (subclass tested after superclass) |
