package model;

import java.sql.*;
import java.util.*;
import javax.swing.JComboBox;

public class ProduksiDAO {
    // Gunakan connection pooling atau buat koneksi baru setiap kali
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public boolean createProduksi(ProduksiModel produksi) throws SQLException {
        String sql = "INSERT INTO produksi (tanggal, produk, id_pekerja, jumlah_batch, total_kg, jumlah_kemasan) " +
                    "VALUES (CURDATE(), ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, produksi.getProduk());
            stmt.setInt(2, produksi.getIdPekerja());
            stmt.setInt(3, produksi.getJumlahBatch());
            stmt.setDouble(4, produksi.getTotalKg());
            stmt.setInt(5, produksi.getJumlahKemasan());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                updateStokProduk(produksi.getProduk(), produksi.getJumlahKemasan());
                return true;
            }
            return false;
        }
    }
    
    private void updateStokProduk(String produk, int jumlahKemasan) throws SQLException {
        String sql = "UPDATE produk_olahan SET stok = stok + ? WHERE nama_produk = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jumlahKemasan);
            stmt.setString(2, produk);
            stmt.executeUpdate();
        }
    }
    
    public List<ProduksiModel> getAllProduksi() throws SQLException {
        List<ProduksiModel> list = new ArrayList<>();
        String sql = """
            SELECT p.id, 
                   DATE_FORMAT(p.tanggal, '%d-%m-%Y') as tanggal,
                   p.produk, 
                   p.id_pekerja, 
                   pk.nama as nama_pekerja, 
                   p.jumlah_batch, 
                   p.total_kg, 
                   p.jumlah_kemasan 
            FROM produksi p
            LEFT JOIN pekerja pk ON p.id_pekerja = pk.id
            ORDER BY p.tanggal DESC""";
            
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ProduksiModel p = new ProduksiModel(
                    rs.getInt("id"),
                    rs.getString("tanggal"),
                    rs.getString("produk"),
                    rs.getInt("id_pekerja"),
                    rs.getString("nama_pekerja"),
                    rs.getInt("jumlah_batch"),
                    rs.getDouble("total_kg"),
                    rs.getInt("jumlah_kemasan")
                );
                list.add(p);
            }
        }
        return list;
    }
    
    public Map<String, Integer> getPekerjaMap(JComboBox<String> comboBox) throws SQLException {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT id, nama FROM pekerja";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            comboBox.removeAllItems();
            while (rs.next()) {
                String nama = rs.getString("nama");
                int id = rs.getInt("id");
                comboBox.addItem(nama);
                map.put(nama, id);
            }
        }
        return map;
    }
    
    public int getUkuranKemasan(String produk) throws SQLException {
        String sql = "SELECT ukuran_kemasan FROM produk_olahan WHERE nama_produk = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produk);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("ukuran_kemasan") : 0;
        }
    }
    
    public Map<String, Integer> getProdukOlahanMap(JComboBox<String> comboBox) throws SQLException {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT nama_produk, id FROM produk_olahan ORDER BY nama_produk";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            comboBox.removeAllItems();
            while (rs.next()) {
                String nama = rs.getString("nama_produk");
                int id = rs.getInt("id");
                comboBox.addItem(nama);
                map.put(nama, id);
            }
        }
        return map;
    }

    public boolean simpanProduksi(int produkId, String namaProduk, int batch, 
                            double kg, int idPegawai) throws SQLException {
    Connection conn = null;
    try {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);

        // 1. Dapatkan dan kunci record produk_olahan (FOR UPDATE)
        int ukuranKemasan = 0;
        int stokAwal = 0;
        String sqlLock = "SELECT ukuran_kemasan, stok FROM produk_olahan WHERE id = ? FOR UPDATE";
        try (PreparedStatement stmt = conn.prepareStatement(sqlLock)) {
            stmt.setInt(1, produkId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) throw new SQLException("Produk tidak ditemukan");
            ukuranKemasan = rs.getInt("ukuran_kemasan");
            stokAwal = rs.getInt("stok");
        }

        // 2. Hitung kemasan
        int kemasan = (int) (kg * 1000 / ukuranKemasan);

        // 3. Simpan produksi
        String sqlProduksi = "INSERT INTO produksi (...) VALUES (...)";
        try (PreparedStatement stmt = conn.prepareStatement(sqlProduksi)) {
            // Set parameter
            stmt.executeUpdate();
        }

        // 4. Update stok dengan VERIFIKASI
        String sqlUpdate = "UPDATE produk_olahan SET stok = ? WHERE id = ? AND stok = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {
            stmt.setInt(1, stokAwal + kemasan);
            stmt.setInt(2, produkId);
            stmt.setInt(3, stokAwal);
            int updated = stmt.executeUpdate();
            if (updated == 0) throw new SQLException("Konflik update stok");
        }

        conn.commit();
        return true;
    } catch (SQLException e) {
        if (conn != null) conn.rollback();
        throw new SQLException("Gagal simpan produksi: " + e.getMessage());
    } finally {
        if (conn != null) {
            try { conn.setAutoCommit(true); conn.close(); } 
            catch (SQLException e) {}
        }
    }
}
    
}