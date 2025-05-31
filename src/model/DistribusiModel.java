package model;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class DistribusiModel {

    public DefaultTableModel loadDataDistribusi() {
        DefaultTableModel model = new DefaultTableModel();
        String[] kolom = {"Nomor Nota", "Nama Customer", "Lokasi Pengiriman", "Status Pembayaran", "Tanggal Kirim", "Total Harga", "Aksi"};
        model.setColumnIdentifiers(kolom);

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT d.no_nota, d.nama_customer, d.alamat, d.status, d.tanggal, d.total " +
                     "FROM distribusi d JOIN customer c ON d.id_customer = c.id_customer")) {

            while (rs.next()) {
                String nomor = rs.getString("no_nota");
                String nama = rs.getString("nama_customer");
                String lokasi = rs.getString("alamat");
                String status = rs.getString("status");
                String tanggal = rs.getString("tanggal");
                String total = "Rp " + rs.getInt("total");

                model.addRow(new Object[]{nomor, nama, lokasi, status, tanggal, total, "Lihat"});
            }

        } catch (Exception e) {
            System.out.println("Gagal load data: " + e.getMessage());
        }

        return model;
    }
}
