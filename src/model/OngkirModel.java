package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OngkirModel {
    private Connection conn;

    public OngkirModel() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    public List<Ongkir> getAllOngkir() throws SQLException {
        List<Ongkir> ongkirList = new ArrayList<>();
        String sql = "SELECT id, area, biaya FROM ongkir ORDER BY area";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ongkirList.add(new Ongkir(
                    rs.getInt("id"),
                    rs.getString("area"),
                    rs.getInt("biaya")
                ));
            }
        }
        return ongkirList;
    }

    public void tambahOngkir(String area, int biaya) throws SQLException {
        String sql = "INSERT INTO ongkir (area, biaya) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, area);
            stmt.setInt(2, biaya);
            stmt.executeUpdate();
        }
    }

    public static class Ongkir {
        private int id;
        private String area;
        private int biaya;

        public Ongkir(int id, String area, int biaya) {
            this.id = id;
            this.area = area;
            this.biaya = biaya;
        }

        // Getter methods
        public int getId() { return id; }
        public String getArea() { return area; }
        public int getBiaya() { return biaya; }
    }
}