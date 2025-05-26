package controller;

import dbaccess.StokPeciDAO;
import model.StokPeci;

import java.sql.Connection;
import java.util.List;

public class StokPeciController {
    private StokPeciDAO dao;

    public StokPeciController(Connection conn) {
        this.dao = new StokPeciDAO(conn);
    }

    public List<StokPeci> getAllStok() {
        return dao.getAll();
    }

    public boolean tambahAtauUpdateStok(int ukuran, int jumlahKodi) {
        StokPeci stokLama = dao.getByUkuran(ukuran);
        if (stokLama != null) {
            // Jika stok sudah ada, tambahkan jumlahnya
            int jumlahBaru = stokLama.getJumlahKodi() + jumlahKodi;
            stokLama.setJumlahKodi(jumlahBaru);
            return dao.update(stokLama);
        } else {
            // Jika belum ada, tambahkan stok baru
            StokPeci stokBaru = new StokPeci(0, ukuran, jumlahKodi);
            return dao.insert(stokBaru);
        }
    }

    public boolean hapusStok(int id) {
        return dao.delete(id);
    }
}
