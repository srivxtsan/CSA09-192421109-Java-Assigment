import java.util.ArrayDeque;
import java.util.Arrays;

/** Task 3 - alternative concept: free-count per floor + stack of free slot numbers. */
public class StackParkingFacility {
    private final String[][] slots;
    private final int[] freeCount;                 // free slots on each floor
    private final ArrayDeque<Integer>[] freeStack; // free slot numbers on each floor

    @SuppressWarnings("unchecked")
    public StackParkingFacility(int[] slotsPerFloor) {
        int F = slotsPerFloor.length;
        slots = new String[F][];
        freeCount = new int[F];
        freeStack = new ArrayDeque[F];
        for (int f = 0; f < F; f++) {
            slots[f] = new String[slotsPerFloor[f]];
            freeStack[f] = new ArrayDeque<>();
            for (int s = slotsPerFloor[f] - 1; s >= 0; s--) freeStack[f].push(s); // lowest on top
            freeCount[f] = slotsPerFloor[f];
        }
    }

    public int[] allocate(String vehicleNo) {          // O(F) worst case, O(1) per floor
        for (int f = 0; f < freeCount.length; f++) {
            if (freeCount[f] > 0) {
                int s = freeStack[f].pop();             // O(1)
                slots[f][s] = vehicleNo;
                freeCount[f]--;
                return new int[]{f, s};
            }
        }
        return null;
    }

    public boolean release(int floor, int slot) {      // O(1)
        if (floor < 0 || floor >= slots.length) return false;
        if (slot < 0 || slot >= slots[floor].length) return false;
        if (slots[floor][slot] == null) return false;
        slots[floor][slot] = null;
        freeStack[floor].push(slot);
        freeCount[floor]++;
        return true;
    }

    public static void main(String[] args) {
        StackParkingFacility p = new StackParkingFacility(new int[]{2, 1});
        System.out.println(Arrays.toString(p.allocate("TN01AB1111")));
        System.out.println(Arrays.toString(p.allocate("TN02CD2222")));
        System.out.println(Arrays.toString(p.allocate("TN03EF3333")));
        System.out.println(Arrays.toString(p.allocate("TN04GH4444")));
        System.out.println(p.release(0, 1) + " " + p.release(0, 1) + " " + p.release(5, 0));
        System.out.println(Arrays.toString(p.allocate("TN04GH4444")));
    }
}
