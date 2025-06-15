package controller;

import model.DistribusiModel;
import model.User;
import view.Distribusi2;
import view.DataCustomer2;
import view.FormTambahDistribusi2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.DataCustomerModel;

public class DistribusiController {
    private Distribusi2 view;
    private DistribusiModel model;
    private User currentUser;

    public DistribusiController(Distribusi2 view, DistribusiModel model, User user) {
        this.view = view;
        this.model = model;
        this.currentUser = user;
        
        initController();
    }

    private void initController() {
        loadData();
        setupButtonListeners();
    }

    public void loadData() {
        view.setTableModel(model.loadDataDistribusi());
    }

    private void setupButtonListeners() {
        // Tombol TAMBAH
        view.getButtonTambah().addActionListener(e -> {
            FormTambahDistribusi2 form = new FormTambahDistribusi2();
            
            form.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    loadData();
                }
            });
            FormTambahDistribusiController controller = new FormTambahDistribusiController(form, currentUser, view);
            form.setController(controller);
            form.setVisible(true);
        });

        // Tombol DATA CUSTOMER
        view.getButtonCustomer().addActionListener(e -> {
            DataCustomer2 customerView = new DataCustomer2();
            DataCustomerModel customerModel = new DataCustomerModel();
            new DataCustomerController(customerModel, customerView, currentUser);
            customerView.setVisible(true);
        });
    }
}