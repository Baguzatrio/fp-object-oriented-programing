
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class testpage3 extends JFrame {
    private JPanel drawer;
    private JPanel contentPanel;
    private JButton btnHamburger;
    private JButton btnLogout;

    private JPanel panelProduksi;
    private JPanel panelPengemasan;
    private JPanel panelDistribusi;

    public testpage3() {
        setTitle("Page3 - Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Navbar
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(new Color(135, 206, 250));
        navbar.setPreferredSize(new Dimension(getWidth(), 50));

        btnHamburger = new JButton("\u2630");
        btnHamburger.setFont(new Font("Arial", Font.BOLD, 24));
        btnHamburger.setFocusPainted(false);
        btnHamburger.setBorder(null);
        btnHamburger.setContentAreaFilled(false);
        btnHamburger.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navbar.add(btnHamburger, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        navbar.add(titleLabel, BorderLayout.CENTER);

        add(navbar, BorderLayout.NORTH);

        // Drawer
        drawer = new JPanel(new BorderLayout());
        drawer.setBackground(new Color(220, 220, 220));
        drawer.setPreferredSize(new Dimension(250, getHeight()));
        drawer.setVisible(false);

        add(drawer, BorderLayout.WEST);

        // Konten utama
        contentPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelProduksi = createClickablePanel("Produksi", new Color(135, 206, 235));
        panelPengemasan = createClickablePanel("Pengemasan", new Color(100, 149, 237));
        panelDistribusi = createClickablePanel("Distribusi", new Color(70, 130, 180));

        contentPanel.add(panelProduksi);
        contentPanel.add(panelPengemasan);
        contentPanel.add(panelDistribusi);

        add(contentPanel, BorderLayout.CENTER);
    }

    public void setupDrawer(String username) {
        drawer.removeAll();

        // Header
        JPanel drawerHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 25));
        drawerHeader.setBackground(new Color(100, 149, 237));
        drawerHeader.setPreferredSize(new Dimension(250, 80));
        JLabel lblUser = new JLabel("User: " + username);
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 16));
        drawerHeader.add(lblUser);
        drawer.add(drawerHeader, BorderLayout.NORTH);

        // Isi - button menu
        JPanel drawerBody = new JPanel(new GridLayout(0, 1, 5, 5));
        drawerBody.setBackground(drawer.getBackground());
        drawerBody.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        String[] menuItems = {"Produksi", "Pengemasan", "Distribusi"};
        for (String menuItem : menuItems) {
            JButton btn = new JButton(menuItem);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setActionCommand(menuItem);
            drawerBody.add(btn);
        }
        drawer.add(drawerBody, BorderLayout.CENTER);

        // Footer - logout
        JPanel drawerFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        drawerFooter.setBackground(drawer.getBackground());
        drawerFooter.setPreferredSize(new Dimension(250, 60));
        btnLogout = new JButton("Logout");
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        drawerFooter.add(btnLogout);
        drawer.add(drawerFooter, BorderLayout.SOUTH);

        drawer.revalidate();
        drawer.repaint();
    }

    private JPanel createClickablePanel(String title, Color bgColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(Color.WHITE);
        panel.add(lbl, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(bgColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                panel.setBackground(bgColor);
            }
        });

        return panel;
    }

    // Getter tombol & panel supaya controller bisa pasang listener
    public JButton getBtnHamburger() {
        return btnHamburger;
    }

    public JButton getBtnLogout() {
        return btnLogout;
    }

    public JPanel getDrawer() {
        return drawer;
    }

    public JPanel getPanelProduksi() {
        return panelProduksi;
    }

    public JPanel getPanelPengemasan() {
        return panelPengemasan;
    }

    public JPanel getPanelDistribusi() {
        return panelDistribusi;
    }

    public void setDrawerVisible(boolean visible) {
        drawer.setVisible(visible);
    }

    public boolean isDrawerVisible() {
        return drawer.isVisible();
    }
}



