package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import model.User;
import view.Dashboard;

public class page3controller {
    private final Dashboard view;
    private final User user;

    public page3controller(Dashboard view, User user) {
        this.view = view;
        this.user = user;
        setupEventListeners();
    }

    private void setupEventListeners() {
        // Daftar Produksi Panel
        view.getDaftarProduksiPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openDistribusiView();
            }
        });

        // Produksi Panel (previously jPanel4)
        view.getProduksiPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openDistribusiView();
            }
        });

        // Distribusi Panel (previously jPanel6)
        view.getDistribusiPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openDistribusiView();
            }
        });
    }

    private void openDistribusiView() {
        new view.Distribusi2(user).setVisible(true);
        view.dispose();
    }
}