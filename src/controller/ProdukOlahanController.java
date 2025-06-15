package controller;

import model.ProdukOlahanDAO;
import model.ProdukOlahanModel;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ProdukOlahanController {
    private final ProdukOlahanDAO dao;

    public ProdukOlahanController() {
        this.dao = new ProdukOlahanDAO();
    }

    public List<ProdukOlahanModel> getAllProduk() {
        try {
            return dao.getAllProduk();
        } catch (SQLException e) {
            System.err.println("Error getting products: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean tambahProduk(String namaProduk, int ukuranKemasan, int harga, int upah) {
        ProdukOlahanModel produk = new ProdukOlahanModel();
        produk.setNamaProduk(namaProduk);
        produk.setUkuranKemasan(ukuranKemasan);
        produk.setHarga(harga);
        produk.setUpah(upah);
        
        try {
            return dao.insertProduk(produk);
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStokProduksi(int produkId, int jumlahKemasan) {
        try {
            return dao.updateStokFromProduction(produkId, jumlahKemasan);
        } catch (SQLException e) {
            System.err.println("Error updating production stock: " + e.getMessage());
            return false;
        }
    }

    public boolean kurangiStokDistribusi(String namaProduk, int jumlah) {
        try {
            return dao.reduceStokFromDistribution(namaProduk, jumlah);
        } catch (SQLException e) {
            System.err.println("Error reducing distribution stock: " + e.getMessage());
            return false;
        }
    }

    public int getStokProduk(String namaProduk) {
        try {
            return dao.getStokByNamaProduk(namaProduk);
        } catch (SQLException e) {
            System.err.println("Error getting product stock: " + e.getMessage());
            return -1;
        }
    }

    public boolean tambahProdukDanResep(String namaProduk, int ukuranKemasan, int harga, int upah) {
        ProdukOlahanModel produk = new ProdukOlahanModel();
        produk.setNamaProduk(namaProduk);
        produk.setUkuranKemasan(ukuranKemasan);
        produk.setHarga(harga);
        produk.setUpah(upah);
        
        try {
            return dao.createProdukOlahanWithResep(produk);
        } catch (SQLException e) {
            System.err.println("Error adding product with recipe: " + e.getMessage());
            return false;
        }
    }

    public boolean sinkronisasiResep() {
        try {
            return dao.syncExistingProdukToResep();
        } catch (SQLException e) {
            System.err.println("Error syncing recipes: " + e.getMessage());
            return false;
        }
    }

    public int getTotalProduk() {
        try {
            return dao.getAllProduk().size();
        } catch (SQLException e) {
            System.err.println("Error counting products: " + e.getMessage());
            return 0;
        }
    }
}