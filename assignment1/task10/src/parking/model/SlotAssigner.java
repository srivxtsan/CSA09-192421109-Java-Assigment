package parking.model;

/** Same package as ParkingSlot, so it may call the package-private clear() and protected assign(). */
public class SlotAssigner {
    public static void park(ParkingSlot s, String regNo) { s.assign(regNo); }
    public static void leave(ParkingSlot s) { s.clear(); }
}
