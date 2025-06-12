package model;

import java.sql.*;
import javax.swing.table.DefaultTableModel;
import controller.DataCustomerController;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataCustomerModel {
    private Connection conn;

    public DataCustomerModel() {
        try {
            conn = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            System.out.println("Gagal koneksi ke database: " + e.getMessage());
        }
    }

    public DefaultTableModel searchCustomerByName(String name) {
    DefaultTableModel model = new DefaultTableModel(
        new Object[][]{}, 
        new String[]{"ID", "Nama", "No. Telp.", "Alamat"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

    try (PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM customer WHERE nama_customer LIKE ?")) { // Ganti 'nama' dengan 'nama_customer'
        ps.setString(1, "%" + name + "%");
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            model.addRow(new Object[] {
                rs.getInt("id_customer"), 
                rs.getString("nama_customer"), 
                rs.getString("telp_customer"), 
                rs.getString("alamat_customer")
            });
        }
    } catch (SQLException e) {
        System.out.println("Error searching customer: " + e.getMessage());
    }
    return model;
}

    public DefaultTableModel loadCustomerData() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[][]{}, 
            new String[]{"ID", "Nama", "No. Telp.", "Alamat"}) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customer")) {
            
            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("id_customer"), 
                    rs.getString("nama_customer"), 
                    rs.getString("telp_customer"), 
                    rs.getString("alamat_customer")
                });
            }
        } catch (SQLException e) {
            System.out.println("Gagal load data customer: " + e.getMessage());
        }
        return model;
    }

    public void addCustomer(int id, String nama, String telp, String alamat) throws SQLException {
        String sql = "INSERT INTO customer (id_customer, nama_customer, telp_customer, alamat_customer) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, nama);
            ps.setString(3, telp);
            ps.setString(4, alamat);
            ps.executeUpdate();
        }
    }

    public void updateCustomer(int id, String nama, String telp, String alamat) throws SQLException {
        String sql = "UPDATE customer SET nama_customer=?, telp_customer=?, alamat_customer=? WHERE id_customer=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.setString(2, telp);
            ps.setString(3, alamat);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }

    public void deleteCustomer(int id) throws SQLException {
        String sql = "DELETE FROM customer WHERE id_customer = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
public String generateNomorNota(String idCustomer) {
        try {
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyyMMdd");
            Date today = new Date();
            
            int urutan = hitungNotaHariIni(Integer.parseInt(idCustomer));
            return dbFormat.format(today) + "-" + idCustomer + "-" + String.format("%03d", urutan);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR-" + System.currentTimeMillis();
        }
    }
    
    public String getTanggalHariIni() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

// Jangan lupa tambahkan juga method hitungNotaHariIni()
private int hitungNotaHariIni(int idCustomer) {
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(
             "SELECT COUNT(*) as jumlah FROM distribusi WHERE id_customer = ? AND tanggal = CURDATE()")) {
         
        stmt.setInt(1, idCustomer);
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? rs.getInt("jumlah") + 1 : 1;
    } catch (SQLException e) {
        e.printStackTrace();
        return 1;
    }
}
}