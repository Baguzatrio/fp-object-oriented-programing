package controller;

import model.DistribusiModel;
import view.Distribusi2;
import view.DataCustomer2;
import view.FormTambahDistribusi2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DistribusiController {

    private Distribusi2 view;
    private DistribusiModel model;

    public DistribusiController(Distribusi2 view, DistribusiModel model) {
        this.view = view;
        this.model = model;

        // Load data distribusi dari model ke tabel
        loadData();

        // Tambahkan aksi tombol TAMBAH
        this.view.getButtonTambah().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Tombol TAMBAH diklik!"); // Debug
                FormTambahDistribusi2 form = new FormTambahDistribusi2();
                form.setVisible(true);
            }
        });

        // Tambahkan aksi tombol DATA CUSTOMER
        this.view.getButtonCustomer().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Tombol DATA CUSTOMER diklik!"); // Debug
                DataCustomer2 customerView = new DataCustomer2();
                customerView.setVisible(true);
            }
        });
    }

    private void loadData() {
        // Set data ke tabel dari model
        view.setTableModel(model.loadDataDistribusi());
    }
}
