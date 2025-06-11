/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.model;

import java.sql.Date;
/**
 *
 * @author Satrio Aji
 */
public class Produksi {
    private int id;
    private Date tanggal;
    private String namaProdukResep; // Nama produk dari resep yang digunakan
    private int jumlahBatch;
    private double totalKg;
    private int jumlahKemasan;

    // Konstruktor kosong
    public Produksi() {}

    // Konstruktor lengkap (untuk mengambil dari DB)
    public Produksi(int id, Date tanggal, String namaProdukResep, int jumlahBatch, double totalKg, int jumlahKemasan) {
        this.id = id;
        this.tanggal = tanggal;
        this.namaProdukResep = namaProdukResep;
        this.jumlahBatch = jumlahBatch;
        this.totalKg = totalKg;
        this.jumlahKemasan = jumlahKemasan;
    }

    // Konstruktor untuk INSERT (tanpa ID dan tanggal, karena tanggal CURRENT_DATE())
    public Produksi(String namaProdukResep, int jumlahBatch, double totalKg, int jumlahKemasan) {
        this.namaProdukResep = namaProdukResep;
        this.jumlahBatch = jumlahBatch;
        this.totalKg = totalKg;
        this.jumlahKemasan = jumlahKemasan;
    }

    // Getters
    public int getId() { return id; }
    public Date getTanggal() { return tanggal; }
    public String getNamaProdukResep() { return namaProdukResep; }
    public int getJumlahBatch() { return jumlahBatch; }
    public double getTotalKg() { return totalKg; }
    public int getJumlahKemasan() { return jumlahKemasan; }

    // Setters (jika dibutuhkan, untuk update)
    public void setId(int id) { this.id = id; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }
    public void setNamaProdukResep(String namaProdukResep) { this.namaProdukResep = namaProdukResep; }
    public void setJumlahBatch(int jumlahBatch) { this.jumlahBatch = jumlahBatch; }
    public void setTotalKg(double totalKg) { this.totalKg = totalKg; }
    public void setJumlahKemasan(int jumlahKemasan) { this.jumlahKemasan = jumlahKemasan; }
}
