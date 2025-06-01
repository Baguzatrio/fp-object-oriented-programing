package controller;

import model.DatabaseConnection;
import model.User;
import view.*;
import java.sql.*;
import javax.swing.JOptionPane;

public class NavbarController {
    public void navigateTo(String menuName, User user) {
        switch (menuName) {
            case "Dashboard" :
                new page3(user).setVisible(true);
                break;
            case "Produk Olahan" :
                new page3(user).setVisible(true);
                break;
            case "Produksi" :
                new page3(user).setVisible(true);
                break;
            case "Pemasaran" :
                new Distribusi(user).setVisible(true);
                break;
            case "Bahan Baku" :
                new page3(user).setVisible(true);
                break;
            case "Penyimpanan Resep" :
                new page3(user).setVisible(true);
                break;
            case "Perhitungan Upah":
            try {
                DatabaseConnection conn = new DatabaseConnection();
                Connection connection = conn.getConnection(); 
                UpahPekerja upahView = new UpahPekerja();
                UpahPekerjaController upahController = new UpahPekerjaController(connection, upahView);
                upahView.setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Gagal koneksi ke database: " + ex.getMessage());
            }
            break;
            case "Laporan Keuangan" :
                new page3(user).setVisible(true);
                break;
            default:
                System.out.println("Menu tidak dikenali: " + menuName);
        }
    }
}
