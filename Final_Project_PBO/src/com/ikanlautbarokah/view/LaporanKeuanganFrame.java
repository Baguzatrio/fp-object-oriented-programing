/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.view;

import com.ikanlautbarokah.controller.LaporanKeuanganController; // Import Controller

import javax.swing.*;
import java.awt.*;
import java.time.Year; // Untuk tahun
import java.util.stream.IntStream; // Untuk mengisi ComboBox tahun
/**
 *
 * @author Satrio Aji
 */
public class LaporanKeuanganFrame extends JFrame {
    private JLabel pendapatanVal;
    private JLabel biayaVal;
    private JLabel upahVal;
    private JLabel labaVal;

    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JButton generateReportBtn;

    private LaporanKeuanganController controller; // Referensi ke Controller

    public LaporanKeuanganFrame() {
        setTitle("Laporan Keuangan");
        setSize(500, 350); // Ukuran sedikit lebih besar untuk input
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();

        // Inisialisasi Controller
        this.controller = new LaporanKeuanganController(this);
        controller.initializeLaporan(); // Memuat laporan awal (bulan/tahun saat ini)
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10)); // Outer layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel, BorderLayout.CENTER);

        // --- Panel Pilihan Bulan & Tahun ---
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        monthComboBox = new JComboBox<>();
        yearComboBox = new JComboBox<>();
        generateReportBtn = new JButton("Generate Laporan");

        filterPanel.add(new JLabel("Bulan:"));
        filterPanel.add(monthComboBox);
        filterPanel.add(new JLabel("Tahun:"));
        filterPanel.add(yearComboBox);
        filterPanel.add(generateReportBtn);

        mainPanel.add(filterPanel, BorderLayout.NORTH);

        // --- Panel Hasil Laporan ---
        JPanel reportDetailPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        
        pendapatanVal = new JLabel();
        biayaVal = new JLabel();
        upahVal = new JLabel();
        labaVal = new JLabel();

        reportDetailPanel.add(new JLabel("Total Pendapatan:")); reportDetailPanel.add(pendapatanVal);
        reportDetailPanel.add(new JLabel("Biaya Produksi:")); reportDetailPanel.add(biayaVal);
        reportDetailPanel.add(new JLabel("Total Upah Dibayar:")); reportDetailPanel.add(upahVal);
        reportDetailPanel.add(new JLabel("Laba / Rugi Bersih:")); reportDetailPanel.add(labaVal);

        mainPanel.add(reportDetailPanel, BorderLayout.CENTER);

        // --- Action Listener untuk Generate Laporan ---
        generateReportBtn.addActionListener(e -> {
            int selectedYear = (Integer) yearComboBox.getSelectedItem();
            // Konversi nama bulan ke angka (1-12)
            int selectedMonth = monthComboBox.getSelectedIndex() + 1; // Index 0 = "Semua Bulan", jadi +1

            controller.generateAndDisplayLaporan(selectedYear, selectedMonth);
        });
    }

    // --- Metode yang dipanggil oleh Controller untuk memperbarui UI ---
    public void setPendapatan(String value) {
        pendapatanVal.setText(value);
    }

    public void setBiayaProduksi(String value) {
        biayaVal.setText(value);
    }

    public void setUpah(String value) {
        upahVal.setText(value);
    }

    public void setLaba(String value) {
        labaVal.setText(value);
    }
    
    public void setLabaColor(Color color) {
        labaVal.setForeground(color);
    }

    // --- Metode untuk mengisi ComboBox Bulan dan Tahun ---
    public void populateMonthComboBox(int currentMonth) {
        String[] months = {"Semua Bulan", "Januari", "Februari", "Maret", "April", "Mei", "Juni", 
                           "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        monthComboBox.setModel(new DefaultComboBoxModel<>(months));
        // Set bulan saat ini jika ada
        if (currentMonth >= 1 && currentMonth <= 12) {
            monthComboBox.setSelectedIndex(currentMonth); // Index 0 adalah "Semua Bulan"
        } else {
            monthComboBox.setSelectedIndex(0); // Default ke "Semua Bulan"
        }
    }

    public void populateYearComboBox(int currentYear) {
        DefaultComboBoxModel<Integer> yearModel = new DefaultComboBoxModel<>();
        int startYear = 2020; // Tahun awal yang relevan
        int endYear = Year.now().getValue() + 5; // Hingga 5 tahun ke depan dari sekarang

        IntStream.rangeClosed(startYear, endYear).forEach(yearModel::addElement);
        yearComboBox.setModel(yearModel);
        yearComboBox.setSelectedItem(currentYear); // Set tahun saat ini
    }
}