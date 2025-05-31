package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import controller.NavbarController;
import model.User;

public class HamburgerMenu {
    private JPanel drawerPanel;
    private JButton hamburgerButton;
    private boolean drawerOpen = false;
    private JPanel parentPanel;
    private JPanel navBar;
    private NavbarController navCon;
    private User currentUser;

    public HamburgerMenu(JPanel parentPanel, User user) {
        this.parentPanel = parentPanel;
        this.currentUser = user;
        this.navCon = new NavbarController();
        createNavBar();
        createDrawer();
        
        // Pastikan z-order benar
        parentPanel.setComponentZOrder(navBar, 0);
        parentPanel.setComponentZOrder(drawerPanel, 1);
    }

    private void createNavBar() {
    // Buat panel navbar
    navBar = new JPanel();
    navBar.setBackground(new Color(51, 0, 204)); // Warna biru tua
    navBar.setLayout(new BorderLayout());
    navBar.setPreferredSize(new Dimension(parentPanel.getWidth(), 60));
    
    // Panel untuk tombol hamburger (agar bisa di-align ke kiri)
    JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
    leftPanel.setOpaque(false);
    
    // BUAT TOMBOL HAMBURGER YANG PASTI TERLIHAT
    hamburgerButton = new JButton();
    hamburgerButton.setText("≡"); // Alternatif: "☰" atau "≡" atau "|||"
    hamburgerButton.setFont(new Font("Segoe UI", Font.PLAIN, 30)); // Ukuran lebih besar
    hamburgerButton.setForeground(Color.WHITE);
    hamburgerButton.setBackground(new Color(51, 0, 204));
    hamburgerButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    hamburgerButton.setFocusPainted(false);
    hamburgerButton.setContentAreaFilled(false);
    
    // Tambahkan border sementara untuk debugging
    hamburgerButton.setBorder(BorderFactory.createLineBorder(Color.RED));
    
    hamburgerButton.addActionListener(e -> toggleDrawer());
    
    leftPanel.add(hamburgerButton);
    navBar.add(leftPanel, BorderLayout.WEST);
    
    // Tambahkan judul aplikasi di tengah
    JLabel titleLabel = new JLabel("ILBTRACE");
    titleLabel.setForeground(Color.WHITE);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    titleLabel.setHorizontalAlignment(JLabel.CENTER);
    navBar.add(titleLabel, BorderLayout.CENTER);
    
    parentPanel.add(navBar, BorderLayout.NORTH);
}

    private void createDrawer() {
        // Create drawer panel
        drawerPanel = new JPanel();
        drawerPanel.setLayout(new BorderLayout());
        drawerPanel.setBackground(new Color(240, 240, 240));
        
        // Atur ukuran drawer agar full height
        drawerPanel.setBounds(-250, navBar.getHeight(), 250, 
                           parentPanel.getHeight() - navBar.getHeight());
        drawerPanel.setVisible(false);
        
        // Header with user info and close button
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(51, 0, 204));
        headerPanel.setPreferredSize(new Dimension(250, 100));
        headerPanel.setLayout(new BorderLayout());
        
        // Panel untuk nama dan username
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setOpaque(false);
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Label nama lengkap
        JLabel fullNameLabel = new JLabel(currentUser.getNama());
        fullNameLabel.setForeground(Color.WHITE);
        fullNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        // Label username
        JLabel usernameLabel = new JLabel("@" + currentUser.getUsername());
        usernameLabel.setForeground(new Color(200, 200, 200));
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        userInfoPanel.add(fullNameLabel);
        userInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        userInfoPanel.add(usernameLabel);
        
        headerPanel.add(userInfoPanel, BorderLayout.CENTER);
        drawerPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Menu items
        JPanel menuPanel = new JPanel();
       
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        menuPanel.setBackground(new Color(240, 240, 240));
        
        String[] menuItems = {
            "Dashboard", 
            "Produk Olahan", 
            "Produksi", 
            "Pemasaran", 
            "Bahan Baku", 
            "Penyimpanan Resep", 
            "Perhitungan Upah", 
            "Laporan Keuangan"
        };
        
        for (String item : menuItems) {
            JButton menuButton = new JButton(item);
            menuButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            menuButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            menuButton.setBackground(Color.WHITE);
            menuButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            menuButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            menuButton.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            menuButton.setBackground(new Color(200, 230, 255)); // Warna biru muda
            menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            menuButton.setBackground(Color.WHITE);
            menuButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    });
            // Tambahkan action untuk navigasi
            menuButton.addActionListener(e -> {
                navCon.navigateTo(item, currentUser);
                toggleDrawer(); // Tutup drawer setelah navigasi
            });
            
            menuPanel.add(menuButton);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        // Tambahkan scroll pane untuk responsivitas
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        drawerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Footer with logout button
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        footerPanel.setBackground(new Color(240, 240, 240));
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutButton.addActionListener(e -> System.exit(0));
        footerPanel.add(logoutButton, BorderLayout.CENTER);
        
        drawerPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Add drawer to the parent panel
        parentPanel.add(drawerPanel);
    }

public void toggleDrawer() {
    drawerOpen = !drawerOpen;
    
    if (drawerOpen) {
        // Buka drawer dan tutup konten utama
        drawerPanel.setVisible(true);
        parentPanel.getParent().setEnabled(false); // Nonaktifkan interaksi dengan konten utama
        animateDrawer(250, 0);
    } else {
        // Tutup drawer dan aktifkan konten utama
        animateDrawer(0, 250, () -> {
            drawerPanel.setVisible(false);
            parentPanel.getParent().setEnabled(true);
        });
    }
}

private void animateDrawer(int start, int end, Runnable onFinish) {
    new Thread(() -> {
        int step = (start < end) ? 15 : -15;
        for (int i = start; (step > 0) ? i <= end : i >= end; i += step) {
            final int pos = i;
            SwingUtilities.invokeLater(() -> {
                drawerPanel.setBounds(-pos, navBar.getHeight(), 250, 
                                   parentPanel.getHeight() - navBar.getHeight());
            });
            try { Thread.sleep(5); } catch (Exception e) {}
        }
        SwingUtilities.invokeLater(onFinish);
    }).start();
}

private void animateDrawer(int start, int end) {
    animateDrawer(start, end, () -> {});
}

    public JPanel getNavBar() {
        return navBar;
    }

    public JPanel getDrawerPanel() {
        return drawerPanel;
    }

}