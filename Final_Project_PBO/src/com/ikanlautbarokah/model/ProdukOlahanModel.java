/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.model;

import com.ikanlautbarokah.db.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Satrio Aji
 */
public class ProdukOlahanModel {

    public List<ProdukOlahan> getAllProdukOlahan() throws SQLException {
        List<ProdukOlahan> listProdukOlahan = new ArrayList<>();
        String query = "SELECT p.id, p.nama_produk, p.resep_id, r.nama_produk AS nama_resep, p.ukuran_kemasan, p.jumlah " +
                       "FROM produk_olahan p JOIN resep r ON p.resep_id = r.id";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                ProdukOlahan po = new ProdukOlahan(
                    rs.getInt("id"),
                    rs.getString("nama_produk"),
                    rs.getInt("resep_id"),
                    rs.getString("nama_resep"),
                    rs.getInt("ukuran_kemasan"),
                    rs.getInt("jumlah")
                );
                listProdukOlahan.add(po);
            }
        }
        return listProdukOlahan;
    }

    public void addProdukOlahan(ProdukOlahan produkOlahan) throws SQLException {
        String sql = "INSERT INTO produk_olahan (nama_produk, resep_id, ukuran_kemasan, jumlah) VALUES (?, ?, ?, ?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produkOlahan.getNamaProduk());
            stmt.setInt(2, produkOlahan.getResepId());
            stmt.setInt(3, produkOlahan.getUkuranKemasan());
            stmt.setInt(4, produkOlahan.getJumlah());
            stmt.executeUpdate();
        }
    }

    public void updateProdukOlahan(ProdukOlahan produkOlahan) throws SQLException {
        String sql = "UPDATE produk_olahan SET nama_produk=?, resep_id=?, ukuran_kemasan=?, jumlah=? WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produkOlahan.getNamaProduk());
            stmt.setInt(2, produkOlahan.getResepId());
            stmt.setInt(3, produkOlahan.getUkuranKemasan());
            stmt.setInt(4, produkOlahan.getJumlah());
            stmt.setInt(5, produkOlahan.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteProdukOlahan(int id) throws SQLException {
        String sql = "DELETE FROM produk_olahan WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Metode untuk mendapatkan daftar resep dari database (diambil dari ResepModel untuk fleksibilitas)
    // Namun, jika Anda punya ResepModel, lebih baik panggil ResepModel.getAllResep()
    public List<Resep> getAllResep() throws SQLException {
         // Sebaiknya panggil ResepModel di sini, tapi untuk saat ini, kita bisa menduplikasi
         // atau mengubah agar ProdukOlahanModel bergantung pada ResepModel.
         // Untuk menghindari circular dependency di awal, salin saja query-nya dulu
         // atau anggap ResepModel akan diinject.
         List<Resep> listResep = new ArrayList<>();
         String query = "SELECT id, nama_produk FROM resep";
         try (Connection conn = Koneksi.getConnection();
              Statement stmt = conn.createStatement();
              ResultSet rs = stmt.executeQuery(query)) {

             while (rs.next()) {
                 Resep resep = new Resep(rs.getInt("id"), rs.getString("nama_produk"));
                 listResep.add(resep);
             }
         }
         return listResep;
    }
}
