/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.controller;

import com.ikanlautbarokah.model.ProdukOlahan;
import com.ikanlautbarokah.model.ProdukOlahanModel;
import com.ikanlautbarokah.model.Resep; // Import Resep
import com.ikanlautbarokah.view.ProdukOlahanFrame;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Satrio Aji
 */
public class ProdukOlahanController {
    private ProdukOlahanModel model;
    private ProdukOlahanFrame view;

    public ProdukOlahanController(ProdukOlahanFrame view) {
        this.view = view;
        this.model = new ProdukOlahanModel();
    }

    public void loadProdukOlahanData() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0); // Bersihkan tabel
        try {
            List<ProdukOlahan> produkOlahanList = model.getAllProdukOlahan();
            for (ProdukOlahan po : produkOlahanList) {
                Object[] row = {
                    po.getId(),
                    po.getNamaProduk(),
                    po.getNamaResep(), // Tampilkan nama resep
                    po.getUkuranKemasan(),
                    po.getJumlah()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data produk olahan: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public DefaultComboBoxModel<Resep> getResepComboBoxModel() {
        DefaultComboBoxModel<Resep> comboModel = new DefaultComboBoxModel<>();
        try {
            List<Resep> resepList = model.getAllResep(); // Mengambil resep dari ProdukOlahanModel
            for (Resep resep : resepList) {
                comboModel.addElement(resep); // Menambahkan objek Resep langsung
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memuat daftar resep: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return comboModel;
    }

    public void addProdukOlahan(String namaProduk, Resep selectedResep, String ukuranKemasanStr, String jumlahStr) {
        try {
            if (selectedResep == null) {
                JOptionPane.showMessageDialog(view, "Pilih resep terlebih dahulu.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int ukuranKemasan = Integer.parseInt(ukuranKemasanStr);
            int jumlah = Integer.parseInt(jumlahStr);

            ProdukOlahan newProdukOlahan = new ProdukOlahan(namaProduk, selectedResep.getId(), ukuranKemasan, jumlah);
            model.addProdukOlahan(newProdukOlahan);
            JOptionPane.showMessageDialog(view, "Produk olahan berhasil ditambahkan!");
            loadProdukOlahanData(); // Muat ulang data
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Ukuran kemasan dan Jumlah Produk harus berupa angka valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal menambah produk olahan: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void updateProdukOlahan(int id, String namaProduk, Resep selectedResep, String ukuranKemasanStr, String jumlahStr) {
        try {
            if (selectedResep == null) {
                JOptionPane.showMessageDialog(view, "Pilih resep terlebih dahulu.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int ukuranKemasan = Integer.parseInt(ukuranKemasanStr);
            int jumlah = Integer.parseInt(jumlahStr);

            ProdukOlahan updatedProdukOlahan = new ProdukOlahan(id, namaProduk, selectedResep.getId(), selectedResep.getNamaProduk(), ukuranKemasan, jumlah);
            model.updateProdukOlahan(updatedProdukOlahan);
            JOptionPane.showMessageDialog(view, "Produk olahan berhasil diperbarui!");
            loadProdukOlahanData();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Ukuran kemasan dan Jumlah Produk harus berupa angka valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memperbarui produk olahan: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void deleteProdukOlahan(int id) {
        int confirm = JOptionPane.showConfirmDialog(view, "Apakah Anda yakin ingin menghapus produk olahan ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                model.deleteProdukOlahan(id);
                JOptionPane.showMessageDialog(view, "Produk olahan berhasil dihapus!");
                loadProdukOlahanData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Gagal menghapus produk olahan: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
