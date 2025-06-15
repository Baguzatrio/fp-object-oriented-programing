package controller;

import model.*;
import javax.swing.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map; 

public class ProduksiController {
    private final ProduksiDAO produksiDAO;
    
    public ProduksiController() {
        this.produksiDAO = new ProduksiDAO();
    }
    
    public List<ProduksiModel> getAllProduksi() {
        try {
            return produksiDAO.getAllProduksi();
        } catch (SQLException e) {
            showError("Gagal memuat data produksi", e);
            return Collections.emptyList();
        }
    }
    
    public Map<String, Integer> loadResep(JComboBox<String> comboBox) {
        try {
            comboBox.removeAllItems();
            return produksiDAO.getProdukOlahanMap(comboBox);
        } catch (SQLException e) {
            showError("Gagal memuat daftar produk olahan", e);
            return Collections.emptyMap();
        }
    }
    
    public Map<String, Integer> loadPekerja(JComboBox<String> comboBox) {
        try {
            comboBox.removeAllItems();
            return produksiDAO.getPekerjaMap(comboBox);
        } catch (SQLException e) {
            showError("Gagal memuat daftar pekerja", e);
            return Collections.emptyMap();
        }
    }
    
    public boolean prosesInputProduksi(int produkId, String namaProduk, int jumlahBatch, 
                                 double totalKg, int idPegawai) {
        System.out.println("Memproses produksi - Produk ID: " + produkId + 
                         ", Produk: " + namaProduk + 
                         ", Batch: " + jumlahBatch);
        
        try {
            // Validate input
            if (jumlahBatch <= 0 || totalKg <= 0) {
                JOptionPane.showMessageDialog(null,
                    "Jumlah batch dan total kg harus lebih dari 0",
                    "Input Tidak Valid",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }

            boolean result = produksiDAO.simpanProduksi(
                produkId,
                namaProduk,
                jumlahBatch,
                totalKg,
                idPegawai
            );
            
            if (result) {
                JOptionPane.showMessageDialog(null,
                    "Produksi berhasil disimpan",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                    "Gagal menyimpan produksi",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
            return result;
            
        } catch (SQLException e) {
            String errorMsg = "Terjadi kesalahan saat menyimpan produksi";
            if (e.getMessage().contains("stok")) {
                errorMsg = "Stok bahan baku tidak mencukupi";
            }
            
            JOptionPane.showMessageDialog(null,
                errorMsg + "\nDetail: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
                
            return false;
        }
    }
    
    private void handleProductionError(SQLException e) {
        System.err.println("ERROR PRODUKSI: " + e.getMessage());
        
        String errorMsg = "Terjadi kesalahan saat menyimpan produksi";
        if (e.getMessage().contains("stok")) {
            errorMsg = "Stok bahan baku tidak mencukupi";
        } else if (e.getMessage().contains("foreign key")) {
            errorMsg = "Data referensi tidak valid (produk/pekerja tidak ditemukan)";
        }
        
        JOptionPane.showMessageDialog(null,
            errorMsg + "\nDetail: " + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE);
    }
    
    public int getUkuranKemasan(String namaProduk) {
        try {
            return produksiDAO.getUkuranKemasan(namaProduk);
        } catch (SQLException e) {
            showError("Gagal mendapatkan ukuran kemasan", e);
            return 0;
        }
    }
    
    private void showError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
        
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null,
                message + "\nDetail: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        });
    }
}