// Task 3: Campus Library Search Screen using BorderLayout and FlowLayout
import javax.swing.*;
import java.awt.*;

public class LibrarySearchScreen extends JFrame {
    public LibrarySearchScreen() {
        super("Campus Library Search Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout());

        // Top Search Row Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField searchField = new JTextField(20);
        JComboBox<String> categoryBox = new JComboBox<>(new String[]{"Books", "Journals", "Theses", "Magazines"});
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Keyword:"));
        searchPanel.add(searchField);
        searchPanel.add(categoryBox);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Center Results Area
        JTextArea resultsArea = new JTextArea("Search results will appear here...\n");
        resultsArea.setEditable(false);
        add(new JScrollPane(resultsArea), BorderLayout.CENTER);

        // Bottom Navigation & Action Row
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton prevButton = new JButton("Prev");
        JButton nextButton = new JButton("Next");
        JButton reserveButton = new JButton("Reserve");
        bottomPanel.add(prevButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(reserveButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Status bar
        JLabel statusLabel = new JLabel(" Status: Ready");
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(bottomPanel, BorderLayout.NORTH);
        statusPanel.add(statusLabel, BorderLayout.SOUTH);
        add(statusPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarySearchScreen().setVisible(true));
    }
}
