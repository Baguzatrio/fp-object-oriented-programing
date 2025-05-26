package controller;

import dbaccess.DistribusiBahanDAO;
import model.DistribusiBahan;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class DistribusiBahanController {
    private DistribusiBahanDAO dao;

    public DistribusiBahanController(Connection conn) {
        this.dao = new DistribusiBahanDAO(conn);
    }

    public List<DistribusiBahan> getAllDistribusi() {
        return dao.getAllDistribusi();
    }

    public boolean tambahDistribusi(int idPemotong, int idPengrajin, Date tanggal, int ukuran, int jumlahKodi) {
        DistribusiBahan d = new DistribusiBahan(idPemotong, idPengrajin, tanggal, ukuran, jumlahKodi);
        return dao.insertDistribusi(d);
    }

    public boolean hapusDistribusi(int id) {
        return dao.deleteDistribusi(id);
    }
}
