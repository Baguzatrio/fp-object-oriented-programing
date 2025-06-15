package controller;

import model.UpahPekerjaModel;
import model.*;
import view.UpahPekerja2;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.List;
import view.DataPekerja2;

public class UpahPekerjaController {
    private Connection conn;
    private UpahPekerja2 view;
    private UpahPekerjaModel model;
    private User user;

    public UpahPekerjaController(Connection conn, UpahPekerja2 view, User user) {
        this.conn = conn;
        this.view = view;
        this.user = user;
        this.model = new UpahPekerjaModel(conn);

        initController();
        loadData();
    }

    private void initController() {
        view.getBtnDaftarPekerja().addActionListener(e -> {
    view.setEnabled(false); // Nonaktifkan sementara
    
    DataPekerja2 daftarView = new DataPekerja2(user);
    daftarView.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {
            view.setEnabled(true); // Aktifkan kembali
            view.toFront(); // Bawa ke depan
        }
    });
    
    new DataPekerjaController(daftarView);
    daftarView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    daftarView.setVisible(true);
});

        // Action untuk combo box filter utama
        view.getFilterComboBox().addActionListener(e -> {
            updateFilterDetails();
            loadData();
        });

        // Action untuk filter detail (minggu, bulan, tahun)
        view.getWeekComboBox().addActionListener(e -> loadData());
        view.getMonthComboBox().addActionListener(e -> loadData());
        view.getYearComboBox().addActionListener(e -> loadData());
    }

    private void updateFilterDetails() {
        // Sembunyikan semua filter detail terlebih dahulu
        view.getWeekComboBox().setVisible(false);
        view.getMonthComboBox().setVisible(false);
        view.getYearComboBox().setVisible(false);

        // Tampilkan hanya yang relevan berdasarkan filter utama
        String selectedFilter = (String) view.getFilterComboBox().getSelectedItem();
        switch (selectedFilter.toLowerCase()) {
            case "mingguan":
                view.getWeekComboBox().setVisible(true);
                view.getMonthComboBox().setVisible(true);
                view.getYearComboBox().setVisible(true);
                break;
            case "bulanan":
                view.getMonthComboBox().setVisible(true);
                view.getYearComboBox().setVisible(true);
                break;
            case "tahunan":
                view.getYearComboBox().setVisible(true);
                break;
            // Untuk harian tidak perlu menampilkan filter tambahan
        }
    }

    private void loadData() {
        try {
            // Ambil nilai dari filter utama
            String selectedFilter = (String) view.getFilterComboBox().getSelectedItem();
            UpahPekerjaModel.FilterPeriod filter = UpahPekerjaModel.FilterPeriod.valueOf(selectedFilter.toUpperCase());

            // Ambil nilai dari filter detail (bisa null jika tidak digunakan)
            Integer week = null;
            Integer month = null;
            Integer year = null;

            if (view.getWeekComboBox().isVisible()) {
                week = (Integer) view.getWeekComboBox().getSelectedItem();
            }
            if (view.getMonthComboBox().isVisible()) {
                month = view.getMonthComboBox().getSelectedIndex() + 1; // Konversi ke angka bulan (1-12)
            }
            if (view.getYearComboBox().isVisible()) {
                year = (Integer) view.getYearComboBox().getSelectedItem();
            }

            // Ambil data dari model
            List<Object[]> data = model.getDataUpah(filter, week, month, year);

            // Tampilkan data di view
            view.tampilkanData(data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}