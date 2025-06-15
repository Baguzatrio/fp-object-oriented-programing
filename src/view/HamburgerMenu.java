package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import controller.NavbarController;
import model.User;

public class HamburgerMenu {
    private JFrame parentFrame;
    private JPanel navBar, drawerPanel, glassPane;
    private boolean drawerOpen = false;
    private NavbarController navCon;
    private User currentUser;
    private JLayeredPane layeredPane;
    private JButton hamburger;

    public HamburgerMenu(JFrame parentFrame, User user) {
        this.parentFrame = parentFrame;
        this.currentUser = user;
        this.navCon = new NavbarController(parentFrame);

        createNavBar();
        createDrawer();
        createGlassPane();

        // Tambahkan navBar ke atas konten utama
        parentFrame.getContentPane().add(navBar, BorderLayout.NORTH);

        // Tambahkan drawer & glassPane ke layered pane
        layeredPane = parentFrame.getLayeredPane();
        layeredPane.add(drawerPanel, JLayeredPane.MODAL_LAYER);
        layeredPane.add(glassPane, JLayeredPane.PALETTE_LAYER);

        // Resize mengikuti parent frame
        parentFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateSizes();
            }
        });

        updateSizes(); // Panggil awal agar ukuran benar
    }

    private void createNavBar() {
        navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(51, 0, 204));
        navBar.setPreferredSize(new Dimension(parentFrame.getWidth(), 60));

        hamburger = new JButton("≡");
        hamburger.setFont(new Font("Segoe UI", Font.BOLD, 22));
        hamburger.setFocusPainted(false);
        hamburger.setContentAreaFilled(false);
        hamburger.setForeground(Color.WHITE);
        hamburger.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        hamburger.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hamburger.addActionListener(e -> toggleDrawer());

        JLabel title = new JLabel("ILBTRACE", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        navBar.add(hamburger, BorderLayout.WEST);
        navBar.add(title, BorderLayout.CENTER);
    }

    private void createDrawer() {
    drawerPanel = new JPanel(new BorderLayout());
    drawerPanel.setOpaque(false);
    drawerPanel.setVisible(false);
    
    // Panel utama drawer dengan gradasi
    JPanel drawerContent = new JPanel(new BorderLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color color1 = new Color(230, 240, 255);
            Color color2 = new Color(200, 220, 255);
            GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    };
    drawerContent.setPreferredSize(new Dimension(250, parentFrame.getHeight()));

    // HEADER (tetap sama seperti sebelumnya)
    JPanel header = new JPanel(new BorderLayout());
    header.setPreferredSize(new Dimension(250, 120));
    header.setBackground(new Color(51, 102, 204));
    header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    JPanel userInfo = new JPanel();
    userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
    userInfo.setOpaque(false);

    JLabel nameLabel = new JLabel(currentUser.getNama());
    nameLabel.setForeground(Color.WHITE);
    nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

    JLabel usernameLabel = new JLabel("@" + currentUser.getUsername());
    usernameLabel.setForeground(new Color(230, 230, 230));
    usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

    userInfo.add(nameLabel);
    userInfo.add(usernameLabel);
    header.add(userInfo, BorderLayout.CENTER);

    // MENU - Perbaikan di bagian ini
    JPanel content = new JPanel();
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Padding disesuaikan
    content.setOpaque(false);

    String[] menuItems = {
        "Dashboard", "Session", "Produk Olahan", "Produksi", "Pemasaran",
        "Bahan Baku", "Penyimpanan Resep", "Upah Pekerja", "Pengeluaran Lain", "Laporan Keuangan"
    };

    for (String item : menuItems) {
        JButton btn = new JButton(item);
        btn.setMaximumSize(new Dimension(220, 40));
        btn.setPreferredSize(new Dimension(220, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT); // Teks rata kiri
        btn.setFocusPainted(false);
        btn.setBackground(new Color(255, 255, 255, 150));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 220, 255)),
            BorderFactory.createEmptyBorder(5, 15, 5, 15) // Padding kiri-kanan diperbesar
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            navCon.navigateTo(item, currentUser);
            toggleDrawer();
        });
        content.add(btn);
        content.add(Box.createRigidArea(new Dimension(0, 8))); // Spasi antar tombol
    }

    // Tambahkan panel pembungkus untuk memberikan margin
    JPanel wrapper = new JPanel(new BorderLayout());
    wrapper.setOpaque(false);
    wrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    wrapper.add(content, BorderLayout.NORTH);

    JScrollPane scroll = new JScrollPane(wrapper);
    scroll.setBorder(null);
    scroll.setOpaque(false);
    scroll.getViewport().setOpaque(false);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.getViewport().setBorder(null); // Hilangkan border viewport

    // FOOTER
    JPanel footer = new JPanel(new BorderLayout());
    footer.setOpaque(false);
    footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

    JButton logoutBtn = new JButton("Logout");
    logoutBtn.setFocusPainted(false);
    logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    logoutBtn.setForeground(Color.WHITE);
    logoutBtn.setBackground(new Color(51, 102, 204));
    logoutBtn.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(255, 255, 255, 100)),
        BorderFactory.createEmptyBorder(8, 20, 8, 20)
    ));
    logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    logoutBtn.addActionListener(e -> navCon.logout());
    footer.add(logoutBtn, BorderLayout.CENTER);

    drawerContent.add(header, BorderLayout.NORTH);
    drawerContent.add(scroll, BorderLayout.CENTER);
    drawerContent.add(footer, BorderLayout.SOUTH);

    drawerPanel.add(drawerContent);
}

    private void createGlassPane() {
        glassPane = new JPanel();
        glassPane.setOpaque(false);
        glassPane.setVisible(false);
        glassPane.setBounds(0, 0, parentFrame.getWidth(), parentFrame.getHeight());

        glassPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleDrawer();
            }
        });
    }

    private void updateSizes() {
    int width = parentFrame.getWidth();
    int height = parentFrame.getHeight();

    navBar.setPreferredSize(new Dimension(width, 60));
    navBar.revalidate();

    // FIX: Tetap pertahankan width drawer 250px
    drawerPanel.setBounds(0, 0, 250, height); // <-- Ubah width tetap 250
    
    glassPane.setBounds(0, 0, width, height);
}

    public void toggleDrawer() {
        drawerOpen = !drawerOpen;
        drawerPanel.setVisible(drawerOpen);
        glassPane.setVisible(drawerOpen);
        drawerPanel.revalidate();
        drawerPanel.repaint();
    }

    public JPanel getNavBar() {
        return navBar;
    }

    public JPanel getDrawerPanel() {
        return drawerPanel;
    }
}
