/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.model;

/**
 *
 * @author Satrio Aji
 */
public class ResepDetail {
    private int id;
    private int resepId;
    private String namaResep; // Untuk memudahkan tampilan di View, akan di-join dari Resep
    private int bahanBakuId;
    private String namaBahanBaku; // Untuk memudahkan tampilan di View, akan di-join dari BahanBaku
    private double jumlah;

    public ResepDetail() {}

    public ResepDetail(int id, int resepId, String namaResep, int bahanBakuId, String namaBahanBaku, double jumlah) {
        this.id = id;
        this.resepId = resepId;
        this.namaResep = namaResep;
        this.bahanBakuId = bahanBakuId;
        this.namaBahanBaku = namaBahanBaku;
        this.jumlah = jumlah;
    }

    public ResepDetail(int resepId, int bahanBakuId, double jumlah) {
        this.resepId = resepId;
        this.bahanBakuId = bahanBakuId;
        this.jumlah = jumlah;
    }

    // Getters
    public int getId() { return id; }
    public int getResepId() { return resepId; }
    public String getNamaResep() { return namaResep; }
    public int getBahanBakuId() { return bahanBakuId; }
    public String getNamaBahanBaku() { return namaBahanBaku; }
    public double getJumlah() { return jumlah; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setResepId(int resepId) { this.resepId = resepId; }
    public void setNamaResep(String namaResep) { this.namaResep = namaResep; }
    public void setBahanBakuId(int bahanBakuId) { this.bahanBakuId = bahanBakuId; }
    public void setNamaBahanBaku(String namaBahanBaku) { this.namaBahanBaku = namaBahanBaku; }
    public void setJumlah(double jumlah) { this.jumlah = jumlah; }
}
