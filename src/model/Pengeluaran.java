package model;

import java.time.LocalDate;

public class Pengeluaran {
    private int id;
    private LocalDate tanggal;
    private String jenis;
    private String kategori;
    private int jumlah;
    private String keterangan;

    public Pengeluaran(int id, LocalDate tanggal, String jenis, 
                      String kategori, int jumlah, String keterangan) {
        this.id = id;
        this.tanggal = tanggal;
        this.jenis = jenis;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.keterangan = keterangan;
    }

    // Getter methods
    public int getId() { return id; }
    public LocalDate getTanggal() { return tanggal; }
    public String getJenis() { return jenis; }
    public String getKategori() { return kategori; }
    public int getJumlah() { return jumlah; }
    public String getKeterangan() { return keterangan; }

    // Setter methods (jika diperlukan)
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
}