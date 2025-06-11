/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.view;

import com.ikanlautbarokah.controller.ProduksiController; // Import Controller
import com.ikanlautbarokah.model.Resep; // Import Resep POJO untuk JComboBox

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
public class ProduksiFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private ProduksiController controller; // Referensi ke Controller

    // Fields untuk input dialog
    private JComboBox<Resep> resepComboBox;
    private JTextField batchField;
    private JTextField kgField;

    private JButton editBtn;
    private JButton deleteBtn;

    public ProduksiFrame() {
        setTitle("Produksi & Pengemasan");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();

        // Inisialisasi Controller
        this.controller = new ProduksiController(this);
        controller.loadProduksiData(); // Memuat data awal
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // --- Tabel Produksi ---
        String[] kolom = {"ID", "Tanggal", "Produk", "Jumlah Batch", "Total Kg", "Jumlah Kemasan"};
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
        JButton tambahBtn = new JButton("Input Produksi");
        editBtn = new JButton("Edit Produksi");
        deleteBtn = new JButton("Hapus Produksi");

        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        tambahBtn.addActionListener(e -> showInputProduksiDialog());
        editBtn.addActionListener(e -> showEditProduksiDialog());
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

    // Metode untuk menampilkan dialog input produksi
    private void showInputProduksiDialog() {
        resepComboBox = new JComboBox<>(controller.getResepComboBoxModel()); // Dapatkan model dari controller
        batchField = new JTextField();
        kgField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Pilih Produk (Resep):")); panel.add(resepComboBox);
        panel.add(new JLabel("Jumlah Batch:")); panel.add(batchField);
        panel.add(new JLabel("Total Kilogram Produk:")); panel.add(kgField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Input Produksi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.addProduksi(
                (Resep) resepComboBox.getSelectedItem(),
                batchField.getText(),
                kgField.getText()
            );
        }
    }

    // Metode untuk menampilkan dialog edit produksi
    private void showEditProduksiDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris produksi yang ingin diedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil data dari baris yang dipilih
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String currentProdukNama = (String) tableModel.getValueAt(selectedRow, 2);
        int currentJumlahBatch = (int) tableModel.getValueAt(selectedRow, 3);
        double currentTotalKg = (double) tableModel.getValueAt(selectedRow, 4);

        // Perhatikan: Untuk edit, kita tidak mengubah resep yang terkait dengan produksi yang sudah ada
        // Jika perlu, Anda harus membuat mekanisme yang lebih kompleks (misal, membatalkan produksi lama, membuat yang baru)
        JTextField produkNamaDisplay = new JTextField(currentProdukNama); // Display only, not editable
        produkNamaDisplay.setEditable(false);
        batchField = new JTextField(String.valueOf(currentJumlahBatch));
        kgField = new JTextField(String.valueOf(currentTotalKg));

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Produk (Resep):")); panel.add(produkNamaDisplay);
        panel.add(new JLabel("Jumlah Batch:")); panel.add(batchField);
        panel.add(new JLabel("Total Kilogram Produk:")); panel.add(kgField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Produksi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.updateProduksi(
                id,
                currentProdukNama, // Kirim nama produk asli, tidak diubah
                batchField.getText(),
                kgField.getText()
            );
        }
    }

    // Metode untuk menampilkan konfirmasi hapus
    private void showDeleteConfirmation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris produksi yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        controller.deleteProduksi(id);
    }
}
