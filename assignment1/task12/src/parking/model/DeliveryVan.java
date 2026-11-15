package parking.model;

import java.time.Duration;
import java.time.LocalDateTime;

/** NEW: car rate normally, double car rate for hours that START between 09:00 and 17:00. */
public class DeliveryVan extends Vehicle {
    private static final double BASE = 30, PEAK_MULTIPLIER = 2;
    public DeliveryVan(String regNo) { super(regNo); }
    @Override protected double ratePerHour() { return BASE; }

    @Override public double calculateFee(LocalDateTime in, LocalDateTime out) {
        if (out.isBefore(in)) throw new IllegalArgumentException("Exit before entry");
        long minutes = Duration.between(in, out).toMinutes();
        long hours = (minutes + 59) / 60;               // every started hour
        double fee = 0;
        LocalDateTime t = in;
        for (long h = 0; h < hours; h++, t = t.plusHours(1)) {
            boolean peak = t.getHour() >= 9 && t.getHour() < 17;
            fee += peak ? BASE * PEAK_MULTIPLIER : BASE;
        }
        return fee;                                      // (no daily cap for vans - documented limitation)
    }
}
