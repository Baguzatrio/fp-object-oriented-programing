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
import java.util.HashMap;
import java.util.Map;

public class ResepDetailFrame extends JFrame {
    private int resepId;
    private String namaProduk;
    private JTable table;
    private DefaultTableModel tableModel;

    public ResepDetailFrame(int resepId, String namaProduk) {
        this.resepId = resepId;
        this.namaProduk = namaProduk;
        setTitle("Komposisi Resep: " + namaProduk);
        setSize(650, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        loadDetail();
    }

    private void initComponents() {
        String[] kolom = {"ID", "Bahan Baku", "Jumlah", "Satuan"};
        tableModel = new DefaultTableModel(null, kolom);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        JButton tambahBtn = new JButton("Tambah Bahan ke Resep");
        tambahBtn.addActionListener(e -> tambahDetail());

        add(scrollPane, BorderLayout.CENTER);
        add(tambahBtn, BorderLayout.SOUTH);
    }

    private void loadDetail() {
        tableModel.setRowCount(0);
        try (Connection conn = Koneksi.getConnection()) {
            String sql = "SELECT rd.id, bb.nama, rd.jumlah, bb.satuan FROM resep_detail rd " +
                         "JOIN bahan_baku bb ON rd.bahan_baku_id = bb.id WHERE rd.resep_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, resepId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getDouble(3),
                    rs.getString(4)
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat detail: " + e.getMessage());
        }
    }

    private void tambahDetail() {
        try (Connection conn = Koneksi.getConnection()) {
            JComboBox<String> bahanCombo = new JComboBox<>();
            Map<String, Integer> bahanMap = new HashMap<>();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, nama FROM bahan_baku");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("nama");
                bahanCombo.addItem(nama);
                bahanMap.put(nama, id);
            }

            JTextField jumlahField = new JTextField();

            JPanel panel = new JPanel(new GridLayout(2, 2));
            panel.add(new JLabel("Bahan Baku:")); panel.add(bahanCombo);
            panel.add(new JLabel("Jumlah:")); panel.add(jumlahField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Tambah Komposisi", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String selectedNama = (String) bahanCombo.getSelectedItem();
                int bahanId = bahanMap.get(selectedNama);
                double jumlah = Double.parseDouble(jumlahField.getText());

                String sql = "INSERT INTO resep_detail (resep_id, bahan_baku_id, jumlah) VALUES (?, ?, ?)";
                PreparedStatement insert = conn.prepareStatement(sql);
                insert.setInt(1, resepId);
                insert.setInt(2, bahanId);
                insert.setDouble(3, jumlah);
                insert.executeUpdate();
                loadDetail();
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Gagal tambah detail: " + e.getMessage());
        }
    }
       public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            JTextField idField = new JTextField();
            JTextField nameField = new JTextField();
            
            JPanel inputPanel = new JPanel(new GridLayout(2, 2));
            inputPanel.add(new JLabel("Recipe ID:"));
            inputPanel.add(idField);
            inputPanel.add(new JLabel("Recipe Name:"));
            inputPanel.add(nameField);
            
            int result = JOptionPane.showConfirmDialog(
                null, 
                inputPanel, 
                "Enter Recipe Details", 
                JOptionPane.OK_CANCEL_OPTION
            );
            
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int recipeId = Integer.parseInt(idField.getText());
                    String recipeName = nameField.getText();
                    
                    try (Connection testConn = Koneksi.getConnection()) {
                        if (testConn != null) {
                            System.out.println("Database connection successful!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Database connection failed!");
                            return;
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Connection test failed: " + ex.getMessage());
                        ex.printStackTrace();
                        return;
                    }
                    
                    ResepDetailFrame frame = new ResepDetailFrame(recipeId, recipeName);
                    frame.setVisible(true);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid Recipe ID! Must be a number.");
                }
            }
        });
    }
}
