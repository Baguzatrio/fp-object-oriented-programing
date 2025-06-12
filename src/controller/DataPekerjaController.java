package controller;

import java.sql.Date;
import model.DataPekerjaModel;
import view.DataPekerja2;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DataPekerjaController {
    private final DataPekerja2 view;
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public DataPekerjaController(DataPekerja2 view) {
        this.view = view;
    }
    
    public void loadData() {
    List<DataPekerjaModel> pekerjaList = DataPekerjaModel.getAll();
    List<Object[]> rowData = new ArrayList<>();
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    for (DataPekerjaModel pekerja : pekerjaList) {
        rowData.add(new Object[]{
            pekerja.getId(),
            pekerja.getNama(),
            sdf.format(pekerja.getMulaiBekerja()), // Formatted here
            pekerja.getNoTelp(),
            pekerja.getAlamat()
        });
    }
    
    view.displayData(rowData);
}
    
    public void tambahPekerja(String nama, String tanggalStr, String noTelp, String alamat) {
        try {
            DataPekerjaModel pekerja = new DataPekerjaModel();
            pekerja.setNama(nama);
            pekerja.setMulaiBekerja((Date) inputFormat.parse(tanggalStr));
            pekerja.setNoTelp(noTelp);
            pekerja.setAlamat(alamat);

            if (pekerja.save()) {
                view.showMessage("Data berhasil ditambahkan");
                view.clearForm();
                loadData();
            } else {
                view.showError("Gagal menambahkan data");
            }
        } catch (ParseException e) {
            view.showError("Format tanggal salah! Gunakan DDMMYYYY");
        }
    }

    public void updatePekerja(int id, String nama, String tanggalStr, String noTelp, String alamat) {
        try {
            DataPekerjaModel pekerja = DataPekerjaModel.findById(id);
            if (pekerja != null) {
                pekerja.setNama(nama);
                pekerja.setMulaiBekerja((Date) inputFormat.parse(tanggalStr));
                pekerja.setNoTelp(noTelp);
                pekerja.setAlamat(alamat);

                if (pekerja.save()) {
                    view.showMessage("Data berhasil diperbarui");
                    loadData();
                }
            }
        } catch (ParseException e) {
            view.showError("Format tanggal salah! Gunakan DDMMYYYY");
        }
    }

    public void deletePekerja(int id) {
    if (view.confirmAction("Yakin ingin menghapus data ini?")) {
        DataPekerjaModel pekerja = DataPekerjaModel.findById(id);
        if (pekerja != null && pekerja.delete()) {
            view.showMessage("Data berhasil dihapus");
            loadData();
            view.clearForm();
        } else {
            view.showError("Gagal menghapus data");
        }
    }
}

    public void prepareUpdateForm(int id) {
        DataPekerjaModel pekerja = DataPekerjaModel.findById(id);
        if (pekerja != null) {
            view.prepareUpdateForm(
                pekerja.getNama(),
                inputFormat.format(pekerja.getMulaiBekerja()),
                pekerja.getNoTelp(),
                pekerja.getAlamat()
            );
        }
    }
}