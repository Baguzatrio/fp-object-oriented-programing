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

    // Getters and Setters remain the same
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

    // CRUD Operations remain the same
    public static List<DataPekerjaModel> getAll() {
        List<DataPekerjaModel> list = new ArrayList<>();
        String sql = "SELECT * FROM pekerja";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                DataPekerjaModel pekerja = new DataPekerjaModel();
                pekerja.setId(rs.getInt("id"));
                pekerja.setNama(rs.getString("nama"));
                pekerja.setMulaiBekerja(rs.getDate("mulai_bekerja"));
                pekerja.setNoTelp(rs.getString("no_telp"));
                pekerja.setAlamat(rs.getString("alamat"));
                list.add(pekerja);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static DataPekerjaModel findById(int id) {
        String sql = "SELECT * FROM pekerja WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    DataPekerjaModel pekerja = new DataPekerjaModel();
                    pekerja.setId(rs.getInt("id"));
                    pekerja.setNama(rs.getString("nama"));
                    pekerja.setMulaiBekerja(rs.getDate("mulai_bekerja"));
                    pekerja.setNoTelp(rs.getString("no_telp"));
                    pekerja.setAlamat(rs.getString("alamat"));
                    return pekerja;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean save() {
        String sql;
        boolean isInsert = (id == 0);
        
        if (isInsert) {
            sql = "INSERT INTO pekerja (nama, mulai_bekerja, no_telp, alamat) VALUES (?, ?, ?, ?)";
        } else {
            sql = "UPDATE pekerja SET nama = ?, mulai_bekerja = ?, no_telp = ?, alamat = ? WHERE id = ?";
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, nama);
            stmt.setDate(2, mulaiBekerja);
            stmt.setString(3, noTelp);
            stmt.setString(4, alamat);
            
            if (!isInsert) {
                stmt.setInt(5, id);
            }
            
            int affectedRows = stmt.executeUpdate();
            
            if (isInsert && affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getInt(1);
                    }
                }
            }
            
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete() {
        if (id == 0) return false;
        
        String sql = "DELETE FROM pekerja WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<DataPekerjaModel> searchByName(String name) {
        List<DataPekerjaModel> list = new ArrayList<>();
        String sql = "SELECT * FROM pekerja WHERE nama LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DataPekerjaModel pekerja = new DataPekerjaModel();
                    pekerja.setId(rs.getInt("id"));
                    pekerja.setNama(rs.getString("nama"));
                    pekerja.setMulaiBekerja(rs.getDate("mulai_bekerja"));
                    pekerja.setNoTelp(rs.getString("no_telp"));
                    pekerja.setAlamat(rs.getString("alamat"));
                    list.add(pekerja);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}