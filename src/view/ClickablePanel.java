package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClickablePanel extends JPanel {
    private Color defaultBackground;
    private Color hoverBackground;
    private Color pressedBackground;
    private Runnable clickAction;

    public ClickablePanel(int radius, Color defaultBg, Color hoverBg, Color pressedBg) {
        super();
        this.defaultBackground = defaultBg;
        this.hoverBackground = hoverBg;
        this.pressedBackground = pressedBg;
        
        setOpaque(false);
        setBackground(defaultBackground);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickAction != null) {
                    clickAction.run();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBackground);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(defaultBackground);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedBackground);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverBackground);
            }
        });
    }

    public void setClickAction(Runnable action) {
        this.clickAction = action;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.dispose();
        super.paintComponent(g);
    }
}