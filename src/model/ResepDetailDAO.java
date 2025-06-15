package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResepDetailDAO {
 
// Method pengecekan keberadaan resep
public boolean isResepExist(int resepId) {
    String sql = "SELECT COUNT(*) FROM resep WHERE id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, resepId);
        ResultSet rs = stmt.executeQuery();
        return rs.next() && rs.getInt(1) > 0;
    } catch (SQLException e) {
        System.err.println("Error checking resep existence: " + e.getMessage());
        return false;
    }
}

public boolean isBahanBakuExist(int bahanBakuId) {
    String sql = "SELECT COUNT(*) FROM bahan_baku WHERE id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, bahanBakuId);
        ResultSet rs = stmt.executeQuery();
        return rs.next() && rs.getInt(1) > 0;
    } catch (SQLException e) {
        System.err.println("Error checking bahan baku existence: " + e.getMessage());
        return false;
    }
}
    // Update jumlah bahan
    public boolean updateJumlah(int id, double jumlah) {
        String sql = "UPDATE resep_detail SET jumlah = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, jumlah);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hapusDetail(int detailId) {
        String sql = "DELETE FROM resep_detail WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, detailId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ResepDetailModel> getDetailByResepId(int resepId) {
        List<ResepDetailModel> list = new ArrayList<>();
        String sql = "SELECT rd.id, rd.resep_id, rd.bahan_baku_id, bb.nama, rd.jumlah, bb.satuan " +
                     "FROM resep_detail rd " +
                     "JOIN bahan_baku bb ON rd.bahan_baku_id = bb.id " +
                     "WHERE rd.resep_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, resepId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ResepDetailModel detail = new ResepDetailModel();
                detail.setId(rs.getInt("id"));
                detail.setResepId(rs.getInt("resep_id"));
                detail.setBahanBakuId(rs.getInt("bahan_baku_id"));
                detail.setNamaBahan(rs.getString("nama"));
                detail.setJumlah(rs.getDouble("jumlah"));
                detail.setSatuan(rs.getString("satuan"));
                list.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean tambahDetail(int resepId, int bahanBakuId, double jumlah, String satuan) {
        String sql = "INSERT INTO resep_detail (resep_id, bahan_baku_id, jumlah) VALUES (?, ?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, resepId);
        stmt.setInt(2, bahanBakuId);
        stmt.setDouble(3, jumlah);
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Insert error: " + e.getMessage());
        return false;
    }
    }
}