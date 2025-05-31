package controller;

import model.DistribusiModel;
import view.Distribusi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.DataCustomer;
import view.FormTambahDistribusi;

public class DistribusiController {

    private Distribusi view;
    private DistribusiModel model;

    public DistribusiController(Distribusi view, DistribusiModel model) {
        this.view = view;
        this.model = model;

        loadData();

        // Tambah aksi tombol
        this.view.getButtonTambah().addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        FormTambahDistribusi form = new FormTambahDistribusi();
        form.setVisible(true);
    }
});

this.view.getButtonCustomer().addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        DataCustomer cus = new DataCustomer();
        cus.setVisible(true);
    }
});

    }

    private void loadData() {
        view.setTableModel(model.loadDataDistribusi());
    }
}
