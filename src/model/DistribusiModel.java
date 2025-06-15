package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

public class DistribusiModel {
    private Connection conn;
    
    public DistribusiModel() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }
    
    public DefaultTableModel loadDataDistribusi() {
        DefaultTableModel model = new DefaultTableModel();
        String[] kolom = {"Nomor Nota", "Nama Customer", "Lokasi Pengiriman", "Status Pembayaran", "Tanggal Kirim", "Total Harga", "Detail"};
        model.setColumnIdentifiers(kolom);

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT d.no_nota, d.nama_customer, d.alamat, d.status, d.tanggal, d.total " + 
                     "FROM distribusi d JOIN customer c ON d.id_customer = c.id_customer" + //spasi
                      "ORDER BY d.tanggal DESC, d.no_nota DESC");) {

            while (rs.next()) {
                String nomor = rs.getString("no_nota");
                String nama = rs.getString("nama_customer");
                String lokasi = rs.getString("alamat");
                String status = rs.getString("status");
                String tanggal = rs.getString("tanggal");
                String total = "Rp " + rs.getInt("total");

                model.addRow(new Object[]{nomor, nama, lokasi, status, tanggal, total, "Lihat"});
            }

        } catch (Exception e) {
            System.out.println("Gagal load data: " + e.getMessage());
        }

        return model;
    }
    
    public Map<String, Object> getDistribusiByNoNota(String noNota) throws SQLException {
    String sql = "SELECT * FROM distribusi WHERE no_nota = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, noNota);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Map<String, Object> data = new HashMap<>();
                data.put("no_nota", rs.getString("no_nota"));
                data.put("id_customer", rs.getString("id_customer"));
                data.put("nama_customer", rs.getString("nama_customer"));
                data.put("alamat", rs.getString("alamat"));
                data.put("status", rs.getString("status"));
                data.put("tanggal", rs.getString("tanggal"));
                data.put("total", rs.getInt("total"));
                data.put("dibayar", rs.getInt("dibayar"));
                data.put("kembalian", rs.getInt("kembalian"));
                return data;
            }
        }
    }
    throw new SQLException("Data tidak ditemukan");
}

public List<Map<String, Object>> getDetailDistribusi(String noNota) throws SQLException {
    List<Map<String, Object>> items = new ArrayList<>();
    String sql = "SELECT nama_produk, harga, jumlah, subtotal FROM detail_distribusi WHERE no_nota = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, noNota);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("nama_produk", rs.getString("nama_produk"));
                item.put("harga", rs.getInt("harga"));
                item.put("jumlah", rs.getInt("jumlah"));
                item.put("subtotal", rs.getInt("subtotal"));
                items.add(item);
            }
        }
    }
    return items;
}

public void updateDistribusi(String noNota, String idCustomer, String namaCustomer, 
                           String alamat, String status, String tanggal, int total, 
                           int dibayar, int kembalian, List<Map<String, Object>> items) throws SQLException {
    try {
        conn.setAutoCommit(false);
        
        // 1. Update data distribusi
        String updateSql = "UPDATE distribusi SET id_customer = ?, nama_customer = ?, alamat = ?, " +
                         "status = ?, tanggal = ?, total = ?, dibayar = ?, kembalian = ? " +
                         "WHERE no_nota = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setString(1, idCustomer);
            stmt.setString(2, namaCustomer);
            stmt.setString(3, alamat);
            stmt.setString(4, status);
            stmt.setString(5, tanggal);
            stmt.setInt(6, total);
            stmt.setInt(7, dibayar);
            stmt.setInt(8, kembalian);
            stmt.setString(9, noNota);
            stmt.executeUpdate();
        }
        
        // 2. Hapus detail lama
        String deleteSql = "DELETE FROM detail_distribusi WHERE no_nota = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
            stmt.setString(1, noNota);
            stmt.executeUpdate();
        }
        
        // 3. Simpan detail baru
        String insertSql = "INSERT INTO detail_distribusi (no_nota, nama_produk, harga, jumlah, subtotal) " +
                         "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            for (Map<String, Object> item : items) {
                stmt.setString(1, noNota);
                stmt.setString(2, (String) item.get("produk"));
                stmt.setInt(3, (Integer) item.get("harga"));
                stmt.setInt(4, (Integer) item.get("jumlah"));
                stmt.setInt(5, (Integer) item.get("subtotal"));
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
        
        conn.commit();
    } catch (SQLException e) {
        conn.rollback();
        throw e;
    } finally {
        conn.setAutoCommit(true);
    }
}
}
