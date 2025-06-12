/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import model.DatabaseConnection;
import model.ResepModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResepDAO {

    public List<ResepModel> getAllResep() {
        List<ResepModel> list = new ArrayList<>();
        String sql = "SELECT * FROM resep";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ResepModel resep = new ResepModel();
                resep.setId(rs.getInt("id"));
                resep.setNamaProduk(rs.getString("nama_produk"));
                list.add(resep);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(ResepModel resep) {
        String sql = "INSERT INTO resep (nama_produk) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resep.getNamaProduk());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
