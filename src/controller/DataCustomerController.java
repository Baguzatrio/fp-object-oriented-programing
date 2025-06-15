package controller;

import model.DataCustomerModel;
import view.DataCustomer2;
import controller.DistribusiController;
import model.DistribusiModel;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import model.User;
import view.Distribusi2;

public class DataCustomerController {
    private DataCustomerModel model;
    private DataCustomer2 view;
    private User user;

    public DataCustomerController(DataCustomerModel model, DataCustomer2 view, User user) {
        this.model = model;
        this.view = view;
        this.user = user;
        
        initializeController();
    }

    private void initializeController() {
        loadCustomerData();
        setupButtonListeners();
        setupTableSelectionListener();
        setupSearchFunctionality();
    }

    private void loadCustomerData() {
        try {
            DefaultTableModel tableModel = model.loadCustomerData();
            view.getTableCustomer().setModel(tableModel);
        } catch (Exception e) {
            view.showMessage("Error loading customer data: " + e.getMessage());
        }
    }

    private void setupButtonListeners() {
        // Add/Save button
        view.addAddButtonListener(e -> handleAddCustomer());
        
        // Delete button
        view.addDeleteButtonListener(e -> handleDeleteCustomer());
        
        // Edit button
        view.addEditButtonListener(e -> handleEditCustomer());
        
        // Back button
        view.addBackButtonListener(e -> handleBackAction());
        
        // Search button
        view.addSearchButtonListener(e -> handleSearch());
    }

    private void setupTableSelectionListener() {
        view.getTableCustomer().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = view.getTableCustomer().getSelectedRow();
                if (selectedRow >= 0) {
                    populateFormFields(selectedRow);
                }
            }
        });
    }

    private void setupSearchFunctionality() {
        view.getSearchField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                performSearch();
            }
        });
    }

    private void populateFormFields(int row) {
        view.getTextField1().setText(view.getTableCustomer().getValueAt(row, 0).toString());
        view.getTextField2().setText(view.getTableCustomer().getValueAt(row, 1).toString());
        view.getTextField3().setText(view.getTableCustomer().getValueAt(row, 2).toString());
        view.getTextField4().setText(view.getTableCustomer().getValueAt(row, 3).toString());
    }

    private void handleAddCustomer() {
        try {
            int id = Integer.parseInt(view.getTextField1().getText().trim());
            String nama = view.getTextField2().getText().trim();
            String telp = view.getTextField3().getText().trim();
            String alamat = view.getTextField4().getText().trim();

            if (nama.isEmpty() || telp.isEmpty() || alamat.isEmpty()) {
                view.showMessage("Semua field harus diisi!");
                return;
            }

            model.addCustomer(id, nama, telp, alamat);
            view.showMessage("Customer berhasil ditambahkan");
            clearForm();
            loadCustomerData();
        } catch (NumberFormatException e) {
            view.showMessage("ID harus berupa angka");
        } catch (SQLException e) {
            view.showMessage("Error: " + e.getMessage());
        }
    }

    private void handleDeleteCustomer() {
        int selectedRow = view.getTableCustomer().getSelectedRow();
        if (selectedRow < 0) {
            view.showMessage("Pilih customer yang akan dihapus");
            return;
        }

        int confirm = view.showConfirmDialog("Yakin ingin menghapus customer ini?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(view.getTableCustomer().getValueAt(selectedRow, 0).toString());
                model.deleteCustomer(id);
                view.showMessage("Customer berhasil dihapus");
                loadCustomerData();
                clearForm();
            } catch (SQLException e) {
                view.showMessage("Error: " + e.getMessage());
            }
        }
    }

    private void handleEditCustomer() {
        int selectedRow = view.getTableCustomer().getSelectedRow();
        if (selectedRow < 0) {
            view.showMessage("Pilih customer yang akan diubah");
            return;
        }

        try {
            int id = Integer.parseInt(view.getTextField1().getText().trim());
            String nama = view.getTextField2().getText().trim();
            String telp = view.getTextField3().getText().trim();
            String alamat = view.getTextField4().getText().trim();

            model.updateCustomer(id, nama, telp, alamat);
            view.showMessage("Data customer berhasil diupdate");
            loadCustomerData();
        } catch (SQLException e) {
            view.showMessage("Error: " + e.getMessage());
        }
    }

    private void handleBackAction() {
        view.dispose();
        try {
        Distribusi2 distribusiView = new Distribusi2(user);
        DistribusiModel distribusiModel = new DistribusiModel();
        new DistribusiController(distribusiView, distribusiModel, user);
        distribusiView.setVisible(true);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(view, 
            "Gagal terhubung ke database: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }

    private void handleSearch() {
        String keyword = view.getSearchField().getText().trim();
        if (keyword.isEmpty()) {
            loadCustomerData();
            return;
        }

        try {
            DefaultTableModel searchResult = model.searchCustomerByName(keyword);
            view.getTableCustomer().setModel(searchResult);
            
            if (searchResult.getRowCount() == 0) {
                view.showMessage("Tidak ditemukan customer dengan nama '" + keyword + "'");
            }
        } catch (Exception e) {
            view.showMessage("Error saat mencari: " + e.getMessage());
        }
    }

    private void performSearch() {
        if (view.getSearchField().getText().trim().isEmpty()) {
            loadCustomerData();
        } else {
            handleSearch();
        }
    }

    private void clearForm() {
        view.getTextField1().setText("");
        view.getTextField2().setText("");
        view.getTextField3().setText("");
        view.getTextField4().setText("");
    }
}