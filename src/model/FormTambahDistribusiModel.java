package model;

import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class FormTambahDistribusiModel {
    private Connection conn;
    private Map<String, ProdukOlahan> produkMap = new LinkedHashMap<>();
    private Map<String, Customer> customerMap = new HashMap<>();

    public class Customer {
        private String id;
        private String alamat;

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

    public class ProdukOlahan {
        private String nama;
        private int harga;
        private int stok;

        public ProdukOlahan(String nama, int harga, int stok) {
            this.nama = nama;
            this.harga = harga;
            this.stok = stok;
        }

        public String getNama() {
            return nama;
        }

        public int getHarga() {
            return harga;
        }

        public int getStok() {
            return stok;
        }
    }

    public FormTambahDistribusiModel() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
        loadProdukOlahan();
    }

    private void loadProdukOlahan() throws SQLException {
        produkMap.clear();
        String sql = "SELECT nama_produk, harga, stok FROM produk_olahan WHERE stok > 0";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String nama = rs.getString("nama_produk");
                int harga = rs.getInt("harga");
                int stok = rs.getInt("stok");
                produkMap.put(nama, new ProdukOlahan(nama, harga, stok));
            }
        }
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
        try {
            conn.setAutoCommit(false);
            
            // 1. Validate stock first
            validateStockAvailability(items);
            
            // 2. Save main distribusi record
            saveMainDistribution(nomorNota, idCustomer, namaCustomer, alamat, status, tanggal, total, dibayar, kembalian);
            
            // 3. Save details and update stock
            saveDistributionDetailsAndUpdateStock(nomorNota, items);
            
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            loadProdukOlahan(); // Refresh product data after update
        }
    }

    private void validateStockAvailability(List<Map<String, Object>> items) throws SQLException {
        for (Map<String, Object> item : items) {
            String produk = (String) item.get("produk");
            int jumlah = (Integer) item.get("jumlah");
            int stokTersedia = getCurrentStockFromDB(produk);
            
            if (jumlah > stokTersedia) {
                throw new SQLException("Stok " + produk + " tidak mencukupi. Stok tersedia: " + stokTersedia);
            }
        }
    }

    private int getCurrentStockFromDB(String namaProduk) throws SQLException {
        String sql = "SELECT stok FROM produk_olahan WHERE nama_produk = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, namaProduk);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("stok") : 0;
            }
        }
    }

    private void saveMainDistribution(String nomorNota, String idCustomer, String namaCustomer,
                                    String alamat, String status, String tanggal, int total,
                                    int dibayar, int kembalian) throws SQLException {
        String sql = "INSERT INTO distribusi (no_nota, id_customer, nama_customer, alamat, status, tanggal, total, dibayar, kembalian) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
    }

    private void saveDistributionDetailsAndUpdateStock(String nomorNota, List<Map<String, Object>> items) throws SQLException {
        String detailSql = "INSERT INTO detail_distribusi (no_nota, nama_produk, harga, jumlah, subtotal) " +
                          "VALUES (?, ?, ?, ?, ?)";
        String updateStokSql = "UPDATE produk_olahan SET stok = stok - ? WHERE nama_produk = ?";
        
        try (PreparedStatement detailStmt = conn.prepareStatement(detailSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateStokSql)) {
            
            for (Map<String, Object> item : items) {
                String produk = (String) item.get("produk");
                int jumlah = (Integer) item.get("jumlah");
                
                // Save detail
                detailStmt.setString(1, nomorNota);
                detailStmt.setString(2, produk);
                detailStmt.setInt(3, (Integer) item.get("harga"));
                detailStmt.setInt(4, jumlah);
                detailStmt.setInt(5, (Integer) item.get("subtotal"));
                detailStmt.addBatch();
                
                // Update stock
                updateStmt.setInt(1, jumlah);
                updateStmt.setString(2, produk);
                updateStmt.addBatch();
            }
            
            detailStmt.executeBatch();
            updateStmt.executeBatch();
        }
    }

    public Map<String, ProdukOlahan> getProdukMap() {
        return Collections.unmodifiableMap(produkMap);
    }

    public int getStokProduk(String namaProduk) {
        try {
            return getCurrentStockFromDB(namaProduk);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}