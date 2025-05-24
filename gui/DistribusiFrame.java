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

public class DistribusiFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public DistribusiFrame() {
        setTitle("Distribusi & Penjualan Produk");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        loadDistribusi();
    }

    private void initComponents() {
        String[] kolom = {"ID", "Tanggal", "Produk", "Jumlah", "Tujuan", "Harga Total", "Metode Bayar"};
        tableModel = new DefaultTableModel(null, kolom);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        JButton tambahBtn = new JButton("Input Distribusi");
        tambahBtn.addActionListener(e -> inputDistribusi());

        add(scrollPane, BorderLayout.CENTER);
        add(tambahBtn, BorderLayout.SOUTH);
    }

    private void loadDistribusi() {
        tableModel.setRowCount(0);
        try (Connection conn = Koneksi.getConnection()) {
            String sql = "SELECT * FROM distribusi";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getDate("tanggal"),
                    rs.getString("produk"),
                    rs.getInt("jumlah"),
                    rs.getString("tujuan"),
                    rs.getDouble("harga_total"),
                    rs.getString("metode_bayar")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data distribusi: " + e.getMessage());
        }
    }

    private void inputDistribusi() {
        try (Connection conn = Koneksi.getConnection()) {
            JTextField produkField = new JTextField();
            JTextField jumlahField = new JTextField();
            JTextField tujuanField = new JTextField();
            JTextField hargaField = new JTextField();

            JComboBox<String> metodeBayar = new JComboBox<>(new String[]{"Tunai", "Tempo"});

            JPanel panel = new JPanel(new GridLayout(5, 2));
            panel.add(new JLabel("Produk:")); panel.add(produkField);
            panel.add(new JLabel("Jumlah (kemasan):")); panel.add(jumlahField);
            panel.add(new JLabel("Tujuan:")); panel.add(tujuanField);
            panel.add(new JLabel("Harga Total:")); panel.add(hargaField);
            panel.add(new JLabel("Metode Bayar:")); panel.add(metodeBayar);

            int result = JOptionPane.showConfirmDialog(this, panel, "Input Distribusi", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String produk = produkField.getText();
                int jumlah = Integer.parseInt(jumlahField.getText());
                String tujuan = tujuanField.getText();
                double harga = Double.parseDouble(hargaField.getText());
                String metode = (String) metodeBayar.getSelectedItem();

                // Simpan ke tabel distribusi
                String insertSql = "INSERT INTO distribusi (tanggal, produk, jumlah, tujuan, harga_total, metode_bayar) VALUES (CURRENT_DATE(), ?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, produk);
                insertStmt.setInt(2, jumlah);
                insertStmt.setString(3, tujuan);
                insertStmt.setDouble(4, harga);
                insertStmt.setString(5, metode);
                insertStmt.executeUpdate();

                loadDistribusi();
                JOptionPane.showMessageDialog(this, "Distribusi berhasil dicatat.");
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Gagal input distribusi: " + e.getMessage());
        }
    }
}
