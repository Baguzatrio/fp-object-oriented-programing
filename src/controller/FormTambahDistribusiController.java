package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.FormTambahDistribusiModel;
import view.FormTambahDistribusi2;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.*;
import model.User;

public class FormTambahDistribusiController {
    private FormTambahDistribusi2 view;
    private FormTambahDistribusiModel model;
    private User user;
    
    public FormTambahDistribusiController(FormTambahDistribusi2 view) {
        this.view = view;
        try {
            this.model = new FormTambahDistribusiModel();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal terhubung ke database: " + e.getMessage());
        }
    }
    
    public void loadCustomerData() {
        try {
            Map<String, FormTambahDistribusiModel.Customer> customerMap = model.loadCustomerData();
            DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
            for (String nama : customerMap.keySet()) {
                comboModel.addElement(nama);
            }
            view.getComboBoxNama().setModel(comboModel);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data customer: " + e.getMessage());
        }
    }
    
    public void handleTableUpdate(TableModelEvent e) {
        DefaultTableModel model = view.getTableModel();
        int row = e.getFirstRow();
        int col = e.getColumn();
        
        if (col == 1) { // nama produk dipilih
            String nama = (String) model.getValueAt(row, 1);
            Integer harga = this.model.getProdukMap().getOrDefault(nama, 0);
            model.setValueAt(harga, row, 2);
            Integer jumlah = (Integer) model.getValueAt(row, 3);
            model.setValueAt(harga * jumlah, row, 4);
        }
        
        if (col == 3) { // jumlah diubah
            Integer jumlah = (Integer) model.getValueAt(row, 3);
            Integer harga = (Integer) model.getValueAt(row, 2);
            model.setValueAt(harga * jumlah, row, 4);
        }
    }
    
    public int calculateTotal(DefaultTableModel model) {
        int total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object val = model.getValueAt(i, 4);
            if (val instanceof Integer) {
                total += (Integer) val;
            }
        }
        return total;
    }
    
    public void handleSaveButton() {
        try {
            String nomorNota = view.getTextFieldNoNota().getText();
            String nama = (String) view.getComboBoxNama().getSelectedItem();
            String idCustomer = view.getTextFieldIdCustomer().getText();
            String alamat = view.getTextPaneAlamat().getText();
            String tanggal = view.getTextFieldTanggal().getText();
            int total = Integer.parseInt(view.getTextFieldTotal().getText());
            int dibayar = Integer.parseInt(view.getTextFieldDibayar().getText());
            int kembalian = dibayar - total;
            String status = kembalian >= 0 ? "Lunas" : "Belum Lunas";
            
            // Prepare items
            List<Map<String, Object>> items = new ArrayList<>();
            DefaultTableModel tableModel = view.getTableModel();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String produk = (String) tableModel.getValueAt(i, 1);
                Integer jumlah = (Integer) tableModel.getValueAt(i, 3);
                if (produk != null && !produk.isEmpty() && jumlah > 0) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("produk", produk);
                    item.put("harga", tableModel.getValueAt(i, 2));
                    item.put("jumlah", jumlah);
                    item.put("subtotal", tableModel.getValueAt(i, 4));
                    items.add(item);
                }
            }
            
            model.saveDistribusi(nomorNota, idCustomer, nama, alamat, status, tanggal, total, dibayar, kembalian, items);
            JOptionPane.showMessageDialog(view, "Nota berhasil disimpan!");
            view.dispose();
            // Navigasi ke form lain jika diperlukan
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Gagal menyimpan data: " + e.getMessage());
        }
    }
    public void handleBackToDistribusi() {
    view.dispose(); // Menutup form sekarang
    view.Distribusi2 Distribusi = new view.Distribusi2(user);
    Distribusi.setVisible(true);
}

    public class ControllerDistribusi {
    private FormTambahDistribusi2 view;

    public ControllerDistribusi(FormTambahDistribusi2 view) {
        this.view = view;
        initListener();
    }

    private void initListener() {
        view.getBtnCetakNota().addActionListener(e -> showPreviewNota());
    }
    
    private void showPreviewNota() {
        try {
            // Validasi data sebelum membuat preview
            if (!isDataValid()) {
                JOptionPane.showMessageDialog(view, "Data belum lengkap!");
                return;
            }

            // Ambil data dari view
            String nomorNota = view.getTextFieldNoNota().getText();
            String nama = view.getComboBoxNama().getSelectedItem().toString();
            String alamat = view.getTextPaneAlamat().getText();
            String tanggal = view.getTextFieldTanggal().getText();
            String total = view.getTextFieldTotal().getText();
            String bayar = view.getTextFieldDibayar().getText();
            String kembali = view.getTextFieldKembalian().getText();

            // Ambil data dari tabel
            List<String[]> daftarBarang = getDataBarang();
            
            // Tampilkan preview
            new view.FormPreviewNotaA4(view, nomorNota, nama, alamat, tanggal, 
                                total, bayar, kembali, daftarBarang);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private boolean isDataValid() {
        // Validasi data penting
        return !view.getTextFieldNoNota().getText().isEmpty() &&
               view.getComboBoxNama().getSelectedItem() != null &&
               view.getTableModel().getRowCount() > 0;
    }
    
    private List<String[]> getDataBarang() {
        List<String[]> daftarBarang = new ArrayList<>();
        DefaultTableModel model = view.getTableModel();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 1) != null && !model.getValueAt(i, 1).toString().isEmpty()) {
                daftarBarang.add(new String[]{
                    model.getValueAt(i, 1).toString(), // Nama Produk
                    model.getValueAt(i, 3).toString(), // Jumlah
                    model.getValueAt(i, 2).toString(), // Harga
                    model.getValueAt(i, 4).toString()  // Subtotal
                });
            }
        }
        return daftarBarang;
    }
}

    public Map<String, Integer> getProdukMap() {
        return model.getProdukMap();
    }
}