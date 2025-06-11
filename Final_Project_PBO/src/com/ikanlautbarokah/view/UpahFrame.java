/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.view;

import com.ikanlautbarokah.controller.UpahController; // Import Controller

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 *
 * @author Satrio Aji
 */
public class UpahFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private UpahController controller; // Referensi ke Controller

    // Fields untuk input dialog
    private JTextField namaPegawaiField;
    private JTextField jumlahProduksiField;
    private JTextField upahPerUnitField;

    private JButton editBtn;
    private JButton deleteBtn;

    public UpahFrame() {
        setTitle("Penghitungan Upah Pegawai");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();

        // Inisialisasi Controller
        this.controller = new UpahController(this);
        controller.loadUpahData(); // Memuat data awal
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // --- Tabel Upah ---
        String[] kolom = {"ID", "Nama Pegawai", "Jumlah Produksi", "Upah per Produksi", "Total Upah"};
        tableModel = new DefaultTableModel(null, kolom) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRow() != -1) {
                    editBtn.setEnabled(true);
                    deleteBtn.setEnabled(true);
                } else {
                    editBtn.setEnabled(false);
                    deleteBtn.setEnabled(false);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Panel Tombol Aksi (Input, Edit, Hapus) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton tambahBtn = new JButton("Input Upah");
        editBtn = new JButton("Edit Upah");
        deleteBtn = new JButton("Hapus Upah");

        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        tambahBtn.addActionListener(e -> showInputUpahDialog());
        editBtn.addActionListener(e -> showEditUpahDialog());
        deleteBtn.addActionListener(e -> showDeleteConfirmation());

        buttonPanel.add(tambahBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Metode untuk mendapatkan TableModel (dipanggil oleh Controller)
    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    // Metode untuk menampilkan dialog input upah
    private void showInputUpahDialog() {
        namaPegawaiField = new JTextField();
        jumlahProduksiField = new JTextField();
        upahPerUnitField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Nama Pegawai:")); panel.add(namaPegawaiField);
        panel.add(new JLabel("Jumlah Produksi:")); panel.add(jumlahProduksiField);
        panel.add(new JLabel("Upah per Produksi:")); panel.add(upahPerUnitField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Input Upah", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.addUpah(
                namaPegawaiField.getText(),
                jumlahProduksiField.getText(),
                upahPerUnitField.getText()
            );
        }
    }

    // Metode untuk menampilkan dialog edit upah
    private void showEditUpahDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris upah yang ingin diedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil data dari baris yang dipilih
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String currentNamaPegawai = (String) tableModel.getValueAt(selectedRow, 1);
        int currentJumlahProduksi = (int) tableModel.getValueAt(selectedRow, 2);
        double currentUpahPerUnit = (double) tableModel.getValueAt(selectedRow, 3);

        namaPegawaiField = new JTextField(currentNamaPegawai);
        jumlahProduksiField = new JTextField(String.valueOf(currentJumlahProduksi));
        upahPerUnitField = new JTextField(String.valueOf(currentUpahPerUnit));

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Nama Pegawai:")); panel.add(namaPegawaiField);
        panel.add(new JLabel("Jumlah Produksi:")); panel.add(jumlahProduksiField);
        panel.add(new JLabel("Upah per Produksi:")); panel.add(upahPerUnitField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Upah", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.updateUpah(
                id,
                namaPegawaiField.getText(),
                jumlahProduksiField.getText(),
                upahPerUnitField.getText()
            );
        }
    }

    // Metode untuk menampilkan konfirmasi hapus
    private void showDeleteConfirmation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris upah yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        controller.deleteUpah(id);
    }
}