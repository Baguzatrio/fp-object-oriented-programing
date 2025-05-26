package model;

public class Pegawai {
    private int id;
    private String nama;
    private String jabatan;

    public Pegawai() {}

    public Pegawai(int id, String nama, String jabatan) {
        this.id = id;
        this.nama = nama;
        this.jabatan = jabatan;
    }

    public Pegawai(String nama, String jabatan) {
        this.nama = nama;
        this.jabatan = jabatan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

}
