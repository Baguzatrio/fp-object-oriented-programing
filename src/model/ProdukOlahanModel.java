package model;

public class ProdukOlahanModel {
    private int id;
    private String namaProduk;
    private int ukuranKemasan; // in grams
    private int stok;
    private int harga;
    private int upah;

    public ProdukOlahanModel() {}

    public ProdukOlahanModel(int id, String namaProduk, int ukuranKemasan, int stok, int harga, int upah) {
        this.id = id;
        this.namaProduk = namaProduk;
        this.ukuranKemasan = ukuranKemasan;
        this.stok = stok; // Will be calculated from production results
        this.harga = harga;
        this.upah = upah;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    public int getUkuranKemasan() { return ukuranKemasan; }
    public void setUkuranKemasan(int ukuranKemasan) { 
        this.ukuranKemasan = ukuranKemasan; 
    }

    public int getStok() { return stok; }
    public void setStok(int stok) { 
        this.stok = stok; // Will be updated by production system
    }

    public int getHarga() { return harga; }
    public void setHarga(int harga) { 
        this.harga = harga;
    }

    public int getUpah() { return upah; }
    public void setUpah(int upah) { 
        this.upah = upah; 
    }

    // Helper method for display
    public String getUkuranDisplay() {
        return ukuranKemasan + " gr";
    }

    public String getHargaDisplay() {
        return "Rp " + String.format("%,d", harga);
    }

    public String getUpahDisplay() {
        return "Rp " + String.format("%,d", upah);
    }
}