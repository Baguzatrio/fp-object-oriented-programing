package model;

public class BahanBakuModel {
    private int id;
    private String nama;
    private double stok;
    private String satuan;
    private double hargaPerUnit;
    private String tanggalBeli;
    private String tanggalKadaluarsa;
    private double beratPerUnit; // in kg

    public BahanBakuModel() {
        this.beratPerUnit = 1.0; // Nilai default 1 kg
    }

    public BahanBakuModel(int id, String nama, double stok, String satuan, 
                         double hargaPerUnit, String tanggalBeli, 
                         String tanggalKadaluarsa, double beratPerUnit) {
        this.id = id;
        this.nama = nama;
        this.stok = stok;
        this.satuan = satuan;
        this.hargaPerUnit = hargaPerUnit;
        this.tanggalBeli = tanggalBeli;
        this.tanggalKadaluarsa = tanggalKadaluarsa;
        this.beratPerUnit = beratPerUnit;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getStok() { return stok; }
    public void setStok(double stok) { this.stok = stok; }

    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }

    public double getHargaPerUnit() { return hargaPerUnit; }
    public void setHargaPerUnit(double hargaPerUnit) { this.hargaPerUnit = hargaPerUnit; }

    public String getTanggalBeli() { return tanggalBeli; }
    public void setTanggalBeli(String tanggalBeli) { this.tanggalBeli = tanggalBeli; }

    public String getTanggalKadaluarsa() { return tanggalKadaluarsa; }
    public void setTanggalKadaluarsa(String tanggalKadaluarsa) { this.tanggalKadaluarsa = tanggalKadaluarsa; }
    
    public double getBeratPerUnit() { return beratPerUnit; }
    public void setBeratPerUnit(double beratPerUnit) { this.beratPerUnit = beratPerUnit; }
    
    // Method untuk menghitung total secara dinamis
    public double getTotalBerat() {
        return stok * beratPerUnit;
    }
    
    public double getTotalHarga() {
        return stok * hargaPerUnit;
    }
}