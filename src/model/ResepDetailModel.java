package model;

public class ResepDetailModel {
    private int id;
    private int resepId;       // Tambahkan
    private int bahanBakuId;   // Tambahkan
    private String namaBahan;  // Dari join
    private double jumlah;
    private String satuan;    // Dari join

    // Constructor
    public ResepDetailModel() {}

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getResepId() { return resepId; }
    public void setResepId(int resepId) { this.resepId = resepId; }

    public int getBahanBakuId() { return bahanBakuId; }
    public void setBahanBakuId(int bahanBakuId) { this.bahanBakuId = bahanBakuId; }

    public String getNamaBahan() { return namaBahan; }
    public void setNamaBahan(String namaBahan) { this.namaBahan = namaBahan; }

    public double getJumlah() { return jumlah; }
    public void setJumlah(double jumlah) { this.jumlah = jumlah; }

    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }
}