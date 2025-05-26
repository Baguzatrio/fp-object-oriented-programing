package model;

import java.sql.Date;

public class BahanBaku {
    private int id;
    private String nama;
    private int jumlah;
    private String satuan;
    private int hargaPerSatuan;
    private Date tanggalMasuk;

    public BahanBaku() {}

    public BahanBaku(int id, String nama, int jumlah, String satuan, int hargaPerSatuan, Date tanggalMasuk) {
        this.id = id;
        this.nama = nama;
        this.jumlah = jumlah;
        this.satuan = satuan;
        this.hargaPerSatuan = hargaPerSatuan;
        this.tanggalMasuk = tanggalMasuk;
    }

    public BahanBaku(String nama, int jumlah, String satuan, int hargaPerSatuan, Date tanggalMasuk) {
        this.nama = nama;
        this.jumlah = jumlah;
        this.satuan = satuan;
        this.hargaPerSatuan = hargaPerSatuan;
        this.tanggalMasuk = tanggalMasuk;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public int getHargaPerSatuan() {
        return hargaPerSatuan;
    }

    public void setHargaPerSatuan(int hargaPerSatuan) {
        this.hargaPerSatuan = hargaPerSatuan;
    }

    public Date getTanggalMasuk() {
        return tanggalMasuk;
    }

    public void setTanggalMasuk(Date tanggalMasuk) {
        this.tanggalMasuk = tanggalMasuk;
    }
}
