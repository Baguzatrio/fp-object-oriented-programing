package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class HalfCircleButton extends JButton {
    public HalfCircleButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setBackground(new Color(70, 130, 180));
        setFont(new Font("SansSerif", Font.BOLD, 18));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int width = getWidth() * 2; // agar jadi setengah lingkaran
        int height = getHeight();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new Ellipse2D.Double(-getWidth(), 0, width, height)); // geser setengah ke kiri

        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(60, 60); // ukuran tombol
    }

    @Override
    protected void paintBorder(Graphics g) {
        // tanpa border
    }

    @Override
    public boolean contains(int x, int y) {
        int radius = getHeight() / 2;
        return (Math.pow(x - radius, 2) + Math.pow(y - radius, 2)) <= Math.pow(radius, 2);
    }
}
