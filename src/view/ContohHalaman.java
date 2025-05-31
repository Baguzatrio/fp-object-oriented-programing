package view;

import javax.swing.*;
import java.awt.*;

public class ContohHalaman extends javax.swing.JFrame {
    private HamburgerMenu hamburgerMenu;
    
    public ContohHalaman() {
        initComponents();
        
        // Buat panel utama dengan BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);
        
        // Tambahkan hamburger menu
        hamburgerMenu = new HamburgerMenu(mainPanel);
        
        // Tambahkan konten halaman lainnya
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(new JLabel("Ini adalah konten halaman"));
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void initComponents() {
        // Kode inisialisasi lainnya
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new ContohHalaman().setVisible(true);
        });
    }
}