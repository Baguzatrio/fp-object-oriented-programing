package controller;

import view.page0;
import view.page1;
import view.page2;

public class page0controller {
    private page0 view;
    
    public page0controller(page0 view) {
        this.view = view;
        
        this.view.addLoginListener(e -> bukaLogin());
        this.view.addSignupListener(e -> bukaSignup());
    }
    
    private void bukaLogin() {
        new page1().setVisible(true);
        view.dispose();
    }
    
    private void bukaSignup()  {
        new page2().setVisible(true);
        view.dispose();
    }
}
