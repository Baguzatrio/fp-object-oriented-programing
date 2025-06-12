package controller;

import view.page0;
import view.Login;
import view.Register;

public class page0controller {
    private page0 view;
    
    public page0controller(page0 view) {
        this.view = view;
        
        this.view.addLoginListener(e -> bukaLogin());
        this.view.addSignupListener(e -> bukaSignup());
    }
    
    private void bukaLogin() {
        new Login().setVisible(true);
        view.dispose();
    }
    
    private void bukaSignup()  {
        new Register().setVisible(true);
        view.dispose();
    }
}
