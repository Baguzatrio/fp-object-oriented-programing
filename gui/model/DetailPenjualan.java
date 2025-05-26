package model;

public class DetailPenjualan {
    private int id;
    private int idPenjualan;
    private int ukuran;
    private int jumlah;
    private int hargaKodi;

    public DetailPenjualan() {}

    public DetailPenjualan(int id, int idPenjualan, int ukuran, int jumlah, int hargaKodi) {
        this.id = id;
        this.idPenjualan = idPenjualan;
        this.ukuran = ukuran;
        this.jumlah = jumlah;
        this.hargaKodi = hargaKodi;
    }

    public DetailPenjualan(int idPenjualan, int ukuran, int jumlah, int hargaKodi) {
        this.idPenjualan = idPenjualan;
        this.ukuran = ukuran;
        this.jumlah = jumlah;
        this.hargaKodi = hargaKodi;
    }

    // Getter Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPenjualan() { return idPenjualan; }
    public void setIdPenjualan(int idPenjualan) { this.idPenjualan = idPenjualan; }

    public int getUkuran() { return ukuran; }
    public void setUkuran(int ukuran) { this.ukuran = ukuran; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }

    public int getHargaKodi() { return hargaKodi; }
    public void setHargaKodi(int hargaKodi) { this.hargaKodi = hargaKodi; }
}
