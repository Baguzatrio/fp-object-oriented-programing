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

public class ProdukOlahanFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public ProdukOlahanFrame() {
        setTitle("Manajemen Data Produk Olahan");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        loadData();
    }

    private void initComponents() {
        String[] kolom = {"ID", "Nama Produk", "Resep", "Ukuran Kemasan (gram)", "Jumlah Produk"};
        tableModel = new DefaultTableModel(null, kolom);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        JButton tambahBtn = new JButton("Tambah Produk");
        tambahBtn.addActionListener(e -> tambahProduk());

        add(scrollPane, BorderLayout.CENTER);
        add(tambahBtn, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = Koneksi.getConnection()) {
            String sql = "SELECT p.id, p.nama_produk, r.nama_produk AS nama_resep, p.ukuran_kemasan, p.jumlah " +
                         "FROM produk_olahan p JOIN resep r ON p.resep_id = r.id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("nama_produk"),
                    rs.getString("nama_resep"),
                    rs.getInt("ukuran_kemasan"),
                    rs.getInt("jumlah")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data produk olahan: " + e.getMessage());
        }
    }

    private void tambahProduk() {
        JTextField namaField = new JTextField();
        JTextField ukuranField = new JTextField();
        JTextField jumlahField = new JTextField();

        JComboBox<String> resepCombo = new JComboBox<>();
        Map<String, Integer> resepMap = new HashMap<>();

        try (Connection conn = Koneksi.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, nama_produk FROM resep");
            while (rs.next()) {
                String nama = rs.getString("nama_produk");
                int id = rs.getInt("id");
                resepCombo.addItem(nama);
                resepMap.put(nama, id);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat resep: " + e.getMessage());
            return;
        }

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Nama Produk:")); panel.add(namaField);
        panel.add(new JLabel("Resep:")); panel.add(resepCombo);
        panel.add(new JLabel("Ukuran Kemasan (gram):")); panel.add(ukuranField);
        panel.add(new JLabel("Jumlah Produk Jadi:")); panel.add(jumlahField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Tambah Produk Olahan", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = Koneksi.getConnection()) {
                String sql = "INSERT INTO produk_olahan (nama_produk, resep_id, ukuran_kemasan, jumlah) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, namaField.getText());
                stmt.setInt(2, resepMap.get(resepCombo.getSelectedItem().toString()));
                stmt.setInt(3, Integer.parseInt(ukuranField.getText()));
                stmt.setInt(4, Integer.parseInt(jumlahField.getText()));
                stmt.executeUpdate();
                loadData();
            } catch (SQLException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Gagal menambah produk: " + ex.getMessage());
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
            
            ProdukOlahanFrame frame = new ProdukOlahanFrame();
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

