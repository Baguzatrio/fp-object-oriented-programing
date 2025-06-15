package view;

import controller.LaporanKeuanganController;
import model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.swing.table.JTableHeader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LaporanKeuangan extends JFrame {
    private static final Color MIDNIGHT_BLUE = new Color(25, 25, 112);
    private static final Color SKY_BLUE = new Color(135, 206, 235);
    private static final Color LIGHT_BLUE = new Color(173, 216, 230);
    private static final Color WHITE = Color.WHITE;
    private static final Color LIGHT_GRAY = new Color(240, 240, 240);
    
    private User currentUser;
    private JPanel contentPanel;
    private JComboBox<String> filterPeriode;
    private JComboBox<String> filterBulan;
    private JComboBox<String> filterTahun;
    private JComboBox<Integer> filterMinggu;
    private JLabel labelBulan;
    private JLabel labelTahun;
    private JLabel labelMinggu;
    private JTable dataTable;
    private LaporanKeuanganController controller;
    private JLabel saldoHariIniLabel;
    private JLabel saldoMingguIniLabel;
    private JLabel saldoBulanIniLabel;
    private JLabel saldoTotalLabel;
    private HamburgerMenu hamburgerMenu;

    public LaporanKeuangan(User user) {
        this.currentUser = user;
        this.controller = new LaporanKeuanganController();
        setTitle("Laporan Keuangan");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        updateFilterVisibility();
        updateAllCharts();
    }

    private void initComponents() {
        // Main content panel with gradient background
        contentPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 25, 112), 
                    getWidth(), getHeight(), new Color(135, 206, 235));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setOpaque(false);
        add(contentPanel, BorderLayout.CENTER);

        // Navbar with custom styling
        hamburgerMenu = new HamburgerMenu(this, currentUser);
        JPanel navBar = hamburgerMenu.getNavBar();
        navBar.setBackground(MIDNIGHT_BLUE);
        getContentPane().add(navBar, BorderLayout.NORTH);

        // Top panel with filters and balance info
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Filter Panel with styling
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setOpaque(false);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(WHITE, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Style filter components
        filterPeriode = createStyledComboBox(new String[]{"Harian", "Mingguan", "Bulanan", "Tahunan"});
        filterPeriode.addActionListener(e -> updateFilterVisibility());
        
        filterBulan = createStyledComboBox(new String[]{
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        });
        filterBulan.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        
        filterTahun = createStyledComboBox();
        int currentYear = LocalDate.now().getYear();
        for (int year = currentYear - 5; year <= currentYear + 1; year++) {
            filterTahun.addItem(String.valueOf(year));
        }
        filterTahun.setSelectedItem(String.valueOf(currentYear));
        
        filterMinggu = createStyledComboBox();
        for (int week = 1; week <= 52; week++) {
            filterMinggu.addItem(week);
        }
        filterMinggu.setSelectedItem(LocalDate.now().get(WeekFields.ISO.weekOfYear()));

        labelBulan = createStyledLabel("Bulan:");
        labelTahun = createStyledLabel("Tahun:");
        labelMinggu = createStyledLabel("Minggu:");

        JButton applyButton = createStyledButton("Terapkan Filter", SKY_BLUE, MIDNIGHT_BLUE);
        applyButton.addActionListener(e -> updateAllCharts());
        
        JButton exportButton = createStyledButton("Export ke Excel", LIGHT_BLUE, MIDNIGHT_BLUE);
        exportButton.addActionListener(e -> exportToExcel());

        filterPanel.add(createStyledLabel("Periode:"));
        filterPanel.add(filterPeriode);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(labelMinggu);
        filterPanel.add(filterMinggu);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(labelBulan);
        filterPanel.add(filterBulan);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(labelTahun);
        filterPanel.add(filterTahun);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(applyButton);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(exportButton);

        // Balance info panel with styling
        JPanel saldoPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        saldoPanel.setOpaque(false);
        saldoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(WHITE, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        saldoHariIniLabel = createSaldoLabel("Saldo Hari Ini");
        saldoMingguIniLabel = createSaldoLabel("Saldo Minggu Ini");
        saldoBulanIniLabel = createSaldoLabel("Saldo Bulan Ini");
        saldoTotalLabel = createSaldoLabel("Saldo Total");
        
        saldoPanel.add(saldoHariIniLabel);
        saldoPanel.add(saldoMingguIniLabel);
        saldoPanel.add(saldoBulanIniLabel);
        saldoPanel.add(saldoTotalLabel);

        topPanel.add(filterPanel, BorderLayout.NORTH);
        topPanel.add(saldoPanel, BorderLayout.CENTER);
        contentPanel.add(topPanel, BorderLayout.NORTH);

        // Main data panel (table + charts)
        JPanel dataPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        dataPanel.setOpaque(false);
        dataPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Table with styling
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(WHITE, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        dataTable = new JTable(new DefaultTableModel(
            new Object[]{"Tanggal", "Jenis", "Kategori", "Jumlah (Rp)"}, 0));
        dataTable.setAutoCreateRowSorter(true);
        dataTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dataTable.setRowHeight(25);
        dataTable.setSelectionBackground(LIGHT_BLUE);
        dataTable.setSelectionForeground(MIDNIGHT_BLUE);
        
        // Style table header
        JTableHeader header = dataTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(MIDNIGHT_BLUE);
        header.setForeground(WHITE);
        
        JScrollPane tableScrollPane = new JScrollPane(dataTable);
        tableScrollPane.setPreferredSize(new Dimension(500, 400));
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableScrollPane.getViewport().setBackground(new Color(255, 255, 255, 200));
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        dataPanel.add(tablePanel);

        // Charts panel with styling
        JPanel chartsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        chartsPanel.setOpaque(false);
        
        // Line Chart
        JPanel lineChartPanel = new JPanel(new BorderLayout());
        lineChartPanel.setOpaque(false);
        lineChartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(WHITE, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        chartsPanel.add(lineChartPanel);
        
        // Pie Chart
        JPanel pieChartPanel = new JPanel(new BorderLayout());
        pieChartPanel.setOpaque(false);
        pieChartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(WHITE, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        chartsPanel.add(pieChartPanel);
        
        dataPanel.add(chartsPanel);
        contentPanel.add(dataPanel, BorderLayout.CENTER);

        updateSaldoInfo();
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return label;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboBox.setBackground(WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SKY_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return comboBox;
    }

    private JComboBox createStyledComboBox() {
        JComboBox comboBox = new JComboBox();
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboBox.setBackground(WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SKY_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return comboBox;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private JLabel createSaldoLabel(String title) {
        JLabel label = new JLabel(title, JLabel.CENTER);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(WHITE, 1),
            BorderFactory.createEmptyBorder(15, 5, 15, 5)
        ));
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(WHITE);
        label.setBackground(new Color(25, 25, 112, 150));
        label.setOpaque(true);
        return label;
    }

    private void updateFilterVisibility() {
        String periode = (String) filterPeriode.getSelectedItem();
        
        filterMinggu.setVisible("Mingguan".equals(periode));
        labelMinggu.setVisible("Mingguan".equals(periode));
        
        filterBulan.setVisible("Bulanan".equals(periode));
        labelBulan.setVisible("Bulanan".equals(periode));
        
        filterTahun.setVisible(!"Harian".equals(periode));
        labelTahun.setVisible(!"Harian".equals(periode));
    }

    private void updateAllCharts() {
        String periode = (String) filterPeriode.getSelectedItem();
        int bulan = filterBulan.getSelectedIndex() + 1;
        int tahun = Integer.parseInt((String) filterTahun.getSelectedItem());
        int minggu = (int) filterMinggu.getSelectedItem();
        
        List<Map<String, Object>> data = null;
        
        switch (periode) {
            case "Harian":
                data = controller.getDailyData(LocalDate.now());
                break;
            case "Mingguan":
                data = controller.getWeeklyData(minggu, tahun);
                break;
            case "Bulanan":
                data = controller.getMonthlyData(bulan, tahun);
                break;
            case "Tahunan":
                data = controller.getYearlyData(tahun);
                break;
        }
        
        updateTable(data);
        updateLineChart(data, periode, bulan, tahun, minggu);
        updatePieChart(data);
        updateSaldoInfo();
    }

    private void updateTable(List<Map<String, Object>> data) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);
        
        if (data != null) {
            for (Map<String, Object> row : data) {
                model.addRow(new Object[]{
                    row.get("tanggal"),
                    row.get("jenis"),
                    row.get("kategori"),
                    String.format("%,.2f", row.get("jumlah"))
                });
            }
        }
    }

    private void updateLineChart(List<Map<String, Object>> data, String periode, int bulan, int tahun, int minggu) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        Map<String, Double> pendapatanMap = new LinkedHashMap<>();
        Map<String, Double> pengeluaranMap = new LinkedHashMap<>();
        Map<String, Double> labaMap = new LinkedHashMap<>();
        
        if (data != null) {
            for (Map<String, Object> row : data) {
                String tanggal = row.get("tanggal").toString();
                String jenis = (String) row.get("jenis");
                double jumlah = ((Number) row.get("jumlah")).doubleValue();
                
                if ("pendapatan".equalsIgnoreCase(jenis)) {
                    pendapatanMap.merge(tanggal, jumlah, Double::sum);
                } else {
                    pengeluaranMap.merge(tanggal, -jumlah, Double::sum);
                }
            }
            
            pendapatanMap.forEach((tanggal, pendapatan) -> {
                double pengeluaran = pengeluaranMap.getOrDefault(tanggal, 0.0);
                labaMap.put(tanggal, pendapatan + pengeluaran);
            });
            
            pendapatanMap.forEach((tanggal, jumlah) -> 
                dataset.addValue(jumlah, "Pendapatan", tanggal));
            pengeluaranMap.forEach((tanggal, jumlah) -> 
                dataset.addValue(jumlah, "Pengeluaran", tanggal));
            labaMap.forEach((tanggal, jumlah) -> 
                dataset.addValue(jumlah, "Laba", tanggal));
        }

        String title = "Trend " + periode;
        if ("Mingguan".equals(periode)) {
            title += " " + minggu + " Tahun " + tahun;
        } else if ("Bulanan".equals(periode)) {
            title += " " + YearMonth.of(tahun, bulan).format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        } else if ("Tahunan".equals(periode)) {
            title += " " + tahun;
        }

        JFreeChart chart = ChartFactory.createLineChart(
            title,
            "Tanggal",
            "Jumlah (Rp)",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );

        chart.setBackgroundPaint(new Color(0, 0, 0, 0)); // Transparent
        chart.getTitle().setPaint(MIDNIGHT_BLUE);
        chart.getLegend().setBackgroundPaint(new Color(255, 255, 255, 200));
        
        // Style the plot
        chart.getCategoryPlot().setBackgroundPaint(new Color(255, 255, 255, 200));
        chart.getCategoryPlot().setRangeGridlinePaint(WHITE);
        chart.getCategoryPlot().setDomainGridlinePaint(WHITE);
        chart.getCategoryPlot().setOutlinePaint(null);

        updateChartPanel(0, new ChartPanel(chart) {
            {
                setBackground(new Color(0, 0, 0, 0)); // Transparent
                setOpaque(false);
            }
        });
    }

    private void updatePieChart(List<Map<String, Object>> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        if (data != null) {
            double pendapatan = controller.getTotalPendapatan(data);
            double pengeluaran = controller.getTotalPengeluaran(data);
            
            dataset.setValue("Pendapatan", pendapatan);
            dataset.setValue("Pengeluaran", pengeluaran);
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "Komposisi Keuangan",
            dataset,
            true, true, false
        );

        chart.setBackgroundPaint(new Color(0, 0, 0, 0)); // Transparent
        chart.getTitle().setPaint(MIDNIGHT_BLUE);
        chart.getLegend().setBackgroundPaint(new Color(255, 255, 255, 200));
        
        // Style the plot
        chart.getPlot().setBackgroundPaint(new Color(255, 255, 255, 200));
        chart.getPlot().setOutlinePaint(null);

        updateChartPanel(1, new ChartPanel(chart) {
            {
                setBackground(new Color(0, 0, 0, 0)); // Transparent
                setOpaque(false);
            }
        });
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

    private void updateSaldoInfo() {
        double saldoHariIni = controller.getSaldoHariIni();
        double saldoMingguIni = controller.getSaldoMingguIni();
        double saldoBulanIni = controller.getSaldoBulanIni();
        double saldoTotal = controller.getSaldoKeseluruhan();
        
        saldoHariIniLabel.setText("<html><center>Saldo Hari Ini<br><b>" + 
                                 String.format("Rp%,.2f", saldoHariIni) + "</b></center></html>");
        saldoMingguIniLabel.setText("<html><center>Saldo Minggu Ini<br><b>" + 
                                  String.format("Rp%,.2f", saldoMingguIni) + "</b></center></html>");
        saldoBulanIniLabel.setText("<html><center>Saldo Bulan Ini<br><b>" + 
                                 String.format("Rp%,.2f", saldoBulanIni) + "</b></center></html>");
        saldoTotalLabel.setText("<html><center>Saldo Total<br><b>" + 
                              String.format("Rp%,.2f", saldoTotal) + "</b></center></html>");
    }

    private void exportToExcel() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Simpan Laporan Keuangan");
    fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
    fileChooser.setSelectedFile(new File("Laporan_Keuangan.xlsx"));
    
    int userSelection = fileChooser.showSaveDialog(this);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();
        if (!fileToSave.getName().toLowerCase().endsWith(".xlsx")) {
            fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
        }
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Laporan Keuangan");
            
            // Create styles
            CellStyle headerStyle = workbook.createCellStyle();
            
            // POI Font untuk Excel
            org.apache.poi.ss.usermodel.Font poiHeaderFont = workbook.createFont();
            poiHeaderFont.setBold(true);
            headerStyle.setFont(poiHeaderFont);
            
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
            
            // Header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Tanggal", "Jenis", "Kategori", "Jumlah (Rp)"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Data rows
            DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(model.getValueAt(i, j).toString());
                    
                    if (j == 3) { // Format kolom jumlah
                        try {
                            double amount = Double.parseDouble(
                                model.getValueAt(i, j).toString().replaceAll("[^\\d.]", ""));
                            cell.setCellValue(amount);
                            cell.setCellStyle(currencyStyle);
                        } catch (NumberFormatException e) {
                            cell.setCellValue(model.getValueAt(i, j).toString());
                        }
                    }
                }
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Save file
            try (java.io.FileOutputStream out = new java.io.FileOutputStream(fileToSave)) {
                workbook.write(out);
                JOptionPane.showMessageDialog(this, 
                    "Laporan berhasil diexport ke:\n" + fileToSave.getAbsolutePath(),
                    "Export Berhasil", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error saat export Excel:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
}