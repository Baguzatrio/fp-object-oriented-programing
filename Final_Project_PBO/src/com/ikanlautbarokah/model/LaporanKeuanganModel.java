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
import java.text.DecimalFormat; // Untuk format angka
/**
 *
 * @author Satrio Aji
 */
public class LaporanKeuanganModel {

    // Konstanta biaya produksi per kg
    private static final double BIAYA_PRODUKSI_PER_KG = 5000.0;

    // Kelas internal untuk menampung hasil laporan
    public static class RingkasanLaporan {
        public double totalPendapatan;
        public double totalBiayaProduksi;
        public double totalUpah;
        public double labaBersih;

        public RingkasanLaporan(double pendapatan, double biayaProduksi, double upah, double laba) {
            this.totalPendapatan = pendapatan;
            this.totalBiayaProduksi = biayaProduksi;
            this.totalUpah = upah;
            this.labaBersih = laba;
        }
    }

    /**
     * Mengambil laporan keuangan untuk bulan dan tahun tertentu.
     *
     * @param tahun Tahun yang ingin dilaporkan (misal: 2025).
     * @param bulan Bulan yang ingin dilaporkan (1-12). Jika 0, berarti laporan tahunan.
     * @return Objek RingkasanLaporan yang berisi perhitungan keuangan.
     * @throws SQLException Jika terjadi error database.
     */
    public RingkasanLaporan generateLaporan(int tahun, int bulan) throws SQLException {
        double pendapatan = 0.0;
        double totalKgProduksi = 0.0;
        double totalUpah = 0.0;

        String filterSql = "";
        if (bulan > 0 && bulan <= 12) {
            filterSql = " WHERE YEAR(tanggal) = ? AND MONTH(tanggal) = ?";
        } else { // Laporan tahunan jika bulan tidak valid (misal 0)
            filterSql = " WHERE YEAR(tanggal) = ?";
        }

        try (Connection conn = Koneksi.getConnection()) {
            // 1. Total Pendapatan dari Distribusi
            String sqlPendapatan = "SELECT SUM(harga_total) FROM distribusi" + filterSql;
            try (PreparedStatement ps = conn.prepareStatement(sqlPendapatan)) {
                if (bulan > 0) {
                    ps.setInt(1, tahun);
                    ps.setInt(2, bulan);
                } else {
                    ps.setInt(1, tahun);
                }
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    pendapatan = rs.getDouble(1);
                }
            }

            // 2. Total Kilogram Produksi dari Produksi
            String sqlProduksi = "SELECT SUM(total_kg) FROM produksi" + filterSql;
            try (PreparedStatement ps = conn.prepareStatement(sqlProduksi)) {
                if (bulan > 0) {
                    ps.setInt(1, tahun);
                    ps.setInt(2, bulan);
                } else {
                    ps.setInt(1, tahun);
                }
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    totalKgProduksi = rs.getDouble(1);
                }
            }
            double biayaProduksi = totalKgProduksi * BIAYA_PRODUKSI_PER_KG;

            // 3. Total Upah Dibayar
            // Catatan: Tabel 'upah' tidak memiliki kolom tanggal. Ini adalah asumsi.
            // Jika upah ingin difilter per bulan/tahun, tabel upah perlu kolom tanggal.
            // Untuk saat ini, kita akan asumsikan upah dihitung keseluruhan atau sesuai filter yang paling mendekati.
            // Jika 'upah' adalah laporan bulanan/tahunan terpisah, maka query ini harus disesuaikan.
            // Untuk contoh ini, saya akan tambahkan filter dummy untuk upah.
            // PENTING: Untuk upah yang akurat, tabel 'upah' harus punya kolom tanggal.
            String sqlUpah = "SELECT SUM(total_upah) FROM upah"; // Query aslinya tanpa filter tanggal
            // Jika ingin difilter, tabel upah harus punya kolom tanggal
            // Misalnya: SELECT SUM(total_upah) FROM upah WHERE YEAR(tanggal_pembayaran) = ? AND MONTH(tanggal_pembayaran) = ?
            try (Statement stmt = conn.createStatement()) { // Menggunakan Statement karena query asli tanpa filter
                ResultSet rs = stmt.executeQuery(sqlUpah);
                if (rs.next()) {
                    totalUpah = rs.getDouble(1);
                }
            }

            // Laba / Rugi Bersih
            double labaBersih = pendapatan - biayaProduksi - totalUpah;

            return new RingkasanLaporan(pendapatan, biayaProduksi, totalUpah, labaBersih);

        } catch (SQLException e) {
            throw e; // Lemparkan error kembali
        }
    }
}
