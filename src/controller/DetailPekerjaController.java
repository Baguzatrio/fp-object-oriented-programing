package controller;

import model.DetailPekerjaModel;
import view.DetailPekerja;

import java.util.HashMap;
import java.util.List;

public class DetailPekerjaController {
    private DetailPekerja view;
    private DetailPekerjaModel model;

    public DetailPekerjaController(DetailPekerja view) {
        this.view = view;
        this.model = new DetailPekerjaModel();
    }

    public void loadData(int idPekerja) {
        try {
            // Ambil data detail pekerja
            HashMap<String, String> detail = model.getPekerjaDetail(idPekerja);
            if (detail != null) {
                view.setPekerjaDetail(
                    detail.get("nama"),
                    detail.get("mulai_bekerja"),
                    detail.get("no_telp"),
                    detail.get("alamat")
                );
            } else {
                view.showError("Data pekerja tidak ditemukan.");
                return;
            }

            // Ambil data upah pekerja
            List<HashMap<String, String>> upahList = model.getUpahPekerja(idPekerja);
            view.setUpahTable(upahList);
        } catch (Exception e) {
            view.showError("Terjadi kesalahan: " + e.getMessage());
        }
    }

    public void loadData(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
