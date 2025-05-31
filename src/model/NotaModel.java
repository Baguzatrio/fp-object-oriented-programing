package model;

import java.sql.*;
import java.util.*;

public class NotaModel {
    private Connection conn;

    public NotaModel(Connection conn) {
        this.conn = conn;
    }

    public Map<String, String> getDataNota(String nomorNota) throws SQLException {
        Map<String, String> data = new HashMap<>();
        String query = "SELECT * FROM nota WHERE nomor_nota = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, nomorNota);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            data.put("nama", rs.getString("nama_customer"));
            data.put("alamat", rs.getString("alamat"));
            data.put("tanggal", rs.getString("tanggal"));
            data.put("total", rs.getString("total"));
            data.put("bayar", rs.getString("bayar"));
            data.put("kembali", rs.getString("kembali"));
        }

        rs.close();
        ps.close();
        return data;
    }

    public List<String[]> getBarangNota(String nomorNota) throws SQLException {
        List<String[]> barang = new ArrayList<>();
        String query = "SELECT nama_barang, jumlah, harga, subtotal FROM detail_nota WHERE nomor_nota = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, nomorNota);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            barang.add(new String[]{
                rs.getString("nama_barang"),
                rs.getString("jumlah"),
                rs.getString("harga"),
                rs.getString("subtotal")
            });
        }

        rs.close();
        ps.close();
        return barang;
    }
}

