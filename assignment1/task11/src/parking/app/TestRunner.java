package parking.app;

import java.time.LocalDateTime;
import parking.model.*;
import parking.service.*;

public class TestRunner {
    static int pass, fail;
    static void row(String name, String expected, String actual) {
        boolean ok = expected.equals(actual);
        if (ok) pass++; else fail++;
        System.out.printf("%-48s | %-22s | %-22s | %s%n", name, expected, actual, ok ? "PASS" : "FAIL");
    }
    static String thrown(Runnable r) {
        try { r.run(); return "no exception"; } catch (RuntimeException e) { return e.getClass().getSimpleName(); }
    }
    static LocalDateTime at(int h, int m) { return LocalDateTime.of(2026, 11, 15, h, m); }

    public static void main(String[] args) {
        System.out.printf("%-48s | %-22s | %-22s | %s%n", "Test", "Expected", "Actual", "Result");
        System.out.println("-".repeat(110));
        Vehicle two = new TwoWheeler("TN01A1"), car = new Car("TN02B2"), ev = new ElectricCar("TN03C3");

        // normal
        row("TwoWheeler 130 min", "30.0", "" + two.calculateFee(130));
        row("Car 130 min", "90.0", "" + car.calculateFee(130));
        row("EV 130 min + 10 kWh", "210.0", "" + (ev.calculateFee(130) + ((Chargeable) ev).chargingFee(10)));
        // boundary
        row("Zero minutes", "0.0", "" + car.calculateFee(0));
        row("Exactly 60 min (slab boundary)", "30.0", "" + car.calculateFee(60));
        row("61 min (just over boundary)", "60.0", "" + car.calculateFee(61));
        row("Daily maximum, 700 min", "240.0", "" + car.calculateFee(700));
        row("Exactly 24 h (1440 min)", "240.0", "" + car.calculateFee(1440));
        row("25 h (1500 min)", "270.0", "" + car.calculateFee(1500));
        ParkingFacility one = new ParkingFacility(new int[]{1});
        one.enter(new Car("LAST1"), UserCategory.VISITOR, false, at(9, 0));
        row("Last free slot taken -> next entry", "IllegalStateException", thrown(() -> one.enter(new Car("LAST2"), UserCategory.VISITOR, false, at(9, 1))));
        row("Slot freed after exit, reuse works", "[0, 0]", java.util.Arrays.toString(freeThenAlloc(one)));
        // invalid
        row("Unknown vehicle type", "IllegalArgumentException", thrown(() -> VehicleFactory.create("TRUCK", "X1")));
        ParkingFacility f = new ParkingFacility(new int[]{2, 1});
        f.enter(car, UserCategory.VISITOR, false, at(9, 0));
        row("Duplicate registration number", "IllegalStateException", thrown(() -> f.enter(new Car("TN02B2"), UserCategory.VISITOR, false, at(9, 5))));
        row("Invalid slot number release(5,0)", "false", "" + f.release(5, 0));
        row("Negative slot release(0,-1)", "false", "" + f.release(0, -1));
        row("Visitor with monthly pass", "IllegalArgumentException", thrown(() -> f.enter(new Car("V9"), UserCategory.VISITOR, true, at(9, 5))));
        row("Exit before entry", "IllegalArgumentException", thrown(() -> f.exit("TN02B2", at(8, 0), 0)));
        row("Exit of unknown vehicle", "IllegalStateException", thrown(() -> f.exit("NOPE", at(10, 0), 0)));
        row("Negative minutes", "IllegalArgumentException", thrown(() -> car.calculateFee(-1)));
        row("Equal regNo -> equals true (Car vs EV)", "true", "" + new Car("A1").equals(new ElectricCar("A1")));

        // hand-calculated day
        ParkingFacility day = new ParkingFacility(new int[]{3, 3});
        day.enter(new TwoWheeler("D1"), UserCategory.VISITOR, false, at(9, 0));
        day.enter(new Car("D2"), UserCategory.VISITOR, false, at(9, 0));
        day.enter(new ElectricCar("D3"), UserCategory.VISITOR, false, at(9, 0));
        day.enter(new Car("D4"), UserCategory.STUDENT, true, at(9, 0));
        day.enter(new Car("D5"), UserCategory.VISITOR, false, at(9, 0));
        double sum = day.exit("D1", at(9, 45), 0) + day.exit("D2", at(11, 10), 0)
                + day.exit("D3", at(10, 10), 10) + day.exit("D4", at(14, 0), 0) + day.exit("D5", at(20, 40), 0);
        row("Day total (hand = 10+90+180+0+240)", "520.0", "" + sum);
        row("Facility totalCollection matches", "520.0", "" + day.getTotalCollection());
        row("All slots free at end of day", "0", "" + day.occupied());
        System.out.println("-".repeat(110));
        System.out.println("PASSED " + pass + "  FAILED " + fail);
    }
    static int[] freeThenAlloc(ParkingFacility p) {
        p.exit("LAST1", at(10, 0), 0);
        return p.allocate("X");
    }
}
