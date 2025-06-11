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
public class DistribusiModel {

    public List<Distribusi> getAllDistribusi() throws SQLException {
        List<Distribusi> listDistribusi = new ArrayList<>();
        String query = "SELECT id, tanggal, produk, jumlah, tujuan, harga_total, metode_bayar FROM distribusi";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Distribusi dist = new Distribusi(
                    rs.getInt("id"),
                    rs.getDate("tanggal"),
                    rs.getString("produk"),
                    rs.getInt("jumlah"),
                    rs.getString("tujuan"),
                    rs.getDouble("harga_total"),
                    rs.getString("metode_bayar")
                );
                listDistribusi.add(dist);
            }
        }
        return listDistribusi;
    }

    public void addDistribusi(Distribusi distribusi) throws SQLException {
        // Tanggal otomatis diisi CURRENT_DATE() di database
        String sql = "INSERT INTO distribusi (tanggal, produk, jumlah, tujuan, harga_total, metode_bayar) VALUES (CURRENT_DATE(), ?, ?, ?, ?, ?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, distribusi.getProduk());
            stmt.setInt(2, distribusi.getJumlah());
            stmt.setString(3, distribusi.getTujuan());
            stmt.setDouble(4, distribusi.getHargaTotal());
            stmt.setString(5, distribusi.getMetodeBayar());
            stmt.executeUpdate();
        }
    }

    // Anda bisa menambahkan metode updateDistribusi, deleteDistribusi di sini
    public void updateDistribusi(Distribusi distribusi) throws SQLException {
        // ID dan Tanggal tidak diupdate, hanya data lainnya
        String sql = "UPDATE distribusi SET produk=?, jumlah=?, tujuan=?, harga_total=?, metode_bayar=? WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, distribusi.getProduk());
            stmt.setInt(2, distribusi.getJumlah());
            stmt.setString(3, distribusi.getTujuan());
            stmt.setDouble(4, distribusi.getHargaTotal());
            stmt.setString(5, distribusi.getMetodeBayar());
            stmt.setInt(6, distribusi.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteDistribusi(int id) throws SQLException {
        String sql = "DELETE FROM distribusi WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
