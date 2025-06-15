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
    private static final Color ERROR_RED = new Color(220, 53, 69);
    
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
    private JLabel totalLabel, bayarLabel, kembaliLabel, ongkirLabel;
    public JTextField customerField, idField, notaField, tanggalField;
    private JTextField totalField, bayarField, kembaliField, ongkirField;
    private JTextPane alamatPane;
    private JScrollPane tableScrollPane, alamatScrollPane;
    private JTable distribusiTable;
    private JButton simpanButton, kembaliButton, cetakButton, kelolaOngkirButton;
    private JComboBox<String> ongkirCombo;
    
    // Data
    private DefaultTableModel tableModel;
    private Map<String, Customer> customerMap = new HashMap<>();
    private FormTambahDistribusiController controller;

    public FormTambahDistribusi2() {
        initComponents();
        applyStyles();
        setupAutocomplete();
        setupOngkirCombo();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (controller != null) {
                    controller.handleWindowClosed();
                }
            }
        });
    }

    private void initComponents() {
        setTitle("Tambah Distribusi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Main Panel
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(WHITE);
        
        // Header Panel
        headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(WHITE);
        
        titleLabel = new JLabel("TAMBAH PENJUALAN", JLabel.CENTER);
        
        // Customer Info Panel
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
        tanggalField.setEditable(false);
        customerPanel.add(tanggalField, gbc);
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        idLabel = new JLabel("ID Customer");
        customerPanel.add(idLabel, gbc);
        
        gbc.gridx = 1;
        idField = createSmallField();
        idField.setEditable(false);
        customerPanel.add(idField, gbc);
        
        gbc.gridx = 2;
        notaLabel = new JLabel("No. Nota");
        customerPanel.add(notaLabel, gbc);
        
        gbc.gridx = 3;
        notaField = createSmallField();
        notaField.setEditable(false);
        customerPanel.add(notaField, gbc);
        
        // Row 2 - Address
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
            @Override 
            public boolean isCellEditable(int row, int column) {
                return column == 1 || column == 3;
            }
            
            @Override 
            public Class<?> getColumnClass(int columnIndex) {
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
        ongkirLabel = new JLabel("Ongkos Kirim");
        
        totalField = createSmallField();
        bayarField = createSmallField();
        kembaliField = createSmallField();
        ongkirField = createSmallField();
        
        totalField.setEditable(false);
        kembaliField.setEditable(false);
        ongkirField.setEditable(false);
        
        paymentPanel.add(createLabelFieldPanel(totalLabel, totalField));
        paymentPanel.add(Box.createVerticalStrut(8));
        paymentPanel.add(createLabelFieldPanel(bayarLabel, bayarField));
        paymentPanel.add(Box.createVerticalStrut(8));
        paymentPanel.add(createLabelFieldPanel(kembaliLabel, kembaliField));
        paymentPanel.add(Box.createVerticalStrut(8));
        
        // Ongkir Panel
        JPanel ongkirPanel = new JPanel(new BorderLayout(5, 5));
        ongkirPanel.setBackground(WHITE);
        
        ongkirCombo = new JComboBox<>();
        ongkirCombo.addItem("Gratis (Tidak ada ongkir)");
        
        kelolaOngkirButton = new JButton("...");
kelolaOngkirButton.setPreferredSize(new Dimension(30, 25));
kelolaOngkirButton.addActionListener(e -> {
    if (controller != null) {
        controller.handleKelolaOngkir();
    } else {
        JOptionPane.showMessageDialog(this, "Controller belum diinisialisasi", "Error", JOptionPane.ERROR_MESSAGE);
    }
});
        
        JPanel ongkirInputPanel = new JPanel(new BorderLayout(5, 5));
        ongkirInputPanel.add(ongkirCombo, BorderLayout.CENTER);
        ongkirInputPanel.add(kelolaOngkirButton, BorderLayout.EAST);
        
        ongkirPanel.add(ongkirLabel, BorderLayout.WEST);
        ongkirPanel.add(ongkirInputPanel, BorderLayout.CENTER);
        ongkirPanel.add(ongkirField, BorderLayout.EAST);
        
        paymentPanel.add(ongkirPanel);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(WHITE);
        
        kembaliButton = new JButton("KEMBALI");
        simpanButton = new JButton("SIMPAN");
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
    
    private void setupOngkirCombo() {
        ongkirCombo.addActionListener(e -> {
            String selected = (String) ongkirCombo.getSelectedItem();
            if (selected != null) {
                if (selected.equals("Gratis (Tidak ada ongkir)")) {
                    ongkirField.setText("0");
                } else {
                    // Extract the cost from the selected item
                    String[] parts = selected.split(" - Rp");
                    if (parts.length > 1) {
                        ongkirField.setText(parts[1]);
                    }
                }
                updateTotalDanKembalian();
            }
        });
    }
    
    public void loadOngkirData(String[] ongkirOptions) {
        ongkirCombo.removeAllItems();
        ongkirCombo.addItem("Gratis (Tidak ada ongkir)");
        for (String option : ongkirOptions) {
            ongkirCombo.addItem(option);
        }
    }
    
    public int getSelectedOngkir() {
        try {
            return Integer.parseInt(ongkirField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private JTextField createSmallField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(150, 30));
        field.setMaximumSize(new Dimension(150, 30));
        return field;
    }
    
    private JPanel createLabelFieldPanel(JLabel label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(WHITE);
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        field.setPreferredSize(new Dimension(150, 30));
        return panel;
    }
    
    private void applyStyles() {
        // Title
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(MIDNIGHT_BLUE);
        
        // Labels
        Component[] labels = {customerLabel, idLabel, notaLabel, tanggalLabel, 
                            alamatLabel, totalLabel, bayarLabel, kembaliLabel, ongkirLabel};
        for (Component label : labels) {
            ((JLabel)label).setFont(LABEL_FONT);
            ((JLabel)label).setForeground(DARK_TEXT);
        }
        
        // Buttons
        JButton[] buttons = {simpanButton, kembaliButton, cetakButton, kelolaOngkirButton};
        for (JButton button : buttons) {
            button.setFont(BUTTON_FONT);
            button.setBorder(new EmptyBorder(5, 10, 5, 10));
            button.setFocusPainted(false);
        }
        
        simpanButton.setBackground(SKY_BLUE);
        simpanButton.setForeground(MIDNIGHT_BLUE);
        
        kembaliButton.setBackground(LIGHT_BLUE);
        kembaliButton.setForeground(MIDNIGHT_BLUE);
        
        cetakButton.setBackground(MIDNIGHT_BLUE);
        cetakButton.setForeground(WHITE);
        
        kelolaOngkirButton.setFont(BUTTON_FONT);
kelolaOngkirButton.setBackground(LIGHT_BLUE);
kelolaOngkirButton.setForeground(MIDNIGHT_BLUE);
kelolaOngkirButton.setBorder(new EmptyBorder(5, 10, 5, 10));
kelolaOngkirButton.setFocusPainted(false);
        
        // ComboBox
        ongkirCombo.setFont(FIELD_FONT);
        ongkirCombo.setBorder(new CompoundBorder(
            new LineBorder(LIGHT_BLUE, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        
        // Text fields
        Component[] textFields = {customerField, idField, notaField, tanggalField, 
                                totalField, bayarField, kembaliField, ongkirField};
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
        try {
            int totalProduk = 0;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Object value = tableModel.getValueAt(i, 4);
                if (value instanceof Integer) {
                    totalProduk += (Integer) value;
                }
            }
            
            int ongkir = ongkirField.getText().isEmpty() ? 0 : Integer.parseInt(ongkirField.getText());
            int total = totalProduk + ongkir;
            totalField.setText(String.valueOf(total));
            
            int dibayar = bayarField.getText().isEmpty() ? 0 : Integer.parseInt(bayarField.getText());
            int kembalian = dibayar - total;
            kembaliField.setText(String.valueOf(kembalian));
            kembaliField.setForeground(kembalian < 0 ? ERROR_RED : MIDNIGHT_BLUE);
            
            simpanButton.setEnabled(totalProduk > 0 && !customerField.getText().isEmpty());
        } catch (NumberFormatException e) {
            kembaliField.setText("0");
            kembaliField.setForeground(MIDNIGHT_BLUE);
            simpanButton.setEnabled(false);
        }
    }
    
    // Getters
    public JTextField getTextFieldNoNota() { return notaField; }
    public JTextField getTextFieldIdCustomer() { return idField; }
    public JTextPane getTextPaneAlamat() { return alamatPane; }
    public JTextField getTextFieldTanggal() { return tanggalField; }
    public JTextField getTextFieldTotal() { return totalField; }
    public JTextField getTextFieldDibayar() { return bayarField; }
    public JTextField getTextFieldKembalian() { return kembaliField; }
    public JTextField getOngkirField() { return ongkirField; }
    public JComboBox<String> getOngkirCombo() { return ongkirCombo; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getTabelDistribusi() { return distribusiTable; }
    public JButton getBtnCetakNota() { return cetakButton; }
    public JButton getSimpanButton() { return simpanButton; }
    public JButton getKembaliButton() { return kembaliButton; }
    public JButton getKelolaOngkirButton() { return kelolaOngkirButton; }
    
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
    
    public void setController(FormTambahDistribusiController controller) {
        this.controller = controller;
    }
    
    public JTextField getCustomerField() {
        return customerField;
    }
}