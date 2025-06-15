package model;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.time.temporal.TemporalAdjusters;

public class LaporanKeuanganModel {
    private static final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy");
    private static final DateTimeFormatter weekFormatter = DateTimeFormatter.ofPattern("'Minggu' w, MMM yyyy");
    
    public List<Map<String, Object>> fetchDailyData(LocalDate date) {
        String query = "SELECT tanggal, jenis, kategori, jumlah " +
                      "FROM laporan_keuangan " +
                      "WHERE DATE(tanggal) = ? " +
                      "ORDER BY tanggal DESC";
        return fetchDataWithDate(query, java.sql.Date.valueOf(date));
    }

    public List<Map<String, Object>> fetchWeeklyData(int week, int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1)
            .with(WeekFields.ISO.weekOfYear(), week)
            .with(TemporalAdjusters.previousOrSame(WeekFields.ISO.getFirstDayOfWeek()));
        
        LocalDate endDate = startDate.plusDays(6);
        
        String query = "SELECT tanggal, jenis, kategori, jumlah " +
                      "FROM laporan_keuangan " +
                      "WHERE DATE(tanggal) BETWEEN ? AND ? " +
                      "ORDER BY tanggal";
        
        return fetchDataWithDateRange(query, startDate, endDate);
    }

    public List<Map<String, Object>> fetchMonthlyData(int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        
        String query = "SELECT tanggal, jenis, kategori, jumlah " +
                      "FROM laporan_keuangan " +
                      "WHERE DATE(tanggal) BETWEEN ? AND ? " +
                      "ORDER BY tanggal";
        
        return fetchDataWithDateRange(query, startDate, endDate);
    }

    public List<Map<String, Object>> fetchYearlyData(int year) {
        String query = "SELECT tanggal, jenis, kategori, jumlah " +
                      "FROM laporan_keuangan " +
                      "WHERE YEAR(tanggal) = ? " +
                      "ORDER BY tanggal";
        
        return fetchDataWithYear(query, year);
    }

    public List<Map<String, Object>> fetchAllData() {
        String query = "SELECT tanggal, jenis, kategori, jumlah " +
                      "FROM laporan_keuangan " +
                      "ORDER BY tanggal DESC";
        return fetchData(query);
    }

    public double getSaldoHariIni() {
        String query = "SELECT SUM(CASE WHEN jenis = 'pendapatan' THEN jumlah ELSE -jumlah END) as saldo " +
                      "FROM laporan_keuangan " +
                      "WHERE DATE(tanggal) = CURDATE()";
        return executeSaldoQuery(query);
    }

    public double getSaldoMingguIni() {
        String query = "SELECT SUM(CASE WHEN jenis = 'pendapatan' THEN jumlah ELSE -jumlah END) as saldo " +
                      "FROM laporan_keuangan " +
                      "WHERE YEARWEEK(tanggal, 1) = YEARWEEK(CURDATE(), 1)";
        return executeSaldoQuery(query);
    }

    public double getSaldoBulanIni() {
        String query = "SELECT SUM(CASE WHEN jenis = 'pendapatan' THEN jumlah ELSE -jumlah END) as saldo " +
                      "FROM laporan_keuangan " +
                      "WHERE YEAR(tanggal) = YEAR(CURDATE()) AND MONTH(tanggal) = MONTH(CURDATE())";
        return executeSaldoQuery(query);
    }

    public double getSaldoKeseluruhan() {
        String query = "SELECT SUM(CASE WHEN jenis = 'pendapatan' THEN jumlah ELSE -jumlah END) as saldo " +
                      "FROM laporan_keuangan";
        return executeSaldoQuery(query);
    }

    public Map<String, Double> getSaldoPerPeriode(String periode) {
        Map<String, Double> saldoMap = new LinkedHashMap<>();
        
        switch (periode) {
            case "Harian":
                String queryHarian = "SELECT DATE(tanggal) as tanggal, " +
                                    "SUM(CASE WHEN jenis = 'pendapatan' THEN jumlah ELSE -jumlah END) as saldo " +
                                    "FROM laporan_keuangan " +
                                    "GROUP BY DATE(tanggal) " +
                                    "ORDER BY DATE(tanggal) DESC";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(queryHarian);
                     ResultSet rs = stmt.executeQuery()) {
                    
                    while (rs.next()) {
                        saldoMap.put(rs.getDate("tanggal").toString(), rs.getDouble("saldo"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
                
            case "Mingguan":
                String queryMingguan = "SELECT YEAR(tanggal) as tahun, WEEK(tanggal, 1) as minggu, " +
                                      "SUM(CASE WHEN jenis = 'pendapatan' THEN jumlah ELSE -jumlah END) as saldo " +
                                      "FROM laporan_keuangan " +
                                      "GROUP BY YEAR(tanggal), WEEK(tanggal, 1) " +
                                      "ORDER BY tahun DESC, minggu DESC";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(queryMingguan);
                     ResultSet rs = stmt.executeQuery()) {
                    
                    while (rs.next()) {
                        saldoMap.put("Minggu " + rs.getInt("minggu") + ", " + rs.getInt("tahun"), 
                                    rs.getDouble("saldo"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
                
            case "Bulanan":
                String queryBulanan = "SELECT YEAR(tanggal) as tahun, MONTH(tanggal) as bulan, " +
                                     "SUM(CASE WHEN jenis = 'pendapatan' THEN jumlah ELSE -jumlah END) as saldo " +
                                     "FROM laporan_keuangan " +
                                     "GROUP BY YEAR(tanggal), MONTH(tanggal) " +
                                     "ORDER BY tahun DESC, bulan DESC";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(queryBulanan);
                     ResultSet rs = stmt.executeQuery()) {
                    
                    while (rs.next()) {
                        saldoMap.put(YearMonth.of(rs.getInt("tahun"), rs.getInt("bulan")).format(monthFormatter), 
                                    rs.getDouble("saldo"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
                
            case "Tahunan":
                String queryTahunan = "SELECT YEAR(tanggal) as tahun, " +
                                     "SUM(CASE WHEN jenis = 'pendapatan' THEN jumlah ELSE -jumlah END) as saldo " +
                                     "FROM laporan_keuangan " +
                                     "GROUP BY YEAR(tanggal) " +
                                     "ORDER BY tahun DESC";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(queryTahunan);
                     ResultSet rs = stmt.executeQuery()) {
                    
                    while (rs.next()) {
                        saldoMap.put(String.valueOf(rs.getInt("tahun")), rs.getDouble("saldo"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
        
        return saldoMap;
    }

    public void insertTransaksi(String jenis, String kategori, double jumlah) {
        String query = "INSERT INTO laporan_keuangan (tanggal, jenis, kategori, jumlah) " +
                       "VALUES (CURRENT_TIMESTAMP, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, jenis);
            stmt.setString(2, kategori);
            stmt.setDouble(3, jumlah);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertTransaksiDistribusi(double jumlah) {
        insertTransaksi("pendapatan", "distribusi", jumlah);
    }

    public void insertTransaksiUpahPekerja(double jumlah) {
        insertTransaksi("pengeluaran", "upah pekerja", jumlah);
    }

    public void insertTransaksiBahanBaku(double jumlah) {
        insertTransaksi("pengeluaran", "bahan baku", jumlah);
    }
    
    public void insertTransaksiPengeluaranLain(double jumlah) {
    insertTransaksi("pengeluaran", "pengeluaran lain", jumlah);
    }

    public double getTotalPendapatan(List<Map<String, Object>> data) {
        return data.stream()
                .filter(row -> "pendapatan".equalsIgnoreCase((String) row.get("jenis")))
                .mapToDouble(row -> ((Number) row.get("jumlah")).doubleValue())
                .sum();
    }

    public double getTotalPengeluaran(List<Map<String, Object>> data) {
        return data.stream()
                .filter(row -> "pengeluaran".equalsIgnoreCase((String) row.get("jenis")))
                .mapToDouble(row -> ((Number) row.get("jumlah")).doubleValue())
                .sum();
    }

    public double getLaba(List<Map<String, Object>> data) {
        return getTotalPendapatan(data) - getTotalPengeluaran(data);
    }

    private List<Map<String, Object>> fetchData(String query) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("tanggal", rs.getDate("tanggal"));
                row.put("jenis", rs.getString("jenis"));
                row.put("kategori", rs.getString("kategori"));
                row.put("jumlah", rs.getDouble("jumlah"));
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<Map<String, Object>> fetchDataWithDate(String query, java.sql.Date date) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setDate(1, date);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("tanggal", rs.getDate("tanggal"));
                row.put("jenis", rs.getString("jenis"));
                row.put("kategori", rs.getString("kategori"));
                row.put("jumlah", rs.getDouble("jumlah"));
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<Map<String, Object>> fetchDataWithDateRange(String query, LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("tanggal", rs.getDate("tanggal"));
                row.put("jenis", rs.getString("jenis"));
                row.put("kategori", rs.getString("kategori"));
                row.put("jumlah", rs.getDouble("jumlah"));
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<Map<String, Object>> fetchDataWithYear(String query, int year) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("tanggal", rs.getDate("tanggal"));
                row.put("jenis", rs.getString("jenis"));
                row.put("kategori", rs.getString("kategori"));
                row.put("jumlah", rs.getDouble("jumlah"));
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private double executeSaldoQuery(String query) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble("saldo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}