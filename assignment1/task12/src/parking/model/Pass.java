package parking.model;

import java.time.LocalDate;

/** NEW: abstraction for anything that waives the parking fee. */
public interface Pass {
    boolean covers(UserCategory category, LocalDate date);
}
