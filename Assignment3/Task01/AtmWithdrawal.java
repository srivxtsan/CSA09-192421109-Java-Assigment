// Task 1: ATM Withdrawal Exception Handling Simulation
import java.util.Scanner;

public class AtmWithdrawal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double balance = 5000.00;
        
        System.out.print("Enter account number: ");
        String accNo = scanner.nextLine();
        
        try {
            System.out.print("Enter withdrawal amount: ");
            if (!scanner.hasNextDouble()) {
                throw new NumberFormatException("Invalid input: amount must be a number.");
            }
            double amount = scanner.nextDouble();
            
            if (amount < 0) {
                throw new IllegalArgumentException("Amount cannot be negative.");
            }
            if (amount > balance) {
                throw new ArithmeticException("Insufficient balance.");
            }
            
            balance -= amount;
            System.out.println("Please collect your cash. Remaining balance: " + balance);
            
        } catch (NumberFormatException | IllegalArgumentException e) {
            System.out.println("Input Error: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("Transaction Error: " + e.getMessage());
        } finally {
            System.out.println("ATM session complete. Please take your card.");
            scanner.close();
        }
    }
}
