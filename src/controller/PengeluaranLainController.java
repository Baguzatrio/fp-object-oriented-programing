package controller;

import model.PengeluaranLainModel;
import model.Pengeluaran;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PengeluaranLainController {
    private final PengeluaranLainModel model;

    public PengeluaranLainController(Connection connection) throws SQLException {
    this.model = new PengeluaranLainModel(connection);
       }

    // 1. Operasi CRUD
    public void tambahPengeluaran(LocalDate tanggal, String jenis, String kategori, 
                                int jumlah, String keterangan) throws SQLException {
        model.tambahPengeluaran(tanggal, jenis, kategori, jumlah, keterangan);
    }

    public List<Pengeluaran> getAllPengeluaran() throws SQLException {
        return model.getAllPengeluaran();
    }

    public void updatePengeluaran(int id, LocalDate tanggal, String jenis, 
                                String kategori, int jumlah, String keterangan) throws SQLException {
        model.updatePengeluaran(id, tanggal, jenis, kategori, jumlah, keterangan);
    }

    public void hapusPengeluaran(int id) throws SQLException {
        model.hapusPengeluaran(id);
    }

    // 2. Operasi Laporan
    public int getTotalPengeluaranHariIni() throws SQLException {
        LocalDate hariIni = LocalDate.now();
        return model.getTotalPengeluaran(hariIni, hariIni);
    }

    public int getTotalPengeluaranBulanIni() throws SQLException {
        LocalDate sekarang = LocalDate.now();
        LocalDate awalBulan = sekarang.withDayOfMonth(1);
        return model.getTotalPengeluaran(awalBulan, sekarang);
    }

    // 3. Operasi Filter
    public List<Pengeluaran> filterByTanggal(LocalDate mulai, LocalDate sampai) throws SQLException {
        return model.filterByTanggal(mulai, sampai);
    }

    public List<Pengeluaran> filterByJenis(String jenis) throws SQLException {
        return model.filterByJenis(jenis);
    }

    // 4. Validasi Data
    public boolean validateInput(String jumlah) {
        try {
            Integer.parseInt(jumlah);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}