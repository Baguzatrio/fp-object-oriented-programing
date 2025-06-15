package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import model.User;

public class SessionLogView extends JFrame {
    // Warna sama persis dengan Distribusi2
    private static final Color MIDNIGHT_BLUE = new Color(25, 42, 86);
    private static final Color SKY_BLUE = new Color(135, 206, 235);
    private static final Color LIGHT_BLUE = new Color(173, 216, 230);
    private static final Color WHITE = Color.WHITE;
    private static final Color BLACK = Color.BLACK;

    private JTable logTable;
    private JComboBox<String> moduleFilter;
    private JComboBox<String> userFilter;
    private JButton refreshBtn;
    private JButton exportBtn;
    private HamburgerMenu hamburgerMenu;
    private User currentUser;
    private JPanel mainPanel;
    private JScrollPane scrollPane;

    public SessionLogView(User user) {
        this.currentUser = user;
        initUI();
        applyStyles();
        setFrameSettings();
    }

    private void initUI() {
        // MAIN PANEL pakai BoxLayout supaya navbar + content tersusun vertikal
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(LIGHT_BLUE);

        // 1. HAMBURGER MENU (diambil dari Distribusi2)
        hamburgerMenu = new HamburgerMenu(this, currentUser);
        JPanel navBar = hamburgerMenu.getNavBar();
navBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // atur tinggi tetap
navBar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 60)); // opsional tambahan
mainPanel.add(navBar);

        // 2. HEADER PANEL (judul + filter + tombol)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(MIDNIGHT_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JLabel titleLabel = new JLabel("USER ACTIVITY LOGS", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // FILTER PANEL (kiri)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setOpaque(false);

        JLabel moduleLabel = new JLabel("Module:");
        moduleLabel.setForeground(WHITE);
        filterPanel.add(moduleLabel);

        moduleFilter = new JComboBox<>();
        moduleFilter.setBackground(WHITE);
        moduleFilter.setForeground(MIDNIGHT_BLUE);
        filterPanel.add(moduleFilter);

        JLabel userLabel = new JLabel("User:");
        userLabel.setForeground(WHITE);
        filterPanel.add(userLabel);

        userFilter = new JComboBox<>();
        userFilter.setBackground(WHITE);
        userFilter.setForeground(MIDNIGHT_BLUE);
        filterPanel.add(userFilter);

        headerPanel.add(filterPanel, BorderLayout.CENTER);

        // BUTTON PANEL (kanan)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setOpaque(false);

        refreshBtn = createHoverButton("Refresh");
        exportBtn = createHoverButton("Export CSV");
        buttonPanel.add(refreshBtn);
        buttonPanel.add(exportBtn);

        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // 3. TABLE
        logTable = new JTable();
        logTable.setAutoCreateRowSorter(true);
        logTable.setRowHeight(28);
        logTable.setShowGrid(true);
        logTable.setGridColor(MIDNIGHT_BLUE);
        logTable.setBackground(WHITE);
        logTable.setForeground(MIDNIGHT_BLUE);

        // Cell styling
        logTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(WHITE);
                setForeground(MIDNIGHT_BLUE);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, MIDNIGHT_BLUE));
                return this;
            }
        });

        // Header styling
        logTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(MIDNIGHT_BLUE);
                setForeground(WHITE);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 1, WHITE),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
                return this;
            }
        });

        // 4. SCROLLPANE
        scrollPane = new JScrollPane(logTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        scrollPane.getViewport().setBackground(WHITE);

        // 5. Gabungkan HEADER PANEL dan TABLE
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(LIGHT_BLUE);
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Tambahkan ke mainPanel
        mainPanel.add(contentPanel);
    }

    // Method untuk tombol hover style (sama seperti Distribusi2)
    private JButton createHoverButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(72, 61, 139));
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 149, 237));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(72, 61, 139));
            }
        });
        return button;
    }

    private void applyStyles() {
        setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    private void setFrameSettings() {
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(mainPanel);
    }

    // Load data dari database ke tabel (fungsionalitas dipertahankan)
    public void loadData(ResultSet resultSet) throws SQLException {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            model.addColumn(metaData.getColumnName(i));
        }

        while (resultSet.next()) {
            Object[] row = new Object[metaData.getColumnCount()];
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                row[i - 1] = resultSet.getObject(i);
            }
            model.addRow(row);
        }

        logTable.setModel(model);
    }

    // Getter methods
    public JComboBox<String> getModuleFilter() { return moduleFilter; }
    public JComboBox<String> getUserFilter() { return userFilter; }
    public JButton getRefreshBtn() { return refreshBtn; }
    public JButton getExportBtn() { return exportBtn; }
}
