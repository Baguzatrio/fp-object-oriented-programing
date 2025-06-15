package controller;

import model.ResepDAO;
import model.ResepDetailDAO;
import java.util.List;
import model.ResepDetailModel;
import model.ResepModel;

public class ResepDetailController {
    private ResepDetailDAO resepDetailDAO;
    private ResepDAO resepDAO;

    public ResepDetailController() {
        this.resepDetailDAO = new ResepDetailDAO();
        this.resepDAO = new ResepDAO();
    }

    public List<ResepDetailModel> getDetailByResepId(int resepId) {
        return resepDetailDAO.getDetailByResepId(resepId);
    }

    public boolean tambahDetail(int resepId, int bahanBakuId, double jumlah, String satuan) {
        // Validasi input
        if (jumlah <= 0) {
            return false;
        }
        
        return resepDetailDAO.tambahDetail(resepId, bahanBakuId, jumlah, satuan);
    }

    public boolean hapusDetail(int detailId) {
        return resepDetailDAO.hapusDetail(detailId);
    }

    public List<ResepModel> getAllReseps() {
        return resepDAO.getAllReseps();
    }

}