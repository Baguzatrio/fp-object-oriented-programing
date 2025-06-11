/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.view;

import com.ikanlautbarokah.controller.ResepDetailController;
import com.ikanlautbarokah.model.BahanBaku; // Untuk JComboBox

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Satrio Aji
 */
public class ResepDetailFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private ResepDetailController controller;
    private JLabel resepNamaLabel;

    // Fields untuk input dialog
    private JComboBox<BahanBaku> bahanBakuComboBox;
    private JTextField jumlahField;

    private JButton editBtn;
    private JButton deleteBtn;

    public ResepDetailFrame(int resepId, String namaResep) {
        setTitle("Detail Komposisi Resep: " + namaResep);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();

        resepNamaLabel.setText("Komposisi untuk Resep: " + namaResep);

        // Inisialisasi Controller dengan ID resep yang dipilih
        this.controller = new ResepDetailController(this, resepId);
        controller.loadResepDetailData(); // Memuat data awal
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // Header untuk nama resep
        resepNamaLabel = new JLabel("Komposisi Resep", SwingConstants.CENTER);
        resepNamaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(resepNamaLabel, BorderLayout.NORTH);

        // --- Tabel Resep Detail ---
        String[] kolom = {"ID", "Nama Bahan Baku", "Jumlah", "ID Bahan Baku (hidden)"}; // ID Bahan Baku disembunyikan
        tableModel = new DefaultTableModel(null, kolom) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.getColumnModel().getColumn(3).setMinWidth(0); // Sembunyikan kolom ID Bahan Baku
        table.getColumnModel().getColumn(3).setMaxWidth(0);
        table.getColumnModel().getColumn(3).setWidth(0);
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
        JButton tambahBtn = new JButton("Tambah Komposisi");
        editBtn = new JButton("Edit Komposisi");
        deleteBtn = new JButton("Hapus Komposisi");

        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        tambahBtn.addActionListener(e -> showTambahKomposisiDialog());
        editBtn.addActionListener(e -> showEditKomposisiDialog());
        deleteBtn.addActionListener(e -> showDeleteConfirmation());

        buttonPanel.add(tambahBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    private void showTambahKomposisiDialog() {
        bahanBakuComboBox = new JComboBox<>(controller.getBahanBakuComboBoxModel()); // Dapatkan model dari controller
        jumlahField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Bahan Baku:")); panel.add(bahanBakuComboBox);
        panel.add(new JLabel("Jumlah:")); panel.add(jumlahField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Tambah Komposisi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.addResepDetail(
                (BahanBaku) bahanBakuComboBox.getSelectedItem(),
                jumlahField.getText()
            );
        }
    }

    private void showEditKomposisiDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris komposisi yang ingin diedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int detailId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentNamaBahan = (String) tableModel.getValueAt(selectedRow, 1);
        double currentJumlah = (double) tableModel.getValueAt(selectedRow, 2);

        bahanBakuComboBox = new JComboBox<>(controller.getBahanBakuComboBoxModel());
        // Set bahan baku yang dipilih di ComboBox berdasarkan nama
        for (int i = 0; i < bahanBakuComboBox.getItemCount(); i++) {
            if (bahanBakuComboBox.getItemAt(i).getNama().equals(currentNamaBahan)) {
                bahanBakuComboBox.setSelectedIndex(i);
                break;
            }
        }
        jumlahField = new JTextField(String.valueOf(currentJumlah));

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Bahan Baku:")); panel.add(bahanBakuComboBox);
        panel.add(new JLabel("Jumlah:")); panel.add(jumlahField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Komposisi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.updateResepDetail(
                detailId,
                (BahanBaku) bahanBakuComboBox.getSelectedItem(),
                jumlahField.getText()
            );
        }
    }

    private void showDeleteConfirmation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris komposisi yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int detailId = (int) tableModel.getValueAt(selectedRow, 0);
        controller.deleteResepDetail(detailId);
    }
}
