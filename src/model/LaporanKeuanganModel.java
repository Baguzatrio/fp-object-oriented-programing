package model;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;

public class LaporanKeuanganModel {
    private static final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy");
    
    public List<Map<String, Object>> fetchDailyData() {
        String query = "SELECT tanggal, jenis, kategori, jumlah " +
                      "FROM laporan_keuangan " +
                      "ORDER BY tanggal DESC";
        return fetchData(query);
    }

    public List<Map<String, Object>> fetchWeeklyData() {
        String query = "SELECT tanggal, jenis, kategori, jumlah FROM laporan_keuangan ORDER BY tanggal";
        List<Map<String, Object>> rawData = fetchData(query);
        
        Map<String, Map<String, Double>> weeklyData = new LinkedHashMap<>();
        
        for (Map<String, Object> row : rawData) {
            java.util.Date tanggal = (java.util.Date) row.get("tanggal");
            String jenis = (String) row.get("jenis");
            String kategori = (String) row.get("kategori");
            double jumlah = ((Number) row.get("jumlah")).doubleValue();
            
            LocalDate date = tanggal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String weekKey = "Minggu " + date.get(WeekFields.ISO.weekOfYear());
            
            String dataKey = jenis + "|" + kategori;
            weeklyData.putIfAbsent(weekKey, new HashMap<>());
            weeklyData.get(weekKey).merge(dataKey, jumlah, Double::sum);
        }
        
        return formatGroupedData(weeklyData);
    }

    public List<Map<String, Object>> fetchMonthlyData() {
        String query = "SELECT YEAR(tanggal) as tahun, MONTH(tanggal) as bulan, " +
                      "jenis, kategori, SUM(jumlah) as jumlah " +
                      "FROM laporan_keuangan " +
                      "GROUP BY YEAR(tanggal), MONTH(tanggal), jenis, kategori " +
                      "ORDER BY tahun DESC, bulan DESC";
        
        List<Map<String, Object>> result = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                int tahun = rs.getInt("tahun");
                int bulan = rs.getInt("bulan");
                
                row.put("tanggal", YearMonth.of(tahun, bulan).format(monthFormatter));
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

    public List<Map<String, Object>> fetchYearlyData() {
        String query = "SELECT YEAR(tanggal) as tahun, jenis, kategori, SUM(jumlah) as jumlah " +
                      "FROM laporan_keuangan " +
                      "GROUP BY YEAR(tanggal), jenis, kategori " +
                      "ORDER BY tahun DESC";
        
        List<Map<String, Object>> result = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("tanggal", rs.getInt("tahun") + "");
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

    private List<Map<String, Object>> formatGroupedData(Map<String, Map<String, Double>> groupedData) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Map.Entry<String, Map<String, Double>> entry : groupedData.entrySet()) {
            for (Map.Entry<String, Double> dataEntry : entry.getValue().entrySet()) {
                String[] parts = dataEntry.getKey().split("\\|");
                Map<String, Object> row = new HashMap<>();
                row.put("tanggal", entry.getKey());
                row.put("jenis", parts[0]);
                row.put("kategori", parts[1]);
                row.put("jumlah", dataEntry.getValue());
                result.add(row);
            }
        }
        return result;
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
}