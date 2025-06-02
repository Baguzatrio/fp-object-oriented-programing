package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UpahPekerjaModel {
    private Connection conn;

    public UpahPekerjaModel(Connection conn) {
        this.conn = conn;
    }

    public List<Object[]> getDataUpah() throws SQLException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT tanggal, id_pekerja, nama, produk, jumlah, upah_per_unit, total_upah FROM upah_pekerja";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                data.add(new Object[]{
                    rs.getString("tanggal"),
                    rs.getInt("id_pekerja"),
                    rs.getString("nama"),
                    rs.getString("produk"),
                    rs.getInt("jumlah"),
                    rs.getInt("upah_per_unit"),
                    rs.getInt("total_upah")
                });
            }
        }
        return data;
    }

    public Object[] getDetailPekerja(int idPekerja) throws SQLException {
        String sql = "SELECT * FROM pekerja WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPekerja);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Object[]{
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("alamat"),
                    rs.getString("no_telp"),
                    rs.getString("tanggal_bergabung")
                };
            }
        }
        return null;
    }
}
