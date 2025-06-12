package controller;

import model.LaporanKeuanganModel;
import java.util.*;

public class LaporanKeuanganController {
    private LaporanKeuanganModel model = new LaporanKeuanganModel();

    public List<Map<String, Object>> getFilteredData(String filterType) {
        switch (filterType) {
            case "Harian":
                return model.fetchDailyData();
            case "Mingguan":
                return model.fetchWeeklyData();
            case "Bulanan":
                return model.fetchMonthlyData();
            case "Tahunan":
                return model.fetchYearlyData();
            default:
                return model.fetchMonthlyData();
        }
    }

    public double getTotalPendapatan(List<Map<String, Object>> data) {
        return model.getTotalPendapatan(data);
    }

    public double getTotalPengeluaran(List<Map<String, Object>> data) {
        return model.getTotalPengeluaran(data);
    }
}