package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResepDAO {
    
    // 1. Method untuk mendapatkan semua resep
     public List<ResepModel> getAllResep() {
        List<ResepModel> list = new ArrayList<>();
        String sql = "SELECT id, nama_produk FROM produk_olahan ORDER BY nama_produk";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ResepModel resep = new ResepModel();
                resep.setId(rs.getInt("id"));
                resep.setNamaProduk(rs.getString("nama_produk"));
                // Produk olahan id sama dengan id karena ini sebenarnya produk olahan
                resep.setProdukOlahanId(rs.getInt("id"));
                list.add(resep);
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil data produk olahan: " + e.getMessage());
        }
        return list;
    }

    // 2. Method untuk mendapatkan resep by ID
    public ResepModel getById(int id) {
        String sql = "SELECT * FROM resep WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                ResepModel resep = new ResepModel();
                resep.setId(rs.getInt("id"));
                resep.setNamaProduk(rs.getString("nama_produk"));
                resep.setProdukOlahanId(rs.getInt("produk_olahan_id"));
                return resep;
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil resep by ID: " + e.getMessage());
        }
        return null;
    }

    // 3. Method untuk mendapatkan resep by produk_olahan_id
    public ResepModel getByProdukOlahanId(int produkOlahanId) {
        String sql = "SELECT * FROM resep WHERE produk_olahan_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, produkOlahanId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                ResepModel resep = new ResepModel();
                resep.setId(rs.getInt("id"));
                resep.setNamaProduk(rs.getString("nama_produk"));
                resep.setProdukOlahanId(rs.getInt("produk_olahan_id"));
                return resep;
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil resep by produk_olahan_id: " + e.getMessage());
        }
        return null;
    }

    // 4. Method untuk membuat resep baru
    public boolean buatResep(ResepModel resep) {
        String sql = "INSERT INTO resep (nama_produk, produk_olahan_id) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, resep.getNamaProduk());
            stmt.setInt(2, resep.getProdukOlahanId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        resep.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Gagal membuat resep: " + e.getMessage());
        }
        return false;
    }

    // 5. Method untuk update resep
    public boolean updateResep(ResepModel resep) {
        String sql = "UPDATE resep SET nama_produk = ?, produk_olahan_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, resep.getNamaProduk());
            stmt.setInt(2, resep.getProdukOlahanId());
            stmt.setInt(3, resep.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal update resep: " + e.getMessage());
        }
        return false;
    }

    // 6. Method untuk menghapus resep
    public boolean hapusResep(int id) {
        String sql = "DELETE FROM resep WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal menghapus resep: " + e.getMessage());
        }
        return false;
    }

    // 7. Method untuk mendapatkan detail bahan baku dalam resep
    public List<ResepDetailModel> getDetailBahanBaku(int resepId) {
        List<ResepDetailModel> list = new ArrayList<>();
        String sql = "SELECT rd.id, bb.nama as nama_bahan, rd.jumlah, bb.satuan " +
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
                detail.setNamaBahan(rs.getString("nama_bahan"));
                detail.setJumlah(rs.getDouble("jumlah"));
                detail.setSatuan(rs.getString("satuan"));
                list.add(detail);
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil detail bahan baku: " + e.getMessage());
        }
        return list;
    }
    public boolean isResepExist(int resepId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM resep WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, resepId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public List<ResepModel> getAllReseps() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}