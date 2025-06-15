package controller;

import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.DistribusiModel;
import model.User;
import model.FormTambahDistribusiModel;
import model.OngkirModel;
import view.Distribusi2;
import view.FormTambahDistribusi2;
import view.FormPreviewNotaA4;
import view.KelolaOngkirView;

public class FormTambahDistribusiController {
    public FormTambahDistribusi2 view;
    public FormTambahDistribusiModel model;
    private User currentUser;
    private Distribusi2 parentWindow;

    public FormTambahDistribusiController(FormTambahDistribusi2 view, User user, Distribusi2 parentWindow) {
        this.view = view;
        this.currentUser = user;
        this.parentWindow = parentWindow;
        
        try {
            this.model = new FormTambahDistribusiModel();
            initializeController();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, 
                "Database connection error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void handleWindowClosed() {
        if (parentWindow != null) {
            parentWindow.refreshData();  // Pastikan Distribusi2 punya method refreshData()
        }
    }

    private void initializeController() {
        setupListeners();
        loadInitialData();
        setupFormDefaults();
    }

    private void setupListeners() {
        view.getBtnCetakNota().addActionListener(e -> handleCetakNota());
        view.getSimpanButton().addActionListener(e -> handleSaveButton());
        view.getKembaliButton().addActionListener(e -> {
            try {
                handleBackToDistribusi();
            } catch (SQLException ex) {
                Logger.getLogger(FormTambahDistribusiController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        view.getTableModel().addTableModelListener(e -> {
            handleTableUpdate(e);
            view.updateTotalDanKembalian();
            updateButtonStates();
        });
    }

    private void loadInitialData() {
        try {
            view.setCustomerMap(model.loadCustomerData());
            view.getTextFieldTanggal().setText(model.getTanggalHariIni());
            
            // Auto-generate invoice number when customer is selected
            view.getTextFieldIdCustomer().getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
                    public void changedUpdate(javax.swing.event.DocumentEvent e) { updateNotaNumber(); }
                    public void removeUpdate(javax.swing.event.DocumentEvent e) { updateNotaNumber(); }
                    public void insertUpdate(javax.swing.event.DocumentEvent e) { updateNotaNumber(); }
                    
                    private void updateNotaNumber() {
                        try {
                            String idCustomer = view.getTextFieldIdCustomer().getText();
                            if (!idCustomer.isEmpty()) {
                                String nomorNota = model.generateNomorNota(idCustomer);
                                view.getTextFieldNoNota().setText(nomorNota);
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(view, 
                                "Error generating invoice number: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, 
                "Failed to load customer data: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupFormDefaults() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0); // Clear existing rows
        for (int i = 0; i < 5; i++) {
            tableModel.addRow(new Object[]{i+1, "", 0, 0, 0});
        }
        updateButtonStates();
    }

    public void handleTableUpdate(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
            DefaultTableModel tableModel = view.getTableModel();
            int row = e.getFirstRow();
            int col = e.getColumn();
            
            if (col == 1) { // Product name column changed
                String productName = (String) tableModel.getValueAt(row, 1);
                if (productName != null && !productName.isEmpty()) {
                    int stokTersedia = model.getStokProduk(productName);
                    int harga = model.getProdukMap().get(productName).getHarga();
                    tableModel.setValueAt(harga, row, 2);
                    tableModel.setValueAt(stokTersedia, row, 3); // Auto-set max quantity
                    updateSubtotal(row, tableModel);
                }
            } 
            else if (col == 3) { // Quantity column changed
                String productName = (String) tableModel.getValueAt(row, 1);
                if (productName != null && !productName.isEmpty()) {
                    int stokTersedia = model.getStokProduk(productName);
                    int jumlah = (Integer) tableModel.getValueAt(row, 3);
                    
                    // Adjust quantity if exceeds available stock
                    if (jumlah > stokTersedia) {
                        tableModel.setValueAt(stokTersedia, row, 3);
                        JOptionPane.showMessageDialog(view,
                            "Jumlah melebihi stok tersedia. Diubah menjadi " + stokTersedia,
                            "Peringatan", JOptionPane.WARNING_MESSAGE);
                    }
                    updateSubtotal(row, tableModel);
                }
            }
        }
    }

    private void updateSubtotal(int row, DefaultTableModel tableModel) {
        try {
            Integer quantity = (Integer) tableModel.getValueAt(row, 3);
            Integer price = (Integer) tableModel.getValueAt(row, 2);
            tableModel.setValueAt(price * quantity, row, 4);
        } catch (Exception e) {
            // Handle invalid inputs
            tableModel.setValueAt(0, row, 4);
        }
    }

    public int calculateTotal(DefaultTableModel model) {
        int total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, 4);
            if (value instanceof Integer) {
                total += (Integer) value;
            }
        }
        return total;
    }

    public void handleSaveButton() {
        handleCetakNota(); // Simpan dan cetak sekaligus
    }

    public void handleCetakNota() {
        if (!validateForm()) {
            return;
        }

        try {
            String nomorNota = view.getNomorNota();
            String namaCustomer = view.getNamaCustomer();
            String idCustomer = view.getTextFieldIdCustomer().getText();
            String alamat = view.getAlamatCustomer();
            String tanggal = view.getTanggal();
            int ongkir = view.getSelectedOngkir();
            int total = Integer.parseInt(view.getTotalHarga());
            int dibayar = Integer.parseInt(view.getBayar());
            int kembalian = Integer.parseInt(view.getKembali());
            String status = kembalian >= 0 ? "Lunas" : "Belum Lunas";

            List<Map<String, Object>> items = prepareItemsFromTable();
            
            // Save to database
            model.saveDistribusi(
                nomorNota, idCustomer, namaCustomer, alamat, 
                status, tanggal, total, dibayar, kembalian, items
            );

            // Prepare data for preview
            List<String[]> itemList = new ArrayList<>();
            for (Map<String, Object> item : items) {
                itemList.add(new String[]{
                    item.get("produk").toString(),
                    item.get("jumlah").toString(),
                    item.get("harga").toString(),
                    item.get("subtotal").toString()
                });
            }
            
            // Show preview
            new FormPreviewNotaA4(
                view, nomorNota, namaCustomer, alamat, 
                tanggal, String.valueOf(total), String.valueOf(dibayar), 
                String.valueOf(kembalian), itemList
            );
            
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(view, 
                "Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateForm() {
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
                int stokTersedia = model.getStokProduk(product);
                if (quantity > stokTersedia) {
                    JOptionPane.showMessageDialog(view,
                        "Stok " + product + " tidak mencukupi. Stok tersedia: " + stokTersedia,
                        "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
        }

        return true;
    }

    private List<Map<String, Object>> prepareItemsFromTable() {
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

    private void updateButtonStates() {
    boolean hasValidItems = hasValidItemsInTable();
    view.getBtnCetakNota().setEnabled(hasValidItems);
    view.getSimpanButton().setEnabled(hasValidItems);
}

private boolean hasValidItemsInTable() {
    DefaultTableModel tableModel = view.getTableModel();
    for (int i = 0; i < tableModel.getRowCount(); i++) {
        String product = (String) tableModel.getValueAt(i, 1);
        Object quantityObj = tableModel.getValueAt(i, 3);
        if (product != null && !product.isEmpty() && 
            quantityObj instanceof Integer && (Integer)quantityObj > 0) {
            return true;
        }
    }
    return false;
}

public void handleKelolaOngkir() {
    try {
        // Buat dialog modal
        JDialog dialog = new JDialog();
        dialog.setTitle("Kelola Ongkos Kirim");
        
        // Buat instance KelolaOngkirView
        KelolaOngkirView kelolaView = new KelolaOngkirView();
        
        // Tambahkan ke dialog
        dialog.setContentPane(kelolaView);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(view);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // Tambahkan WindowListener untuk refresh data setelah ditutup
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                refreshOngkirOptions();
            }
        });
        
        dialog.setVisible(true);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(view, 
            "Gagal membuka pengelolaan ongkir: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

private void refreshOngkirOptions() {
    try {
        OngkirModel ongkirModel = new OngkirModel();
        List<OngkirModel.Ongkir> ongkirList = ongkirModel.getAllOngkir();
        
        String[] ongkirOptions = new String[ongkirList.size()];
        for (int i = 0; i < ongkirList.size(); i++) {
            OngkirModel.Ongkir o = ongkirList.get(i);
            ongkirOptions[i] = o.getArea() + " - Rp" + o.getBiaya();
        }
        
        view.loadOngkirData(ongkirOptions);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(view, 
            "Gagal memuat data ongkir: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    public void handleBackToDistribusi() throws SQLException {
    // Tutup form saat ini
    view.dispose();
    
    // Buka kembali distribusi dengan data terbaru
    Distribusi2 distribusiView = new Distribusi2(currentUser);
    DistribusiModel distribusiModel = new DistribusiModel();
    new DistribusiController(distribusiView, distribusiModel, currentUser);
    distribusiView.setVisible(true);
}
}