package controller;

import model.BahanBakuDAO;
import model.BahanBakuModel;

import java.util.List;
import view.BahanBaku;

public class BahanBakuController {
    private BahanBakuDAO dao;

    public BahanBakuController() {
        dao = new BahanBakuDAO();
    }

    public List<BahanBakuModel> getAllBahan(String filterNama) {
        return dao.getAll(filterNama);
    }

    public boolean tambahBahan(BahanBakuModel bahan) {
        if (bahan.getNama() == null || bahan.getNama().isEmpty()) {
            return false;
        }
        return dao.insert(bahan);
    }
    
    public boolean reduceStock(int id, double amount) {
        return dao.reduceStock(id, amount);
    }
}
