package fppbo.drawer;

import raven.drawer.component.SimpleDrawerBuilder;
import raven.drawer.component.footer.SimpleFooterData;
import raven.drawer.component.header.SimpleHeaderData;
import raven.drawer.component.menu.SimpleMenuOption;
import raven.drawer.component.menu.data.MenuItem;

/**
 *
 * @author Lenovo
 */
public class MyDrawerBuilder extends SimpleDrawerBuilder{

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        return new SimpleHeaderData()
            .setTitle("Andy")
            .setDescription("Admin");
    }

    @Override
    public SimpleMenuOption getSimpleMenuOption() {
        String menus[][] = {
            {"Dashboard"},
            {"Manajemen Bahan Baku"},
            {"Manajemen Bahan Baku"},
            {"Manajemen Bahan Baku"},
            {"Manajemen Bahan Baku"},
            {"Manajemen Bahan Baku"},
            {"Manajemen Bahan Baku"},
            {"Manajemen Bahan Baku"},
            {"Manajemen Bahan Baku"}
        };
                
        String icons[] = {
            "dashbboard.svg",
            "dashbboard.svg",
            "dashbboard.svg",
            "dashbboard.svg",
            "dashbboard.svg",
            "dashbboard.svg",
            "dashbboard.svg",
            "dashbboard.svg",
            "dashbboard.svg"
        };
        
        // Konversi ke Item[]
    raven.drawer.component.menu.data.Item[] menuItems = new raven.drawer.component.menu.data.Item[menus.length];
    for (int i = 0; i < menus.length; i++) {
        menuItems[i] = new raven.drawer.component.menu.data.Item(menus[i][0], icons[i]);
    }
    
        return new SimpleMenuOption()
                .setMenus(menuItems);
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData();
    }
    
    public javax.swing.JPanel getMyFooterPanel() {
        return new MyFooterPanel();
    }
    
}
