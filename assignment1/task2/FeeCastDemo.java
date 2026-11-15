/** Task 2 - shows how integer division / casting gives a wrong fee and how the design avoids it. */
public class FeeCastDemo {
    static final double RATE_PER_HOUR = 30.0;

    // WRONG 1: integer division truncates 130/60 = 2 hours (should be 3 started hours)
    static double wrongTruncate(int minutes) { return (minutes / 60) * RATE_PER_HOUR; }

    // WRONG 2: casting AFTER the integer division - precision is already lost
    static double wrongCast(int minutes) { return (double) (minutes / 60) * RATE_PER_HOUR; }

    // WRONG 3: floating hours -> fractional fee, not "every started hour is charged"
    static double wrongFraction(int minutes) { return (minutes / 60.0) * RATE_PER_HOUR; }

    // CORRECT: integer ceiling division, no floating point until the final multiplication
    static double correct(int minutes) {
        int hours = (minutes + 59) / 60;        // ceil(minutes / 60)
        return hours * RATE_PER_HOUR;           // int * double -> double (widening, safe)
    }

    public static void main(String[] args) {
        int minutes = 130;
        System.out.println("Minutes parked          : " + minutes);
        System.out.println("wrongTruncate (int div) : Rs " + wrongTruncate(minutes));
        System.out.println("wrongCast               : Rs " + wrongCast(minutes));
        System.out.println("wrongFraction           : Rs " + wrongFraction(minutes));
        System.out.println("correct (ceiling)       : Rs " + correct(minutes));

        // long -> int narrowing risk (explicit cast needed, checked with Math.toIntExact)
        long bigMinutes = 3_000_000_000L;
        System.out.println("(int) 3,000,000,000L    : " + (int) bigMinutes + "  <- silent overflow");
        try { Math.toIntExact(bigMinutes); }
        catch (ArithmeticException e) { System.out.println("Math.toIntExact        : " + e.getMessage()); }
    }
}
