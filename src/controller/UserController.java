/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.*;
import model.User;
import model.DatabaseConnection;

public class UserController {
    public boolean login(User user) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getPassword());
            ResultSet rs = pst.executeQuery();

            boolean loginSukses = rs.next();

            rs.close();
            pst.close();
            conn.close();

            return loginSukses;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

