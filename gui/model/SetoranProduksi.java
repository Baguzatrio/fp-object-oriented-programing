package model;

import java.sql.Date;

public class SetoranProduksi {
    private int id;
    private int idPegawai;
    private Date tanggal;
    private int ukuran;
    private int jumlah;

    public SetoranProduksi() {}

    public SetoranProduksi(int id, int idPegawai, Date tanggal, int ukuran, int jumlah) {
        this.id = id;
        this.idPegawai = idPegawai;
        this.tanggal = tanggal;
        this.ukuran = ukuran;
        this.jumlah = jumlah;
    }

    public SetoranProduksi(int idPegawai, Date tanggal, int ukuran, int jumlah) {
        this.idPegawai = idPegawai;
        this.tanggal = tanggal;
        this.ukuran = ukuran;
        this.jumlah = jumlah;
    }

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPegawai() { return idPegawai; }
    public void setIdPegawai(int idPegawai) { this.idPegawai = idPegawai; }

    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }

    public int getUkuran() { return ukuran; }
    public void setUkuran(int ukuran) { this.ukuran = ukuran; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
}
