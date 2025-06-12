package controller;

import model.NotaModel;
import view.FormPreviewNotaA4;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;

public class NotaController {
    private NotaModel model;
    private Connection conn;
    private JFrame parentFrame;

    public NotaController(Connection conn) {
        this.conn = conn;
        this.model = new NotaModel(conn);
    }

    public void tampilkanNota(String nomorNota) {
        try {
            Map<String, String> data = model.getDataNota(nomorNota);
            List<String[]> barang = model.getBarangNota(nomorNota);

            if (data.isEmpty()) {
                System.out.println("Data tidak ditemukan.");
                return;
            }

            new FormPreviewNotaA4(parentFrame,
                nomorNota,
                data.get("nama"),
                data.get("alamat"),
                data.get("tanggal"),
                data.get("total"),
                data.get("bayar"),
                data.get("kembali"),
                barang
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
