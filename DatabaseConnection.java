package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Buat variabel static supaya bisa dipakai di method static
    private static final String url = "jdbc:mysql://localhost:3306/db_peci_palu";
    private static final String user = "root";
    private static final String password = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load driver MySQL
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver tidak ditemukan!", e);
        }

        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
       try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Koneksi berhasil!");
            }
        } catch (SQLException e) {
            System.out.println("Koneksi gagal!");
            e.printStackTrace();
        }

    }
}
