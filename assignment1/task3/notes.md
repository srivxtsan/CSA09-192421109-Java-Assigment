# Task 3 – Design the Control Logic
Code: `ParkingFacility.java` (completed TODOs; output in `output.txt` equals the expected output) and `StackParkingFacility.java` (alternative).

## 1. Search for a free slot
Floors are searched bottom-up, slots left-to-right; the **first** free slot is used (nearest to entrance, predictable). The search stops as soon as one is found (`return` inside the inner loop leaves both loops). If both loops finish, the facility is full and `null` is returned – the caller turns this into "Parking full" (no ticket issued, nothing changes). `continue` is used to skip an unsuitable slot (e.g. an EV-only slot for a non-EV); `break` leaves a loop when only the loop must stop and code after it must still run.

## 2. Fee slabs (if–else)
```
minutes == 0            -> fee 0 (grace)
hours  = ceil(minutes/60)               (integer arithmetic)
days   = minutes / 1440, rem = minutes % 1440
hours  = min(ceil(rem/60), 8)           (daily maximum)
fee    = (days*8 + hours) * ratePerHour
```
`switch` is used for choosing the vehicle type from text (VehicleFactory); the rate itself is polymorphic.

## 3. Pseudocode – allocation
```
ALGORITHM AllocateSlot(slots, vehicleNo)
INPUT : slots[0..F-1][0..Sf-1], vehicleNo
OUTPUT: {floor, slot} or NULL
BEGIN
  FOR f ← 0 TO F-1 DO
    FOR s ← 0 TO length(slots[f]) - 1 DO
      IF slots[f][s] = NULL THEN
        slots[f][s] ← vehicleNo
        RETURN {f, s}
      END IF
    END FOR
  END FOR
  RETURN NULL                       // facility full
END
```
## 4. Pseudocode – release
```
ALGORITHM ReleaseSlot(slots, floor, slot)
BEGIN
  IF floor < 0 OR floor ≥ F THEN RETURN false
  IF slot < 0 OR slot ≥ length(slots[floor]) THEN RETURN false
  IF slots[floor][slot] = NULL THEN RETURN false      // already free
  slots[floor][slot] ← NULL
  RETURN true
END
```
## 5. Pseudocode – fee
```
ALGORITHM ComputeFee(minutes, rate, hasFreePass)
INPUT : minutes ≥ 0, rate per hour, hasFreePass
OUTPUT: fee
BEGIN
  IF minutes < 0 THEN ERROR "negative duration"
  IF hasFreePass OR minutes = 0 THEN RETURN 0
  days  ← minutes DIV 1440
  rem   ← minutes MOD 1440
  hours ← (rem + 59) DIV 60
  IF hours > 8 THEN hours ← 8
  RETURN (days × 8 + hours) × rate
END
```
## 6. When nothing is free
`allocate` returns `null`; `ParkingFacility.enter` throws `IllegalStateException("Parking full")`. No ticket number is consumed and no state is modified.

## 7. Alternative concept – free count per floor + stack of free slots
```
allocate: FOR f: IF freeCount[f] > 0 THEN s ← freeStack[f].pop(); slots[f][s] ← v; freeCount[f]--; RETURN {f,s}
release : validate; slots[f][s] ← NULL; freeStack[f].push(s); freeCount[f]++
```
| | Scan (given) | Free-count + stack |
|---|---|---|
| `allocate` time | worst O(F·S) (all earlier slots full) | O(F) (skip full floors by count; O(1) pop) – O(1) if a global "next floor with space" is kept |
| `release` time | O(1) | O(1) |
| Extra space | O(1) | O(F) counts + O(F·S) stack entries (each free slot number as an object/int) |
Trade-off: the stack makes allocation faster on a nearly-full facility but needs more memory and extra code to keep three structures consistent. Note the stack returns the *most recently freed* slot, not the lowest-numbered (use a `PriorityQueue`, O(log S), to keep "lowest first"). `StackParkingFacility.java` produces the same expected output.
