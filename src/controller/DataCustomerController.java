package controller;

import model.DataCustomerModel;
import view.DataCustomer;
import controller.DistribusiController;
import model.DistribusiModel;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import model.User;
import view.Distribusi;

public class DataCustomerController {
    private DataCustomerModel model;
    private DataCustomer view;
    private User user;

    public DataCustomerController(DataCustomerModel model, DataCustomer view, User user) {
        this.model = model;
        this.view = view;
        this.user = user;
        
        loadCustomerData();
        initView();
        initController();
    }

    private void initView() {
        view.setVisible(true);
    }

    private void initController() {
        view.addAddButtonListener(e -> addCustomer());
        view.addDeleteButtonListener(e -> deleteCustomer());
        view.addEditButtonListener(e -> enableEdit());
        view.addBackButtonListener(e -> {
            view.dispose();
            Distribusi distribusiView = new Distribusi(user);
            DistribusiModel distribusiModel = new DistribusiModel(); 
            DistribusiController distribusicon = new DistribusiController(distribusiView, distribusiModel);
            distribusiView.setVisible(true);
        });
        
        view.getTableCustomer().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTableCustomer().getSelectedRow();
                if (row >= 0) {
                    view.getTextField1().setText(view.getTableCustomer().getValueAt(row, 0).toString());
                    view.getTextField2().setText(view.getTableCustomer().getValueAt(row, 1).toString());
                    view.getTextField3().setText(view.getTableCustomer().getValueAt(row, 2).toString());
                    view.getTextField4().setText(view.getTableCustomer().getValueAt(row, 3).toString());
                }
            }
        });
        view.addSearchButtonListener(e -> {
        String searchText = view.getTextField5().getText().trim();
        if (searchText.isEmpty()) {
            // Jika kolom pencarian kosong, tampilkan semua data
            loadCustomerData();
        } else {
            // Jika ada teks pencarian, lakukan pencarian
            DefaultTableModel searchModel = model.searchCustomerByName(searchText);
            view.getTableCustomer().setModel(searchModel);
            
            // Jika tidak ada hasil, tampilkan pesan
            if (searchModel.getRowCount() == 0) {
                view.showMessage("Data tidak ditemukan");
            }
        }
    });
         view.getSearchField().getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            checkAndReset();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            checkAndReset();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            checkAndReset();
        }
        
        private void checkAndReset() {
            if (view.getSearchField().getText().trim().isEmpty()) {
                loadCustomerData();
            }
        }
    });
    }

    private void searchCustomer() {
    String keyword = view.getSearchField().getText();
    DefaultTableModel filteredModel = model.searchCustomerByName(keyword);
    view.getTableCustomer().setModel(filteredModel);
}

    private void loadCustomerData() {
        DefaultTableModel tableModel = model.loadCustomerData();
        view.getTableCustomer().setModel(tableModel);
    }

    private void addCustomer() {
        try {
            int id = Integer.parseInt(view.getTextField1().getText());
            String nama = view.getTextField2().getText();
            String telp = view.getTextField3().getText();
            String alamat = view.getTextField4().getText();
            
            model.addCustomer(id, nama, telp, alamat);
            view.showMessage("Data berhasil disimpan!");
            loadCustomerData();
        } catch (NumberFormatException e) {
            view.showMessage("ID harus berupa angka!");
        } catch (SQLException e) {
            view.showMessage("Gagal menyimpan data: " + e.getMessage());
        }
    }

    private void deleteCustomer() {
        int row = view.getTableCustomer().getSelectedRow();
        if (row >= 0) {
            int confirm = view.showConfirmDialog("Yakin mau hapus data ini?");
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    int id = Integer.parseInt(view.getTableCustomer().getValueAt(row, 0).toString());
                    model.deleteCustomer(id);
                    loadCustomerData();
                    view.showMessage("Data berhasil dihapus");
                } catch (SQLException e) {
                    view.showMessage("Gagal menghapus data: " + e.getMessage());
                }
            }
        } else {
            view.showMessage("Pilih data yang ingin dihapus");
        }
    }

    private void enableEdit() {
        int row = view.getTableCustomer().getSelectedRow();
        if (row >= 0) {
            try {
                int id = Integer.parseInt(view.getTableCustomer().getValueAt(row, 0).toString());
                String nama = view.getTextField2().getText();
                String telp = view.getTextField3().getText();
                String alamat = view.getTextField4().getText();
                
                model.updateCustomer(id, nama, telp, alamat);
                view.showMessage("Data berhasil diperbarui");
                loadCustomerData();
            } catch (SQLException e) {
                view.showMessage("Gagal memperbarui data: " + e.getMessage());
            }
        } else {
            view.showMessage("Pilih data yang ingin diubah!");
        }
    }
    
}