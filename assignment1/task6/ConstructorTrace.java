/** Task 6 - hierarchy + constructor execution order trace for ElectricCar. */
class Vehicle {
    private final String regNo;
    Vehicle(String regNo) { this.regNo = regNo; System.out.println("1. Vehicle(" + regNo + ") constructor"); }
    String getRegNo() { return regNo; }
    double ratePerHour() { return 0; }
    String describe() { return "Vehicle " + regNo; }
}
class Car extends Vehicle {
    Car(String regNo) { super(regNo); System.out.println("2. Car(" + regNo + ") constructor"); }
    @Override double ratePerHour() { return 30; }
    @Override String describe() { return "Car/" + super.describe(); }       // super for method
}
class ElectricCar extends Car {
    private final int batteryKWh;
    ElectricCar(String regNo, int batteryKWh) {
        super(regNo);                                                       // super for constructor
        this.batteryKWh = batteryKWh;
        System.out.println("3. ElectricCar(" + regNo + ") constructor, battery=" + batteryKWh);
    }
    @Override String describe() { return "Electric" + super.describe() + " [" + batteryKWh + " kWh]"; }
}
public class ConstructorTrace {
    public static void main(String[] args) {
        System.out.println("new ElectricCar(\"TN03C3\", 40):");
        Vehicle v = new ElectricCar("TN03C3", 40);
        System.out.println(v.describe() + " rate=" + v.ratePerHour());
    }
}
