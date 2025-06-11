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
public class ResepModel {

    // --- Operasi untuk Tabel Resep (induk) ---

    public List<Resep> getAllResep() throws SQLException {
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

    public void addResep(String namaProduk) throws SQLException {
        String sql = "INSERT INTO resep (nama_produk) VALUES (?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, namaProduk);
            stmt.executeUpdate();
        }
    }

    public void updateResep(Resep resep) throws SQLException {
        String sql = "UPDATE resep SET nama_produk=? WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, resep.getNamaProduk());
            stmt.setInt(2, resep.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteResep(int id) throws SQLException {
        String sql = "DELETE FROM resep WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                // Mungkin resep tidak ditemukan atau ada masalah lain
                throw new SQLException("Resep dengan ID " + id + " tidak ditemukan atau tidak dapat dihapus.");
            }
        } catch (SQLException e) {
            // Periksa jika error disebabkan oleh foreign key constraint
            if (e.getSQLState().startsWith("23")) { // SQLState for integrity constraint violation
                throw new SQLException("Tidak dapat menghapus resep karena masih digunakan oleh produk olahan atau detail komposisi. " +
                                      "Harap hapus produk olahan dan detail komposisi terkait terlebih dahulu, " +
                                      "atau pastikan database Anda dikonfigurasi dengan ON DELETE CASCADE.", e);
            } else {
                // Lempar kembali exception SQL lainnya
                throw e;
            }
        }
    }

    // --- Operasi untuk Tabel ResepDetail (komposisi) ---

    public List<ResepDetail> getResepDetailByResepId(int resepId) throws SQLException {
        List<ResepDetail> listResepDetail = new ArrayList<>();
        String query = "SELECT rd.id, rd.resep_id, r.nama_produk AS nama_resep, rd.bahan_baku_id, bb.nama AS nama_bahan_baku, rd.jumlah " +
                       "FROM resep_detail rd " +
                       "JOIN resep r ON rd.resep_id = r.id " +
                       "JOIN bahan_baku bb ON rd.bahan_baku_id = bb.id " +
                       "WHERE rd.resep_id = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, resepId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ResepDetail rd = new ResepDetail(
                    rs.getInt("id"),
                    rs.getInt("resep_id"),
                    rs.getString("nama_resep"),
                    rs.getInt("bahan_baku_id"),
                    rs.getString("nama_bahan_baku"),
                    rs.getDouble("jumlah")
                );
                listResepDetail.add(rd);
            }
        }
        return listResepDetail;
    }

    public void addResepDetail(ResepDetail resepDetail) throws SQLException {
        String sql = "INSERT INTO resep_detail (resep_id, bahan_baku_id, jumlah) VALUES (?, ?, ?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, resepDetail.getResepId());
            stmt.setInt(2, resepDetail.getBahanBakuId());
            stmt.setDouble(3, resepDetail.getJumlah());
            stmt.executeUpdate();
        }
    }

    public void updateResepDetail(ResepDetail resepDetail) throws SQLException {
        String sql = "UPDATE resep_detail SET bahan_baku_id=?, jumlah=? WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, resepDetail.getBahanBakuId());
            stmt.setDouble(2, resepDetail.getJumlah());
            stmt.setInt(3, resepDetail.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteResepDetail(int id) throws SQLException {
        String sql = "DELETE FROM resep_detail WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
