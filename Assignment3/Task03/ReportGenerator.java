// Task 3: Daily Sales Report Generator - Try-with-resources vs Manual Close
import java.io.*;

public class ReportGenerator {
    // Manual Close Version
    public static void generateManual(String inputFileName, String outputFileName) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(inputFileName));
            writer = new BufferedWriter(new FileWriter(outputFileName));
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Manual Error: " + e.getMessage());
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) { e.printStackTrace(); }
            try {
                if (writer != null) writer.close();
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    // Try-with-Resources Version
    public static void generateAuto(String inputFileName, String outputFileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Auto Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Create a dummy file for testing
        try (PrintWriter pw = new PrintWriter("store1.txt")) {
            pw.println("Store 1 Daily Sales: $1200");
        } catch (IOException e) { e.printStackTrace(); }

        generateAuto("store1.txt", "summary.txt");
        System.out.println("Report generated successfully using try-with-resources.");
    }
}
