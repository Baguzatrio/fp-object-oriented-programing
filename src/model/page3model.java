/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class page3model {
    private String username;
    private boolean drawerVisible;

    public page3model(String username) {
        this.username = username;
        this.drawerVisible = false;
    }

    public String getUsername() {
        return username;
    }

    public boolean isDrawerVisible() {
        return drawerVisible;
    }

    public void setDrawerVisible(boolean drawerVisible) {
        this.drawerVisible = drawerVisible;
    }
}

