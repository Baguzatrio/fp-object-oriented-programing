/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.model;

/**
 *
 * @author Satrio Aji
 */
public class ProdukOlahan {
    private int id;
    private String namaProduk;
    private int resepId;
    private String namaResep; // Untuk memudahkan tampilan di View, akan di-join dari Resep
    private int ukuranKemasan;
    private int jumlah;

    public ProdukOlahan() {}

    public ProdukOlahan(int id, String namaProduk, int resepId, String namaResep, int ukuranKemasan, int jumlah) {
        this.id = id;
        this.namaProduk = namaProduk;
        this.resepId = resepId;
        this.namaResep = namaResep;
        this.ukuranKemasan = ukuranKemasan;
        this.jumlah = jumlah;
    }

    public ProdukOlahan(String namaProduk, int resepId, int ukuranKemasan, int jumlah) {
        this.namaProduk = namaProduk;
        this.resepId = resepId;
        this.ukuranKemasan = ukuranKemasan;
        this.jumlah = jumlah;
    }

    // Getters
    public int getId() { return id; }
    public String getNamaProduk() { return namaProduk; }
    public int getResepId() { return resepId; }
    public String getNamaResep() { return namaResep; }
    public int getUkuranKemasan() { return ukuranKemasan; }
    public int getJumlah() { return jumlah; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }
    public void setResepId(int resepId) { this.resepId = resepId; }
    public void setNamaResep(String namaResep) { this.namaResep = namaResep; }
    public void setUkuranKemasan(int ukuranKemasan) { this.ukuranKemasan = ukuranKemasan; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
}
