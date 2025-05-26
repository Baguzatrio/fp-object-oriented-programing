package model;

public class StokPeci {
    private int id;
    private int ukuran;
    private int jumlahKodi;

    public StokPeci(int id, int ukuran, int jumlahKodi) {
        this.id = id;
        this.ukuran = ukuran;
        this.jumlahKodi = jumlahKodi;
    }

    public int getId() {
        return id;
    }

    public int getUkuran() {
        return ukuran;
    }

    public int getJumlahKodi() {
        return jumlahKodi;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUkuran(int ukuran) {
        this.ukuran = ukuran;
    }

    public void setJumlahKodi(int jumlahKodi) {
        this.jumlahKodi = jumlahKodi;
    }
}
