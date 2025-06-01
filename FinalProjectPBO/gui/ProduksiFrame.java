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
import java.util.HashMap;
import java.util.Map;

public class ProduksiFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public ProduksiFrame() {
        setTitle("Produksi & Pengemasan");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        loadProduksi();
    }

    private void initComponents() {
        String[] kolom = {"ID", "Tanggal", "Produk", "Jumlah Batch", "Total Kg", "Jumlah Kemasan"};
        tableModel = new DefaultTableModel(null, kolom);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        JButton tambahBtn = new JButton("Input Produksi");
        tambahBtn.addActionListener(e -> inputProduksi());

        add(scrollPane, BorderLayout.CENTER);
        add(tambahBtn, BorderLayout.SOUTH);
    }

    private void loadProduksi() {
        tableModel.setRowCount(0);
        try (Connection conn = Koneksi.getConnection()) {
            String sql = "SELECT * FROM produksi";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getDate("tanggal"),
                    rs.getString("produk"),
                    rs.getInt("jumlah_batch"),
                    rs.getDouble("total_kg"),
                    rs.getInt("jumlah_kemasan")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data produksi: " + e.getMessage());
        }
    }

    private void inputProduksi() {
        try (Connection conn = Koneksi.getConnection()) {
            JComboBox<String> resepCombo = new JComboBox<>();
            Map<String, Integer> resepMap = new HashMap<>();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, nama_produk FROM resep");
            while (rs.next()) {
                String nama = rs.getString("nama_produk");
                int id = rs.getInt("id");
                resepCombo.addItem(nama);
                resepMap.put(nama, id);
            }

            JTextField batchField = new JTextField();
            JTextField kgField = new JTextField();

            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Pilih Produk (Resep):")); panel.add(resepCombo);
            panel.add(new JLabel("Jumlah Batch:")); panel.add(batchField);
            panel.add(new JLabel("Total Kilogram Produk:")); panel.add(kgField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Input Produksi", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String selected = (String) resepCombo.getSelectedItem();
                int resepId = resepMap.get(selected);
                int batch = Integer.parseInt(batchField.getText());
                double kg = Double.parseDouble(kgField.getText());

                // Kurangi stok bahan sesuai resep
                String queryDetail = "SELECT bahan_baku_id, jumlah FROM resep_detail WHERE resep_id = ?";
                PreparedStatement detailStmt = conn.prepareStatement(queryDetail);
                detailStmt.setInt(1, resepId);
                ResultSet detailRS = detailStmt.executeQuery();
                boolean cukupStok = true;

                Map<Integer, Double> bahanUpdate = new HashMap<>();
                while (detailRS.next()) {
                    int bahanId = detailRS.getInt("bahan_baku_id");
                    double jumlahPerBatch = detailRS.getDouble("jumlah");
                    double total = jumlahPerBatch * batch;
                    // Cek stok tersedia
                    PreparedStatement cekStmt = conn.prepareStatement("SELECT stok FROM bahan_baku WHERE id = ?");
                    cekStmt.setInt(1, bahanId);
                    ResultSet cekRS = cekStmt.executeQuery();
                    if (cekRS.next()) {
                        double stok = cekRS.getDouble("stok");
                        if (stok < total) cukupStok = false;
                        bahanUpdate.put(bahanId, stok - total);
                    }
                }

                if (!cukupStok) {
                    JOptionPane.showMessageDialog(this, "Stok bahan baku tidak mencukupi!");
                    return;
                }

                // Update stok bahan baku
                for (Map.Entry<Integer, Double> entry : bahanUpdate.entrySet()) {
                    PreparedStatement updateStmt = conn.prepareStatement("UPDATE bahan_baku SET stok = ? WHERE id = ?");
                    updateStmt.setDouble(1, entry.getValue());
                    updateStmt.setInt(2, entry.getKey());
                    updateStmt.executeUpdate();
                }

                // Simpan data produksi
                int jumlahKemasan = (int) (kg * 1000 / 500); // Asumsi 1 kemasan = 500g
                String insertSql = "INSERT INTO produksi (tanggal, produk, jumlah_batch, total_kg, jumlah_kemasan) VALUES (CURRENT_DATE(), ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, selected);
                insertStmt.setInt(2, batch);
                insertStmt.setDouble(3, kg);
                insertStmt.setInt(4, jumlahKemasan);
                insertStmt.executeUpdate();

                loadProduksi();
                JOptionPane.showMessageDialog(this, "Data produksi berhasil disimpan.");
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Gagal input produksi: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            ProduksiFrame frame = new ProduksiFrame();
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

