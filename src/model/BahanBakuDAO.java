package model;

import model.DatabaseConnection;
import model.BahanBakuModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BahanBakuDAO {

    public List<BahanBakuModel> getAll() {
        List<BahanBakuModel> list = new ArrayList<>();
        String sql = "SELECT * FROM bahan_baku";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                BahanBakuModel b = new BahanBakuModel(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getDouble("stok"),
                    rs.getString("satuan"),
                    rs.getDouble("harga_per_unit"),
                    rs.getDate("tanggal_beli").toString(),
                    rs.getDate("tanggal_kadaluarsa").toString()
                );
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(BahanBakuModel bahan) {
        String sql = "INSERT INTO bahan_baku (nama, stok, satuan, harga_per_unit, tanggal_beli, tanggal_kadaluarsa) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bahan.getNama());
            stmt.setDouble(2, bahan.getStok());
            stmt.setString(3, bahan.getSatuan());
            stmt.setDouble(4, bahan.getHargaPerUnit());
            stmt.setDate(5, Date.valueOf(bahan.getTanggalBeli()));
            stmt.setDate(6, Date.valueOf(bahan.getTanggalKadaluarsa()));
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
