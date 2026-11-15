import java.time.Duration;
import java.time.LocalTime;

/** Task 8 - compile-time polymorphism (overloading) + upcasting + dispatch trace. */
abstract class Vehicle {
    protected abstract double ratePerHour();
    public double calculateFee(int minutes) { return ((minutes + 59) / 60) * ratePerHour(); }
    // overloaded: chosen at COMPILE time from the argument types
    public double calculateFee(LocalTime in, LocalTime out) {
        return calculateFee((int) Duration.between(in, out).toMinutes());
    }
    public double calculateFee(int hours, int extraMinutes) { return calculateFee(hours * 60 + extraMinutes); }
}
class Car extends Vehicle { @Override protected double ratePerHour() { return 30; } }
class TwoWheeler extends Vehicle { @Override protected double ratePerHour() { return 10; } }

public class OverloadDemo {
    public static void main(String[] args) {
        Vehicle v = new Car();                       // declared type Vehicle, actual type Car (upcast)
        System.out.println("calculateFee(int)            : " + v.calculateFee(130));
        System.out.println("calculateFee(int,int)        : " + v.calculateFee(2, 10));
        System.out.println("calculateFee(LocalTime,...)  : " + v.calculateFee(LocalTime.of(9, 0), LocalTime.of(11, 10)));
        v = new TwoWheeler();                        // same reference, different object
        System.out.println("Same call, TwoWheeler        : " + v.calculateFee(130));
    }
}
