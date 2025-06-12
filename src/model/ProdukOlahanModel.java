package model;

public class ProdukOlahanModel {
    private int id;
    private String namaProduk;
    private int resepId;
    private String namaResep;
    private int ukuranKemasan;
    private int jumlah;

    public ProdukOlahanModel() {}

    public ProdukOlahanModel(int id, String namaProduk, int resepId, String namaResep, int ukuranKemasan, int jumlah) {
        this.id = id;
        this.namaProduk = namaProduk;
        this.resepId = resepId;
        this.namaResep = namaResep;
        this.ukuranKemasan = ukuranKemasan;
        this.jumlah = jumlah;
    }

    // Getters dan Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    public int getResepId() { return resepId; }
    public void setResepId(int resepId) { this.resepId = resepId; }

    public String getNamaResep() { return namaResep; }
    public void setNamaResep(String namaResep) { this.namaResep = namaResep; }

    public int getUkuranKemasan() { return ukuranKemasan; }
    public void setUkuranKemasan(int ukuranKemasan) { this.ukuranKemasan = ukuranKemasan; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
}