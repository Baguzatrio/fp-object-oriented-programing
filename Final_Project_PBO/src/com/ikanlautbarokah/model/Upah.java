/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.model;

/**
 *
 * @author Satrio Aji
 */
public class Upah {
    private int id;
    private String namaPegawai;
    private int jumlahProduksi;
    private double upahPerUnit;
    private double totalUpah;

    // Konstruktor kosong
    public Upah() {}

    // Konstruktor lengkap (untuk mengambil dari DB)
    public Upah(int id, String namaPegawai, int jumlahProduksi, double upahPerUnit, double totalUpah) {
        this.id = id;
        this.namaPegawai = namaPegawai;
        this.jumlahProduksi = jumlahProduksi;
        this.upahPerUnit = upahPerUnit;
        this.totalUpah = totalUpah;
    }

    // Konstruktor untuk INSERT (tanpa ID, karena auto-increment)
    public Upah(String namaPegawai, int jumlahProduksi, double upahPerUnit, double totalUpah) {
        this.namaPegawai = namaPegawai;
        this.jumlahProduksi = jumlahProduksi;
        this.upahPerUnit = upahPerUnit;
        this.totalUpah = totalUpah;
    }

    // Getters
    public int getId() { return id; }
    public String getNamaPegawai() { return namaPegawai; }
    public int getJumlahProduksi() { return jumlahProduksi; }
    public double getUpahPerUnit() { return upahPerUnit; }
    public double getTotalUpah() { return totalUpah; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNamaPegawai(String namaPegawai) { this.namaPegawai = namaPegawai; }
    public void setJumlahProduksi(int jumlahProduksi) { this.jumlahProduksi = jumlahProduksi; }
    public void setUpahPerUnit(double upahPerUnit) { this.upahPerUnit = upahPerUnit; }
    public void setTotalUpah(double totalUpah) { this.totalUpah = totalUpah; }
}
