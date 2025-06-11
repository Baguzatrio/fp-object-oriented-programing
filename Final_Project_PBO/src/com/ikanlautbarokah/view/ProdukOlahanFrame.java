/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.view;

import com.ikanlautbarokah.controller.ProdukOlahanController; // Import Controller
import com.ikanlautbarokah.model.Resep; // Import Resep POJO untuk JComboBox

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Satrio Aji
 */
public class ProdukOlahanFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private ProdukOlahanController controller; // Referensi ke Controller

    // Fields untuk input dialog
    private JTextField namaProdukField;
    private JComboBox<Resep> resepComboBox; // JComboBox of Resep objects
    private JTextField ukuranKemasanField;
    private JTextField jumlahProdukField;

    private JButton editBtn;
    private JButton deleteBtn;

    public ProdukOlahanFrame() {
        setTitle("Manajemen Data Produk Olahan");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();

        // Inisialisasi Controller dan berikan referensi ke View ini
        this.controller = new ProdukOlahanController(this);
        controller.loadProdukOlahanData(); // Memuat data awal melalui controller
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // --- Tabel Produk Olahan ---
        String[] kolom = {"ID", "Nama Produk", "Resep", "Ukuran Kemasan (gram)", "Jumlah Produk"};
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

        // --- Panel Tombol Aksi (Tambah, Edit, Hapus) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton tambahBtn = new JButton("Tambah Produk");
        editBtn = new JButton("Edit Produk");
        deleteBtn = new JButton("Hapus Produk");

        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        tambahBtn.addActionListener(e -> showTambahProdukDialog());
        editBtn.addActionListener(e -> showEditProdukDialog());
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

    // Metode untuk menampilkan dialog tambah produk
    private void showTambahProdukDialog() {
        namaProdukField = new JTextField();
        resepComboBox = new JComboBox<>(controller.getResepComboBoxModel()); // Dapatkan model dari controller
        ukuranKemasanField = new JTextField();
        jumlahProdukField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Nama Produk:")); panel.add(namaProdukField);
        panel.add(new JLabel("Resep:")); panel.add(resepComboBox);
        panel.add(new JLabel("Ukuran Kemasan (gram):")); panel.add(ukuranKemasanField);
        panel.add(new JLabel("Jumlah Produk Jadi:")); panel.add(jumlahProdukField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Tambah Produk Olahan", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.addProdukOlahan(
                namaProdukField.getText(),
                (Resep) resepComboBox.getSelectedItem(), // Kirim objek Resep
                ukuranKemasanField.getText(),
                jumlahProdukField.getText()
            );
        }
    }

    // Metode untuk menampilkan dialog edit produk
    private void showEditProdukDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String namaProduk = (String) tableModel.getValueAt(selectedRow, 1);
        String namaResepSaatIni = (String) tableModel.getValueAt(selectedRow, 2); // Ini hanya nama, bukan objek Resep
        int ukuranKemasan = (int) tableModel.getValueAt(selectedRow, 3);
        int jumlahProduk = (int) tableModel.getValueAt(selectedRow, 4);

        namaProdukField = new JTextField(namaProduk);
        resepComboBox = new JComboBox<>(controller.getResepComboBoxModel());

        // Set resep yang dipilih di ComboBox berdasarkan nama
        for (int i = 0; i < resepComboBox.getItemCount(); i++) {
            if (resepComboBox.getItemAt(i).getNamaProduk().equals(namaResepSaatIni)) {
                resepComboBox.setSelectedIndex(i);
                break;
            }
        }

        ukuranKemasanField = new JTextField(String.valueOf(ukuranKemasan));
        jumlahProdukField = new JTextField(String.valueOf(jumlahProduk));

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Nama Produk:")); panel.add(namaProdukField);
        panel.add(new JLabel("Resep:")); panel.add(resepComboBox);
        panel.add(new JLabel("Ukuran Kemasan (gram):")); panel.add(ukuranKemasanField);
        panel.add(new JLabel("Jumlah Produk Jadi:")); panel.add(jumlahProdukField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Produk Olahan", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.updateProdukOlahan(
                id,
                namaProdukField.getText(),
                (Resep) resepComboBox.getSelectedItem(), // Kirim objek Resep
                ukuranKemasanField.getText(),
                jumlahProdukField.getText()
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
        controller.deleteProdukOlahan(id);
    }
}
