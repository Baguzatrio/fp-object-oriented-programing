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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BahanBakuFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public BahanBakuFrame() {
        setTitle("Manajemen Bahan Baku");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        loadData();
    }

    private void initComponents() {
        String[] kolom = {"ID", "Nama Bahan", "Stok", "Satuan", "Harga", "Tgl Beli", "Tgl Kadaluwarsa"};
        tableModel = new DefaultTableModel(null, kolom);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        JButton tambahBtn = new JButton("Tambah Bahan Baku");
        tambahBtn.addActionListener(e -> tambahBahanBaku());

        add(scrollPane, BorderLayout.CENTER);
        add(tambahBtn, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = Koneksi.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM bahan_baku");
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getDouble("stok"),
                    rs.getString("satuan"),
                    rs.getDouble("harga_per_unit"),
                    rs.getDate("tanggal_beli"),
                    rs.getDate("tanggal_kadaluarsa")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
    }

    private void tambahBahanBaku() {
        JTextField namaField = new JTextField();
        JTextField stokField = new JTextField();
        JTextField satuanField = new JTextField();
        JTextField hargaField = new JTextField();
        JTextField tglBeliField = new JTextField("YYYY-MM-DD");
        JTextField tglKadaluarsaField = new JTextField("YYYY-MM-DD");

        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.add(new JLabel("Nama Bahan:")); inputPanel.add(namaField);
        inputPanel.add(new JLabel("Stok:")); inputPanel.add(stokField);
        inputPanel.add(new JLabel("Satuan:")); inputPanel.add(satuanField);
        inputPanel.add(new JLabel("Harga/Unit:")); inputPanel.add(hargaField);
        inputPanel.add(new JLabel("Tanggal Beli:")); inputPanel.add(tglBeliField);
        inputPanel.add(new JLabel("Tanggal Kadaluwarsa:")); inputPanel.add(tglKadaluarsaField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Tambah Bahan Baku", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = Koneksi.getConnection()) {
                String sql = "INSERT INTO bahan_baku (nama, stok, satuan, harga_per_unit, tanggal_beli, tanggal_kadaluarsa) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, namaField.getText());
                stmt.setDouble(2, Double.parseDouble(stokField.getText()));
                stmt.setString(3, satuanField.getText());
                stmt.setDouble(4, Double.parseDouble(hargaField.getText()));
                stmt.setDate(5, Date.valueOf(tglBeliField.getText()));
                stmt.setDate(6, Date.valueOf(tglKadaluarsaField.getText()));
                stmt.executeUpdate();
                loadData();
            } catch (SQLException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Gagal menambah data: " + ex.getMessage());
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            BahanBakuFrame frame = new BahanBakuFrame();
            frame.setVisible(true);
            
            // Test connection immediately
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

