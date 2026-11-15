# Task 9 – Introduce Abstraction
Code: `AbstractionDemo.java` (output in `output.txt`).

## 1. What is abstract
- `Vehicle` is an **abstract class**: it holds shared state (`regNo`), a constructor with validation, and the shared fee algorithm, but "a generic vehicle" must never be created. The abstract method `ratePerHour()` states *what* each subclass must supply; `calculateFee()` is the shared *how*.
- `Chargeable` is an **interface**: charging is a capability that unrelated classes could have (EVs, e-bikes, even a charging slot), not a kind of vehicle. `Payable` (anything with an amount due) is another capability.

## 2. Abstract class vs interface – why each
| Need | Choice |
|---|---|
| shared fields/constructor/protected helpers, a common ancestor | abstract class |
| a capability for unrelated types, and a class may have several | interface |
A class can extend only **one** class but implement **many** interfaces, so `ElectricCar extends Car implements Chargeable, Payable` gets multiple behaviours.

## 3. Default and static methods in an interface
- `default double chargingFeeWithGst(int kWh)` – adds behaviour to all implementers without breaking existing ones (backward-compatible evolution; implementers may override).
- `static boolean isValidEnergy(int kWh)` – a helper that belongs to the interface and needs no object.
- Interface fields are `public static final` constants (`RATE_PER_KWH`).

## 4. Alternative concept – `Vehicle` as interface, `Chargeable` as abstract class
```java
interface Vehicle { String getRegNo(); double ratePerHour(); default double calculateFee(int m){ return ((m+59)/60)*ratePerHour(); } }
abstract class Chargeable { abstract double chargingFee(int kWh); }
class ElectricCar extends Chargeable implements Vehicle { ... }   // now cannot extend Car!
```
| Lost / changed | Explanation |
|---|---|
| **State** | an interface cannot hold instance fields, so `regNo` must be re-declared in every implementer (or fetched via `getRegNo()`) |
| **Constructors** | interfaces have none → the validation of `regNo` is duplicated in each class |
| **Multiple inheritance** | `ElectricCar` can extend only one class; making `Chargeable` an abstract class uses up that single slot, so `ElectricCar extends Car` is impossible |
| Access control | interface methods are implicitly `public`; no `protected` helpers |
| Time per call | interface call via `invokeinterface` vs `invokevirtual`: both O(1); the JIT inlines both when monomorphic – no measurable difference |
| Space per object | same header; interface version stores `regNo` once per implementer class – equal per object (O(1) per object) |
