// Task 4: Draw Pad with Repaint and Point Storing
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DrawPad extends Frame {
    private final List<Point> points = new ArrayList<>();
    private final Label status = new Label("Clicks: 0");
    private int count = 0;

    public DrawPad() {
        super("Draw Pad");
        setLayout(new BorderLayout());
        add(status, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO 1: Override mouseClicked correctly: store the point, update the label and request a repaint
                points.add(e.getPoint());
                count++;
                status.setText("Clicks: " + count);
                repaint();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        setSize(400, 300);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // TODO 2: Draw every stored point as a 5x6 filled circle centred on the point
        g.setColor(Color.BLUE);
        for (Point p : points) {
            g.fillOval(p.x - 2, p.y - 3, 5, 6);
        }
    }

    public static void main(String[] args) {
        new DrawPad();
    }
}
