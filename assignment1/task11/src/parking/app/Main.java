package parking.app;

import java.time.LocalDateTime;
import java.util.Scanner;
import parking.model.*;
import parking.service.*;

/** Console app: ENTER <type> <reg> <STUDENT|STAFF|VISITOR> <pass:true|false> <HH:mm> | EXIT <reg> <HH:mm> <kWh> | QUIT */
public class Main {
    public static void main(String[] args) {
        ParkingFacility p = new ParkingFacility(new int[]{2, 3, 4});
        Scanner in = new Scanner(System.in);
        System.out.println("Commands: ENTER <TWO_WHEELER|CAR|EV> <reg> <STUDENT|STAFF|VISITOR> <true|false> <HH:mm> | EXIT <reg> <HH:mm> <kWh> | QUIT");
        while (in.hasNextLine()) {
            String[] a = in.nextLine().trim().split("\\s+");
            try {
                if (a[0].equalsIgnoreCase("QUIT")) break;
                else if (a[0].equalsIgnoreCase("ENTER")) {
                    ParkingTicket t = p.enter(VehicleFactory.create(a[1], a[2]), UserCategory.valueOf(a[3].toUpperCase()),
                            Boolean.parseBoolean(a[4]), time(a[5]));
                    System.out.println("Issued " + t);
                } else if (a[0].equalsIgnoreCase("EXIT")) {
                    System.out.println("Fee Rs " + p.exit(a[1], time(a[2]), Integer.parseInt(a[3])));
                } else System.out.println("Unknown command");
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Total collection Rs " + p.getTotalCollection());
    }
    static LocalDateTime time(String hhmm) {
        String[] h = hhmm.split(":");
        return LocalDateTime.of(2026, 11, 15, Integer.parseInt(h[0]), Integer.parseInt(h[1]));
    }
}
