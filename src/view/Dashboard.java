package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import controller.page3controller;
import java.awt.geom.Path2D;
import model.User;

public class Dashboard extends JFrame {
    private HamburgerMenu hamburgerMenu;
    private User currentUser;
    private JPanel contentPanel;
    private GradientRoundedPanel iconContainer;
    private JPanel titlePanel;
    private JLabel titleLabel;
    private RoundedPanel[] iconPanels;
    private JLabel[] iconLabels;
    private JLabel[] textLabels;

    public Dashboard(User user) {
        this.currentUser = user;
        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setLayout(new BorderLayout());
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Navbar dan drawer
        hamburgerMenu = new HamburgerMenu(this, currentUser);
        getContentPane().add(hamburgerMenu.getNavBar(), BorderLayout.NORTH);

        // Content
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        // Title
        titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        titleLabel = new JLabel("Produksi Lancar, Data Tercatat, Bisnis Terkontrol");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        titleLabel.setForeground(new Color(51, 51, 255));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        titlePanel.add(titleLabel);

        // Icon Container (gradasi + rounded)
        iconContainer = new GradientRoundedPanel(45, new Color(135, 206, 250), new Color(173, 216, 230));
        iconContainer.setLayout(new GridLayout(1, 3, 30, 0));
        iconContainer.setOpaque(false);
        iconContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        String[] iconTexts = {"Daftar Produksi", "Produksi", "Distribusi"};
        iconPanels = new RoundedPanel[3];
        iconLabels = new JLabel[3];
        textLabels = new JLabel[3];

        for (int i = 0; i < 3; i++) {
            JPanel container = new JPanel(new BorderLayout());
            container.setOpaque(false);

            iconPanels[i] = new RoundedPanel(40);
            iconPanels[i].setBackground(new Color(255, 255, 255));
            iconPanels[i].setLayout(new GridBagLayout());
            iconPanels[i].setPreferredSize(new Dimension(100, 80));

            iconLabels[i] = new JLabel();
            iconPanels[i].add(iconLabels[i]);

            textLabels[i] = new JLabel(iconTexts[i], SwingConstants.CENTER);
            textLabels[i].setFont(new Font("Segoe UI", Font.BOLD, 18));
            JPanel labelPanel = new JPanel();
            labelPanel.setOpaque(false);
            labelPanel.add(textLabels[i]);

            container.add(iconPanels[i], BorderLayout.CENTER);
            container.add(labelPanel, BorderLayout.SOUTH);

            iconContainer.add(container);
        }

        contentPanel.add(titlePanel, BorderLayout.NORTH);
        contentPanel.add(iconContainer, BorderLayout.CENTER);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        addIcons();
        new page3controller(this, currentUser);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeIcons();
            }
        });

        setVisible(true);
    }

    private void addIcons() {
        String[] iconPaths = {
            "/fppbo/icons/factory.png",
            "/fppbo/icons/package.png",
            "/fppbo/icons/truck.png"
        };

        for (int i = 0; i < iconPanels.length; i++) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(iconPaths[i]));
                iconLabels[i].setIcon(icon);
                resizeIcon(i);
            } catch (Exception e) {
                JLabel placeholder = new JLabel("Icon " + (i + 1));
                iconPanels[i].add(placeholder);
            }
        }
    }

    private void resizeIcons() {
        for (int i = 0; i < iconPanels.length; i++) {
            resizeIcon(i);
        }
    }

    private void resizeIcon(int index) {
        int size = Math.min(iconPanels[index].getWidth(), iconPanels[index].getHeight());
        int iconSize = Math.max(30, size * 2 / 3);

        ImageIcon original = (ImageIcon) iconLabels[index].getIcon();
        if (original != null) {
            Image scaled = original.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            iconLabels[index].setIcon(new ImageIcon(scaled));
        }
    }

    public JPanel getDaftarProduksiPanel() {
        return (JPanel) iconPanels[0].getParent();
    }

    public JPanel getProduksiPanel() {
        return (JPanel) iconPanels[1].getParent();
    }

    public JPanel getDistribusiPanel() {
        return (JPanel) iconPanels[2].getParent();
    }
}

class GradientRoundedPanel extends JPanel {
    private int cornerRadius;
    private Color colorStart;
    private Color colorEnd;

    public GradientRoundedPanel(int radius, Color start, Color end) {
        this.cornerRadius = radius;
        this.colorStart = start;
        this.colorEnd = end;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gradient = new GradientPaint(0, 0, colorStart, 0, height, colorEnd);
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
        super.paintComponent(g);
    }
}

class RoundedPanel extends JPanel {
    private int cornerRadius;

    public RoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        graphics.dispose();
    }
}

class RoundedPanel1 extends JPanel {
    private int arc;

    public RoundedPanel1(int arc) {
        this.arc = arc;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Path2D path = new Path2D.Double();
        path.moveTo(0, h);
        path.lineTo(0, arc);
        path.quadTo(0, 0, arc, 0);
        path.lineTo(w - arc, 0);
        path.quadTo(w, 0, w, arc);
        path.lineTo(w, h);
        path.closePath();

        g2.setColor(getBackground());
        g2.fill(path);

        g2.dispose();
    }
}
