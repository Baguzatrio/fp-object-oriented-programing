package model;

import java.sql.Date;
import java.util.List;

public class Penjualan {
    private int id;
    private Date tanggal;
    private String namaToko;
    private String kotaToko;
    private List<DetailPenjualan> detailPenjualan;

    public Penjualan() {}

    public Penjualan(int id, Date tanggal, String namaToko, String kotaToko) {
        this.id = id;
        this.tanggal = tanggal;
        this.namaToko = namaToko;
        this.kotaToko = kotaToko;
    }

    public Penjualan(Date tanggal, String namaToko, String kotaToko) {
        this.tanggal = tanggal;
        this.namaToko = namaToko;
        this.kotaToko = kotaToko;
    }

    // Getter Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }

    public String getNamaToko() { return namaToko; }
    public void setNamaToko(String namaToko) { this.namaToko = namaToko; }

    public String getKotaToko() { return kotaToko; }
    public void setKotaToko(String kotaToko) { this.kotaToko = kotaToko; }

    public List<DetailPenjualan> getDetailPenjualan() { return detailPenjualan; }
    public void setDetailPenjualan(List<DetailPenjualan> detailPenjualan) { this.detailPenjualan = detailPenjualan; }
}
