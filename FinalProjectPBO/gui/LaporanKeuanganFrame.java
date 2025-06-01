/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

/**
 *
 * @author ASUS
 */
//
import db.Koneksi;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LaporanKeuanganFrame extends JFrame {
    public LaporanKeuanganFrame() {
        setTitle("Laporan Keuangan");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel pendapatanLabel = new JLabel("Total Pendapatan:");
        JLabel biayaProduksiLabel = new JLabel("Biaya Produksi:");
        JLabel upahLabel = new JLabel("Total Upah Dibayar:");
        JLabel labaLabel = new JLabel("Laba / Rugi Bersih:");

        JLabel pendapatanVal = new JLabel();
        JLabel biayaVal = new JLabel();
        JLabel upahVal = new JLabel();
        JLabel labaVal = new JLabel();

        try (Connection conn = Koneksi.getConnection()) {
            // Total pendapatan
            String sql1 = "SELECT SUM(harga_total) FROM distribusi";
            ResultSet rs1 = conn.createStatement().executeQuery(sql1);
            double pendapatan = rs1.next() ? rs1.getDouble(1) : 0.0;
            pendapatanVal.setText("Rp " + pendapatan);

            // Biaya produksi (asumsi 5000 per kg)
            String sql2 = "SELECT SUM(total_kg) FROM produksi";
            ResultSet rs2 = conn.createStatement().executeQuery(sql2);
            double totalKg = rs2.next() ? rs2.getDouble(1) : 0.0;
            double biayaProduksi = totalKg * 5000; // bisa disesuaikan
            biayaVal.setText("Rp " + biayaProduksi);

            // Total upah
            String sql3 = "SELECT SUM(total_upah) FROM upah";
            ResultSet rs3 = conn.createStatement().executeQuery(sql3);
            double totalUpah = rs3.next() ? rs3.getDouble(1) : 0.0;
            upahVal.setText("Rp " + totalUpah);

            // Laba / Rugi
            double laba = pendapatan - biayaProduksi - totalUpah;
            labaVal.setText("Rp " + laba);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghitung laporan: " + e.getMessage());
        }

        panel.add(pendapatanLabel); panel.add(pendapatanVal);
        panel.add(biayaProduksiLabel); panel.add(biayaVal);
        panel.add(upahLabel); panel.add(upahVal);
        panel.add(labaLabel); panel.add(labaVal);

        add(panel);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
               
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
          
            LaporanKeuanganFrame frame = new LaporanKeuanganFrame();
            frame.setVisible(true);
            
        
            try (Connection testConn = Koneksi.getConnection()) {
                if (testConn != null) {
                    System.out.println("Database connection successful!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Database connection failed!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Connection test failed: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
}
