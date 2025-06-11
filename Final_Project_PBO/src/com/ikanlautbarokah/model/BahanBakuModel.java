/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.model;

import com.ikanlautbarokah.db.Koneksi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Satrio Aji
 */
public class BahanBakuModel {

    public List<BahanBaku> getAllBahanBaku() throws SQLException {
        List<BahanBaku> listBahanBaku = new ArrayList<>();
        String query = "SELECT id, nama, stok, satuan, harga_per_unit, tanggal_beli, tanggal_kadaluwarsa FROM bahan_baku";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                BahanBaku bb = new BahanBaku(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getDouble("stok"),
                    rs.getString("satuan"),
                    rs.getDouble("harga_per_unit"),
                    rs.getDate("tanggal_beli"),
                    rs.getDate("tanggal_kadaluwarsa")
                );
                listBahanBaku.add(bb);
            }
        }
        return listBahanBaku;
    }

    public void addBahanBaku(BahanBaku bahanBaku) throws SQLException {
        String sql = "INSERT INTO bahan_baku (nama, stok, satuan, harga_per_unit, tanggal_beli, tanggal_kadaluwarsa) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bahanBaku.getNama());
            stmt.setDouble(2, bahanBaku.getStok());
            stmt.setString(3, bahanBaku.getSatuan());
            stmt.setDouble(4, bahanBaku.getHargaPerUnit());
            stmt.setDate(5, bahanBaku.getTanggalBeli());
            stmt.setDate(6, bahanBaku.getTanggalKadaluwarsa());
            stmt.executeUpdate();
        }
    }

    // Anda bisa menambahkan metode lain seperti updateBahanBaku, deleteBahanBaku, getBahanBakuById di sini
    // Contoh update:
    public void updateBahanBaku(BahanBaku bahanBaku) throws SQLException {
        String sql = "UPDATE bahan_baku SET nama=?, stok=?, satuan=?, harga_per_unit=?, tanggal_beli=?, tanggal_kadaluwarsa=? WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bahanBaku.getNama());
            stmt.setDouble(2, bahanBaku.getStok());
            stmt.setString(3, bahanBaku.getSatuan());
            stmt.setDouble(4, bahanBaku.getHargaPerUnit());
            stmt.setDate(5, bahanBaku.getTanggalBeli());
            stmt.setDate(6, bahanBaku.getTanggalKadaluwarsa());
            stmt.setInt(7, bahanBaku.getId());
            stmt.executeUpdate();
        }
    }

    // Contoh delete:
    public void deleteBahanBaku(int id) throws SQLException {
        String sql = "DELETE FROM bahan_baku WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
     public BahanBaku getBahanBakuById(int id) throws SQLException {
        String query = "SELECT id, nama, stok, satuan, harga_per_unit, tanggal_beli, tanggal_kadaluwarsa FROM bahan_baku WHERE id = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new BahanBaku(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getDouble("stok"),
                        rs.getString("satuan"),
                        rs.getDouble("harga_per_unit"),
                        rs.getDate("tanggal_beli"),
                        rs.getDate("tanggal_kadaluwarsa")
                    );
                }
            }
        }
        return null; // Bahan baku tidak ditemukan
    }
}