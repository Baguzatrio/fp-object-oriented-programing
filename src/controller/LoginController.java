package controller;

import model.LoginUser;
import model.User;
import view.Login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class LoginController {
    public Login view;
    public LoginUser login;
   
    public LoginController(Login view) {
        this.view = view;
        this.login = new LoginUser();

        view.addLoginnListener(new LoginListener());
        view.addRegisterListener(new RegisterListener());
    }

    public class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();

            User user = login.getUser(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(view, "Login berhasil!");
                view.setUser(user); // kamu harus buat setUser()
                view.goToDashboard();
            } else {
                JOptionPane.showMessageDialog(view, "Username atau password salah.");
            }
        }
    }
    public class RegisterListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        view.goToPage2();  // Pastikan ada method goToPage2() di page1
    }
}    
}
