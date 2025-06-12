package model;

public class ResepModel {
    private int id;
    private String namaProduk;

    public ResepModel() {}

    public ResepModel(int id, String namaProduk) {
        this.id = id;
        this.namaProduk = namaProduk;
    }

    public int getId() {
        return id;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    @Override
    public String toString() {
        return namaProduk;
    }
}
