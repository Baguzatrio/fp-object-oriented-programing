/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.model;

import java.sql.Date; // Penting untuk tipe Date SQL
/**
 *
 * @author Satrio Aji
 */
public class Distribusi {
    private int id;
    private Date tanggal;
    private String produk;
    private int jumlah;
    private String tujuan;
    private double hargaTotal;
    private String metodeBayar; // ENUM('Tunai','Tempo') di DB

    // Konstruktor kosong
    public Distribusi() {}

    // Konstruktor lengkap (untuk mengambil dari DB)
    public Distribusi(int id, Date tanggal, String produk, int jumlah, String tujuan, double hargaTotal, String metodeBayar) {
        this.id = id;
        this.tanggal = tanggal;
        this.produk = produk;
        this.jumlah = jumlah;
        this.tujuan = tujuan;
        this.hargaTotal = hargaTotal;
        this.metodeBayar = metodeBayar;
    }

    // Konstruktor untuk INSERT (tanpa ID dan tanggal, karena tanggal CURRENT_DATE())
    public Distribusi(String produk, int jumlah, String tujuan, double hargaTotal, String metodeBayar) {
        this.produk = produk;
        this.jumlah = jumlah;
        this.tujuan = tujuan;
        this.hargaTotal = hargaTotal;
        this.metodeBayar = metodeBayar;
    }

    // Getters
    public int getId() { return id; }
    public Date getTanggal() { return tanggal; }
    public String getProduk() { return produk; }
    public int getJumlah() { return jumlah; }
    public String getTujuan() { return tujuan; }
    public double getHargaTotal() { return hargaTotal; }
    public String getMetodeBayar() { return metodeBayar; }

    // Setters (jika dibutuhkan, untuk update)
    public void setId(int id) { this.id = id; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }
    public void setProduk(String produk) { this.produk = produk; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public void setTujuan(String tujuan) { this.tujuan = tujuan; }
    public void setHargaTotal(double hargaTotal) { this.hargaTotal = hargaTotal; }
    public void setMetodeBayar(String metodeBayar) { this.metodeBayar = metodeBayar; }
}
