package controller;

import dbaccess.PenjualanDAO;
import model.Penjualan;

import java.sql.Connection;
import java.util.List;

public class PenjualanController {
    private PenjualanDAO dao;

    public PenjualanController(Connection conn) {
        this.dao = new PenjualanDAO(conn);
    }

    public boolean tambahPenjualan(Penjualan p) {
        return dao.insertPenjualan(p);
    }

    public List<Penjualan> getSemuaPenjualan() {
        return dao.getAllPenjualan();
    }
}
