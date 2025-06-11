/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.controller;

import com.ikanlautbarokah.model.User;
import com.ikanlautbarokah.model.UserModel;
import com.ikanlautbarokah.view.LoginFrame;
import com.ikanlautbarokah.view.DashboardFrame; // Import DashboardFrame

import javax.swing.JOptionPane;
import java.sql.SQLException;

/**
 *
 * @author Satrio Aji
 */
public class LoginController {
    private UserModel userModel;
    private LoginFrame loginFrame;

    public LoginController(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        this.userModel = new UserModel();
    }

    public void handleLogin(String username, String password) {
        try {
            User user = userModel.authenticate(username, password);

            if (user != null) {
                JOptionPane.showMessageDialog(loginFrame, "Login berhasil sebagai " + user.getRole());
                // Navigasi ke DashboardFrame
                DashboardFrame dashboard = new DashboardFrame();
                dashboard.setVisible(true);
                loginFrame.dispose(); // Tutup frame login
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Username atau password salah.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(loginFrame, "Error koneksi database: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(loginFrame, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
