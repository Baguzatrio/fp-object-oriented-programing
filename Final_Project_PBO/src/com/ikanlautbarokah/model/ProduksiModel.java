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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Satrio Aji
 */
public class ProduksiModel {

    private ResepModel resepModel;
    private BahanBakuModel bahanBakuModel; // Perlu akses ke model BahanBaku

    public ProduksiModel() {
        this.resepModel = new ResepModel(); // Inisialisasi model lain yang dibutuhkan
        this.bahanBakuModel = new BahanBakuModel();
    }

    public List<Produksi> getAllProduksi() throws SQLException {
        List<Produksi> listProduksi = new ArrayList<>();
        String query = "SELECT id, tanggal, produk, jumlah_batch, total_kg, jumlah_kemasan FROM produksi";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Produksi prod = new Produksi(
                    rs.getInt("id"),
                    rs.getDate("tanggal"),
                    rs.getString("produk"),
                    rs.getInt("jumlah_batch"),
                    rs.getDouble("total_kg"),
                    rs.getInt("jumlah_kemasan")
                );
                listProduksi.add(prod);
            }
        }
        return listProduksi;
    }

    /**
     * Menyimpan data produksi baru dan mengurangi stok bahan baku.
     * Operasi ini dilakukan dalam sebuah transaksi.
     * @param produksi Objek Produksi yang akan disimpan.
     * @param resepId ID resep yang terkait dengan produksi ini.
     * @throws SQLException Jika terjadi error database atau stok tidak mencukupi.
     */
    public void addProduksi(Produksi produksi, int resepId) throws SQLException {
        Connection conn = null;
        try {
            conn = Koneksi.getConnection();
            conn.setAutoCommit(false); // Mulai transaksi

            // 1. Periksa dan kurangi stok bahan baku
            String queryDetail = "SELECT bahan_baku_id, jumlah FROM resep_detail WHERE resep_id = ?";
            Map<Integer, Double> bahanYangDibutuhkan = new HashMap<>(); // ID bahan baku -> total jumlah yang dibutuhkan

            try (PreparedStatement detailStmt = conn.prepareStatement(queryDetail)) {
                detailStmt.setInt(1, resepId);
                ResultSet detailRS = detailStmt.executeQuery();
                while (detailRS.next()) {
                    int bahanId = detailRS.getInt("bahan_baku_id");
                    double jumlahPerBatch = detailRS.getDouble("jumlah");
                    bahanYangDibutuhkan.put(bahanId, jumlahPerBatch * produksi.getJumlahBatch());
                }
            }

            // Cek ketersediaan stok dan update sementara dalam map
            Map<Integer, Double> stokBaruBahanBaku = new HashMap<>(); // ID bahan baku -> stok baru
            for (Map.Entry<Integer, Double> entry : bahanYangDibutuhkan.entrySet()) {
                int bahanId = entry.getKey();
                double jumlahDibutuhkan = entry.getValue();

                BahanBaku currentBahan = bahanBakuModel.getBahanBakuById(bahanId); // Perlu metode getBahanBakuById di BahanBakuModel
                if (currentBahan == null || currentBahan.getStok() < jumlahDibutuhkan) {
                    conn.rollback(); // Batalkan transaksi
                    throw new SQLException("Stok bahan baku '" + (currentBahan != null ? currentBahan.getNama() : "ID: " + bahanId) + "' tidak mencukupi!");
                }
                stokBaruBahanBaku.put(bahanId, currentBahan.getStok() - jumlahDibutuhkan);
            }

            // Lakukan update stok bahan baku
            String updateStokSql = "UPDATE bahan_baku SET stok = ? WHERE id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateStokSql)) {
                for (Map.Entry<Integer, Double> entry : stokBaruBahanBaku.entrySet()) {
                    updateStmt.setDouble(1, entry.getValue());
                    updateStmt.setInt(2, entry.getKey());
                    updateStmt.executeUpdate();
                }
            }

            // 2. Simpan data produksi
            String insertProduksiSql = "INSERT INTO produksi (tanggal, produk, jumlah_batch, total_kg, jumlah_kemasan) VALUES (CURRENT_DATE(), ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertProduksiSql)) {
                insertStmt.setString(1, produksi.getNamaProdukResep());
                insertStmt.setInt(2, produksi.getJumlahBatch());
                insertStmt.setDouble(3, produksi.getTotalKg());
                insertStmt.setInt(4, produksi.getJumlahKemasan());
                insertStmt.executeUpdate();
            }

            conn.commit(); // Konfirmasi transaksi
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Batalkan transaksi jika ada error
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            throw e; // Lemparkan error kembali
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Kembalikan auto-commit
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Failed to close connection: " + e.getMessage());
                }
            }
        }
    }

    // Anda bisa menambahkan metode updateProduksi dan deleteProduksi di sini jika diperlukan
    // Update Produksi (jika tidak melibatkan perubahan stok bahan baku yang rumit)
    public void updateProduksi(Produksi produksi) throws SQLException {
        String sql = "UPDATE produksi SET produk=?, jumlah_batch=?, total_kg=?, jumlah_kemasan=? WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produksi.getNamaProdukResep());
            stmt.setInt(2, produksi.getJumlahBatch());
            stmt.setDouble(3, produksi.getTotalKg());
            stmt.setInt(4, produksi.getJumlahKemasan());
            stmt.setInt(5, produksi.getId());
            stmt.executeUpdate();
        }
    }

    // Delete Produksi (jika ada kebutuhan untuk mengembalikan stok bahan baku, perlu transaksi terpisah)
    public void deleteProduksi(int id) throws SQLException {
        // PERHATIAN: Jika menghapus produksi harus mengembalikan stok bahan baku,
        // maka logika ini juga perlu transaksi dan mengambil detail resep yang digunakan pada produksi ini.
        // Untuk saat ini, kita hanya menghapus data produksi saja.
        String sql = "DELETE FROM produksi WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Metode bantuan untuk ProduksiModel, agar bisa mendapatkan data BahanBaku berdasarkan ID
    // Ini seharusnya ada di BahanBakuModel, jadi kita tambahkan di sana juga
    // Atau, ProduksiModel bisa memiliki instance dari BahanBakuModel dan memanggilnya.
    // Kita akan pastikan BahanBakuModel punya metode ini.
}
