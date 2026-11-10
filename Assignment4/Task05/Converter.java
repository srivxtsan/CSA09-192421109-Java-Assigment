// Task 5: Currency Converter Lambda Version
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Converter extends JFrame {
    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JLabel result = new JLabel("");

    private static double rate(String currency) {
        return currency.equals("USD") ? 0.012 : 0.011;
    }

    public Converter() {
        super("Currency Converter");
        setLayout(new FlowLayout());

        JButton convertButton = new JButton("Convert");
        add(new JLabel("INR")); 
        add(amount); 
        add(target);
        add(convertButton); 
        add(result);

        // TODO 1: Register ONE lambda listener for both the Convert button and the Enter key in amount field
        ActionListener convertAction = e -> convert();
        convertButton.addActionListener(convertAction);
        amount.addActionListener(convertAction); // Triggers on Enter key

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void convert() {
        // TODO 2: Parse the amount and show the converted value with 2 decimals, or "Invalid amount" if parsing fails
        try {
            double val = Double.parseDouble(amount.getText().trim());
            String curr = (String) target.getSelectedItem();
            double res = val * rate(curr);
            result.setText(String.format("= %.2f %s", res, curr));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
            result.setText("Invalid amount");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Converter().setVisible(true));
    }
}
