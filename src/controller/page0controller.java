package controller;

import view.Landing;
import view.Login;
import view.Register;

public class page0controller {
    private Landing view;
    
    public page0controller(Landing view) {
        this.view = view;
        
        this.view.addLoginListener(e -> bukaLogin());
        this.view.addSignupListener(e -> bukaSignup());
    }
    
    private void bukaLogin() {
    Login login = new Login();
    new LoginController(login); // Add this line to attach controller
    login.setVisible(true);
    view.dispose();
}
    
    private void bukaSignup()  {
        new Register().setVisible(true);
        view.dispose();
    }
}
