/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

/**
 *
 * @author ASUS
 */
import db.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ResepFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public ResepFrame() {
        setTitle("Manajemen Resep");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        loadResep();
    }

    private void initComponents() {
        String[] kolom = {"ID", "Nama Produk"};
        tableModel = new DefaultTableModel(null, kolom);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton tambahBtn = new JButton("Tambah Resep Baru");
        JButton detailBtn = new JButton("Detail Komposisi");

        tambahBtn.addActionListener(e -> tambahResep());
        detailBtn.addActionListener(e -> lihatDetailKomposisi());

        buttonPanel.add(tambahBtn);
        buttonPanel.add(detailBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadResep() {
        tableModel.setRowCount(0);
        try (Connection conn = Koneksi.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM resep");
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("nama_produk")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat resep: " + e.getMessage());
        }
    }

    private void tambahResep() {
        String namaProduk = JOptionPane.showInputDialog(this, "Masukkan nama produk baru:");
        if (namaProduk != null && !namaProduk.isEmpty()) {
            try (Connection conn = Koneksi.getConnection()) {
                String sql = "INSERT INTO resep (nama_produk) VALUES (?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, namaProduk);
                stmt.executeUpdate();
                loadResep();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menambah resep: " + e.getMessage());
            }
        }
    }

    private void lihatDetailKomposisi() {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            int resepId = (int) table.getValueAt(selected, 0);
            String namaProduk = (String) table.getValueAt(selected, 1);
            new ResepDetailFrame(resepId, namaProduk).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Pilih resep yang ingin dilihat.");
        }
    }
}
