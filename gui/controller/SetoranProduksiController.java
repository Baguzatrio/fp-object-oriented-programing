package controller;

import dbaccess.SetoranProduksiDAO;
import dbaccess.StokPeciDAO;
import model.SetoranProduksi;
import model.StokPeci;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class SetoranProduksiController {
    private SetoranProduksiDAO dao;
    private StokPeciDAO stokPeciDAO;

    public SetoranProduksiController(Connection conn) {
        this.dao = new SetoranProduksiDAO(conn);
        this.stokPeciDAO = new StokPeciDAO(conn);
    }

    public List<SetoranProduksi> getAllSetoran() {
        return dao.getAllSetoran();
    }

    public boolean tambahSetoran(int idPegawai, Date tanggal, int ukuran, int jumlah) {
        SetoranProduksi s = new SetoranProduksi(idPegawai, tanggal, ukuran, jumlah);
        boolean sukses = dao.insertSetoran(s);

        if (sukses) {
            // Cek apakah stok untuk ukuran ini sudah ada
            StokPeci stok = stokPeciDAO.getByUkuran(ukuran);
            if (stok != null) {
                // Tambahkan jumlah
                stok.setJumlahKodi(stok.getJumlahKodi() + jumlah);
                return stokPeciDAO.update(stok);
            } else {
                // Buat stok baru
                StokPeci stokBaru = new StokPeci(0, ukuran, jumlah);
                return stokPeciDAO.insert(stokBaru);
            }
        }

        return false;
    }

    public boolean hapusSetoran(int id) {
        return dao.deleteSetoran(id);
    }
}
