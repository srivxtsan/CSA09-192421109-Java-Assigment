// Task 1.2: Separated Service Class for Fee Payment
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FeeService {
    private static final String URL = "jdbc:mysql://localhost:3306/college";
    private static final String USER = "root";
    private static final String PWD = "password";

    public void processPayment(String regNoStr, String amountStr) throws Exception {
        if (regNoStr == null || regNoStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be empty.");
        }
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format. Please enter a valid number.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        // Database operation & simulated network delay (done off EDT or handled via service)
        try (Connection c = DriverManager.getConnection(URL, USER, PWD);
             var pstmt = c.prepareStatement("INSERT INTO fees VALUES (?, ?)")) {
            pstmt.setString(1, regNoStr.trim());
            pstmt.setDouble(2, amount);
            pstmt.executeUpdate();
            
            // Simulating bank gateway confirmation delay
            Thread.sleep(3000);
        }
    }
}
