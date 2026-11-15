import java.util.HashSet;
import java.util.Set;

/** Task 7 - overriding, final, toString/equals/hashCode, and the switch-on-type alternative. */
abstract class Vehicle {
    public static final int MINUTES_PER_HOUR = 60;          // final variable (constant)
    private final String regNo;                               // final field (identity never changes)
    protected Vehicle(String regNo) { this.regNo = regNo; }
    public final String getRegNo() { return regNo; }          // final method: cannot be overridden
    protected abstract double ratePerHour();
    public double calculateFee(int minutes) {                 // template; subclasses may specialise
        return ((minutes + MINUTES_PER_HOUR - 1) / MINUTES_PER_HOUR) * ratePerHour();
    }
    @Override public String toString() { return getClass().getSimpleName() + "[" + regNo + "]"; }
    @Override public boolean equals(Object o) { return o instanceof Vehicle v && regNo.equals(v.regNo); }
    @Override public int hashCode() { return regNo.hashCode(); }
}
class TwoWheeler extends Vehicle {
    TwoWheeler(String r) { super(r); }
    @Override protected double ratePerHour() { return 10; }
}
class Car extends Vehicle {
    Car(String r) { super(r); }
    @Override protected double ratePerHour() { return 30; }
}
final class EBike extends Vehicle {                           // final class: no further subclassing
    EBike(String r) { super(r); }
    @Override protected double ratePerHour() { return 20; }
    @Override public double calculateFee(int minutes) { return minutes == 0 ? 0 : 20; } // flat fee
}

/** Alternative: one method with switch on a type field (every new type edits this class). */
class SwitchVehicle {
    final String regNo, type;
    SwitchVehicle(String regNo, String type) { this.regNo = regNo; this.type = type; }
    double calculateFee(int minutes) {
        int h = (minutes + 59) / 60;
        switch (type) {
            case "TWO_WHEELER": return h * 10;
            case "CAR":         return h * 30;
            case "EBIKE":       return minutes == 0 ? 0 : 20;
            default: throw new IllegalArgumentException("Unknown type " + type);
        }
    }
}

public class OverrideDemo {
    public static void main(String[] args) {
        Vehicle[] vs = { new TwoWheeler("TN01A1"), new Car("TN02B2"), new EBike("TN09E9") };
        for (Vehicle v : vs) System.out.println(v + " 130 min -> Rs " + v.calculateFee(130));

        Set<Vehicle> set = new HashSet<>();
        set.add(new Car("TN02B2"));
        System.out.println("Same regNo added again? " + !set.add(new Car("TN02B2")) + " (set size " + set.size() + ")");
        System.out.println("Switch version Car 130 min -> Rs " + new SwitchVehicle("TN02B2", "CAR").calculateFee(130));
    }
}
