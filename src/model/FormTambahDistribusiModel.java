package model;

import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class FormTambahDistribusiModel {
    private Connection conn;
    private Map<String, Integer> produkMap = new LinkedHashMap<>();
    private Map<String, Customer> customerMap = new HashMap<>();

    public class Customer {
        public String id;
        public String alamat;

        public Customer(String id, String alamat) {
            this.id = id;
            this.alamat = alamat;
        }
        public String getId() {
        return id;
    }

    public String getAlamat() {
        return alamat;
    }
    }

    public FormTambahDistribusiModel() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
        initializeProdukMap();
    }

    private void initializeProdukMap() {
        produkMap.put("Tempura", 10000);
        produkMap.put("Bakso Ikan", 9000);
        produkMap.put("Otak-otak Ikan", 8000);
    }

    public Map<String, Customer> loadCustomerData() throws SQLException {
        customerMap.clear();
        String sql = "SELECT id_customer, nama_customer, alamat_customer FROM customer";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String id = rs.getString("id_customer");
                String nama = rs.getString("nama_customer");
                String alamat = rs.getString("alamat_customer");
                customerMap.put(nama, new Customer(id, alamat));
            }
        }
        return customerMap;
    }

    public int hitungNotaHariIni(int idCustomer) throws SQLException {
        String sql = "SELECT COUNT(*) as jumlah FROM distribusi WHERE id_customer = ? AND tanggal = CURDATE()";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCustomer);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("jumlah") + 1 : 1;
            }
        }
    }

    public String generateNomorNota(String idCustomer) throws SQLException {
        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyyMMdd");
        java.util.Date today = new java.util.Date();
        
        int urutan = hitungNotaHariIni(Integer.parseInt(idCustomer));
        return dbFormat.format(today) + "-" + idCustomer + "-" + String.format("%03d", urutan);
    }
    
    public String getTanggalHariIni() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());
    }

    public void saveDistribusi(String nomorNota, String idCustomer, String namaCustomer, 
                             String alamat, String status, String tanggal, int total, 
                             int dibayar, int kembalian, List<Map<String, Object>> items) throws SQLException {
        // Save main distribusi record
        String sql = "INSERT INTO distribusi (no_nota, id_customer, nama_customer, alamat, status, tanggal, total, dibayar, kembalian) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomorNota);
            stmt.setString(2, idCustomer);
            stmt.setString(3, namaCustomer);
            stmt.setString(4, alamat);
            stmt.setString(5, status);
            stmt.setString(6, tanggal);
            stmt.setInt(7, total);
            stmt.setInt(8, dibayar);
            stmt.setInt(9, kembalian);
            stmt.executeUpdate();
        }

        // Save detail items
        String detailSql = "INSERT INTO detail_distribusi (no_nota, nama_produk, harga, jumlah, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement detailStmt = conn.prepareStatement(detailSql)) {
            for (Map<String, Object> item : items) {
                detailStmt.setString(1, nomorNota);
                detailStmt.setString(2, (String) item.get("produk"));
                detailStmt.setInt(3, (Integer) item.get("harga"));
                detailStmt.setInt(4, (Integer) item.get("jumlah"));
                detailStmt.setInt(5, (Integer) item.get("subtotal"));
                detailStmt.addBatch();
            }
            detailStmt.executeBatch();
        }
    }

    public Map<String, Integer> getProdukMap() {
        return produkMap;
    }
}