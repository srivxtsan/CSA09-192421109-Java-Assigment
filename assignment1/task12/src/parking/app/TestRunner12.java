package parking.app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import parking.model.*;
import parking.service.*;

public class TestRunner12 {
    static int pass, fail;
    static void row(String name, String expected, String actual) {
        boolean ok = expected.equals(actual);
        if (ok) pass++; else fail++;
        System.out.printf("%-52s | %-24s | %-24s | %s%n", name, expected, actual, ok ? "PASS" : "FAIL");
    }
    static String thrown(Runnable r) { try { r.run(); return "no exception"; } catch (RuntimeException e) { return e.getClass().getSimpleName(); } }
    static LocalDateTime at(int h, int m) { return LocalDateTime.of(2026, 11, 15, h, m); }
    static final LocalDate TODAY = LocalDate.of(2026, 11, 15);

    public static void main(String[] args) {
        System.out.printf("%-52s | %-24s | %-24s | %s%n", "Test", "Expected", "Actual", "Result");
        System.out.println("-".repeat(115));
        // --- regression: old behaviour must be unchanged ---
        row("[regression] Car 130 min", "90.0", "" + new Car("R1").calculateFee(130));
        row("[regression] TwoWheeler 130 min", "30.0", "" + new TwoWheeler("R2").calculateFee(130));
        row("[regression] EV 130 min + 10 kWh", "210.0", "" + (new ElectricCar("R3").calculateFee(130) + new ElectricCar("R3").chargingFee(10)));
        row("[regression] Car 700 min daily max", "240.0", "" + new Car("R4").calculateFee(700));
        // --- e-bike ---
        row("E-bike 5 min", "20.0", "" + new EBike("E1").calculateFee(5));
        row("E-bike 600 min (still flat)", "20.0", "" + new EBike("E1").calculateFee(600));
        row("E-bike 0 min", "0.0", "" + new EBike("E1").calculateFee(0));
        // --- delivery van (rate by start of each started hour) ---
        DeliveryVan van = new DeliveryVan("V1");
        row("Van 10:00-12:00 (2 peak hours)", "120.0", "" + van.calculateFee(at(10, 0), at(12, 0)));
        row("Van 08:00-09:00 (off-peak hour)", "30.0", "" + van.calculateFee(at(8, 0), at(9, 0)));
        row("Van 08:30-10:00 (off-peak + peak)", "90.0", "" + van.calculateFee(at(8, 30), at(10, 0)));
        row("Van 16:30-18:30 (peak + off-peak)", "90.0", "" + van.calculateFee(at(16, 30), at(18, 30)));
        row("Van 17:00-18:00 (17:00 is off-peak)", "30.0", "" + van.calculateFee(at(17, 0), at(18, 0)));
        row("Van 0 minutes", "0.0", "" + van.calculateFee(at(10, 0), at(10, 0)));
        // --- passes ---
        ParkingFacility f = new ParkingFacility(new int[]{20, 30, 40, 50, 60});
        Pass day = new OneDayPass(TODAY);
        f.enter(new Car("P1"), UserCategory.VISITOR, day, at(9, 0));
        row("Visitor + one-day pass (today): free", "0.0", "" + f.exit("P1", at(14, 0), 0));
        row("One-day pass for another day rejected", "IllegalArgumentException",
                thrown(() -> f.enter(new Car("P2"), UserCategory.VISITOR, new OneDayPass(TODAY.plusDays(1)), at(9, 0))));
        row("Student cannot use a one-day pass", "IllegalArgumentException",
                thrown(() -> f.enter(new Car("P3"), UserCategory.STUDENT, day, at(9, 0))));
        Pass monthly = new MonthlyPass(TODAY.minusDays(10), TODAY.plusDays(20));
        f.enter(new Car("P4"), UserCategory.STAFF, monthly, at(9, 0));
        row("Staff + valid monthly pass: free", "0.0", "" + f.exit("P4", at(17, 0), 0));
        row("Expired monthly pass rejected", "IllegalArgumentException",
                thrown(() -> f.enter(new Car("P5"), UserCategory.STUDENT, new MonthlyPass(TODAY.minusDays(40), TODAY.minusDays(10)), at(9, 0))));
        row("Visitor with monthly pass rejected", "IllegalArgumentException",
                thrown(() -> f.enter(new Car("P6"), UserCategory.VISITOR, monthly, at(9, 0))));
        // --- factory ---
        row("Factory: EBIKE", "EBike", VehicleFactory.create("ebike", "F1").getClass().getSimpleName());
        row("Factory: VAN", "DeliveryVan", VehicleFactory.create("VAN", "F2").getClass().getSimpleName());
        row("Factory: unknown still rejected", "IllegalArgumentException", thrown(() -> VehicleFactory.create("BUS", "F3")));
        // --- jagged 5 floors ---
        ParkingFacility small = new ParkingFacility(new int[]{1, 2, 3, 4, 5});
        int last = 0; int[] pos = null;
        for (int i = 0; i < 15; i++) { pos = small.allocate("S" + i); last = i; }
        row("15 allocations over floors {1,2,3,4,5}", "[4, 4]", java.util.Arrays.toString(pos));
        row("16th allocation -> full", "null", "" + small.allocate("S16"));
        // --- mixed day, hand calculated ---
        ParkingFacility d = new ParkingFacility(new int[]{20, 30, 40, 50, 60});
        d.enter(new EBike("M1"), UserCategory.VISITOR, null, at(9, 0));
        d.enter(new DeliveryVan("M2"), UserCategory.VISITOR, null, at(8, 30));
        d.enter(new Car("M3"), UserCategory.VISITOR, day, at(9, 0));
        d.enter(new ElectricCar("M4"), UserCategory.STAFF, monthly, at(9, 0));
        double sum = d.exit("M1", at(12, 0), 0) + d.exit("M2", at(10, 0), 0) + d.exit("M3", at(15, 0), 0) + d.exit("M4", at(11, 0), 10);
        row("Mixed day (20 + 90 + 0 + 120 charging)", "230.0", "" + sum);
        System.out.println("-".repeat(115));
        System.out.println("PASSED " + pass + "  FAILED " + fail);
    }
}
