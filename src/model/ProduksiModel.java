package model;

import java.sql.*;
import java.util.*;

public class ProduksiModel {
    private int id;
    private String tanggal;
    private String produk;
    private int idPekerja;
    private String namaPekerja;
    private int jumlahBatch;
    private double totalKg;
    private int jumlahKemasan;
    
    // Constructor
    public ProduksiModel() {}
    
    public ProduksiModel(int id, String tanggal, String produk, int idPegawai, 
                        String namaPegawai, int jumlahBatch, double totalKg, 
                        int jumlahKemasan) {
        this.id = id;
        this.tanggal = tanggal;
        this.produk = produk;
        this.idPekerja = idPegawai;
        this.namaPekerja = namaPegawai;
        this.jumlahBatch = jumlahBatch;
        this.totalKg = totalKg;
        this.jumlahKemasan = jumlahKemasan;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    
    public String getProduk() { return produk; }
    public void setProduk(String produk) { this.produk = produk; }
    
    public int getIdPekerja() { return idPekerja; }
    public void setIdPekerja(int idPekerja) { this.idPekerja = idPekerja; }
    
    public String getPekerja() { return namaPekerja; }
    public void setPekerja(String namaPekerja) { this.namaPekerja = namaPekerja; }
    
    public int getJumlahBatch() { return jumlahBatch; }
    public void setJumlahBatch(int jumlahBatch) { 
        this.jumlahBatch = jumlahBatch;
        this.totalKg = jumlahBatch * 12; // Auto-calculate kg (1 batch = 12kg)
    }
    
    public double getTotalKg() { return totalKg; }
    
    public int getJumlahKemasan() { return jumlahKemasan; }
    public void setJumlahKemasan(int ukuranKemasan) {
        // Calculate kemasan: (totalKg * 1000) / ukuranKemasan
        this.jumlahKemasan = (int) ((this.totalKg * 1000) / ukuranKemasan);
    }
}