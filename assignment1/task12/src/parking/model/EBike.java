package parking.model;

/** NEW: flat fee Rs 20 per visit (0 minutes = free). */
public class EBike extends Vehicle {
    public static final double FLAT_FEE = 20;
    public EBike(String regNo) { super(regNo); }
    @Override protected double ratePerHour() { return FLAT_FEE; }
    @Override public double calculateFee(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Duration cannot be negative");
        return minutes == 0 ? 0 : FLAT_FEE;
    }
}
