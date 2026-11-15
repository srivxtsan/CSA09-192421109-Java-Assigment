# Task 2 – Choose the Data Representation
Code: `FeeCastDemo.java` (`javac FeeCastDemo.java && java FeeCastDemo`, output in `output.txt`).

## 1. Data types
| Field | Type | Reason |
|---|---|---|
| floor, slot number | `int` | small whole numbers used as array indices (indices must be `int`) |
| vehicle number | `String` | letters + digits (TN01AB1234); normalised with `trim().toUpperCase()` |
| ticket number | `int` (`long` if millions of tickets) | whole, only increases |
| entry/exit time | `LocalDateTime` (or `long` epoch millis) | correct date arithmetic; avoids hand-made time maths |
| duration | `int` minutes | whole minutes; `Duration.between(...).toMinutes()` returns `long`, so narrowed with a checked cast |
| rate, fee | `double` for this assignment (Rs with paise) – `BigDecimal` in a real billing system | `double` cannot represent 0.1 exactly; with the whole-rupee rates here the results are exact |
| pass flag | `boolean` | two states |
| user category | `enum` | fixed set, compile-time safety |

## 2. Floors and slots – jagged array
`String[][] slots` – `slots[f] = new String[slotsPerFloor[f]]`. A 2-D rectangular array (`new String[F][S]`) would waste memory and invent non-existent slots when floors have different sizes (Task 12: floors with different counts). Jagged arrays cost exactly Σ slots[f] cells. `slots[f].length` gives each floor's size, so loops never go out of bounds. Layouts 1×20, 3×50, 5×100 all fit the same code.

## 3. Type conversion and its risk
- `int * double` → `int` is widened to `double` automatically (safe).
- `double → int`, `long → int` need an explicit cast and can lose data: `(int) 3_000_000_000L` gives `-1294967296`. Use `Math.toIntExact()` so an overflow throws.
- **Wrong fee examples (130 minutes, car Rs 30/h, correct = 3 started hours = Rs 90):**
| Code | Result | Problem |
|---|---|---|
| `(minutes / 60) * 30` | Rs 60 | integer division truncates 2.16 → 2 |
| `(double)(minutes / 60) * 30` | Rs 60 | cast applied **after** the division – precision already lost |
| `(minutes / 60.0) * 30` | Rs 65 | fractional hours: policy is "every started hour" |
| `((minutes + 59) / 60) * 30` | **Rs 90** | integer ceiling, then widening multiply |
The design uses the ceiling formula `(minutes + 59) / 60` – integer-only, no rounding error.

## 4. Input
`Scanner(System.in)` for the console application (commands read line by line, split on whitespace, `Integer.parseInt` for numbers, errors caught as exceptions). Command-line arguments are unsuitable for a long-running interactive system because the data changes during the run; they are fine for fixed configuration such as the floor layout.

## 5. Alternative concept – 1-D array or ArrayList
- **1-D array**, `index = floor × slotsPerFloor + slot`: works only if every floor has the **same** size. With different sizes you need a prefix-sum array `start[f]` (`index = start[f] + slot`) and a reverse lookup to recover the floor; mistakes in the offset silently map to the wrong floor.
- **`ArrayList<String[]>`**: each element is one floor's array; floors can be added at run time, sizes may differ, but it stores references to arrays (extra object per floor) and boxing-free but with list overhead.
| | Jagged `String[][]` | 1-D with formula | `ArrayList<String[]>` |
|---|---|---|---|
| Locate slot (f,s) | O(1) – two index operations | O(1) – one multiplication+add (needs prefix sums if sizes differ) | O(1) – `get(f)[s]` |
| Space | Σ Sf cells + F array headers | Σ Sf cells (+F prefix ints), one header | Σ Sf cells + F headers + list backing array (up to 1.5×F capacity) |
| Different floor sizes | natural | needs prefix array | natural |
| Add a floor later | copy to larger array O(F) | copy whole array O(F·S) | `add()` amortised O(1) |
