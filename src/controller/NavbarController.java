/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.User;
import view.*;

public class NavbarController {
    public void navigateTo(String menuName, User user) {
        switch (menuName) {
            case "Dashboard" :
                new page3(user).setVisible(true);
                break;
            case "Produk Olahan" :
                new page3(user).setVisible(true);
                break;
            case "Produksi" :
                new page3(user).setVisible(true);
                break;
            case "Pemasaran" :
                new Distribusi(user).setVisible(true);
                break;
            case "Bahan Baku" :
                new page3(user).setVisible(true);
                break;
            case "Penyimpanan Resep" :
                new page3(user).setVisible(true);
                break;
            case "Perhitungan Upah" :
                new page3(user).setVisible(true);
                break;
            case "Laporan Keuangan" :
                new page3(user).setVisible(true);
                break;
            default:
                System.out.println("Menu tidak dikenali: " + menuName);
        }
    }
}
