/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.controller;

import com.ikanlautbarokah.model.Produksi;
import com.ikanlautbarokah.model.ProduksiModel;
import com.ikanlautbarokah.model.Resep; // Import Resep untuk ComboBox
import com.ikanlautbarokah.model.ResepModel; // Untuk mendapatkan daftar resep
import com.ikanlautbarokah.view.ProduksiFrame;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author Satrio Aji
 */
public class ProduksiController {
    private ProduksiModel model;
    private ResepModel resepModel; // Untuk mendapatkan daftar resep di dialog
    private ProduksiFrame view;

    public ProduksiController(ProduksiFrame view) {
        this.view = view;
        this.model = new ProduksiModel();
        this.resepModel = new ResepModel(); // Inisialisasi ResepModel
    }

    public void loadProduksiData() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0); // Bersihkan tabel
        try {
            List<Produksi> produksiList = model.getAllProduksi();
            for (Produksi prod : produksiList) {
                Object[] row = {
                    prod.getId(),
                    prod.getTanggal(),
                    prod.getNamaProdukResep(),
                    prod.getJumlahBatch(),
                    prod.getTotalKg(),
                    prod.getJumlahKemasan()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data produksi: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public DefaultComboBoxModel<Resep> getResepComboBoxModel() {
        DefaultComboBoxModel<Resep> comboModel = new DefaultComboBoxModel<>();
        try {
            List<Resep> resepList = resepModel.getAllResep();
            for (Resep resep : resepList) {
                comboModel.addElement(resep); // Menambahkan objek Resep langsung
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memuat daftar resep: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return comboModel;
    }

    public void addProduksi(Resep selectedResep, String batchStr, String kgStr) {
        try {
            if (selectedResep == null) {
                JOptionPane.showMessageDialog(view, "Pilih produk (resep) terlebih dahulu.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int jumlahBatch = Integer.parseInt(batchStr);
            double totalKg = Double.parseDouble(kgStr);

            if (jumlahBatch <= 0 || totalKg <= 0) {
                 JOptionPane.showMessageDialog(view, "Jumlah batch dan total kilogram harus lebih besar dari 0.", "Input Error", JOptionPane.WARNING_MESSAGE);
                 return;
            }

            int jumlahKemasan = (int) (totalKg * 1000 / 500); // Asumsi 1 kemasan = 500g

            Produksi newProduksi = new Produksi(selectedResep.getNamaProduk(), jumlahBatch, totalKg, jumlahKemasan);
            model.addProduksi(newProduksi, selectedResep.getId()); // Kirim objek Produksi dan resepId
            JOptionPane.showMessageDialog(view, "Produksi berhasil dicatat dan stok bahan baku dikurangi!");
            loadProduksiData(); // Muat ulang data
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Jumlah Batch dan Total Kilogram harus berupa angka valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal mencatat produksi: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Tambahkan metode edit dan delete jika diperlukan
    public void updateProduksi(int id, String produkNama, String batchStr, String kgStr) {
        try {
            int jumlahBatch = Integer.parseInt(batchStr);
            double totalKg = Double.parseDouble(kgStr);

             if (jumlahBatch <= 0 || totalKg <= 0) {
                 JOptionPane.showMessageDialog(view, "Jumlah batch dan total kilogram harus lebih besar dari 0.", "Input Error", JOptionPane.WARNING_MESSAGE);
                 return;
            }
            int jumlahKemasan = (int) (totalKg * 1000 / 500);

            // Perhatian: Update produksi tanpa mengembalikan/mengurangi stok adalah penyederhanaan.
            // Jika update ini mengubah jumlah, maka perlu logika kompensasi stok yang lebih kompleks.
            Produksi updatedProduksi = new Produksi(id, null, produkNama, jumlahBatch, totalKg, jumlahKemasan);
            model.updateProduksi(updatedProduksi);
            JOptionPane.showMessageDialog(view, "Data produksi berhasil diperbarui!");
            loadProduksiData();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Jumlah Batch dan Total Kilogram harus berupa angka valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memperbarui produksi: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void deleteProduksi(int id) {
        int confirm = JOptionPane.showConfirmDialog(view, "Apakah Anda yakin ingin menghapus data produksi ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // PERHATIAN: Menghapus produksi tanpa mengembalikan stok bahan baku adalah penyederhanaan.
                // Jika ingin mengembalikan stok, logika ini perlu transaksi dan mengambil detail produksi
                // yang dihapus untuk mengembalikan stok yang relevan.
                model.deleteProduksi(id);
                JOptionPane.showMessageDialog(view, "Data produksi berhasil dihapus!");
                loadProduksiData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Gagal menghapus produksi: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}