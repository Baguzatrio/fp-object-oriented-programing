/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.ProdukOlahanDAO;
import model.ProdukOlahanModel;

import java.util.*;

public class ProdukOlahanController {
    private ProdukOlahanDAO dao;

    public ProdukOlahanController() {
        dao = new ProdukOlahanDAO();
    }

    public List<ProdukOlahanModel> getAllProduk() {
        return dao.getAllProduk();
    }

    public boolean tambahProduk(ProdukOlahanModel produk) {
        return dao.insertProduk(produk);
    }

    public Map<String, Integer> getResepMap() {
        return dao.getResepMap();
    }
}

