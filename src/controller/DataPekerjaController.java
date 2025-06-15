package controller;

import java.sql.Date;
import model.DataPekerjaModel;
import view.DataPekerja2;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DataPekerjaController {
    private final DataPekerja2 view;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public DataPekerjaController(DataPekerja2 view) {
        this.view = view;
    }
    
    public void loadData() {
        List<DataPekerjaModel> pekerjaList = DataPekerjaModel.getAll();
        List<Object[]> rowData = new ArrayList<>();
        
        for (DataPekerjaModel pekerja : pekerjaList) {
            rowData.add(new Object[]{
                pekerja.getId(),
                pekerja.getNama(),
                dateFormat.format(pekerja.getMulaiBekerja()),
                pekerja.getNoTelp(),
                pekerja.getAlamat()
            });
        }
        
        view.displayData(rowData);
    }
    
    private Date parseTanggal(String tanggalStr) throws ParseException {
        if (tanggalStr == null || tanggalStr.trim().isEmpty()) {
            throw new ParseException("Tanggal tidak boleh kosong", 0);
        }
        
        try {
            java.util.Date parsedDate = dateFormat.parse(tanggalStr);
            return new Date(parsedDate.getTime());
        } catch (ParseException e) {
            throw new ParseException("Format tanggal harus DD/MM/YYYY (contoh: 25/02/2025)", 0);
        }
    }
    
    public void tambahPekerja(String nama, String tanggalStr, String noTelp, String alamat) {
        try {
            // Input validation
            if (nama == null || nama.trim().isEmpty()) {
                throw new IllegalArgumentException("Nama tidak boleh kosong");
            }
            
            if (noTelp == null || noTelp.trim().isEmpty()) {
                throw new IllegalArgumentException("Nomor telepon tidak boleh kosong");
            }
            
            Date tanggal = parseTanggal(tanggalStr);
            
            DataPekerjaModel pekerja = new DataPekerjaModel();
            pekerja.setNama(nama);
            pekerja.setMulaiBekerja(tanggal);
            pekerja.setNoTelp(noTelp);
            pekerja.setAlamat(alamat);

            if (pekerja.save()) {
                view.showMessage("Data pekerja berhasil ditambahkan");
                view.clearForm();
                loadData();
            } else {
                view.showError("Gagal menambahkan data pekerja");
            }
        } catch (ParseException e) {
            view.showError("Format tanggal salah!\n" + e.getMessage());
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        } catch (Exception e) {
            view.showError("Terjadi kesalahan: " + e.getMessage());
        }
    }

    public void updatePekerja(int id, String nama, String tanggalStr, String noTelp, String alamat) {
        try {
            // Input validation
            if (nama == null || nama.trim().isEmpty()) {
                throw new IllegalArgumentException("Nama tidak boleh kosong");
            }
            
            if (noTelp == null || noTelp.trim().isEmpty()) {
                throw new IllegalArgumentException("Nomor telepon tidak boleh kosong");
            }
            
            Date tanggal = parseTanggal(tanggalStr);
            
            DataPekerjaModel pekerja = DataPekerjaModel.findById(id);
            if (pekerja != null) {
                pekerja.setNama(nama);
                pekerja.setMulaiBekerja(tanggal);
                pekerja.setNoTelp(noTelp);
                pekerja.setAlamat(alamat);

                if (pekerja.save()) {
                    view.showMessage("Data pekerja berhasil diperbarui");
                    loadData();
                } else {
                    view.showError("Gagal memperbarui data pekerja");
                }
            } else {
                view.showError("Data pekerja tidak ditemukan");
            }
        } catch (ParseException e) {
            view.showError("Format tanggal salah!\n" + e.getMessage());
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        } catch (Exception e) {
            view.showError("Terjadi kesalahan: " + e.getMessage());
        }
    }

    public void deletePekerja(int id) {
        try {
            if (view.confirmAction("Yakin ingin menghapus data pekerja ini?")) {
                DataPekerjaModel pekerja = DataPekerjaModel.findById(id);
                if (pekerja != null && pekerja.delete()) {
                    view.showMessage("Data pekerja berhasil dihapus");
                    loadData();
                    view.clearForm();
                } else {
                    view.showError("Gagal menghapus data pekerja");
                }
            }
        } catch (Exception e) {
            view.showError("Terjadi kesalahan: " + e.getMessage());
        }
    }

    public void prepareUpdateForm(int id) {
        try {
            DataPekerjaModel pekerja = DataPekerjaModel.findById(id);
            if (pekerja != null) {
                view.prepareUpdateForm(
                    pekerja.getNama(),
                    dateFormat.format(pekerja.getMulaiBekerja()),
                    pekerja.getNoTelp(),
                    pekerja.getAlamat()
                );
            } else {
                view.showError("Data pekerja tidak ditemukan");
            }
        } catch (Exception e) {
            view.showError("Terjadi kesalahan: " + e.getMessage());
        }
    }
}