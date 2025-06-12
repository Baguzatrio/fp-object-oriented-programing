package controller;

import model.ProduksiDAO;
import model.ProduksiModel;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProduksiController {
    private ProduksiDAO produksiDAO;

    public ProduksiController() {
        this.produksiDAO = new ProduksiDAO();
    }

    public List<ProduksiModel> getAllProduksi() {
        return produksiDAO.getAllProduksi();
    }

    public Map<String, Integer> loadResep(JComboBox<String> comboBox) {
        return produksiDAO.getResepMap(comboBox);
    }

    public boolean prosesInputProduksi(int resepId, String produk, int batch, double kg) throws SQLException {
        return produksiDAO.kurangiStokDanSimpan(resepId, produk, batch, kg);
    }
}
