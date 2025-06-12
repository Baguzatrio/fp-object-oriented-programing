package model;

public class ResepDetailModel {
    private int id;
    private String namaBahan;
    private double jumlah;
    private String satuan;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNamaBahan() { return namaBahan; }
    public void setNamaBahan(String namaBahan) { this.namaBahan = namaBahan; }

    public double getJumlah() { return jumlah; }
    public void setJumlah(double jumlah) { this.jumlah = jumlah; }

    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }
}