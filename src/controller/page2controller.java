package controller;

import model.User;
import model.UserModel;

import javax.swing.JOptionPane;

public class page2controller {
    private UserModel userModel;

    public page2controller() {
        userModel = new UserModel();
    }

    public void registerUser(String nama, String username, String password, String confirmPassword) {
        if (nama.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "Password tidak cocok!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User user = new User(nama, username, password);
            boolean success = userModel.insertUser(user);
            if (success) {
                JOptionPane.showMessageDialog(null, "Akun berhasil didaftarkan!");
            } else {
                JOptionPane.showMessageDialog(null, "Gagal mendaftarkan akun!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }    
}
