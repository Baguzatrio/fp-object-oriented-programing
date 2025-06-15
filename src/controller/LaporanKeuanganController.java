package controller;

import model.LaporanKeuanganModel;
import java.util.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;

public class LaporanKeuanganController {
    private LaporanKeuanganModel model;
    
    public LaporanKeuanganController() {
        this.model = new LaporanKeuanganModel();
    }

    // Data retrieval methods
    public List<Map<String, Object>> getDailyData(LocalDate date) {
        return model.fetchDailyData(date);
    }

    public List<Map<String, Object>> getWeeklyData(int week, int year) {
        return model.fetchWeeklyData(week, year);
    }

    public List<Map<String, Object>> getMonthlyData(int month, int year) {
        return model.fetchMonthlyData(month, year);
    }

    public List<Map<String, Object>> getYearlyData(int year) {
        return model.fetchYearlyData(year);
    }

    public List<Map<String, Object>> getAllData() {
        return model.fetchAllData();
    }

    // Balance information methods
    public double getSaldoHariIni() {
        return model.getSaldoHariIni();
    }

    public double getSaldoMingguIni() {
        return model.getSaldoMingguIni();
    }

    public double getSaldoBulanIni() {
        return model.getSaldoBulanIni();
    }

    public double getSaldoKeseluruhan() {
        return model.getSaldoKeseluruhan();
    }

    public Map<String, Double> getSaldoPerPeriode(String periode) {
        return model.getSaldoPerPeriode(periode);
    }

    // Financial calculation methods
    public double getTotalPendapatan(List<Map<String, Object>> data) {
        return model.getTotalPendapatan(data);
    }

    public double getTotalPengeluaran(List<Map<String, Object>> data) {
        return model.getTotalPengeluaran(data);
    }

    public double getLaba(List<Map<String, Object>> data) {
        return model.getLaba(data);
    }

    // Transaction recording methods
    public void recordDistribusi(double jumlah) {
        model.insertTransaksiDistribusi(jumlah);
    }

    public void recordUpahPekerja(double jumlah) {
        model.insertTransaksiUpahPekerja(jumlah);
    }

    public void recordBahanBaku(double jumlah) {
        model.insertTransaksiBahanBaku(jumlah);
    }

    // Helper methods for view
    public String formatCurrency(double amount) {
        return String.format("Rp%,.2f", amount);
    }

    public List<Integer> getAvailableYears() {
        List<Integer> years = new ArrayList<>();
        int currentYear = LocalDate.now().getYear();
        for (int year = currentYear - 5; year <= currentYear + 1; year++) {
            years.add(year);
        }
        return years;
    }

    public List<Integer> getAvailableWeeks(int year) {
        List<Integer> weeks = new ArrayList<>();
        int maxWeeks = YearMonth.of(year, 12).atEndOfMonth()
                        .get(WeekFields.ISO.weekOfYear());
        for (int week = 1; week <= maxWeeks; week++) {
            weeks.add(week);
        }
        return weeks;
    }

    public int getCurrentWeek() {
        return LocalDate.now().get(WeekFields.ISO.weekOfYear());
    }

    public int getCurrentMonth() {
        return LocalDate.now().getMonthValue();
    }

    public int getCurrentYear() {
        return LocalDate.now().getYear();
    }
}