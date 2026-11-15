# Task 7 – Override and Specialise Behaviour
Code: `OverrideDemo.java` (output in `output.txt`).

## 1. Methods overridden
- `ratePerHour()` – abstract in `Vehicle`; every subclass supplies its own rate.
- `calculateFee()` – inherited by cars/two-wheelers; `EBike` **overrides** it because its fee is a flat Rs 20, not hourly.
- `toString()`, `equals()`, `hashCode()` from `Object`.

## 2. `@Override` and the rules
`@Override` makes the compiler verify that a parent method really exists (a typo such as `ratePerHor()` becomes a compile error instead of a silent new method). Rules: same name and parameter list (signature); return type same or a **subtype** (covariant); access **not weaker** (`protected` → `protected` or `public`); may not throw broader checked exceptions; `static`, `private` and `final` methods cannot be overridden (static ones are hidden).

## 3. `final`
| Use | Where | Why |
|---|---|---|
| final variable | `regNo`, `MINUTES_PER_HOUR` | identity and constants must not change |
| final method | `getRegNo()` | subclasses must not change how identity is read |
| final class | `EBike` (or `String`-like utility classes) | stops further subclassing so its flat-fee rule can't be altered |

## 4. `toString`, `equals`, `hashCode`
```java
@Override public boolean equals(Object o) { return o instanceof Vehicle v && regNo.equals(v.regNo); }
@Override public int hashCode() { return regNo.hashCode(); }
@Override public String toString() { return getClass().getSimpleName() + "[" + regNo + "]"; }
```
Two vehicles with the same registration number are the same physical vehicle → equal. **Contract:** equal objects must have equal hash codes (both use `regNo`), otherwise `HashSet`/`HashMap` fail – the demo shows a second `Car("TN02B2")` is rejected by the `HashSet`. Without overriding, `==`/default `equals` compare memory addresses.

## 5. Alternative concept – single method with `switch` on a type field
(`SwitchVehicle` in the demo.)
| | Overriding | `switch` on type |
|---|---|---|
| Time per fee | O(1) virtual dispatch | O(1) for a `switch` on a string/enum (hash/jump), up to O(k) for an if-else chain over k types |
| Classes to change for a new type | 0 existing (1 new subclass) | the one big class (re-test everything) + every other `switch` on type in the system |
| Forgetting a case | compiler error (abstract method) | runtime `default` error |
Switch violates the Open/Closed Principle: it is acceptable only for a tiny, never-growing set.
