package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BahanBakuDAO {
    private static final String TABLE_NAME = "bahan_baku";

    public List<BahanBakuModel> getAll(String filterNama) {
        List<BahanBakuModel> list = new ArrayList<>();
        String sql = "SELECT id, nama, stok, satuan, harga_per_unit, " +
                     "tanggal_beli, tanggal_kadaluarsa, berat_per_unit " + // Tambah kolom
                     "FROM bahan_baku " +
                     (filterNama != null ? "WHERE nama LIKE ? " : "") + 
                     "ORDER BY tanggal_kadaluarsa ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (filterNama != null) {
                stmt.setString(1, "%" + filterNama + "%");
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BahanBakuModel b = new BahanBakuModel(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getDouble("stok"),
                        rs.getString("satuan"),
                        rs.getDouble("harga_per_unit"),
                        rs.getDate("tanggal_beli").toString(),
                        rs.getDate("tanggal_kadaluarsa").toString(),
                        rs.getDouble("berat_per_unit") // Ambil nilai dari DB
                    );
                    list.add(b);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(BahanBakuModel bahan) {
        String sql = "INSERT INTO bahan_baku " +
                     "(nama, stok, satuan, harga_per_unit, " +
                     "tanggal_beli, tanggal_kadaluarsa, berat_per_unit) " + // Tambah kolom
                     "VALUES (?, ?, ?, ?, ?, ?, ?)"; // Tambah parameter
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, bahan.getNama());
            stmt.setDouble(2, bahan.getStok());
            stmt.setString(3, bahan.getSatuan());
            stmt.setDouble(4, bahan.getHargaPerUnit());
            stmt.setDate(5, Date.valueOf(bahan.getTanggalBeli()));
            stmt.setDate(6, Date.valueOf(bahan.getTanggalKadaluarsa()));
            stmt.setDouble(7, bahan.getBeratPerUnit()); // Nilai berat per unit
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean reduceStock(int id, double amount) {
        if (amount <= 0) return false;
        
        String sql = "UPDATE " + TABLE_NAME + " " +
                     "SET stok = GREATEST(0, stok - ?) " +
                     "WHERE id = ? AND stok >= ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, amount);
            stmt.setInt(2, id);
            stmt.setDouble(3, amount); // Memastikan stok cukup
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error reducing stock: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}