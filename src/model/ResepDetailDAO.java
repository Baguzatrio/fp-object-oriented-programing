package model;

import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResepDetailDAO {
    public List<ResepDetailModel> getByResepId(int resepId) {
        List<ResepDetailModel> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT rd.id, bb.nama, rd.jumlah, bb.satuan FROM resep_detail rd " +
                         "JOIN bahan_baku bb ON rd.bahan_baku_id = bb.id WHERE rd.resep_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, resepId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ResepDetailModel r = new ResepDetailModel();
                r.setId(rs.getInt(1));
                r.setNamaBahan(rs.getString(2));
                r.setJumlah(rs.getDouble(3));
                r.setSatuan(rs.getString(4));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(int resepId, int bahanBakuId, double jumlah) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO resep_detail (resep_id, bahan_baku_id, jumlah) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, resepId);
            stmt.setInt(2, bahanBakuId);
            stmt.setDouble(3, jumlah);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean insertResepDetail(int resepId, int bahanBakuId, double jumlah) {
    String sql = "INSERT INTO resep_detail (resep_id, bahan_baku_id, jumlah) VALUES (?, ?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, resepId);
        ps.setInt(2, bahanBakuId);
        ps.setDouble(3, jumlah);
        ps.executeUpdate();
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

}
