package controller;

import model.LoginUser;
import model.User;
import view.page1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class LoginController {
    private page1 view;
    private LoginUser login;
   
    public LoginController(page1 view) {
        this.view = view;
        this.login = new LoginUser();

        view.addLoginnListener(new LoginListener());
        view.addRegisterListener(new RegisterListener());
    }

    class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();

            User user = login.getUser(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(view, "Login berhasil!");
                view.setUser(user); // kamu harus buat setUser()
                view.goToPage3();
            } else {
                JOptionPane.showMessageDialog(view, "Username atau password salah.");
            }
        }
    }
    class RegisterListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        view.goToPage2();  // Pastikan ada method goToPage2() di page1
    }
}
}
