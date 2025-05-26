package controller;

import dbaccess.BahanBakuDAO;
import model.BahanBaku;
import java.sql.Connection;
import java.util.List;

public class BahanBakuController {
    private BahanBakuDAO dao;

    public BahanBakuController(Connection conn) {
        this.dao = new BahanBakuDAO(conn);
    }

    public List<BahanBaku> getAllBahanBaku() {
        return dao.getAllBahanBaku();
    }

    public boolean tambahBahanBaku(String nama, int jumlah, String satuan, int harga, java.sql.Date tanggalMasuk) {
        BahanBaku bb = new BahanBaku(nama, jumlah, satuan, harga, tanggalMasuk);
        return dao.insertBahanBaku(bb);
    }

    public boolean updateBahanBaku(int id, String nama, int jumlah, String satuan, int harga, java.sql.Date tanggalMasuk) {
        BahanBaku bb = new BahanBaku(id, nama, jumlah, satuan, harga, tanggalMasuk);
        return dao.updateBahanBaku(bb);
    }

    public boolean hapusBahanBaku(int id) {
        return dao.deleteBahanBaku(id);
    }
}
