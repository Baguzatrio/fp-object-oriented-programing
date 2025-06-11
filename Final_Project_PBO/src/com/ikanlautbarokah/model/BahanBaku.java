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
public class BahanBaku {
    private int id;
    private String nama;
    private double stok;
    private String satuan;
    private double hargaPerUnit;
    private Date tanggalBeli;
    private Date tanggalKadaluwarsa;

    // Konstruktor kosong (jika dibutuhkan, misalnya oleh ORM atau untuk inisialisasi awal)
    public BahanBaku() {}

    // Konstruktor lengkap
    public BahanBaku(int id, String nama, double stok, String satuan, double hargaPerUnit, Date tanggalBeli, Date tanggalKadaluwarsa) {
        this.id = id;
        this.nama = nama;
        this.stok = stok;
        this.satuan = satuan;
        this.hargaPerUnit = hargaPerUnit;
        this.tanggalBeli = tanggalBeli;
        this.tanggalKadaluwarsa = tanggalKadaluwarsa;
    }

    // Konstruktor untuk INSERT (tanpa ID, karena biasanya auto-increment)
    public BahanBaku(String nama, double stok, String satuan, double hargaPerUnit, Date tanggalBeli, Date tanggalKadaluwarsa) {
        this.nama = nama;
        this.stok = stok;
        this.satuan = satuan;
        this.hargaPerUnit = hargaPerUnit;
        this.tanggalBeli = tanggalBeli;
        this.tanggalKadaluwarsa = tanggalKadaluwarsa;
    }

    // Getters
    public int getId() { return id; }
    public String getNama() { return nama; }
    public double getStok() { return stok; }
    public String getSatuan() { return satuan; }
    public double getHargaPerUnit() { return hargaPerUnit; }
    public Date getTanggalBeli() { return tanggalBeli; }
    public Date getTanggalKadaluwarsa() { return tanggalKadaluwarsa; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNama(String nama) { this.nama = nama; }
    public void setStok(double stok) { this.stok = stok; }
    public void setSatuan(String satuan) { this.satuan = satuan; }
    public void setHargaPerUnit(double hargaPerUnit) { this.hargaPerUnit = hargaPerUnit; }
    public void setTanggalBeli(Date tanggalBeli) { this.tanggalBeli = tanggalBeli; }
    public void setTanggalKadaluwarsa(Date tanggalKadaluwarsa) { this.tanggalKadaluwarsa = tanggalKadaluwarsa; }
}
