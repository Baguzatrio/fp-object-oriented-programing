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
}
