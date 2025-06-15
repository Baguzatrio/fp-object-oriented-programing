package model;
import controller.*;
import model.*;

public class User {
    private String username;
    private String password;
    private String nama;
    private boolean drawerVisible;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public User(String nama, String username, String password) {
        this.nama = nama;
        this.username = username;
        this.password = password;
    }

    public String getUsername() { 
        return username; 
    }
    public String getPassword() {
        return password; 
    }
    public String getNama() {
        return nama != null ? nama : username; // Fallback ke username jika nama null
    }
    public boolean isDrawerVisible() {
        return drawerVisible;
    }
    public void setDrawerVisible(boolean drawerVisible) {
        this.drawerVisible = drawerVisible;
    }
}

