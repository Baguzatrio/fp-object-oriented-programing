package model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PengeluaranLainModel {
    private final Connection connection;

    public PengeluaranLainModel(Connection connection) throws SQLException {
        this.connection = connection;
        createTableIfNotExists();
    }

    // 1. Inisialisasi Database
    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS pengeluaran_lain (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "tanggal DATE NOT NULL, " +
                     "jenis VARCHAR(50) NOT NULL, " +
                     "kategori VARCHAR(50) NOT NULL, " +
                     "jumlah INT NOT NULL, " +
                     "keterangan TEXT, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_tanggal ON pengeluaran_lain(tanggal)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_jenis ON pengeluaran_lain(jenis)");
        }
    }

    // 2. Operasi CRUD
    public void tambahPengeluaran(LocalDate tanggal, String jenis, String kategori, 
                                int jumlah, String keterangan) throws SQLException {
        String sql = "INSERT INTO pengeluaran_lain (tanggal, jenis, kategori, jumlah, keterangan) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(tanggal));
            pstmt.setString(2, jenis);
            pstmt.setString(3, kategori);
            pstmt.setInt(4, jumlah);
            pstmt.setString(5, keterangan);
            pstmt.executeUpdate();
        }
    }

    public List<Pengeluaran> getAllPengeluaran() throws SQLException {
        List<Pengeluaran> daftarPengeluaran = new ArrayList<>();
        String sql = "SELECT * FROM pengeluaran_lain ORDER BY tanggal DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                daftarPengeluaran.add(new Pengeluaran(
                    rs.getInt("id"),
                    rs.getDate("tanggal").toLocalDate(),
                    rs.getString("jenis"),
                    rs.getString("kategori"),
                    rs.getInt("jumlah"),
                    rs.getString("keterangan")
                ));
            }
        }
        return daftarPengeluaran;
    }

    public void updatePengeluaran(int id, LocalDate tanggal, String jenis, 
                                String kategori, int jumlah, String keterangan) throws SQLException {
        String sql = "UPDATE pengeluaran_lain SET tanggal = ?, jenis = ?, kategori = ?, " +
                     "jumlah = ?, keterangan = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(tanggal));
            pstmt.setString(2, jenis);
            pstmt.setString(3, kategori);
            pstmt.setInt(4, jumlah);
            pstmt.setString(5, keterangan);
            pstmt.setInt(6, id);
            pstmt.executeUpdate();
        }
    }

    public void hapusPengeluaran(int id) throws SQLException {
        String sql = "DELETE FROM pengeluaran_lain WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // 3. Operasi Khusus
    public int getTotalPengeluaran(LocalDate mulai, LocalDate sampai) throws SQLException {
        String sql = "SELECT SUM(jumlah) AS total FROM pengeluaran_lain " +
                     "WHERE tanggal BETWEEN ? AND ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(mulai));
            pstmt.setDate(2, Date.valueOf(sampai));
            
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt("total") : 0;
        }
    }
    // Filter data
public List<Pengeluaran> filterByTanggal(LocalDate mulai, LocalDate sampai) throws SQLException {
    List<Pengeluaran> result = new ArrayList<>();
    String sql = "SELECT * FROM pengeluaran_lain WHERE tanggal BETWEEN ? AND ? ORDER BY tanggal DESC";
    
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setDate(1, Date.valueOf(mulai));
        pstmt.setDate(2, Date.valueOf(sampai));
        
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            result.add(new Pengeluaran(
                rs.getInt("id"),
                rs.getDate("tanggal").toLocalDate(),
                rs.getString("jenis"),
                rs.getString("kategori"),
                rs.getInt("jumlah"),
                rs.getString("keterangan")
            ));
        }
    }
    return result;
}

public List<Pengeluaran> filterByJenis(String jenis) throws SQLException {
    List<Pengeluaran> result = new ArrayList<>();
    String sql = "SELECT * FROM pengeluaran_lain WHERE jenis = ? ORDER BY tanggal DESC";
    
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, jenis);
        
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            result.add(new Pengeluaran(
                rs.getInt("id"),
                rs.getDate("tanggal").toLocalDate(),
                rs.getString("jenis"),
                rs.getString("kategori"),
                rs.getInt("jumlah"),
                rs.getString("keterangan")
            ));
        }
    }
    return result;
}
}