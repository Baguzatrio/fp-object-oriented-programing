package gui;

import gui.base.BaseDataFrame;
import db.Koneksi;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ProduksiFrame extends BaseDataFrame {
    
    public ProduksiFrame() {
        super("Produksi & Pengemasan", 
              new String[]{"ID", "Tanggal", "Produk", "Jumlah Batch", "Total Kg", "Jumlah Kemasan"});
    }
    
    @Override
    protected String getAddButtonText() {
        return "Input Produksi";
    }
    
    @Override
    protected void loadData() {
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
            showError("Gagal memuat data produksi", e);
        }
    }
    
    @Override
    protected void tambahData() {
        ProduksiInputDialog dialog = new ProduksiInputDialog(this);
        dialog.setVisible(true);
        
        if (dialog.isDataSaved()) {
            loadData();
            showSuccess("Data produksi berhasil disimpan.");
        }
    }
}

// input produksi 
class ProduksiInputDialog extends JDialog {
    private JComboBox<String> resepCombo;
    private JTextField batchField;
    private JTextField kgField;
    private Map<String, Integer> resepMap;
    private boolean dataSaved = false;
    
    public ProduksiInputDialog(JFrame parent) {
        super(parent, "Input Produksi", true);
        setSize(400, 200);
        setLocationRelativeTo(parent);
        initComponents();
        loadResep();
    }
    
    private void initComponents() {
        resepCombo = new JComboBox<>();
        batchField = new JTextField();
        kgField = new JTextField();
        resepMap = new HashMap<>();
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JLabel("Pilih Produk (Resep):"));
        panel.add(resepCombo);
        panel.add(new JLabel("Jumlah Batch:"));
        panel.add(batchField);
        panel.add(new JLabel("Total Kilogram Produk:"));
        panel.add(kgField);
        
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        
        okButton.addActionListener(e -> saveData());
        cancelButton.addActionListener(e -> dispose());
        
        panel.add(okButton);
        panel.add(cancelButton);
        
        add(panel);
    }
    
    private void loadResep() {
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
            JOptionPane.showMessageDialog(this, "Gagal memuat data resep: " + e.getMessage());
        }
    }
    
    private void saveData() {
        try {
            if (!validateInput()) return;
            
            String selected = (String) resepCombo.getSelectedItem();
            int resepId = resepMap.get(selected);
            int batch = Integer.parseInt(batchField.getText());
            double kg = Double.parseDouble(kgField.getText());
            
            try (Connection conn = Koneksi.getConnection()) {
                conn.setAutoCommit(false); // Start
                
                if (!checkAndUpdateStock(conn, resepId, batch)) {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "Stok bahan baku tidak mencukupi!");
                    return;
                }
                
                saveProduksiData(conn, selected, batch, kg);
                conn.commit(); // commit
                
                dataSaved = true;
                dispose();
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + e.getMessage());
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Format angka tidak valid!");
        }
    }
    
    private boolean validateInput() {
        if (resepCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih produk terlebih dahulu!");
            return false;
        }
        
        if (batchField.getText().trim().isEmpty() || kgField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return false;
        }
        
        return true;
    }
    
    private boolean checkAndUpdateStock(Connection conn, int resepId, int batch) throws SQLException {
        String queryDetail = "SELECT bahan_baku_id, jumlah FROM resep_detail WHERE resep_id = ?";
        PreparedStatement detailStmt = conn.prepareStatement(queryDetail);
        detailStmt.setInt(1, resepId);
        ResultSet detailRS = detailStmt.executeQuery();
        
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
                if (stok < total) {
                    return false; // Stok tidak mencukupi
                }
                bahanUpdate.put(bahanId, stok - total);
            }
        }
        
        // Update stok bahan baku
        for (Map.Entry<Integer, Double> entry : bahanUpdate.entrySet()) {
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE bahan_baku SET stok = ? WHERE id = ?");
            updateStmt.setDouble(1, entry.getValue());
            updateStmt.setInt(2, entry.getKey());
            updateStmt.executeUpdate();
        }
        
        return true;
    }
    
    private void saveProduksiData(Connection conn, String produk, int batch, double kg) throws SQLException {
        int jumlahKemasan = (int) (kg * 1000 / 500); // Asumsi 1 kemasan = 500g
        String insertSql = "INSERT INTO produksi (tanggal, produk, jumlah_batch, total_kg, jumlah_kemasan) VALUES (CURRENT_DATE(), ?, ?, ?, ?)";
        
        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
        insertStmt.setString(1, produk);
        insertStmt.setInt(2, batch);
        insertStmt.setDouble(3, kg);
        insertStmt.setInt(4, jumlahKemasan);
        insertStmt.executeUpdate();
    }
    
    public boolean isDataSaved() {
        return dataSaved;
    }
}