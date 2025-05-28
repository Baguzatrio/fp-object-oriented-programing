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

public class UpahFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public UpahFrame() {
        setTitle("Penghitungan Upah Pegawai");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        loadData();
    }

    private void initComponents() {
        String[] kolom = {"ID", "Nama Pegawai", "Jumlah Produksi", "Upah per Produksi", "Total Upah"};
        tableModel = new DefaultTableModel(null, kolom);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        JButton tambahBtn = new JButton("Input Upah");
        tambahBtn.addActionListener(e -> inputUpah());

        add(scrollPane, BorderLayout.CENTER);
        add(tambahBtn, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = Koneksi.getConnection()) {
            String sql = "SELECT * FROM upah";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("nama_pegawai"),
                    rs.getInt("jumlah_produksi"),
                    rs.getDouble("upah_per_unit"),
                    rs.getDouble("total_upah")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data upah: " + e.getMessage());
        }
    }

    private void inputUpah() {
        JTextField namaField = new JTextField();
        JTextField jumlahField = new JTextField();
        JTextField perUnitField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Nama Pegawai:")); panel.add(namaField);
        panel.add(new JLabel("Jumlah Produksi:")); panel.add(jumlahField);
        panel.add(new JLabel("Upah per Produksi:")); panel.add(perUnitField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Input Upah", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = Koneksi.getConnection()) {
                String nama = namaField.getText();
                int jumlah = Integer.parseInt(jumlahField.getText());
                double upahPerUnit = Double.parseDouble(perUnitField.getText());
                double total = jumlah * upahPerUnit;

                String sql = "INSERT INTO upah (nama_pegawai, jumlah_produksi, upah_per_unit, total_upah) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nama);
                stmt.setInt(2, jumlah);
                stmt.setDouble(3, upahPerUnit);
                stmt.setDouble(4, total);
                stmt.executeUpdate();
                loadData();
                JOptionPane.showMessageDialog(this, "Data upah berhasil disimpan.");
            } catch (SQLException | NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Gagal input upah: " + e.getMessage());
            }
        }
    }
}

