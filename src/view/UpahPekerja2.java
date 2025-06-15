package view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import controller.UpahPekerjaController;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import model.User;

public class UpahPekerja2 extends JFrame {
    // Color Palette - match ProdukOlahan
    private static final Color MIDNIGHT_BLUE = new Color(25, 42, 86);
    private static final Color SKY_BLUE = new Color(135, 206, 235);
    private static final Color LIGHT_BLUE = new Color(173, 216, 230);
    private static final Color WHITE = Color.WHITE;
    private static final Color BLACK = Color.BLACK;
    
    private JTable tabelUpah;
    private JPanel contentPanel;
    private JPanel filterDetailPanel;
    private JButton btnDaftarPekerja;
    private JScrollPane scrollPane;
    private JComboBox<String> filterComboBox;
    private JComboBox<Integer> weekComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private DefaultTableModel tableModel;
    private User currentUser;
    private HamburgerMenu hamburgerMenu;

    public UpahPekerja2(User user) {
        this.currentUser = user;
        setTitle("Perhitungan Upah Pekerja");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        
        filterComboBox.setSelectedIndex(0);
        updateFilterDetails();
    }

    private void initComponents() {
        // Main panel with gradient background
        contentPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, MIDNIGHT_BLUE, getWidth(), getHeight(), LIGHT_BLUE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(contentPanel);

        // Navbar dan drawer
        hamburgerMenu = new HamburgerMenu(this, currentUser);
        getContentPane().add(hamburgerMenu.getNavBar(), BorderLayout.NORTH);

        // Title panel - match ProdukOlahan
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(173, 216, 230, 150)); // Semi-transparent
        titlePanel.setBorder(new EmptyBorder(0, 10, 15, 0));
        
        JLabel titleLabel = new JLabel("UPAH PEKERJA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(MIDNIGHT_BLUE);
        titlePanel.add(titleLabel);
        contentPanel.add(titlePanel, BorderLayout.NORTH);

        // Filter panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent
        topPanel.setBorder(new CompoundBorder(
            new LineBorder(MIDNIGHT_BLUE, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setOpaque(false);
        JLabel filterLabel = new JLabel("Filter Periode:");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filterLabel.setForeground(MIDNIGHT_BLUE);
        
        filterComboBox = new JComboBox<>(new String[]{"Harian", "Mingguan", "Bulanan", "Tahunan"});
        filterComboBox.setPreferredSize(new Dimension(150, 30));
        filterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filterComboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MIDNIGHT_BLUE, 1),
            BorderFactory.createEmptyBorder(3, 5, 3, 5)
        ));
        filterPanel.add(filterLabel);
        filterPanel.add(filterComboBox);
        topPanel.add(filterPanel, BorderLayout.WEST);

        // Button style to match ProdukOlahan
        btnDaftarPekerja = new JButton("Daftar Pekerja");
        styleButton(btnDaftarPekerja);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnDaftarPekerja);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Filter details panel
        filterDetailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterDetailPanel.setOpaque(false);
        
        // Week combo box
        weekComboBox = new JComboBox<>();
        for (int i = 1; i <= 5; i++) {
            weekComboBox.addItem(i);
        }
        styleComboBox(weekComboBox, 80);
        weekComboBox.setVisible(false);
        JLabel weekLabel = new JLabel("Minggu: ");
        weekLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        weekLabel.setForeground(MIDNIGHT_BLUE);
        filterDetailPanel.add(weekLabel);
        filterDetailPanel.add(weekComboBox);
        
        // Month combo box
        monthComboBox = new JComboBox<>(new String[] {
            "Januari", "Februari", "Maret", "April", "Mei", "Juni", 
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        });
        styleComboBox(monthComboBox, 120);
        monthComboBox.setVisible(false);
        JLabel monthLabel = new JLabel("Bulan: ");
        monthLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        monthLabel.setForeground(MIDNIGHT_BLUE);
        filterDetailPanel.add(monthLabel);
        filterDetailPanel.add(monthComboBox);
        
        // Year combo box
        yearComboBox = new JComboBox<>();
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        for (int year = currentYear - 3; year <= currentYear + 3; year++) {
            yearComboBox.addItem(year);
        }
        styleComboBox(yearComboBox, 80);
        yearComboBox.setVisible(false);
        JLabel yearLabel = new JLabel("Tahun: ");
        yearLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        yearLabel.setForeground(MIDNIGHT_BLUE);
        filterDetailPanel.add(yearLabel);
        filterDetailPanel.add(yearComboBox);
        
        topPanel.add(filterDetailPanel, BorderLayout.CENTER);
        contentPanel.add(topPanel, BorderLayout.NORTH);
        filterDetailPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Setup table with matching style
        setupTable();

        scrollPane = new JScrollPane(tabelUpah);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void styleButton(JButton button) {
        button.setBackground(MIDNIGHT_BLUE);
        button.setForeground(WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MIDNIGHT_BLUE.darker(), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(SKY_BLUE);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(MIDNIGHT_BLUE);
            }
        });
    }

    private void styleComboBox(JComboBox<?> comboBox, int width) {
        comboBox.setPreferredSize(new Dimension(width, 30));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MIDNIGHT_BLUE, 1),
            BorderFactory.createEmptyBorder(3, 5, 3, 5)
        ));
    }

    private void setupTable() {
        String[] columnNames = {"Periode", "ID Pekerja", "Nama Pekerja", "Produk", "Jumlah", "Upah/Unit", "Total Upah"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 4) {
                    return Integer.class;
                }
                return String.class;
            }
        };
        
        tabelUpah = new JTable(tableModel);
        tabelUpah.setRowHeight(30);
        tabelUpah.setAutoCreateRowSorter(true);
        tabelUpah.setOpaque(false);
        tabelUpah.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelUpah.setGridColor(MIDNIGHT_BLUE);

        // Header styling to match ProdukOlahan
        JTableHeader header = tabelUpah.getTableHeader();
        header.setBackground(MIDNIGHT_BLUE);
        header.setForeground(WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Cell renderer to match ProdukOlahan
        tabelUpah.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(255, 255, 255, 200) : new Color(240, 248, 255, 200));
                } else {
                    c.setBackground(SKY_BLUE);
                }
                
                c.setForeground(BLACK);
                
                if (column >= 4) {
                    ((JLabel)c).setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    ((JLabel)c).setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                return c;
            }
        });
    }

    public void tampilkanData(List<Object[]> data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
    
    public void updateFilterDetails() {
        String selectedFilter = (String) filterComboBox.getSelectedItem();
        
        weekComboBox.setVisible(false);
        monthComboBox.setVisible(false);
        yearComboBox.setVisible(false);
        
        if (selectedFilter != null) {
            switch (selectedFilter.toLowerCase()) {
                case "mingguan":
                    weekComboBox.setVisible(true);
                    monthComboBox.setVisible(true);
                    yearComboBox.setVisible(true);
                    break;
                case "bulanan":
                    monthComboBox.setVisible(true);
                    yearComboBox.setVisible(true);
                    break;
                case "tahunan":
                    yearComboBox.setVisible(true);
                    break;
            }
        }
        
        filterDetailPanel.revalidate();
        filterDetailPanel.repaint();
        contentPanel.revalidate();
    }

    // Getters remain the same
    public JButton getBtnDaftarPekerja() {
        return btnDaftarPekerja;
    }

    public JComboBox<String> getFilterComboBox() {
        return filterComboBox;
    }
    
    public JComboBox<Integer> getWeekComboBox() {
        return weekComboBox;
    }
    
    public JComboBox<String> getMonthComboBox() {
        return monthComboBox;
    }
    
    public JComboBox<Integer> getYearComboBox() {
        return yearComboBox;
    }
}