/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.controller;

import com.ikanlautbarokah.model.Resep;
import com.ikanlautbarokah.model.ResepModel;
import com.ikanlautbarokah.view.ResepFrame;
import com.ikanlautbarokah.view.ResepDetailFrame; // Akan digunakan untuk navigasi

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author Satrio Aji
 */
public class ResepController {
    private ResepModel model;
    private ResepFrame view;

    public ResepController(ResepFrame view) {
        this.view = view;
        this.model = new ResepModel();
    }

    public void loadResepData() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0); // Bersihkan tabel
        try {
            List<Resep> resepList = model.getAllResep();
            for (Resep resep : resepList) {
                Object[] row = {
                    resep.getId(),
                    resep.getNamaProduk()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data resep: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void addResep(String namaProduk) {
        if (namaProduk == null || namaProduk.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Nama resep tidak boleh kosong.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            model.addResep(namaProduk);
            JOptionPane.showMessageDialog(view, "Resep berhasil ditambahkan!");
            loadResepData(); // Muat ulang data
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal menambah resep: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void editResep(int id, String newNamaProduk) {
        if (newNamaProduk == null || newNamaProduk.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Nama resep tidak boleh kosong.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Resep updatedResep = new Resep(id, newNamaProduk);
            model.updateResep(updatedResep);
            JOptionPane.showMessageDialog(view, "Resep berhasil diperbarui!");
            loadResepData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memperbarui resep: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void deleteResep(int id) {
        int confirm = JOptionPane.showConfirmDialog(view, "Apakah Anda yakin ingin menghapus resep ini? Semua komposisi terkait juga akan dihapus.", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                model.deleteResep(id);
                JOptionPane.showMessageDialog(view, "Resep berhasil dihapus!");
                loadResepData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Gagal menghapus resep: " + e.getMessage() + "\nPastikan tidak ada produk olahan yang masih menggunakan resep ini.", "Error Database", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public void showResepDetail(int resepId, String namaProduk) {
        // Note: ResepDetailFrame dan ResepDetailController akan dibuat nanti
        ResepDetailFrame resepDetailFrame = new ResepDetailFrame(resepId, namaProduk);
        resepDetailFrame.setVisible(true);
    }
}
