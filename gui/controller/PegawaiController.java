package controller;

import dbaccess.PegawaiDAO;
import model.Pegawai;

import java.sql.Connection;
import java.util.List;

public class PegawaiController {
    private PegawaiDAO dao;

    public PegawaiController(Connection conn) {
        dao = new PegawaiDAO(conn);
    }

    public boolean tambahPegawai(Pegawai p) {
        return dao.insert(p); // Pastikan ada method insert() di PegawaiDAO
    }

    public boolean hapusPegawai(int id) {
        return dao.delete(id); // Pastikan ada method delete() di PegawaiDAO
    }

    public List<Pegawai> getAllPegawai() {
        return dao.getAll(); // Pastikan ada method getAll() di PegawaiDAO
    }
}
