package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UpahPekerjaModel {
    public enum FilterPeriod {
        HARIAN,  // Filter harian menampilkan semua data tanpa grouping
        MINGGUAN, // Filter mingguan group by week
        BULANAN,  // Filter bulanan group by month
        TAHUNAN 
    }
    private Connection conn;

    public UpahPekerjaModel(Connection conn) {
        this.conn = conn;
    }

    public List<Object[]> getDataUpah(FilterPeriod filter, Integer week, Integer month, Integer year) {
    List<Object[]> data = new ArrayList<>();
    String sql = buildQuery(filter, week, month, year);

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        // Set parameter tambahan berdasarkan filter
        int paramIndex = 1;
        if (filter == FilterPeriod.MINGGUAN && week != null) {
            stmt.setInt(paramIndex++, week);
        }
        if ((filter == FilterPeriod.MINGGUAN || filter == FilterPeriod.BULANAN) && month != null) {
            stmt.setInt(paramIndex++, month);
        }
        if (year != null) {
            stmt.setInt(paramIndex, year);
        }

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Object[] row = new Object[]{
                rs.getString("periode"),
                rs.getInt("id_pekerja"), // Diubah ke getInt karena di tabel tipe-nya INT
                rs.getString("nama"),
                rs.getString("produk"),
                rs.getInt("jumlah"),
                rs.getInt("upah_per_unit"),
                rs.getInt("total_upah")
            };
            data.add(row);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return data;
}
    
    private String buildQuery(FilterPeriod filter, Integer week, Integer month, Integer year) {
    String groupByClause = "";
    String selectClause = "u.tanggal AS periode"; // Default untuk harian
    String whereClause = "WHERE u.id_pekerja IS NOT NULL"; // Ganti kondisi WHERE
    
    switch (filter) {
        case HARIAN:
            break;
            
        case MINGGUAN:
            selectClause = "CONCAT('Minggu ', WEEK(u.tanggal), ' - ', DATE_FORMAT(u.tanggal, '%M %Y')) AS periode";
            groupByClause = "GROUP BY WEEK(u.tanggal), YEAR(u.tanggal), u.id_pekerja";
            
            if (week != null) {
                whereClause += " AND WEEK(u.tanggal) = ?";
            }
            if (month != null) {
                whereClause += " AND MONTH(u.tanggal) = ?";
            }
            if (year != null) {
                whereClause += " AND YEAR(u.tanggal) = ?";
            }
            break;
            
        case BULANAN:
            selectClause = "DATE_FORMAT(u.tanggal, '%M %Y') AS periode";
            groupByClause = "GROUP BY MONTH(u.tanggal), YEAR(u.tanggal), u.id_pekerja";
            
            if (month != null) {
                whereClause += " AND MONTH(u.tanggal) = ?";
            }
            if (year != null) {
                whereClause += " AND YEAR(u.tanggal) = ?";
            }
            break;
            
        case TAHUNAN:
            selectClause = "DATE_FORMAT(u.tanggal, '%Y') AS periode";
            groupByClause = "GROUP BY YEAR(u.tanggal), u.id_pekerja";
            
            if (year != null) {
                whereClause += " AND YEAR(u.tanggal) = ?";
            }
            break;
    }

    return String.format("""
        SELECT
            %s,
            u.id_pekerja,
            p.nama,
            u.produk,
            SUM(u.jumlah) AS jumlah,
            u.upah_per_unit,
            SUM(u.jumlah * u.upah_per_unit) AS total_upah
        FROM upah_pekerja u
        LEFT JOIN pekerja p ON u.id_pekerja = p.id
        %s
        %s
        ORDER BY u.tanggal DESC
        """, selectClause, whereClause, groupByClause);
}
}
