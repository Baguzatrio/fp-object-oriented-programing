/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.view;

import com.ikanlautbarokah.controller.DashboardController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author Satrio Aji
 */
public class DashboardFrame extends JFrame {

    private DashboardController controller; // Referensi ke Controller

    public DashboardFrame() {
        setTitle("Dashboard - Ikan Laut Barokah");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();

        // Inisialisasi Controller dan berikan referensi ke View ini
        this.controller = new DashboardController(this);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Dashboard - Ikan Laut Barokah", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Create buttons
        JButton produkOlahanBtn = createMenuButton("Produk Olahan");
        JButton bahanBakuBtn = createMenuButton("Manajemen Bahan Baku");
        JButton resepBtn = createMenuButton("Manajemen Resep");
        JButton produksiBtn = createMenuButton("Produksi & Pengemasan");
        JButton distribusiBtn = createMenuButton("Distribusi & Penjualan");
        JButton upahBtn = createMenuButton("Penghitungan Upah");
        JButton laporanBtn = createMenuButton("Laporan Keuangan");

        // Add action listeners (delegasikan ke controller)
        produkOlahanBtn.addActionListener(e -> controller.navigateToProdukOlahan());
        bahanBakuBtn.addActionListener(e -> controller.navigateToBahanBaku());
        resepBtn.addActionListener(e -> controller.navigateToResep());
        produksiBtn.addActionListener(e -> controller.navigateToProduksi());
        distribusiBtn.addActionListener(e -> controller.navigateToDistribusi());
        upahBtn.addActionListener(e -> controller.navigateToUpah());
        laporanBtn.addActionListener(e -> controller.navigateToLaporanKeuangan());

        // Add buttons to panel
        mainPanel.add(produkOlahanBtn);
        mainPanel.add(bahanBakuBtn);
        mainPanel.add(resepBtn);
        mainPanel.add(produksiBtn);
        mainPanel.add(distribusiBtn);
        mainPanel.add(upahBtn);
        mainPanel.add(laporanBtn);

        add(mainPanel, BorderLayout.CENTER);

        // Footer with logout
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(new Color(245, 245, 245));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setPreferredSize(new Dimension(100, 35));
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font("Arial", Font.PLAIN, 12));

        logoutBtn.addActionListener(e -> controller.handleLogout()); // Delegasikan ke controller

        footerPanel.add(logoutBtn);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 80));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(33, 37, 41));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Simple hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(248, 249, 250));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }
}
