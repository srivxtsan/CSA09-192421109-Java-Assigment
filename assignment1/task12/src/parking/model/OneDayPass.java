package parking.model;

import java.time.LocalDate;

/** NEW: visitors only, valid for exactly one day. */
public class OneDayPass implements Pass {
    private final LocalDate day;
    public OneDayPass(LocalDate day) { this.day = day; }
    @Override public boolean covers(UserCategory c, LocalDate d) {
        return c == UserCategory.VISITOR && day.equals(d);
    }
}
