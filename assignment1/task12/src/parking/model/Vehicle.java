package parking.model;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class Vehicle {
    public static final int MAX_BILLABLE_HOURS_PER_DAY = 8;   // daily maximum
    private final String regNo;

    protected Vehicle(String regNo) {
        if (regNo == null || regNo.isBlank()) throw new IllegalArgumentException("Registration number required");
        this.regNo = regNo.trim().toUpperCase();
    }

    public String getRegNo() { return regNo; }
    protected abstract double ratePerHour();

    /** Zero minutes = Rs 0. Every started hour is charged; max 8 hours' charge per 24 h block. */
    public double calculateFee(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Duration cannot be negative");
        if (minutes == 0) return 0;
        int days = minutes / 1440;
        int rem = minutes % 1440;
        int hours = Math.min((rem + 59) / 60, MAX_BILLABLE_HOURS_PER_DAY);
        return (days * MAX_BILLABLE_HOURS_PER_DAY + hours) * ratePerHour();
    }

    /** Overload (compile-time polymorphism): delegates to the minutes version. */
    public double calculateFee(LocalDateTime in, LocalDateTime out) {
        if (out.isBefore(in)) throw new IllegalArgumentException("Exit before entry");
        return calculateFee((int) Duration.between(in, out).toMinutes());
    }

    @Override public String toString() { return getClass().getSimpleName() + "[" + regNo + "]"; }
    @Override public boolean equals(Object o) { return o instanceof Vehicle v && regNo.equals(v.regNo); }
    @Override public int hashCode() { return regNo.hashCode(); }
}
