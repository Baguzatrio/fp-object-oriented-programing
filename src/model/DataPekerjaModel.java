package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataPekerjaModel {
    private int id;
    private String nama;
    private Date mulaiBekerja;
    private String noTelp;
    private String alamat;
    
    // Constructors
    public DataPekerjaModel() {}
    
    public DataPekerjaModel(int id, String nama, Date mulaiBekerja, String noTelp, String alamat) {
        this.id = id;
        this.nama = nama;
        this.mulaiBekerja = mulaiBekerja;
        this.noTelp = noTelp;
        this.alamat = alamat;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public Date getMulaiBekerja() { return mulaiBekerja; }
    public void setMulaiBekerja(Date mulaiBekerja) { this.mulaiBekerja = mulaiBekerja; }
    public String getNoTelp() { return noTelp; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    // CRUD Operations
    public boolean save() {
        return this.id == 0 ? insert() : update();
    }
    
    private boolean insert() {
        String sql = "INSERT INTO pekerja (nama, mulai_bekerja, no_telp, alamat) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            setParameters(pstmt);
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) this.id = rs.getInt(1);
                }
                return true;
            }
        } catch (SQLException ex) {
            System.err.println("Error inserting data: " + ex.getMessage());
        }
        return false;
    }
    
    private boolean update() {
        String sql = "UPDATE pekerja SET nama=?, mulai_bekerja=?, no_telp=?, alamat=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setParameters(pstmt);
            pstmt.setInt(5, this.id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error updating data: " + ex.getMessage());
        }
        return false;
    }
    
    public boolean delete() {
        String sql = "DELETE FROM pekerja WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, this.id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error deleting data: " + ex.getMessage());
        }
        return false;
    }
    
    private void setParameters(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, this.nama);
        pstmt.setDate(2, new java.sql.Date(this.mulaiBekerja.getTime()));
        pstmt.setString(3, this.noTelp);
        pstmt.setString(4, this.alamat);
    }
    
    // Static Methods
    public static List<DataPekerjaModel> getAll() {
        List<DataPekerjaModel> list = new ArrayList<>();
        String sql = "SELECT * FROM pekerja";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new DataPekerjaModel(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getDate("mulai_bekerja"),
                    rs.getString("no_telp"),
                    rs.getString("alamat")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching all data: " + ex.getMessage());
        }
        return list;
    }
    
    public static DataPekerjaModel findById(int id) {
        String sql = "SELECT * FROM pekerja WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new DataPekerjaModel(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getDate("mulai_bekerja"),
                        rs.getString("no_telp"),
                        rs.getString("alamat")
                    );
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error finding by ID: " + ex.getMessage());
        }
        return null;
    }
    
    // Utility Method for View
    public Object[] toTableRow() {
        return new Object[] {
            this.id,
            this.nama,
            this.mulaiBekerja,
            this.noTelp,
            this.alamat
        };
    }
}