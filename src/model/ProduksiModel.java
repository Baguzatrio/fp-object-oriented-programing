package model;

import java.sql.Date;

public class ProduksiModel {
    private int id;
    private Date tanggal;
    private String produk;
    private int jumlahBatch;
    private double totalKg;
    private int jumlahKemasan;

    public ProduksiModel(int id, Date tanggal, String produk, int jumlahBatch, double totalKg, int jumlahKemasan) {
        this.id = id;
        this.tanggal = tanggal;
        this.produk = produk;
        this.jumlahBatch = jumlahBatch;
        this.totalKg = totalKg;
        this.jumlahKemasan = jumlahKemasan;
    }

    public ProduksiModel(String produk, int jumlahBatch, double totalKg, int jumlahKemasan) {
        this(0, new Date(System.currentTimeMillis()), produk, jumlahBatch, totalKg, jumlahKemasan);
    }

    public int getId() { return id; }
    public Date getTanggal() { return tanggal; }
    public String getProduk() { return produk; }
    public int getJumlahBatch() { return jumlahBatch; }
    public double getTotalKg() { return totalKg; }
    public int getJumlahKemasan() { return jumlahKemasan; }
}
