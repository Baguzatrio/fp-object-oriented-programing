package controller;

import model.ResepDetailDAO;
import model.ResepDetailModel;

import java.util.List;

public class ResepDetailController {
    private ResepDetailDAO dao = new ResepDetailDAO();

    public List<ResepDetailModel> getDetailByResepId(int resepId) {
        return dao.getByResepId(resepId);
    }

    public boolean tambahDetail(int resepId, int bahanId, double jumlah) {
        return dao.insert(resepId, bahanId, jumlah);
    }

}