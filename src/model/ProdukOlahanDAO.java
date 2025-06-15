package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdukOlahanDAO {
    // Method untuk mendapatkan semua produk olahan
    public List<ProdukOlahanModel> getAllProduk() throws SQLException {
        List<ProdukOlahanModel> list = new ArrayList<>();
        String sql = "SELECT id, nama_produk, ukuran_kemasan, stok, harga, upah FROM produk_olahan";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ProdukOlahanModel produk = new ProdukOlahanModel(
                    rs.getInt("id"),
                    rs.getString("nama_produk"),
                    rs.getInt("ukuran_kemasan"),
                    rs.getInt("stok"),
                    rs.getInt("harga"),
                    rs.getInt("upah")
                );
                list.add(produk);
            }
        }
        return list;
    }

    // Method untuk menambahkan produk olahan baru
    public boolean insertProduk(ProdukOlahanModel produk) throws SQLException {
        String sql = "INSERT INTO produk_olahan (nama_produk, ukuran_kemasan, harga, upah) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, produk.getNamaProduk());
            stmt.setInt(2, produk.getUkuranKemasan());
            stmt.setInt(3, produk.getHarga());
            stmt.setInt(4, produk.getUpah());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        produk.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    // Di method updateStokFromProduction
public boolean updateStokFromProduction(int produkId, int jumlahKemasan) throws SQLException {
    String sql = "UPDATE produk_olahan SET stok = COALESCE(stok, 0) + ? WHERE id = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, jumlahKemasan);
        stmt.setInt(2, produkId);
        
        System.out.println("[DEBUG] Executing: " + sql.replace("?", String.valueOf(jumlahKemasan))
                       .replace("?", String.valueOf(produkId)));
        
        int affectedRows = stmt.executeUpdate();
        System.out.println("[DEBUG] Affected rows: " + affectedRows);
        
        return affectedRows > 0;
    }
}

    // Method untuk mengurangi stok saat distribusi
    public boolean reduceStokFromDistribution(String namaProduk, int jumlah) throws SQLException {
        String sql = "UPDATE produk_olahan SET stok = stok - ? WHERE nama_produk = ? AND stok >= ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, jumlah);
            stmt.setString(2, namaProduk);
            stmt.setInt(3, jumlah);
            return stmt.executeUpdate() > 0;
        }
    }

    // Method untuk mendapatkan stok berdasarkan nama produk
    public int getStokByNamaProduk(String namaProduk) throws SQLException {
        String sql = "SELECT stok FROM produk_olahan WHERE nama_produk = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, namaProduk);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("stok") : 0;
            }
        }
    }

    // Method untuk membuat produk olahan beserta resepnya
    public boolean createProdukOlahanWithResep(ProdukOlahanModel produk) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Insert produk olahan
            String sqlProduk = "INSERT INTO produk_olahan (nama_produk, ukuran_kemasan, harga, upah) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtProduk = conn.prepareStatement(sqlProduk, Statement.RETURN_GENERATED_KEYS);
            stmtProduk.setString(1, produk.getNamaProduk());
            stmtProduk.setInt(2, produk.getUkuranKemasan());
            stmtProduk.setInt(3, produk.getHarga());
            stmtProduk.setInt(4, produk.getUpah());
            
            int affectedRows = stmtProduk.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            // 2. Ambil ID produk yang baru dibuat
            ResultSet generatedKeys = stmtProduk.getGeneratedKeys();
            if (!generatedKeys.next()) {
                conn.rollback();
                return false;
            }
            int produkId = generatedKeys.getInt(1);

            // 3. Buat resep otomatis
            String sqlResep = "INSERT INTO resep (nama_produk, produk_olahan_id) VALUES (?, ?)";
            PreparedStatement stmtResep = conn.prepareStatement(sqlResep);
            stmtResep.setString(1, produk.getNamaProduk());
            stmtResep.setInt(2, produkId);
            stmtResep.executeUpdate();
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) {}
            }
        }
    }

    // Method untuk sinkronisasi produk existing ke resep
    public boolean syncExistingProdukToResep() throws SQLException {
        String sql = "INSERT INTO resep (nama_produk, produk_olahan_id) " +
                    "SELECT nama_produk, id FROM produk_olahan " +
                    "WHERE id NOT IN (SELECT produk_olahan_id FROM resep WHERE produk_olahan_id IS NOT NULL)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(sql) > 0;
        }
    }
}