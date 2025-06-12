package controller;

import model.ResepDAO;
import model.ResepModel;

import java.util.List;

public class ResepController {
    private ResepDAO resepDAO;

    public ResepController() {
        this.resepDAO = new ResepDAO();
    }

    public List<ResepModel> getAllResep() {
        return resepDAO.getAllResep();
    }

    public boolean tambahResep(String namaProduk) {
        if (namaProduk == null || namaProduk.trim().isEmpty()) {
            return false;
        }
        ResepModel resep = new ResepModel();
        resep.setNamaProduk(namaProduk);
        return resepDAO.insert(resep);
    }

}
