/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import javax.swing.*;
import java.awt.event.*;
import model.User;
import view.page3;

public class page3controller {
    private page3 view;

    public page3controller(page3 view, User user) {
        this.view = view;
this.view.jPanel5.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        new view.Distribusi(user).setVisible(true); // tampilkan halaman Produksi
        view.dispose(); // tutup halaman sekarang (opsional)
    }
});

this.view.jPanel4.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        new view.Distribusi(user).setVisible(true); // tampilkan halaman Pengemasan
        view.dispose(); // tutup halaman sekarang
    }
});

this.view.jPanel6.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        new view.Distribusi(user).setVisible(true); // tampilkan halaman Distribusi
        view.dispose(); // tutup halaman sekarang
    }
});

    }

}
