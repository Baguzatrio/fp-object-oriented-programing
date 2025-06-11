/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.view;

import com.ikanlautbarokah.controller.DistribusiController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date; // Hanya untuk menampilkan data Date di dialog
/**
 *
 * @author Satrio Aji
 */
public class DistribusiFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private DistribusiController controller; // Referensi ke Controller

    // Fields untuk input dialog
    private JTextField produkField;
    private JTextField jumlahField;
    private JTextField tujuanField;
    private JTextField hargaField;
    private JComboBox<String> metodeBayarComboBox;

    private JButton editBtn;
    private JButton deleteBtn;

    public DistribusiFrame() {
        setTitle("Distribusi & Penjualan Produk");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();

        // Inisialisasi Controller dan berikan referensi ke View ini
        this.controller = new DistribusiController(this);
        controller.loadDistribusiData(); // Memuat data awal melalui controller
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // --- Tabel Distribusi ---
        String[] kolom = {"ID", "Tanggal", "Produk", "Jumlah", "Tujuan", "Harga Total", "Metode Bayar"};
        tableModel = new DefaultTableModel(null, kolom) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat semua sel tidak dapat diedit langsung
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

        // --- Panel Tombol Aksi (Tambah, Edit, Hapus) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton tambahBtn = new JButton("Input Distribusi");
        editBtn = new JButton("Edit Distribusi");
        deleteBtn = new JButton("Hapus Distribusi");

        // Nonaktifkan tombol edit/hapus secara default
        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        tambahBtn.addActionListener(e -> showInputDistribusiDialog());
        editBtn.addActionListener(e -> showEditDistribusiDialog());
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

    // Metode untuk menampilkan dialog input distribusi
    private void showInputDistribusiDialog() {
        produkField = new JTextField();
        jumlahField = new JTextField();
        tujuanField = new JTextField();
        hargaField = new JTextField();
        metodeBayarComboBox = new JComboBox<>(new String[]{"Tunai", "Tempo"});

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("Produk:")); panel.add(produkField);
        panel.add(new JLabel("Jumlah (kemasan):")); panel.add(jumlahField);
        panel.add(new JLabel("Tujuan:")); panel.add(tujuanField);
        panel.add(new JLabel("Harga Total:")); panel.add(hargaField);
        panel.add(new JLabel("Metode Bayar:")); panel.add(metodeBayarComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Input Distribusi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.addDistribusi(
                produkField.getText(),
                jumlahField.getText(),
                tujuanField.getText(),
                hargaField.getText(),
                (String) metodeBayarComboBox.getSelectedItem()
            );
        }
    }

    // Metode untuk menampilkan dialog edit distribusi
    private void showEditDistribusiDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil data dari baris yang dipilih
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        // Date tanggal = (Date) tableModel.getValueAt(selectedRow, 1); // Tanggal tidak diupdate
        String produk = (String) tableModel.getValueAt(selectedRow, 2);
        int jumlah = (int) tableModel.getValueAt(selectedRow, 3);
        String tujuan = (String) tableModel.getValueAt(selectedRow, 4);
        double hargaTotal = (double) tableModel.getValueAt(selectedRow, 5);
        String metodeBayar = (String) tableModel.getValueAt(selectedRow, 6);

        produkField = new JTextField(produk);
        jumlahField = new JTextField(String.valueOf(jumlah));
        tujuanField = new JTextField(tujuan);
        hargaField = new JTextField(String.valueOf(hargaTotal));
        metodeBayarComboBox = new JComboBox<>(new String[]{"Tunai", "Tempo"});
        metodeBayarComboBox.setSelectedItem(metodeBayar);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("Produk:")); panel.add(produkField);
        panel.add(new JLabel("Jumlah (kemasan):")); panel.add(jumlahField);
        panel.add(new JLabel("Tujuan:")); panel.add(tujuanField);
        panel.add(new JLabel("Harga Total:")); panel.add(hargaField);
        panel.add(new JLabel("Metode Bayar:")); panel.add(metodeBayarComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Distribusi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.updateDistribusi(
                id,
                produkField.getText(),
                jumlahField.getText(),
                tujuanField.getText(),
                hargaField.getText(),
                (String) metodeBayarComboBox.getSelectedItem()
            );
        }
    }

    // Metode untuk menampilkan konfirmasi hapus
    private void showDeleteConfirmation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        controller.deleteDistribusi(id);
    }
}