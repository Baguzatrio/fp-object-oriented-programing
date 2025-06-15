package controller;

import java.awt.Window;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.*;
import view.*;
import java.sql.*;

public class NavbarController {
    private JFrame mainFrame; 

    public NavbarController(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void navigateTo(String menuName, User user) {
        // Tutup semua frame yang sudah ada kecuali mainFrame
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window != mainFrame && window instanceof JFrame) {
                window.dispose();
            }
        }

        // Buka frame yang sesuai
        switch (menuName) {
            case "Dashboard":
                showFrame(new Dashboard(user));
                break;
            case "Session":  // Tambahan menu baru
            try {
                Connection conn = DatabaseConnection.getConnection();
                SessionLogModel model = new SessionLogModel(conn);
                SessionLogView view = new SessionLogView(user);
                new SessionLogController(model, view);
                showFrame(view);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Gagal membuka Session Log: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            break;
            case "Produk Olahan":
                showFrame(new ProdukOlahan(user));
                break;
            case "Produksi":
                showFrame(new Produksi(user));
                break;
            case "Pemasaran":
                try {
                    Distribusi2 distribusiView = new Distribusi2(user);
                    DistribusiModel distribusiModel = new DistribusiModel();
                    new DistribusiController(distribusiView, distribusiModel, user);
                    showFrame(distribusiView);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(mainFrame, 
                        "Gagal terhubung ke database: " + e.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "Bahan Baku":
                showFrame(new BahanBaku(user));
                break;
            case "Penyimpanan Resep":
                showFrame(new Resep(user));
                break;
            case "Upah Pekerja":
                try {
                Connection conn = DatabaseConnection.getConnection();
                UpahPekerja2 upahView = new UpahPekerja2(user);
                new UpahPekerjaController(conn, upahView, user);
                showFrame(upahView);
                }catch (SQLException e) {
                    JOptionPane.showMessageDialog(mainFrame, "Gagal Terhubung Ke Database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
                case "Pengeluaran Lain":
    try {
        Connection conn = DatabaseConnection.getConnection();
        PengeluaranLain view = new PengeluaranLain(user, conn);
        showFrame(view);
    } catch (SQLException e) {
        // Tangkap exception dari DatabaseConnection atau PengeluaranLain
        JOptionPane.showMessageDialog(mainFrame, 
            "Gagal membuka modul Pengeluaran Lain:\n" + e.getMessage(),
            "Error Database", JOptionPane.ERROR_MESSAGE);
        
        // Log error untuk debugging
        System.err.println("Error saat membuka PengeluaranLain:");
        e.printStackTrace();
    }
    break;
            case "Laporan Keuangan":
                showFrame(new LaporanKeuangan(user));
                break;
            default:
                System.out.println("Menu tidak dikenali: " + menuName);
        }
    }
    
    public void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            mainFrame,
            "Apakah Anda yakin ingin logout?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Tutup frame sekarang
            mainFrame.dispose();
            System.exit(0);
        }
    }

    private void showFrame(JFrame frame) {
        // Set ukuran dan posisi konsisten
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(mainFrame);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Tutup mainFrame jika bukan frame pertama
        if (mainFrame != null && !(frame instanceof Dashboard)) {
            mainFrame.dispose();
        }
        
        frame.setVisible(true);
        
        // Update mainFrame referensi jika perlu
        if (frame instanceof Dashboard) {
            mainFrame = frame;
        }
    }
}