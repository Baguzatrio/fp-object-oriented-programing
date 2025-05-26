package model;

import java.sql.Date;

public class DistribusiBahan {
    private int id;
    private int idPemotong;
    private int idPengrajin;
    private Date tanggal;
    private int ukuran;
    private int jumlahKodi;

    public DistribusiBahan() {}

    public DistribusiBahan(int id, int idPemotong, int idPengrajin, Date tanggal, int ukuran, int jumlahKodi) {
        this.id = id;
        this.idPemotong = idPemotong;
        this.idPengrajin = idPengrajin;
        this.tanggal = tanggal;
        this.ukuran = ukuran;
        this.jumlahKodi = jumlahKodi;
    }

    public DistribusiBahan(int idPemotong, int idPengrajin, Date tanggal, int ukuran, int jumlahKodi) {
        this.idPemotong = idPemotong;
        this.idPengrajin = idPengrajin;
        this.tanggal = tanggal;
        this.ukuran = ukuran;
        this.jumlahKodi = jumlahKodi;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPemotong() { return idPemotong; }
    public void setIdPemotong(int idPemotong) { this.idPemotong = idPemotong; }

    public int getIdPengrajin() { return idPengrajin; }
    public void setIdPengrajin(int idPengrajin) { this.idPengrajin = idPengrajin; }

    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }

    public int getUkuran() { return ukuran; }
    public void setUkuran(int ukuran) { this.ukuran = ukuran; }

    public int getJumlahKodi() { return jumlahKodi; }
    public void setJumlahKodi(int jumlahKodi) { this.jumlahKodi = jumlahKodi; }
}
