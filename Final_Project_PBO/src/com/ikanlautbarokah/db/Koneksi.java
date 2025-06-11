/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ikanlautbarokah.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Satrio Aji
 */
public class Koneksi {
    // URL koneksi ke database Anda
    // Format: jdbc:mysql://<host>:<port>/<nama_database>
    // Contoh: localhost:3306 adalah host dan port default untuk MySQL
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ikanlautbarokah"; 
    
    // Username untuk mengakses database MySQL Anda
    private static final String USER = "root"; // Ganti dengan username database Anda (umumnya 'root' untuk XAMPP/WAMP)
    
    // Password untuk user database Anda
    private static final String PASSWORD = ""; // Ganti dengan password database Anda (umumnya kosong untuk XAMPP/WAMP)

    /**
     * Metode untuk mendapatkan objek Connection ke database.
     * @return Objek Connection yang terhubung ke database.
     * @throws SQLException Jika terjadi error saat mencoba terhubung ke database.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Memuat driver JDBC MySQL
            // Ini diperlukan untuk register driver ke DriverManager
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Membuat koneksi ke database menggunakan URL, username, dan password
            System.out.println("Mencoba koneksi ke database..."); // Pesan debug
            Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            System.out.println("Koneksi database berhasil!"); // Pesan debug
            return conn;
        } catch (ClassNotFoundException e) {
            // Tangani jika driver JDBC tidak ditemukan
            System.err.println("MySQL JDBC Driver tidak ditemukan!"); // Pesan error
            e.printStackTrace();
            throw new SQLException("MySQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            // Tangani error SQL lainnya (misalnya, database tidak berjalan, kredensial salah)
            System.err.println("Gagal terhubung ke database!"); // Pesan error
            e.printStackTrace();
            throw e; // Lemparkan kembali SQLException agar ditangani oleh pemanggil (misal: Controller)
        }
    }
    
    // Anda juga bisa menambahkan metode main untuk menguji koneksi secara terpisah
    public static void main(String[] args) {
        try {
            Connection connection = Koneksi.getConnection();
            if (connection != null) {
                System.out.println("Koneksi berhasil!");
                connection.close(); // Tutup koneksi setelah pengujian
            }
        } catch (SQLException e) {
            System.err.println("Koneksi gagal: " + e.getMessage());
        }
    }
}

