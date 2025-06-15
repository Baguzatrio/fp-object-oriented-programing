package view;

import controller.BahanBakuController;
import model.BahanBakuModel;
import model.User;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import javax.swing.border.*;

public class BahanBaku extends JFrame {
    // Color scheme
    private static final Color MIDNIGHT_BLUE = new Color(25, 42, 86);
    private static final Color SKY_BLUE = new Color(135, 206, 235);
    private static final Color LIGHT_BLUE = new Color(173, 216, 230);
    private static final Color WHITE = Color.WHITE;
    private static final Color BLACK = Color.BLACK;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private BahanBakuController controller;
    private HamburgerMenu hamburgerMenu;
    private User currentUser;
    private JScrollPane scrollPane;
    private JPanel formPanel;
    private JTextField namaField, stokField, satuanField, hargaField;
    private JTextField tglBeliField, tglKadaluarsaField, beratPerUnitField;
    private JButton submitBtn, toggleFormBtn;
    private JComboBox<String> filterComboBox;
    private JButton filterButton, clearFilterButton;

    public BahanBaku(User currentUser) {
        this.currentUser = currentUser;
        this.controller = new BahanBakuController();
        
        setTitle("Manajemen Bahan Baku");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        loadData(null);
    }

    private void initComponents() {
        // Main panel with BorderLayout - match ProdukOlahan
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BLUE);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Navbar dan drawer
        hamburgerMenu = new HamburgerMenu(this, currentUser);
        getContentPane().add(hamburgerMenu.getNavBar(), BorderLayout.NORTH);

        // Title panel - match ProdukOlahan
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(LIGHT_BLUE);
        titlePanel.setBorder(new EmptyBorder(0, 10, 15, 0));
        
        JLabel titleLabel = new JLabel("BAHAN BAKU");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(MIDNIGHT_BLUE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // 1. Filter panel - restyle to match ProdukOlahan
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(LIGHT_BLUE);
        
        filterComboBox = new JComboBox<>();
        filterComboBox.setEditable(true);
        filterComboBox.setPreferredSize(new Dimension(200, 30));
        filterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filterComboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MIDNIGHT_BLUE, 1),
            BorderFactory.createEmptyBorder(3, 5, 3, 5)
        ));
        
        filterButton = new JButton("Filter");
        styleButton(filterButton, SKY_BLUE);
        filterButton.addActionListener(e -> applyFilter());
        
        clearFilterButton = new JButton("Clear");
        styleButton(clearFilterButton, LIGHT_BLUE);
        clearFilterButton.addActionListener(e -> clearFilter());
        
        JLabel filterLabel = new JLabel("Filter by Name:");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filterLabel.setForeground(MIDNIGHT_BLUE);
        
        filterPanel.add(filterLabel);
        filterPanel.add(filterComboBox);
        filterPanel.add(filterButton);
        filterPanel.add(clearFilterButton);

        // 2. Table setup - match ProdukOlahan style
        String[] columns = {"ID", "Nama Bahan", "Stok", "Satuan", "Harga", 
                           "Tgl Beli", "Tgl Kadaluwarsa", "Total Berat", "Total Harga"};
        tableModel = new DefaultTableModel(null, columns) {
            @Override public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        styleTable();
        
        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(MIDNIGHT_BLUE, 1, true));
        
        // 3. Form panel (right side) - match ProdukOlahan compact style
        formPanel = createFormPanel();
        formPanel.setVisible(false);

        // Circle add button - match ProdukOlahan
        toggleFormBtn = new CircleButton("+");
        toggleFormBtn.addActionListener(e -> {
            formPanel.setVisible(!formPanel.isVisible());
            revalidate();
            repaint();
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(LIGHT_BLUE);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 10, 10));
        buttonPanel.add(toggleFormBtn);

        // Layout components
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(LIGHT_BLUE);
        contentPanel.add(filterPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(formPanel, BorderLayout.EAST);

        add(mainPanel);
    }

    private void styleTable() {
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setGridColor(MIDNIGHT_BLUE);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(MIDNIGHT_BLUE);
        header.setForeground(WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false);
        
        // Cell renderer to match ProdukOlahan
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? LIGHT_BLUE : new Color(200, 220, 240));
                c.setForeground(BLACK);
                
                if (column >= 3) { // Right-align numeric columns
                    ((JLabel)c).setHorizontalAlignment(JLabel.RIGHT);
                }
                if (isSelected) {
                    c.setBackground(SKY_BLUE);
                    c.setForeground(BLACK);
                }
                
                // Highlight expiring soon rows
                if (isExpiringSoon((String)table.getValueAt(row, 6))) {
                    c.setBackground(new Color(255, 200, 200));
                }
                return c;
            }
        });
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_BLUE),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(250, 400)); // Compact height with scroll
        panel.setBackground(LIGHT_BLUE);
        panel.setBorder(new MatteBorder(0, 1, 0, 0, MIDNIGHT_BLUE));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Title
        JLabel title = new JLabel("Tambah Bahan Baku");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(MIDNIGHT_BLUE);
        title.setBorder(new EmptyBorder(15, 15, 15, 15));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        
        // Form fields - compact style
        panel.add(createCompactField("Nama Bahan", namaField = new JTextField()));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createCompactField("Stok", stokField = new JTextField()));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createCompactField("Satuan", satuanField = new JTextField()));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createCompactField("Harga/Unit", hargaField = new JTextField()));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Date fields with placeholder
        panel.add(createCompactField("Tanggal Beli", tglBeliField = new JTextField("YYYY-MM-DD")));
        tglBeliField.addFocusListener(new PlaceholderFocusListener(tglBeliField, "YYYY-MM-DD"));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        panel.add(createCompactField("Tanggal Kadaluarsa", tglKadaluarsaField = new JTextField("YYYY-MM-DD")));
        tglKadaluarsaField.addFocusListener(new PlaceholderFocusListener(tglKadaluarsaField, "YYYY-MM-DD"));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        panel.add(createCompactField("Berat/Unit (kg)", beratPerUnitField = new JTextField()));
        panel.add(Box.createVerticalGlue());
        
        // Submit button
        submitBtn = new JButton("SIMPAN");
        styleButton(submitBtn, MIDNIGHT_BLUE);
        submitBtn.setForeground(WHITE);
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.addActionListener(e -> saveBahanBaku());
        
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(LIGHT_BLUE);
        btnPanel.add(submitBtn);
        panel.add(btnPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Make form scrollable if too many fields
        JScrollPane formScroll = new JScrollPane(panel);
        formScroll.setBorder(null);
        formScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(formScroll);
        return wrapper;
    }
    
    private JPanel createCompactField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BLUE);
        panel.setBorder(new EmptyBorder(0, 15, 0, 15));
        panel.setMaximumSize(new Dimension(200, 70));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(MIDNIGHT_BLUE);
        
        field.setPreferredSize(new Dimension(150, 25));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MIDNIGHT_BLUE, 1),
            BorderFactory.createEmptyBorder(3, 5, 3, 5)
        ));
        
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(MIDNIGHT_BLUE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBorder(new EmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);
    }

    // Circle Button Class - same as ProdukOlahan
    class CircleButton extends JButton {
        public CircleButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setBackground(MIDNIGHT_BLUE);
            setForeground(WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 25));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isArmed()) {
                g2.setColor(getBackground().darker());
            } else {
                g2.setColor(getBackground());
            }
            g2.fillOval(0, 0, getSize().width-1, getSize().height-1);
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(50, 50);
        }
    }

    // Placeholder focus listener for date fields
    class PlaceholderFocusListener implements java.awt.event.FocusListener {
        private final JTextField textField;
        private final String placeholder;

        public PlaceholderFocusListener(JTextField textField, String placeholder) {
            this.textField = textField;
            this.placeholder = placeholder;
        }

        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            if (textField.getText().equals(placeholder)) {
                textField.setText("");
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            if (textField.getText().isEmpty()) {
                textField.setText(placeholder);
            }
        }
    }

    private void saveBahanBaku() {
        try {
            BahanBakuModel bahan = new BahanBakuModel();
            bahan.setNama(namaField.getText());
            bahan.setStok(Double.parseDouble(stokField.getText()));
            bahan.setSatuan(satuanField.getText());
            bahan.setHargaPerUnit(Double.parseDouble(hargaField.getText()));
            bahan.setTanggalBeli(tglBeliField.getText());
            bahan.setTanggalKadaluarsa(tglKadaluarsaField.getText());
            bahan.setBeratPerUnit(Double.parseDouble(beratPerUnitField.getText()));

            if (controller.tambahBahan(bahan)) {
                JOptionPane.showMessageDialog(this, "Bahan baku berhasil ditambahkan");
                clearForm();
                formPanel.setVisible(false);
                loadData(null);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambah bahan baku");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Format angka tidak valid");
        }
    }

    private void clearForm() {
        namaField.setText("");
        stokField.setText("");
        satuanField.setText("");
        hargaField.setText("");
        tglBeliField.setText("YYYY-MM-DD");
        tglKadaluarsaField.setText("YYYY-MM-DD");
        beratPerUnitField.setText("");
    }

    private void applyFilter() {
        String filterText = (String) filterComboBox.getSelectedItem();
        loadData(filterText);
    }

    private void clearFilter() {
        filterComboBox.setSelectedItem("");
        loadData(null);
    }

    private void loadData(String filterNama) {
        tableModel.setRowCount(0);
        List<BahanBakuModel> list = controller.getAllBahan(filterNama);
        
        // Populate filter combo box
        filterComboBox.removeAllItems();
        filterComboBox.addItem("");
        list.forEach(b -> filterComboBox.addItem(b.getNama()));
        
        // Add data to table with coloring
        for (BahanBakuModel b : list) {
            Object[] row = new Object[]{
                b.getId(),
                b.getNama(),
                b.getStok(),
                b.getSatuan(),
                b.getHargaPerUnit(),
                b.getTanggalBeli(),
                b.getTanggalKadaluarsa(),
                String.format("%.2f kg", b.getTotalBerat()),
                String.format("%,.2f", b.getTotalHarga())
            };
            tableModel.addRow(row);
            
            // Color rows that will expire within 1 month
            if (isExpiringSoon(b.getTanggalKadaluarsa())) {
                setRowColor(tableModel.getRowCount()-1, new Color(255, 200, 200));
            }
        }
    }

    private boolean isExpiringSoon(String expiryDate) {
        try {
            LocalDate expDate = LocalDate.parse(expiryDate);
            return expDate.isBefore(LocalDate.now().plusMonths(1));
        } catch (Exception e) {
            return false;
        }
    }

    class RowColorRenderer extends DefaultTableCellRenderer {
        private Color color;
        
        public RowColorRenderer(Color color) {
            this.color = color;
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, 
                isSelected, hasFocus, row, column);
            c.setBackground(color);
            return c;
        }
    }

    public void setRowColor(int row, Color color) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(
                new RowColorRenderer(color));
        }
    }
}