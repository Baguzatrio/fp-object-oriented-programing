package controller;

import model.DatabaseConnection;
import model.*;
import view.*;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class NavbarController {
    public void navigateTo(String menuName, User user) {
        switch (menuName) {
            case "Dashboard":
                showFrame(new Dashboard(user), "Dashboard");
                break;
            case "Produk Olahan":
                showPanel(new ProdukOlahan(user), "Produk Olahan");
                break;
            case "Produksi":
                showPanel(new Produksi(user), "Produksi");
                break;
            case "Pemasaran":
                showFrame(new Distribusi2(user), "Pemasaran");
                break;
            case "Bahan Baku":
                showPanel(new BahanBaku(user), "Manajemen Bahan Baku");
                break;
            case "Penyimpanan Resep":
                showFrame(new Resep(user), "Penyimpanan Resep");
                break;
            case "Perhitungan Upah":
                showFrame(new UpahPekerja2(user), "Penghitungan Upah Pekerja");
                break;
            case "Laporan Keuangan":
                showFrame(new LaporanKeuangan(user), "Laporan Keuangan");
                break;
            default:
                System.out.println("Menu tidak dikenali: " + menuName);
        }
    }

    private void showFrame(JFrame frame, String title) {
        frame.setTitle(title);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void showPanel(JPanel panel, String title) {
        JFrame frame = new JFrame(title);
        frame.setContentPane(panel);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}