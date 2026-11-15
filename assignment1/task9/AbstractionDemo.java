/** Task 9 - abstract class + interfaces (default and static methods) + multiple behaviours. */
interface Chargeable {
    double RATE_PER_KWH = 12;                                    // implicitly public static final
    double chargingFee(int kWh);                                 // abstract
    default double chargingFeeWithGst(int kWh) { return chargingFee(kWh) * 1.18; }   // default
    static boolean isValidEnergy(int kWh) { return kWh >= 0 && kWh <= 200; }         // static
}
interface Payable { double amountDue(); }

abstract class Vehicle {                                         // state + constructor + abstract rule
    private final String regNo;
    protected Vehicle(String regNo) { this.regNo = regNo; }
    public String getRegNo() { return regNo; }
    protected abstract double ratePerHour();                     // WHAT varies
    public double calculateFee(int minutes) { return ((minutes + 59) / 60) * ratePerHour(); } // HOW is shared
}
class Car extends Vehicle {
    Car(String r) { super(r); }
    @Override protected double ratePerHour() { return 30; }
}
/** One class, two behaviours: is-a Car (single class inheritance) + can-do Chargeable & Payable. */
class ElectricCar extends Car implements Chargeable, Payable {
    private int pendingKWh;
    ElectricCar(String r) { super(r); }
    void charge(int kWh) { if (Chargeable.isValidEnergy(kWh)) pendingKWh += kWh; }
    @Override public double chargingFee(int kWh) { return kWh * RATE_PER_KWH; }
    @Override public double amountDue() { return calculateFee(130) + chargingFee(pendingKWh); }
}
public class AbstractionDemo {
    public static void main(String[] args) {
        ElectricCar ev = new ElectricCar("TN03C3");
        ev.charge(10);
        System.out.println("Charging fee        : " + ev.chargingFee(10));
        System.out.println("Charging fee + GST  : " + ev.chargingFeeWithGst(10));
        System.out.println("Amount due (Payable): " + ((Payable) ev).amountDue());
        System.out.println("isValidEnergy(500)  : " + Chargeable.isValidEnergy(500));
        // Vehicle v = new Vehicle("X");   // compile error: Vehicle is abstract
    }
}
