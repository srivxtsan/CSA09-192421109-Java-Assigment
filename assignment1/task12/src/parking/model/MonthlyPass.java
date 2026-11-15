package parking.model;

import java.time.LocalDate;

/** NEW: students and staff only, valid for a date range. */
public class MonthlyPass implements Pass {
    private final LocalDate from, to;
    public MonthlyPass(LocalDate from, LocalDate to) {
        if (to.isBefore(from)) throw new IllegalArgumentException("Pass end before start");
        this.from = from; this.to = to;
    }
    @Override public boolean covers(UserCategory c, LocalDate d) {
        return c != UserCategory.VISITOR && !d.isBefore(from) && !d.isAfter(to);
    }
}
