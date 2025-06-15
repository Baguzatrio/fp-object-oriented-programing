package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.DistribusiModel;
import model.FormTambahDistribusiModel;
import model.User;
import view.Distribusi2;
import view.FormTambahDistribusi2;

public class FormEditDistribusiController extends FormTambahDistribusiController {
    private String noNota;
    private DistribusiModel distribusiModel;
    
    public FormEditDistribusiController(FormTambahDistribusi2 view, User user, 
                                      Distribusi2 parentWindow, String noNota) {
        super(view, user, parentWindow);
        this.noNota = noNota;
        loadDataDistribusi();
    }
    
    private void loadDataDistribusi() {
        try {
            // Load data dari database berdasarkan noNota
            DistribusiModel distribusiModel = new DistribusiModel();
            Map<String, Object> distribusiData = distribusiModel.getDistribusiByNoNota(noNota);
            List<Map<String, Object>> items = distribusiModel.getDetailDistribusi(noNota);
            
            // Isi form dengan data yang ada
            view.getTextFieldNoNota().setText(noNota);
            view.getTextFieldIdCustomer().setText(distribusiData.get("id_customer").toString());
            
            // Perbaikan: Gunakan getter yang benar untuk field customer
            view.getCustomerField().setText(distribusiData.get("nama_customer").toString());
            
            view.getTextPaneAlamat().setText(distribusiData.get("alamat").toString());
            view.getTextFieldTanggal().setText(distribusiData.get("tanggal").toString());
            view.getTextFieldTotal().setText(distribusiData.get("total").toString());
            view.getTextFieldDibayar().setText(distribusiData.get("dibayar").toString());
            view.getTextFieldKembalian().setText(distribusiData.get("kembalian").toString());
            
            // Isi tabel dengan items
            DefaultTableModel model = view.getTableModel();
            model.setRowCount(0);
            for (Map<String, Object> item : items) {
                model.addRow(new Object[]{
                    model.getRowCount() + 1,
                    item.get("nama_produk"),
                    item.get("harga"),
                    item.get("jumlah"),
                    item.get("subtotal")
                });
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, 
                "Gagal memuat data distribusi: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            view.dispose();
        }
    }
    
@Override
public void handleSaveButton() {
    if (!validateForm()) {
        return;
    }

    try {
        // Update data di database
        String namaCustomer = view.getNamaCustomer();
        String idCustomer = view.getTextFieldIdCustomer().getText();
        String alamat = view.getAlamatCustomer();
        String tanggal = view.getTanggal();
        int total = Integer.parseInt(view.getTotalHarga());
        int dibayar = Integer.parseInt(view.getBayar());
        int kembalian = Integer.parseInt(view.getKembali());
        String status = kembalian >= 0 ? "Lunas" : "Belum Lunas";

        List<Map<String, Object>> items = prepareItemsFromTable();
        
        // Perbaikan di sini: Gunakan distribusiModel yang sudah diinisialisasi
        distribusiModel.updateDistribusi(
            noNota, idCustomer, namaCustomer, alamat, 
            status, tanggal, total, dibayar, kembalian, items
        );

        JOptionPane.showMessageDialog(view, 
            "Data distribusi berhasil diperbarui",
            "Sukses", JOptionPane.INFORMATION_MESSAGE);
        
        view.dispose();
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(view, 
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    protected List<Map<String, Object>> prepareItemsFromTable() {
        List<Map<String, Object>> items = new ArrayList<>();
        DefaultTableModel tableModel = view.getTableModel();
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String product = (String) tableModel.getValueAt(i, 1);
            Object quantityObj = tableModel.getValueAt(i, 3);
            
            if (product != null && !product.isEmpty() && 
                quantityObj instanceof Integer && (Integer)quantityObj > 0) {
                
                Map<String, Object> item = new HashMap<>();
                item.put("produk", product);
                item.put("harga", tableModel.getValueAt(i, 2));
                item.put("jumlah", quantityObj);
                item.put("subtotal", tableModel.getValueAt(i, 4));
                items.add(item);
            }
        }
        return items;
    }

    protected boolean validateForm() {
        if (view.getNamaCustomer().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Nama customer harus diisi", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (view.getBayar().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Jumlah bayar harus diisi", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (calculateTotal(view.getTableModel()) <= 0) {
            JOptionPane.showMessageDialog(view, "Tambahkan minimal 1 produk", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validate stock availability
        DefaultTableModel tableModel = view.getTableModel();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String product = (String) tableModel.getValueAt(i, 1);
            Integer quantity = (Integer) tableModel.getValueAt(i, 3);
            
            if (product != null && !product.isEmpty() && quantity > 0) {
                try {
                    int stokTersedia = model.getStokProduk(product);
                    if (quantity > stokTersedia) {
                        JOptionPane.showMessageDialog(view,
                            "Stok " + product + " tidak mencukupi. Stok tersedia: " + stokTersedia,
                            "Peringatan", JOptionPane.WARNING_MESSAGE);
                        return false;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view,
                        "Error checking stock: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }

        return true;
    }
}