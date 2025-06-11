/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.controller;

import com.ikanlautbarokah.model.LaporanKeuanganModel;
import com.ikanlautbarokah.model.LaporanKeuanganModel.RingkasanLaporan;
import com.ikanlautbarokah.view.LaporanKeuanganFrame;

import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.YearMonth; // Untuk mendapatkan tahun dan bulan saat ini
/**
 *
 * @author Satrio Aji
 */
public class LaporanKeuanganController {
    private LaporanKeuanganModel model;
    private LaporanKeuanganFrame view;
    private DecimalFormat currencyFormatter;

    public LaporanKeuanganController(LaporanKeuanganFrame view) {
        this.view = view;
        this.model = new LaporanKeuanganModel();
        this.currencyFormatter = new DecimalFormat("Rp #,##0.00"); // Untuk format mata uang
    }

    public void generateAndDisplayLaporan(int tahun, int bulan) {
        try {
            RingkasanLaporan laporan = model.generateLaporan(tahun, bulan);

            view.setPendapatan(currencyFormatter.format(laporan.totalPendapatan));
            view.setBiayaProduksi(currencyFormatter.format(laporan.totalBiayaProduksi));
            view.setUpah(currencyFormatter.format(laporan.totalUpah));
            view.setLaba(currencyFormatter.format(laporan.labaBersih));

            // Ubah warna laba/rugi
            if (laporan.labaBersih < 0) {
                view.setLabaColor(java.awt.Color.RED);
            } else {
                view.setLabaColor(java.awt.Color.BLUE); // Atau Color.GREEN
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal mendapatkan laporan keuangan: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Terjadi kesalahan saat membuat laporan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void initializeLaporan() {
        // Mendapatkan bulan dan tahun saat ini untuk inisialisasi
        YearMonth currentYearMonth = YearMonth.now();
        int currentYear = currentYearMonth.getYear();
        int currentMonth = currentYearMonth.getMonthValue();

        // Mengisi ComboBox tahun dan bulan
        view.populateYearComboBox(currentYear);
        view.populateMonthComboBox(currentMonth);

        // Langsung generate laporan untuk bulan dan tahun saat ini
        generateAndDisplayLaporan(currentYear, currentMonth);
    }
}
