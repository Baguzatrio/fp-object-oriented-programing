/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.controller;

import com.ikanlautbarokah.model.Upah;
import com.ikanlautbarokah.model.UpahModel;
import com.ikanlautbarokah.view.UpahFrame;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Satrio Aji
 */
public class UpahController {
    private UpahModel model;
    private UpahFrame view;

    public UpahController(UpahFrame view) {
        this.view = view;
        this.model = new UpahModel();
    }

    public void loadUpahData() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0); // Bersihkan tabel
        try {
            List<Upah> upahList = model.getAllUpah();
            for (Upah upah : upahList) {
                Object[] row = {
                    upah.getId(),
                    upah.getNamaPegawai(),
                    upah.getJumlahProduksi(),
                    upah.getUpahPerUnit(),
                    upah.getTotalUpah()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data upah: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void addUpah(String namaPegawai, String jumlahProduksiStr, String upahPerUnitStr) {
        try {
            int jumlahProduksi = Integer.parseInt(jumlahProduksiStr);
            double upahPerUnit = Double.parseDouble(upahPerUnitStr);
            double totalUpah = jumlahProduksi * upahPerUnit;

            if (jumlahProduksi < 0 || upahPerUnit < 0) {
                 JOptionPane.showMessageDialog(view, "Jumlah produksi dan upah per unit tidak boleh negatif.", "Input Error", JOptionPane.WARNING_MESSAGE);
                 return;
            }

            Upah newUpah = new Upah(namaPegawai, jumlahProduksi, upahPerUnit, totalUpah);
            model.addUpah(newUpah);
            JOptionPane.showMessageDialog(view, "Data upah berhasil disimpan!");
            loadUpahData(); // Muat ulang data
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Jumlah Produksi dan Upah per Produksi harus berupa angka valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal menyimpan data upah: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void updateUpah(int id, String namaPegawai, String jumlahProduksiStr, String upahPerUnitStr) {
        try {
            int jumlahProduksi = Integer.parseInt(jumlahProduksiStr);
            double upahPerUnit = Double.parseDouble(upahPerUnitStr);
            double totalUpah = jumlahProduksi * upahPerUnit;

            if (jumlahProduksi < 0 || upahPerUnit < 0) {
                 JOptionPane.showMessageDialog(view, "Jumlah produksi dan upah per unit tidak boleh negatif.", "Input Error", JOptionPane.WARNING_MESSAGE);
                 return;
            }

            Upah updatedUpah = new Upah(id, namaPegawai, jumlahProduksi, upahPerUnit, totalUpah);
            model.updateUpah(updatedUpah);
            JOptionPane.showMessageDialog(view, "Data upah berhasil diperbarui!");
            loadUpahData();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Jumlah Produksi dan Upah per Produksi harus berupa angka valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memperbarui data upah: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void deleteUpah(int id) {
        int confirm = JOptionPane.showConfirmDialog(view, "Apakah Anda yakin ingin menghapus data upah ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                model.deleteUpah(id);
                JOptionPane.showMessageDialog(view, "Data upah berhasil dihapus!");
                loadUpahData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Gagal menghapus data upah: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
