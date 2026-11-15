package parking.model;

public interface Chargeable {
    double RATE_PER_KWH = 12;
    double chargingFee(int kWh);
    default double chargingFeeWithGst(int kWh) { return chargingFee(kWh) * 1.18; }
}
