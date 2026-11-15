package parking.app;

import parking.service.SlotService;

public class Main {
    public static void main(String[] args) {
        SlotService svc = new SlotService(new int[]{2, 1});
        svc.park(0, 0, "TN01AB1111");
        System.out.println(svc.get(0, 0));
        // svc.get(0,0).vehicleNo = "X";     // compile error: vehicleNo has private access
        // svc.get(0,0).clear();             // compile error: clear() is package-private
        try { svc.get(0, 0).setMinutesParked(-5); } catch (IllegalArgumentException e) { System.out.println("Rejected: " + e.getMessage()); }
        try { svc.get(9, 0); } catch (IllegalArgumentException e) { System.out.println("Rejected: " + e.getMessage()); }
        try { svc.park(0, 0, "TN02"); } catch (IllegalStateException e) { System.out.println("Rejected: " + e.getMessage()); }

        // Wrapper classes / autoboxing pitfall
        Integer a = 127, b = 127, c = 1000, d = 1000;
        System.out.println("127 == 127   : " + (a == b) + "   (cached)");
        System.out.println("1000 == 1000 : " + (c == d) + "  (different objects!)");
        System.out.println("1000 equals  : " + c.equals(d));
        int parsed = Integer.parseInt("45");
        System.out.println("parseInt + 1 : " + (parsed + 1));
    }
}
