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
public class UpahModel {

    public List<Upah> getAllUpah() throws SQLException {
        List<Upah> listUpah = new ArrayList<>();
        String query = "SELECT id, nama_pegawai, jumlah_produksi, upah_per_unit, total_upah FROM upah";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Upah upah = new Upah(
                    rs.getInt("id"),
                    rs.getString("nama_pegawai"),
                    rs.getInt("jumlah_produksi"),
                    rs.getDouble("upah_per_unit"),
                    rs.getDouble("total_upah")
                );
                listUpah.add(upah);
            }
        }
        return listUpah;
    }

    public void addUpah(Upah upah) throws SQLException {
        String sql = "INSERT INTO upah (nama_pegawai, jumlah_produksi, upah_per_unit, total_upah) VALUES (?, ?, ?, ?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, upah.getNamaPegawai());
            stmt.setInt(2, upah.getJumlahProduksi());
            stmt.setDouble(3, upah.getUpahPerUnit());
            stmt.setDouble(4, upah.getTotalUpah());
            stmt.executeUpdate();
        }
    }

    public void updateUpah(Upah upah) throws SQLException {
        String sql = "UPDATE upah SET nama_pegawai=?, jumlah_produksi=?, upah_per_unit=?, total_upah=? WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, upah.getNamaPegawai());
            stmt.setInt(2, upah.getJumlahProduksi());
            stmt.setDouble(3, upah.getUpahPerUnit());
            stmt.setDouble(4, upah.getTotalUpah());
            stmt.setInt(5, upah.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteUpah(int id) throws SQLException {
        String sql = "DELETE FROM upah WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
