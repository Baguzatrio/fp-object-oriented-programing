/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.view;

import com.ikanlautbarokah.controller.ResepController; // Import Controller

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Satrio Aji
 */
public class ResepFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private ResepController controller; // Referensi ke Controller

    private JButton editBtn;
    private JButton deleteBtn;
    private JButton detailBtn;

    public ResepFrame() {
        setTitle("Manajemen Resep");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();

        // Inisialisasi Controller
        this.controller = new ResepController(this);
        controller.loadResepData(); // Memuat data awal
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // --- Tabel Resep ---
        String[] kolom = {"ID", "Nama Produk"};
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
                    detailBtn.setEnabled(true);
                } else {
                    editBtn.setEnabled(false);
                    deleteBtn.setEnabled(false);
                    detailBtn.setEnabled(false);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Panel Tombol Aksi (Tambah, Edit, Hapus, Detail) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton tambahBtn = new JButton("Tambah Resep Baru");
        editBtn = new JButton("Edit Resep");
        deleteBtn = new JButton("Hapus Resep");
        detailBtn = new JButton("Detail Komposisi");

        // Nonaktifkan tombol edit/hapus/detail secara default
        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        detailBtn.setEnabled(false);

        tambahBtn.addActionListener(e -> showTambahResepDialog());
        editBtn.addActionListener(e -> showEditResepDialog());
        deleteBtn.addActionListener(e -> showDeleteConfirmation());
        detailBtn.addActionListener(e -> showDetailKomposisi());

        buttonPanel.add(tambahBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(detailBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Metode untuk mendapatkan TableModel (dipanggil oleh Controller)
    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    // Metode untuk menampilkan dialog tambah resep
    private void showTambahResepDialog() {
        String namaProduk = JOptionPane.showInputDialog(this, "Masukkan nama produk/resep baru:");
        if (namaProduk != null) { // User tidak membatalkan dialog
            controller.addResep(namaProduk);
        }
    }

    // Metode untuk menampilkan dialog edit resep
    private void showEditResepDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih resep yang ingin diedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String currentNamaProduk = (String) tableModel.getValueAt(selectedRow, 1);
        String newNamaProduk = JOptionPane.showInputDialog(this, "Edit nama resep:", currentNamaProduk);

        if (newNamaProduk != null) { // User tidak membatalkan dialog
            controller.editResep(id, newNamaProduk);
        }
    }

    // Metode untuk menampilkan konfirmasi hapus
    private void showDeleteConfirmation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih resep yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        controller.deleteResep(id);
    }

    // Metode untuk menampilkan frame detail komposisi
    private void showDetailKomposisi() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih resep yang ingin dilihat detail komposisinya.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resepId = (int) tableModel.getValueAt(selectedRow, 0);
        String namaProduk = (String) tableModel.getValueAt(selectedRow, 1);
        controller.showResepDetail(resepId, namaProduk);
    }
}
