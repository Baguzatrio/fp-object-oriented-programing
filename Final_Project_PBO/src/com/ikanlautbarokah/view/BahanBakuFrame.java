/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.view;

import com.ikanlautbarokah.controller.BahanBakuController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
/**
 *
 * @author Satrio Aji
 */
public class BahanBakuFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private BahanBakuController controller; // Referensi ke Controller

    // Fields untuk input form (jika Anda ingin menempatkannya di dalam frame)
    private JTextField namaField;
    private JTextField stokField;
    private JTextField satuanField;
    private JTextField hargaField;
    private JTextField tglBeliField;
    private JTextField tglKadaluwarsaField;

    private JButton editBtn;
    private JButton deleteBtn;


    public BahanBakuFrame() {
        setTitle("Manajemen Bahan Baku");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();

        // Inisialisasi Controller dan berikan referensi ke View ini
        this.controller = new BahanBakuController(this);
        controller.loadBahanBakuData(); // Memuat data awal melalui controller
    }

    private void initComponents() {
        // Panel utama menggunakan BorderLayout
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // --- Tabel Bahan Baku ---
        String[] kolom = {"ID", "Nama Bahan", "Stok", "Satuan", "Harga", "Tgl Beli", "Tgl Kadaluwarsa"};
        tableModel = new DefaultTableModel(null, kolom) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat semua sel tidak dapat diedit langsung
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Hanya bisa memilih satu baris
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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); // Padding horizontal
        JButton tambahBtn = new JButton("Tambah Bahan Baku");
        editBtn = new JButton("Edit Bahan Baku");
        deleteBtn = new JButton("Hapus Bahan Baku");

        // Nonaktifkan tombol edit/hapus secara default
        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        tambahBtn.addActionListener(e -> showTambahBahanBakuDialog());
        editBtn.addActionListener(e -> showEditBahanBakuDialog());
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

    // Metode untuk menampilkan dialog tambah bahan baku
    private void showTambahBahanBakuDialog() {
        namaField = new JTextField();
        stokField = new JTextField();
        satuanField = new JTextField();
        hargaField = new JTextField();
        tglBeliField = new JTextField("YYYY-MM-DD");
        tglKadaluwarsaField = new JTextField("YYYY-MM-DD");

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5)); // Menambah gap
        inputPanel.add(new JLabel("Nama Bahan:")); inputPanel.add(namaField);
        inputPanel.add(new JLabel("Stok:")); inputPanel.add(stokField);
        inputPanel.add(new JLabel("Satuan:")); inputPanel.add(satuanField);
        inputPanel.add(new JLabel("Harga/Unit:")); inputPanel.add(hargaField);
        inputPanel.add(new JLabel("Tanggal Beli (YYYY-MM-DD):")); inputPanel.add(tglBeliField);
        inputPanel.add(new JLabel("Tanggal Kadaluwarsa (YYYY-MM-DD):")); inputPanel.add(tglKadaluwarsaField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Tambah Bahan Baku", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            // Panggil metode addBahanBaku dari controller
            controller.addBahanBaku(
                namaField.getText(),
                stokField.getText(),
                satuanField.getText(),
                hargaField.getText(),
                tglBeliField.getText(),
                tglKadaluwarsaField.getText()
            );
        }
    }

    // Metode untuk menampilkan dialog edit bahan baku
    private void showEditBahanBakuDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil data dari baris yang dipilih
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String nama = (String) tableModel.getValueAt(selectedRow, 1);
        double stok = (double) tableModel.getValueAt(selectedRow, 2);
        String satuan = (String) tableModel.getValueAt(selectedRow, 3);
        double harga = (double) tableModel.getValueAt(selectedRow, 4);
        Date tglBeli = (Date) tableModel.getValueAt(selectedRow, 5);
        Date tglKadaluwarsa = (Date) tableModel.getValueAt(selectedRow, 6);

        namaField = new JTextField(nama);
        stokField = new JTextField(String.valueOf(stok));
        satuanField = new JTextField(satuan);
        hargaField = new JTextField(String.valueOf(harga));
        tglBeliField = new JTextField(tglBeli.toString());
        tglKadaluwarsaField = new JTextField(tglKadaluwarsa.toString());

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.add(new JLabel("Nama Bahan:")); inputPanel.add(namaField);
        inputPanel.add(new JLabel("Stok:")); inputPanel.add(stokField);
        inputPanel.add(new JLabel("Satuan:")); inputPanel.add(satuanField);
        inputPanel.add(new JLabel("Harga/Unit:")); inputPanel.add(hargaField);
        inputPanel.add(new JLabel("Tanggal Beli (YYYY-MM-DD):")); inputPanel.add(tglBeliField);
        inputPanel.add(new JLabel("Tanggal Kadaluwarsa (YYYY-MM-DD):")); inputPanel.add(tglKadaluwarsaField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Edit Bahan Baku", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.updateBahanBaku(
                id,
                namaField.getText(),
                stokField.getText(),
                satuanField.getText(),
                hargaField.getText(),
                tglBeliField.getText(),
                tglKadaluwarsaField.getText()
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
        controller.deleteBahanBaku(id);
    }
}
