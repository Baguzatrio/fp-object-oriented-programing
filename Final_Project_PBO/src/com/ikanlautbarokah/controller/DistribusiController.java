/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.controller;

import com.ikanlautbarokah.model.Distribusi;
import com.ikanlautbarokah.model.DistribusiModel;
import com.ikanlautbarokah.view.DistribusiFrame;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Date; // Untuk tipe Date jika diperlukan
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author Satrio Aji
 */
public class DistribusiController {
    private DistribusiModel model;
    private DistribusiFrame view; // Referensi ke View

    public DistribusiController(DistribusiFrame view) {
        this.view = view;
        this.model = new DistribusiModel();
    }

    public void loadDistribusiData() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0); // Bersihkan tabel sebelum memuat data baru
        try {
            List<Distribusi> distribusiList = model.getAllDistribusi();
            for (Distribusi dist : distribusiList) {
                Object[] row = {
                    dist.getId(),
                    dist.getTanggal(),
                    dist.getProduk(),
                    dist.getJumlah(),
                    dist.getTujuan(),
                    dist.getHargaTotal(),
                    dist.getMetodeBayar()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data distribusi: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void addDistribusi(String produk, String jumlahStr, String tujuan, String hargaStr, String metodeBayar) {
        try {
            // Validasi dan konversi input
            int jumlah = Integer.parseInt(jumlahStr);
            double harga = Double.parseDouble(hargaStr);

            Distribusi newDistribusi = new Distribusi(produk, jumlah, tujuan, harga, metodeBayar);
            model.addDistribusi(newDistribusi);
            JOptionPane.showMessageDialog(view, "Distribusi berhasil dicatat!");
            loadDistribusiData(); // Muat ulang data setelah penambahan
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Jumlah dan Harga Total harus berupa angka valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal mencatat distribusi: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void updateDistribusi(int id, String produk, String jumlahStr, String tujuan, String hargaStr, String metodeBayar) {
        try {
            int jumlah = Integer.parseInt(jumlahStr);
            double harga = Double.parseDouble(hargaStr);

            // Karena tanggal diisi CURRENT_DATE(), kita bisa mengambilnya dari DB jika diperlukan
            // atau cukup lewati dalam konstruktor update jika memang tidak diupdate.
            // Untuk kesederhanaan, kita bisa membuat objek baru tanpa tanggal di controller ini
            // jika tanggal tidak diedit di UI.
            Distribusi updatedDistribusi = new Distribusi(id, null, produk, jumlah, tujuan, harga, metodeBayar); // null untuk tanggal karena tidak diupdate
            model.updateDistribusi(updatedDistribusi);
            JOptionPane.showMessageDialog(view, "Data distribusi berhasil diperbarui!");
            loadDistribusiData();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Jumlah dan Harga Total harus berupa angka valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memperbarui distribusi: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void deleteDistribusi(int id) {
        int confirm = JOptionPane.showConfirmDialog(view, "Apakah Anda yakin ingin menghapus data distribusi ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                model.deleteDistribusi(id);
                JOptionPane.showMessageDialog(view, "Data distribusi berhasil dihapus!");
                loadDistribusiData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Gagal menghapus data distribusi: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
