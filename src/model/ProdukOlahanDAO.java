package model;

import model.DatabaseConnection;
import model.ProdukOlahanModel;

import java.sql.*;
import java.util.*;

public class ProdukOlahanDAO {
    public List<ProdukOlahanModel> getAllProduk() {
        List<ProdukOlahanModel> list = new ArrayList<>();
        String sql = "SELECT p.id, p.nama_produk, r.nama_produk AS nama_resep, p.resep_id, p.ukuran_kemasan, p.jumlah " +
                     "FROM produk_olahan p JOIN resep r ON p.resep_id = r.id";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ProdukOlahanModel p = new ProdukOlahanModel(
                    rs.getInt("id"),
                    rs.getString("nama_produk"),
                    rs.getInt("resep_id"),
                    rs.getString("nama_resep"),
                    rs.getInt("ukuran_kemasan"),
                    rs.getInt("jumlah")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertProduk(ProdukOlahanModel produk) {
        String sql = "INSERT INTO produk_olahan (nama_produk, resep_id, ukuran_kemasan, jumlah) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produk.getNamaProduk());
            stmt.setInt(2, produk.getResepId());
            stmt.setInt(3, produk.getUkuranKemasan());
            stmt.setInt(4, produk.getJumlah());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Integer> getResepMap() {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT id, nama_produk FROM resep";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getString("nama_produk"), rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
}