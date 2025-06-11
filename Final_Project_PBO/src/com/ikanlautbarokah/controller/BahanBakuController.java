/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.controller;

import com.ikanlautbarokah.model.BahanBaku;
import com.ikanlautbarokah.model.BahanBakuModel;
import com.ikanlautbarokah.view.BahanBakuFrame;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author Satrio Aji
 */
public class BahanBakuController {
    private BahanBakuModel model;
    private BahanBakuFrame view; // Referensi ke View

    public BahanBakuController(BahanBakuFrame view) {
        this.view = view;
        this.model = new BahanBakuModel();
    }

    public void loadBahanBakuData() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0); // Bersihkan tabel sebelum memuat data baru
        try {
            List<BahanBaku> bahanBakuList = model.getAllBahanBaku();
            for (BahanBaku bb : bahanBakuList) {
                Object[] row = {
                    bb.getId(),
                    bb.getNama(),
                    bb.getStok(),
                    bb.getSatuan(),
                    bb.getHargaPerUnit(),
                    bb.getTanggalBeli(),
                    bb.getTanggalKadaluwarsa()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data bahan baku: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void addBahanBaku(String nama, String stokStr, String satuan, String hargaStr, String tglBeliStr, String tglKadaluwarsaStr) {
        try {
            // Validasi dan konversi input
            double stok = Double.parseDouble(stokStr);
            double harga = Double.parseDouble(hargaStr);
            Date tglBeli = Date.valueOf(tglBeliStr);
            Date tglKadaluwarsa = Date.valueOf(tglKadaluwarsaStr);

            BahanBaku newBahanBaku = new BahanBaku(nama, stok, satuan, harga, tglBeli, tglKadaluwarsa);
            model.addBahanBaku(newBahanBaku);
            JOptionPane.showMessageDialog(view, "Bahan baku berhasil ditambahkan!");
            loadBahanBakuData(); // Muat ulang data setelah penambahan
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Stok dan Harga harus berupa angka valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) { // Untuk Date.valueOf
            JOptionPane.showMessageDialog(view, "Format tanggal tidak valid. Gunakan YYYY-MM-DD.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal menambah bahan baku: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Anda bisa menambahkan metode untuk update dan delete di sini juga
    public void updateBahanBaku(int id, String nama, String stokStr, String satuan, String hargaStr, String tglBeliStr, String tglKadaluwarsaStr) {
        try {
            double stok = Double.parseDouble(stokStr);
            double harga = Double.parseDouble(hargaStr);
            Date tglBeli = Date.valueOf(tglBeliStr);
            Date tglKadaluwarsa = Date.valueOf(tglKadaluwarsaStr);

            BahanBaku updatedBahanBaku = new BahanBaku(id, nama, stok, satuan, harga, tglBeli, tglKadaluwarsa);
            model.updateBahanBaku(updatedBahanBaku);
            JOptionPane.showMessageDialog(view, "Bahan baku berhasil diperbarui!");
            loadBahanBakuData();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Stok dan Harga harus berupa angka valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(view, "Format tanggal tidak valid. Gunakan YYYY-MM-DD.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memperbarui bahan baku: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void deleteBahanBaku(int id) {
        int confirm = JOptionPane.showConfirmDialog(view, "Apakah Anda yakin ingin menghapus bahan baku ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                model.deleteBahanBaku(id);
                JOptionPane.showMessageDialog(view, "Bahan baku berhasil dihapus!");
                loadBahanBakuData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Gagal menghapus bahan baku: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
