package view;

import model.User;
import javax.swing.*;
import java.awt.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.*;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;
import controller.LaporanKeuanganController;

public class LaporanKeuangan extends JFrame {
    private User currentUser;
    private HamburgerMenu hamburgerMenu;
    private JPanel contentPanel;
    private JComboBox<String> filterPeriode;
    private JComboBox<String> filterBulan;
    private JComboBox<String> filterTahun;
    private JLabel labelBulan;
    private JLabel labelTahun;
    private JTable dataTable;
    private LaporanKeuanganController controller;

    public LaporanKeuangan(User user) {
        this.currentUser = user;
        this.controller = new LaporanKeuanganController();
        setTitle("Laporan Keuangan");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // Navbar
        hamburgerMenu = new HamburgerMenu(mainPanel, currentUser);
        mainPanel.add(hamburgerMenu.getNavBar(), BorderLayout.NORTH);

        // Panel isi
        contentPanel = new JPanel(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // ComboBox Filter Periode
        filterPeriode = new JComboBox<>(new String[]{"Harian", "Mingguan", "Bulanan", "Tahunan"});
        filterPeriode.addActionListener(e -> updateFilterVisibility());
        
        // ComboBox Bulan dan Tahun
        filterBulan = new JComboBox<>(new String[]{
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        });
        filterTahun = new JComboBox<>(new String[]{"2023", "2024", "2025"});

        JButton applyButton = new JButton("Terapkan Filter");
        applyButton.addActionListener(e -> updateAllCharts());

        labelBulan = new JLabel("Bulan:");
labelTahun = new JLabel("Tahun:");

filterPanel.add(new JLabel("Periode:"));
filterPanel.add(filterPeriode);
filterPanel.add(Box.createHorizontalStrut(10));
filterPanel.add(labelBulan);
filterPanel.add(filterBulan);
filterPanel.add(Box.createHorizontalStrut(10));
filterPanel.add(labelTahun);
filterPanel.add(filterTahun);
filterPanel.add(Box.createHorizontalStrut(20));
filterPanel.add(applyButton);


        contentPanel.add(filterPanel, BorderLayout.NORTH);

        // Panel data (Tabel + Charts)
        JPanel dataPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        dataPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Detail Transaksi"));
        dataTable = new JTable(new DefaultTableModel(
            new Object[]{"Tanggal", "Jenis", "Kategori", "Jumlah"}, 0));
        tablePanel.add(new JScrollPane(dataTable), BorderLayout.CENTER);
        dataPanel.add(tablePanel);

        // Charts
        JPanel chartsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        
        // Bar Chart
        JPanel barChartPanel = new JPanel(new BorderLayout());
        barChartPanel.setBorder(BorderFactory.createTitledBorder("Grafik Keuangan"));
        chartsPanel.add(barChartPanel);
        
        // Pie Chart
        JPanel pieChartPanel = new JPanel(new BorderLayout());
        pieChartPanel.setBorder(BorderFactory.createTitledBorder("Komposisi"));
        chartsPanel.add(pieChartPanel);
        
        dataPanel.add(chartsPanel);
        contentPanel.add(dataPanel, BorderLayout.CENTER);

        // Set visibility awal
        updateFilterVisibility();
        updateAllCharts();
        setVisible(true);
    }

    private void updateFilterVisibility() {
    String periode = (String) filterPeriode.getSelectedItem();
    boolean showMonth = "Bulanan".equals(periode); // Bulan hanya muncul saat "Bulanan"
    boolean showYear = !"Harian".equals(periode);  // Tahun muncul kecuali di "Harian"

    filterBulan.setVisible(showMonth);
    labelBulan.setVisible(showMonth);

    filterTahun.setVisible(showYear);
    labelTahun.setVisible(showYear);
}



    private void updateAllCharts() {
        String periode = (String) filterPeriode.getSelectedItem();
        String bulan = (String) filterBulan.getSelectedItem();
        String tahun = (String) filterTahun.getSelectedItem();
        
        List<Map<String, Object>> data = controller.getFilteredData(periode);
        
        updateTable(data);
        updateBarChart(data, periode, bulan, tahun);
        updatePieChart(data);
    }

    private void updateTable(List<Map<String, Object>> data) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);
        
        for (Map<String, Object> row : data) {
            model.addRow(new Object[]{
                row.get("tanggal"),
                row.get("jenis"),
                row.get("kategori"),
                String.format("Rp%,.2f", row.get("jumlah"))
            });
        }
    }

    private void updateBarChart(List<Map<String, Object>> data, String periode, String bulan, String tahun) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Group data by kategori untuk bar chart
        data.forEach(row -> {
            String jenis = (String) row.get("jenis");
            String kategori = (String) row.get("kategori");
            double jumlah = ((Number) row.get("jumlah")).doubleValue();
            dataset.addValue(jumlah, jenis, kategori);
        });

        String title = "Laporan " + periode;
        if (!"Tahunan".equals(periode)) {
            title += " " + bulan + " " + tahun;
        }

        JFreeChart chart = ChartFactory.createBarChart(
            title,
            "Kategori",
            "Jumlah (Rp)",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );

        // Update panel
        updateChartPanel(0, new ChartPanel(chart));
    }

    private void updatePieChart(List<Map<String, Object>> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Pendapatan", controller.getTotalPendapatan(data));
        dataset.setValue("Pengeluaran", controller.getTotalPengeluaran(data));

        JFreeChart chart = ChartFactory.createPieChart(
            "Pendapatan vs Pengeluaran",
            dataset,
            true, true, false
        );

        // Update panel
        updateChartPanel(1, new ChartPanel(chart));
    }

    private void updateChartPanel(int chartIndex, ChartPanel chartPanel) {
        JPanel dataPanel = (JPanel) contentPanel.getComponent(1);
        JPanel chartsPanel = (JPanel) dataPanel.getComponent(1);
        JPanel targetPanel = (JPanel) chartsPanel.getComponent(chartIndex);
        
        targetPanel.removeAll();
        targetPanel.add(chartPanel);
        targetPanel.revalidate();
        targetPanel.repaint();
    }

}