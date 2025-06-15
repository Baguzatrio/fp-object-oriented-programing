package model;

public class ResepModel {
    private int id;
    private String namaProduk;
    private int produkOlahanId; // Tambahkan field baru

    // Constructor
    public ResepModel() {}

    public ResepModel(int id, String namaProduk, int produkOlahanId) {
        this.id = id;
        this.namaProduk = namaProduk;
        this.produkOlahanId = produkOlahanId;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    public int getProdukOlahanId() { return produkOlahanId; }
    public void setProdukOlahanId(int produkOlahanId) { this.produkOlahanId = produkOlahanId; }

    @Override
    public String toString() {
        return namaProduk;
    }

    public Object getDeskripsi() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}