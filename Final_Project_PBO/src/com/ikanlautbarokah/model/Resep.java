/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.model;

/**
 *
 * @author Satrio Aji
 */
public class Resep {
    private int id;
    private String namaProduk; // nama_produk di tabel resep adalah nama resep itu sendiri

    public Resep() {}

    public Resep(int id, String namaProduk) {
        this.id = id;
        this.namaProduk = namaProduk;
    }

    public int getId() { return id; }
    public String getNamaProduk() { return namaProduk; }

    public void setId(int id) { this.id = id; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    @Override
    public String toString() {
        return namaProduk; // Penting untuk JComboBox agar menampilkan nama resep
    }
}
