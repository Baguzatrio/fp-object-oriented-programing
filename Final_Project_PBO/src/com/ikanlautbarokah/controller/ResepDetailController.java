/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.controller;

import com.ikanlautbarokah.model.ResepDetail;
import com.ikanlautbarokah.model.ResepModel;
import com.ikanlautbarokah.model.BahanBaku; // Untuk ComboBox bahan baku
import com.ikanlautbarokah.model.BahanBakuModel; // Untuk mengambil daftar bahan baku
import com.ikanlautbarokah.view.ResepDetailFrame;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Satrio Aji
 */
public class ResepDetailController {
    private ResepModel resepModel;
    private BahanBakuModel bahanBakuModel; // Untuk mendapatkan daftar bahan baku
    private ResepDetailFrame view;
    private int currentResepId;

    public ResepDetailController(ResepDetailFrame view, int resepId) {
        this.view = view;
        this.resepModel = new ResepModel();
        this.bahanBakuModel = new BahanBakuModel(); // Inisialisasi BahanBakuModel
        this.currentResepId = resepId;
    }

    public void loadResepDetailData() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0); // Bersihkan tabel
        try {
            List<ResepDetail> detailList = resepModel.getResepDetailByResepId(currentResepId);
            for (ResepDetail rd : detailList) {
                Object[] row = {
                    rd.getId(),
                    rd.getNamaBahanBaku(), // Tampilkan nama bahan baku
                    rd.getJumlah(),
                    rd.getBahanBakuId() // Sembunyikan atau gunakan untuk edit
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memuat detail resep: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public DefaultComboBoxModel<BahanBaku> getBahanBakuComboBoxModel() {
        DefaultComboBoxModel<BahanBaku> comboModel = new DefaultComboBoxModel<>();
        try {
            List<BahanBaku> bahanBakuList = bahanBakuModel.getAllBahanBaku();
            for (BahanBaku bb : bahanBakuList) {
                comboModel.addElement(bb); // Menambahkan objek BahanBaku langsung
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memuat daftar bahan baku: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return comboModel;
    }

    public void addResepDetail(BahanBaku selectedBahanBaku, String jumlahStr) {
        try {
            if (selectedBahanBaku == null) {
                JOptionPane.showMessageDialog(view, "Pilih bahan baku terlebih dahulu.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double jumlah = Double.parseDouble(jumlahStr);
            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(view, "Jumlah harus lebih besar dari 0.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ResepDetail newDetail = new ResepDetail(currentResepId, selectedBahanBaku.getId(), jumlah);
            resepModel.addResepDetail(newDetail);
            JOptionPane.showMessageDialog(view, "Komposisi berhasil ditambahkan!");
            loadResepDetailData();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Jumlah harus berupa angka valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal menambah komposisi: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void updateResepDetail(int detailId, BahanBaku selectedBahanBaku, String jumlahStr) {
        try {
            if (selectedBahanBaku == null) {
                JOptionPane.showMessageDialog(view, "Pilih bahan baku terlebih dahulu.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double jumlah = Double.parseDouble(jumlahStr);
            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(view, "Jumlah harus lebih besar dari 0.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ResepDetail updatedDetail = new ResepDetail(detailId, currentResepId, null, selectedBahanBaku.getId(), null, jumlah);
            resepModel.updateResepDetail(updatedDetail);
            JOptionPane.showMessageDialog(view, "Komposisi berhasil diperbarui!");
            loadResepDetailData();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Jumlah harus berupa angka valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memperbarui komposisi: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void deleteResepDetail(int detailId) {
        int confirm = JOptionPane.showConfirmDialog(view, "Apakah Anda yakin ingin menghapus komposisi ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                resepModel.deleteResepDetail(detailId);
                JOptionPane.showMessageDialog(view, "Komposisi berhasil dihapus!");
                loadResepDetailData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Gagal menghapus komposisi: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
