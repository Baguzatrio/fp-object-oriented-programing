/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.model;

import com.ikanlautbarokah.db.Koneksi; // Pastikan package Koneksi sesuai

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author Satrio Aji
 */
public class UserModel {

    public User authenticate(String username, String password) throws SQLException {
        String query = "SELECT id, username, role FROM users WHERE username = ? AND password = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // Ingat: Untuk aplikasi produksi, password harus di-hash!

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String role = rs.getString("role");
                return new User(id, username, role);
            } else {
                return null; // Login gagal
            }
        }
    }
}
