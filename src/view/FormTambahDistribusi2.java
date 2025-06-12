package view;

import controller.FormTambahDistribusiController;
import model.FormTambahDistribusiModel.Customer;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class FormTambahDistribusi2 extends JFrame {
    // Color Palette
    private static final Color MIDNIGHT_BLUE = new Color(25, 42, 86);
    private static final Color SKY_BLUE = new Color(135, 206, 235);
    private static final Color LIGHT_BLUE = new Color(173, 216, 230);
    private static final Color WHITE = Color.WHITE;
    private static final Color DARK_TEXT = new Color(51, 51, 51);
    
    // Fonts
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    // Components
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JLabel titleLabel;
    private JLabel customerLabel, idLabel, notaLabel, tanggalLabel, alamatLabel;
    private JLabel totalLabel, bayarLabel, kembaliLabel;
    private JTextField customerField, idField, notaField, tanggalField;
    private JTextField totalField, bayarField, kembaliField;
    private JTextPane alamatPane;
    private JScrollPane tableScrollPane, alamatScrollPane;
    private JTable distribusiTable;
    private JButton simpanButton, kembaliButton, cetakButton;
    
    // Data
    private FormTambahDistribusiController controller;
    private DefaultTableModel tableModel;
    private Map<String, Customer> customerMap = new HashMap<>();

    public FormTambahDistribusi2() {
        initComponents();
        setupController();
        applyStyles();
        initializeForm();
    }

    private void initComponents() {
        setTitle("Tambah Distribusi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550); // Slightly smaller form
        setLocationRelativeTo(null);
        
        // Main Panel with nice background
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(WHITE);
        
        // Header Panel
        headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(WHITE);
        
        titleLabel = new JLabel("TAMBAH PENJUALAN", JLabel.CENTER);
        
        // Customer Info Panel - using GridBagLayout for better control
        JPanel customerPanel = new JPanel(new GridBagLayout());
        customerPanel.setBackground(WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        customerLabel = new JLabel("Customer");
        customerPanel.add(customerLabel, gbc);
        
        gbc.gridx = 1;
        customerField = createSmallField();
        customerPanel.add(customerField, gbc);
        
        gbc.gridx = 2;
        tanggalLabel = new JLabel("Tanggal");
        customerPanel.add(tanggalLabel, gbc);
        
        gbc.gridx = 3;
        tanggalField = createSmallField();
        customerPanel.add(tanggalField, gbc);
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        idLabel = new JLabel("ID Customer");
        customerPanel.add(idLabel, gbc);
        
        gbc.gridx = 1;
        idField = createSmallField();
        customerPanel.add(idField, gbc);
        
        gbc.gridx = 2;
        notaLabel = new JLabel("No. Nota");
        customerPanel.add(notaLabel, gbc);
        
        gbc.gridx = 3;
        notaField = createSmallField();
        customerPanel.add(notaField, gbc);
        
        // Row 2 - Address (bigger field)
        gbc.gridx = 0; gbc.gridy = 2;
        alamatLabel = new JLabel("Alamat");
        customerPanel.add(alamatLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        alamatPane = new JTextPane();
        alamatScrollPane = new JScrollPane(alamatPane);
        alamatScrollPane.setPreferredSize(new Dimension(200, 60));
        customerPanel.add(alamatScrollPane, gbc);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(customerPanel);
        
        // Table Setup
        tableModel = new DefaultTableModel(
            new Object[]{"No.", "Nama Produk", "Harga Satuan", "Jumlah", "Subtotal"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) {
                return column == 1 || column == 3;
            }
            @Override public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2 || columnIndex == 3 || columnIndex == 4) {
                    return Integer.class;
                }
                return String.class;
            }
        };
        
        distribusiTable = new JTable(tableModel);
        tableScrollPane = new JScrollPane(distribusiTable);
        
        // Add empty rows
        for (int i = 0; i < 5; i++) {
            tableModel.addRow(new Object[]{i + 1, "", 0, 0, 0});
        }
        
        // Footer Panel
        JPanel footerPanel = new JPanel(new BorderLayout(10, 10));
        footerPanel.setBackground(WHITE);
        
        // Payment Panel
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));
        paymentPanel.setBackground(WHITE);
        
        totalLabel = new JLabel("TOTAL");
        bayarLabel = new JLabel("BAYAR");
        kembaliLabel = new JLabel("KEMBALI");
        
        totalField = createSmallField();
        bayarField = createSmallField();
        kembaliField = createSmallField();
        
        totalField.setEditable(false);
        kembaliField.setEditable(false);
        
        paymentPanel.add(createLabelFieldPanel(totalLabel, totalField));
        paymentPanel.add(Box.createVerticalStrut(8));
        paymentPanel.add(createLabelFieldPanel(bayarLabel, bayarField));
        paymentPanel.add(Box.createVerticalStrut(8));
        paymentPanel.add(createLabelFieldPanel(kembaliLabel, kembaliField));
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(WHITE);
        
        simpanButton = new JButton("SIMPAN");
        kembaliButton = new JButton("KEMBALI");
        cetakButton = new JButton("CETAK");
        
        buttonPanel.add(kembaliButton);
        buttonPanel.add(simpanButton);
        buttonPanel.add(cetakButton);
        
        footerPanel.add(buttonPanel, BorderLayout.WEST);
        footerPanel.add(paymentPanel, BorderLayout.EAST);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JTextField createSmallField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(120, 25)); // Smaller size
        field.setMaximumSize(new Dimension(120, 25));
        return field;
    }
    
    private JPanel createLabelFieldPanel(JLabel label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(WHITE);
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        field.setPreferredSize(new Dimension(120, 25));
        return panel;
    }
    
    private void setupController() {
        this.controller = new FormTambahDistribusiController(this);
        
        simpanButton.addActionListener(e -> controller.handleSaveButton());
        kembaliButton.addActionListener(e -> controller.handleBackToDistribusi());
        
        distribusiTable.getModel().addTableModelListener(e -> {
            controller.handleTableUpdate(e);
            updateTotalDanKembalian();
        });
        
        bayarField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateTotalDanKembalian(); }
            public void removeUpdate(DocumentEvent e) { updateTotalDanKembalian(); }
            public void changedUpdate(DocumentEvent e) {}
        });
    }
    
    private void applyStyles() {
        // Title
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(MIDNIGHT_BLUE);
        
        // Labels
        Component[] labels = {customerLabel, idLabel, notaLabel, tanggalLabel, 
                            alamatLabel, totalLabel, bayarLabel, kembaliLabel};
        for (Component label : labels) {
            ((JLabel)label).setFont(LABEL_FONT);
            ((JLabel)label).setForeground(DARK_TEXT);
        }
        
        // Buttons
        JButton[] buttons = {simpanButton, kembaliButton, cetakButton};
        for (JButton button : buttons) {
            button.setFont(BUTTON_FONT);
            button.setBorder(new EmptyBorder(8, 15, 8, 15));
            button.setFocusPainted(false);
        }
        
        simpanButton.setBackground(SKY_BLUE);
        simpanButton.setForeground(MIDNIGHT_BLUE);
        
        kembaliButton.setBackground(LIGHT_BLUE);
        kembaliButton.setForeground(MIDNIGHT_BLUE);
        
        cetakButton.setBackground(MIDNIGHT_BLUE);
        cetakButton.setForeground(WHITE);
        
        // Text fields
        Component[] textFields = {customerField, idField, notaField, tanggalField, 
                                totalField, bayarField, kembaliField};
        for (Component field : textFields) {
            ((JTextField)field).setFont(FIELD_FONT);
            ((JTextField)field).setBorder(new CompoundBorder(
                new LineBorder(LIGHT_BLUE, 1),
                new EmptyBorder(5, 8, 5, 8)
            ));
            ((JTextField)field).setBackground(WHITE);
        }
        
        // Table
        distribusiTable.setFont(FIELD_FONT);
        distribusiTable.setShowGrid(true);
        distribusiTable.setGridColor(LIGHT_BLUE);
        distribusiTable.setRowHeight(25);
        distribusiTable.getTableHeader().setFont(LABEL_FONT);
        distribusiTable.getTableHeader().setBackground(MIDNIGHT_BLUE);
        distribusiTable.getTableHeader().setForeground(WHITE);
        
        // Text pane
        alamatPane.setFont(FIELD_FONT);
        alamatPane.setBorder(new CompoundBorder(
            new LineBorder(LIGHT_BLUE, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        alamatPane.setBackground(WHITE);
    }
    
    private void initializeForm() {
        setupAutocomplete();
        simpanButton.setEnabled(false);
        controller.loadCustomerData();
    }
    
    private void setupAutocomplete() {
        JPopupMenu suggestionPopup = new JPopupMenu();
        suggestionPopup.setBorder(new LineBorder(LIGHT_BLUE));
        suggestionPopup.setBackground(WHITE);
        
        customerField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { showSuggestions(); }
            public void removeUpdate(DocumentEvent e) { showSuggestions(); }
            public void changedUpdate(DocumentEvent e) {}
            
            private void showSuggestions() {
                String input = customerField.getText().trim().toLowerCase();
                suggestionPopup.setVisible(false);
                suggestionPopup.removeAll();
                
                if (input.isEmpty()) return;
                
                for (String nama : customerMap.keySet()) {
                    if (nama.toLowerCase().startsWith(input)) {
                        JMenuItem item = new JMenuItem(nama);
                        item.setFont(FIELD_FONT);
                        item.setForeground(DARK_TEXT);
                        item.setBorder(new EmptyBorder(5, 10, 5, 10));
                        item.addActionListener(e -> {
                            customerField.setText(nama);
                            suggestionPopup.setVisible(false);
                            isiDataCustomer(nama);
                        });
                        suggestionPopup.add(item);
                    }
                }
                
                if (suggestionPopup.getComponentCount() > 0) {
                    suggestionPopup.show(customerField, 0, customerField.getHeight());
                }
            }
        });
    }
    
    private void isiDataCustomer(String nama) {
        Customer cust = customerMap.get(nama);
        if (cust != null) {
            idField.setText(cust.getId());
            alamatPane.setText(cust.getAlamat());
        }
    }
    
    public void updateTotalDanKembalian() {
        int total = controller.calculateTotal(tableModel);
        totalField.setText(String.valueOf(total));
        
        try {
            int dibayar = Integer.parseInt(bayarField.getText());
            int kembalian = dibayar - total;
            kembaliField.setText(String.valueOf(kembalian));
            kembaliField.setForeground(kembalian < 0 ? Color.RED : MIDNIGHT_BLUE);
        } catch (NumberFormatException e) {
            kembaliField.setText("0");
            kembaliField.setForeground(MIDNIGHT_BLUE);
        }
        simpanButton.setEnabled(total > 0);
    }
    
    // All original getter methods preserved
    public JTextField getTextFieldNoNota() { return notaField; }
    public JTextField getTextFieldIdCustomer() { return idField; }
    public JTextPane getTextPaneAlamat() { return alamatPane; }
    public JTextField getTextFieldTanggal() { return tanggalField; }
    public JTextField getTextFieldTotal() { return totalField; }
    public JTextField getTextFieldDibayar() { return bayarField; }
    public JTextField getTextFieldKembalian() { return kembaliField; }
    public JComboBox<String> getComboBoxNama() { return new JComboBox<>(); }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getTabelDistribusi() { return distribusiTable; }
    public JButton getBtnCetakNota() { return cetakButton; }
    
    public String getNomorNota() { return notaField.getText(); }
    public String getNamaCustomer() { return customerField.getText(); }
    public String getAlamatCustomer() { return alamatPane.getText(); }
    public String getTanggal() { return tanggalField.getText(); }
    public String getTotalHarga() { return totalField.getText(); }
    public String getBayar() { return bayarField.getText(); }
    public String getKembali() { return kembaliField.getText(); }
    
    public void setCustomerMap(Map<String, Customer> customerMap) {
        this.customerMap = customerMap;
    }
    
    public Map<String, Customer> getCustomerMap() {
        return customerMap;
    }
}