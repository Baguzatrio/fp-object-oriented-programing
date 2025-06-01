package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import model.DatabaseConnection;

public class DetailPekerjaModel {
    private Connection conn;

    public DetailPekerjaModel() {
    try {
        conn = DatabaseConnection.getConnection();
    } catch (SQLException e) {
        System.err.println("Koneksi gagal: " + e.getMessage());
    }
}

    public HashMap<String, String> getPekerjaDetail(int idPekerja) {
        HashMap<String, String> data = null;
        try {
            String sql = "SELECT nama, mulai_bekerja, no_telp, alamat FROM pekerja WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idPekerja);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                data = new HashMap<>();
                data.put("nama", rs.getString("nama"));
                data.put("mulai_bekerja", rs.getString("mulai_bekerja"));
                data.put("no_telp", rs.getString("no_telp"));
                data.put("alamat", rs.getString("alamat"));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Error getPekerjaDetail: " + e.getMessage());
        }
        return data;
    }

    public List<HashMap<String, String>> getUpahPekerja(int idPekerja) {
        List<HashMap<String, String>> list = new ArrayList<>();
        try {
            String sql = "SELECT tanggal, keterangan, jumlah FROM upah_pekerja WHERE id_pekerja = ? ORDER BY tanggal DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idPekerja);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                HashMap<String, String> item = new HashMap<>();
                item.put("tanggal", rs.getString("tanggal"));
                item.put("keterangan", rs.getString("keterangan"));
                item.put("jumlah", rs.getString("jumlah"));
                list.add(item);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Error getUpahPekerja: " + e.getMessage());
        }
        return list;
    }
}