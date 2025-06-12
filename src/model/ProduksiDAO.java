package model;

import model.DatabaseConnection;

import javax.swing.*;
import java.sql.*;
import java.util.*;

public class ProduksiDAO {
    public static List<ProduksiModel> getAllProduksi() {
        List<ProduksiModel> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM produksi";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ProduksiModel p = new ProduksiModel(
                    rs.getInt("id"),
                    rs.getDate("tanggal"),
                    rs.getString("produk"),
                    rs.getInt("jumlah_batch"),
                    rs.getDouble("total_kg"),
                    rs.getInt("jumlah_kemasan")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memuat data: " + e.getMessage());
        }
        return list;
    }

    public static Map<String, Integer> getResepMap(JComboBox<String> comboBox) {
        Map<String, Integer> map = new HashMap<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, nama_produk FROM resep");
            while (rs.next()) {
                String nama = rs.getString("nama_produk");
                int id = rs.getInt("id");
                comboBox.addItem(nama);
                map.put(nama, id);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memuat resep: " + e.getMessage());
        }
        return map;
    }

    public static boolean kurangiStokDanSimpan(int resepId, String produk, int batch, double kg) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            Map<Integer, Double> bahanUpdate = new HashMap<>();

            PreparedStatement detailStmt = conn.prepareStatement("SELECT bahan_baku_id, jumlah FROM resep_detail WHERE resep_id = ?");
            detailStmt.setInt(1, resepId);
            ResultSet detailRS = detailStmt.executeQuery();
            while (detailRS.next()) {
                int bahanId = detailRS.getInt("bahan_baku_id");
                double jumlahPerBatch = detailRS.getDouble("jumlah");
                double total = jumlahPerBatch * batch;

                PreparedStatement cekStmt = conn.prepareStatement("SELECT stok FROM bahan_baku WHERE id = ?");
                cekStmt.setInt(1, bahanId);
                ResultSet cekRS = cekStmt.executeQuery();
                if (cekRS.next()) {
                    double stok = cekRS.getDouble("stok");
                    if (stok < total) {
                        conn.rollback();
                        return false;
                    }
                    bahanUpdate.put(bahanId, stok - total);
                }
            }

            for (Map.Entry<Integer, Double> entry : bahanUpdate.entrySet()) {
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE bahan_baku SET stok = ? WHERE id = ?");
                updateStmt.setDouble(1, entry.getValue());
                updateStmt.setInt(2, entry.getKey());
                updateStmt.executeUpdate();
            }

            int kemasan = (int) (kg * 1000 / 500);
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO produksi (tanggal, produk, jumlah_batch, total_kg, jumlah_kemasan) VALUES (CURRENT_DATE(), ?, ?, ?, ?)");
            insertStmt.setString(1, produk);
            insertStmt.setInt(2, batch);
            insertStmt.setDouble(3, kg);
            insertStmt.setInt(4, kemasan);
            insertStmt.executeUpdate();

            conn.commit();
            return true;
        }
    }
}
