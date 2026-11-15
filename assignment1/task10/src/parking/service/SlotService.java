package parking.service;

import parking.model.ParkingSlot;
import parking.model.SlotAssigner;

public class SlotService {
    private final ParkingSlot[][] slots;

    public SlotService(int[] perFloor) {
        slots = new ParkingSlot[perFloor.length][];
        for (int f = 0; f < perFloor.length; f++) {
            slots[f] = new ParkingSlot[perFloor[f]];
            for (int s = 0; s < perFloor[f]; s++) slots[f][s] = new ParkingSlot(f, s);
        }
    }
    public ParkingSlot get(int floor, int slot) {
        if (floor < 0 || floor >= slots.length || slot < 0 || slot >= slots[floor].length)
            throw new IllegalArgumentException("Invalid slot " + floor + "/" + slot);
        return slots[floor][slot];
    }
    public void park(int f, int s, String regNo) { SlotAssigner.park(get(f, s), regNo); }
    public void leave(int f, int s) { SlotAssigner.leave(get(f, s)); }
}
