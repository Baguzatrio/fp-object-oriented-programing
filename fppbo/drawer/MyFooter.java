package fppbo.drawer;

import raven.drawer.component.footer.SimpleFooterData;

public class MyFooter extends SimpleFooterData {

    private MyFooterPanel panel;

    public MyFooter() {
        panel = new MyFooterPanel();
    }

    public MyFooterPanel getFooterPanel() {
        return panel;
    }
}
