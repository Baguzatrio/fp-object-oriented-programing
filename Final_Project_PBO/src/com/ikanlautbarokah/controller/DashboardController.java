/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.controller;

import com.ikanlautbarokah.view.DashboardFrame;
import com.ikanlautbarokah.view.LoginFrame;
// Import semua Frame lain yang akan dibuka dari Dashboard
import com.ikanlautbarokah.view.ProdukOlahanFrame;
import com.ikanlautbarokah.view.BahanBakuFrame;
import com.ikanlautbarokah.view.ResepFrame;
import com.ikanlautbarokah.view.ProduksiFrame;
import com.ikanlautbarokah.view.DistribusiFrame;
import com.ikanlautbarokah.view.UpahFrame;
import com.ikanlautbarokah.view.LaporanKeuanganFrame;

import javax.swing.JFrame; // Untuk dispose frame

/**
 *
 * @author Satrio Aji
 */
public class DashboardController {
    private DashboardFrame dashboardFrame;

    public DashboardController(DashboardFrame dashboardFrame) {
        this.dashboardFrame = dashboardFrame;
    }

    // Metode untuk menangani klik tombol Produk Olahan
    public void navigateToProdukOlahan() {
        // Logika bisnis opsional di sini sebelum menampilkan frame
        ProdukOlahanFrame produkOlahanFrame = new ProdukOlahanFrame();
        produkOlahanFrame.setVisible(true);
        dashboardFrame.dispose(); // Tutup dashboard jika ingin pindah sepenuhnya
    }

    // Metode untuk menangani klik tombol Bahan Baku
    public void navigateToBahanBaku() {
        BahanBakuFrame bahanBakuFrame = new BahanBakuFrame();
        bahanBakuFrame.setVisible(true);
        dashboardFrame.dispose();
    }

    // Metode untuk menangani klik tombol Resep
    public void navigateToResep() {
        ResepFrame resepFrame = new ResepFrame();
        resepFrame.setVisible(true);
        dashboardFrame.dispose();
    }

    // Metode untuk menangani klik tombol Produksi & Pengemasan
    public void navigateToProduksi() {
        ProduksiFrame produksiFrame = new ProduksiFrame();
        produksiFrame.setVisible(true);
        dashboardFrame.dispose();
    }

    // Metode untuk menangani klik tombol Distribusi & Penjualan
    public void navigateToDistribusi() {
        DistribusiFrame distribusiFrame = new DistribusiFrame();
        distribusiFrame.setVisible(true);
        dashboardFrame.dispose();
    }

    // Metode untuk menangani klik tombol Penghitungan Upah
    public void navigateToUpah() {
        UpahFrame upahFrame = new UpahFrame();
        upahFrame.setVisible(true);
        dashboardFrame.dispose();
    }

    // Metode untuk menangani klik tombol Laporan Keuangan
    public void navigateToLaporanKeuangan() {
        LaporanKeuanganFrame laporanKeuanganFrame = new LaporanKeuanganFrame();
        laporanKeuanganFrame.setVisible(true);
        dashboardFrame.dispose();
    }

    // Metode untuk menangani aksi Logout
    public void handleLogout() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        dashboardFrame.dispose(); // Tutup dashboard
    }
}
