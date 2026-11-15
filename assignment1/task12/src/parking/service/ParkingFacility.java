package parking.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import parking.model.*;

public class ParkingFacility {
    private final String[][] slots;                              // jagged: floors may differ in size
    private final Map<String, ParkingTicket> active = new HashMap<>();   // regNo -> ticket, O(1)
    private double totalCollection;

    public ParkingFacility(int[] slotsPerFloor) {
        if (slotsPerFloor == null || slotsPerFloor.length == 0) throw new IllegalArgumentException("No floors");
        slots = new String[slotsPerFloor.length][];
        for (int f = 0; f < slotsPerFloor.length; f++) {
            if (slotsPerFloor[f] <= 0) throw new IllegalArgumentException("Floor " + f + " needs >= 1 slot");
            slots[f] = new String[slotsPerFloor[f]];
        }
    }

    public int[] allocate(String vehicleNo) {
        for (int f = 0; f < slots.length; f++)
            for (int s = 0; s < slots[f].length; s++)
                if (slots[f][s] == null) { slots[f][s] = vehicleNo; return new int[]{f, s}; }
        return null;
    }

    public boolean release(int floor, int slot) {
        if (floor < 0 || floor >= slots.length || slot < 0 || slot >= slots[floor].length) return false;
        if (slots[floor][slot] == null) return false;
        slots[floor][slot] = null;
        return true;
    }

    public ParkingTicket enter(Vehicle v, UserCategory c, Pass pass, LocalDateTime time) {
        if (active.containsKey(v.getRegNo())) throw new IllegalStateException("Duplicate registration: " + v.getRegNo());
        int[] pos = allocate(v.getRegNo());
        if (pos == null) throw new IllegalStateException("Parking full");
        ParkingTicket t = new ParkingTicket(v, c, pass, pos[0], pos[1], time);
        active.put(v.getRegNo(), t);
        return t;
    }

    /** Releases the slot and returns the amount payable. kWh is the energy charged (0 if none). */
    public double exit(String regNo, LocalDateTime time, int kWh) {
        ParkingTicket t = active.get(regNo.trim().toUpperCase());
        if (t == null) throw new IllegalStateException("No active ticket for " + regNo);
        if (time.isBefore(t.getEntry())) throw new IllegalArgumentException("Exit before entry");
        active.remove(t.getVehicle().getRegNo());
        release(t.getFloor(), t.getSlot());
        double fee = t.isFree() ? 0 : t.getVehicle().calculateFee(t.getEntry(), time);   // dynamic dispatch
        if (t.getVehicle() instanceof Chargeable c && kWh > 0) fee += c.chargingFee(kWh); // charged separately
        totalCollection += fee;
        return fee;
    }

    public int occupied() { return active.size(); }
    public double getTotalCollection() { return totalCollection; }
}
