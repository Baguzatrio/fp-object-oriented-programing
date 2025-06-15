package view;

import controller.DataPekerjaController;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import model.User;

public class DataPekerja2 extends JFrame {
    private static final Color MIDNIGHT_BLUE = new Color(25, 42, 86);
    private static final Color SKY_BLUE = new Color(135, 206, 235);
    private static final Color LIGHT_BLUE = new Color(173, 216, 230);
    private static final Color WHITE = Color.WHITE;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private DataPekerjaController controller;
    private User currentUser;
    
    // Form components
    private JTextField txtId, txtNama, txtMulaiBekerja, txtNoTelp, txtSearch;
    private JTextPane txtAlamat;
    private JButton btnTambah, btnUbah, btnHapus, btnDetail, btnKembali, btnSimpan;
    
    public DataPekerja2(User user) {
        this.currentUser = user;
        initializeUI();
        controller = new DataPekerjaController(this);
        setupListeners();
        controller.loadData();
    }

    private void initializeUI() {
        setTitle("Data Pekerja");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(25, 25, 112), getWidth(), getHeight(), new Color(135, 206, 235));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setOpaque(false);
        
        // Header
        JLabel lblTitle = new JLabel("DATA PEKERJA", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        
        // Center content
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        
        // Form panel
        JPanel formPanel = createFormPanel();
        formPanel.setBackground(new Color(173, 216, 230, 150));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MIDNIGHT_BLUE, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        centerPanel.add(formPanel, BorderLayout.NORTH);
        
        // Table panel
        JPanel tablePanel = createTablePanel();
        tablePanel.setBackground(new Color(255, 255, 255, 200));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MIDNIGHT_BLUE, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createFormPanel() {
    JPanel panel = new JPanel(new GridLayout(3, 4, 8, 8));
    panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(MIDNIGHT_BLUE, 1),
            "Form Data Pekerja",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            WHITE));
    
    // Row 1
    panel.add(new JLabel("ID:"));
    txtId = new JTextField();
    txtId.setEditable(false);
    panel.add(txtId);
    
    panel.add(new JLabel("Nama:"));
    txtNama = new JTextField();
    panel.add(txtNama);
    
    // Row 2
    panel.add(new JLabel("Mulai Bekerja:"));
    txtMulaiBekerja = createStyledTextField(true);
    txtMulaiBekerja.setToolTipText("Format: DD/MM/YYYY (contoh: 25/02/2025)");
    panel.add(txtMulaiBekerja);
    
    panel.add(new JLabel("No. Telp:"));
    txtNoTelp = new JTextField();
    panel.add(txtNoTelp);
    
    // Row 3
    panel.add(new JLabel("Alamat:"));
    JScrollPane scrollPane = new JScrollPane();
    txtAlamat = new JTextPane();
    scrollPane.setViewportView(txtAlamat);
    panel.add(scrollPane);
    
    // Action buttons
    JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
    actionPanel.setOpaque(false);
    
    btnTambah = createStyledButton("Tambah", MIDNIGHT_BLUE, WHITE);
    btnSimpan = createStyledButton("Simpan", SKY_BLUE, MIDNIGHT_BLUE);
    btnHapus = createStyledButton("Hapus", Color.red, WHITE);
    
    actionPanel.add(btnTambah);
    actionPanel.add(btnSimpan);
    actionPanel.add(btnHapus);
    
    panel.add(new JLabel()); // Empty cell
    panel.add(actionPanel);
    
    return panel;
}
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MIDNIGHT_BLUE, 1),
                "Daftar Pekerja",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                MIDNIGHT_BLUE));
        
        // Table setup
        String[] columns = {"ID", "Nama", "Mulai Bekerja", "No. Telp", "Alamat"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(LIGHT_BLUE);
        table.setSelectionForeground(MIDNIGHT_BLUE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(MIDNIGHT_BLUE);
        header.setForeground(WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Cari:"));
        searchPanel.setForeground(MIDNIGHT_BLUE);
        searchPanel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SKY_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        searchPanel.add(txtSearch);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setOpaque(false);
        
        btnUbah = createStyledButton("Ubah", SKY_BLUE, MIDNIGHT_BLUE);
        btnDetail = createStyledButton("Detail", LIGHT_BLUE, MIDNIGHT_BLUE);
        btnKembali = createStyledButton("Kembali ke Upah Pekerja", MIDNIGHT_BLUE, WHITE);
        
        panel.add(btnUbah);
        panel.add(btnDetail);
        panel.add(btnKembali);
        
        return panel;
    }
    
    private JTextField createStyledTextField(boolean editable) {
        JTextField textField = new JTextField();
        textField.setEditable(editable);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SKY_BLUE, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return textField;
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
    
    private void setupListeners() {
        btnTambah.addActionListener(e -> controller.tambahPekerja(
            txtNama.getText(),
            txtMulaiBekerja.getText(),
            txtNoTelp.getText(),
            txtAlamat.getText()
        ));
        
        btnSimpan.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                controller.updatePekerja(
                    id,
                    txtNama.getText(),
                    txtMulaiBekerja.getText(),
                    txtNoTelp.getText(),
                    txtAlamat.getText()
                );
            }
        });
        
        btnHapus.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                controller.deletePekerja(id);
            }
        });
        
        btnUbah.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                controller.prepareUpdateForm(id);
            }
        });
        
        btnDetail.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            int id = selectedRow >= 0 ? (int) tableModel.getValueAt(selectedRow, 0) : -1;
            new DetailPekerja2(id).setVisible(true);
        });
        
        btnKembali.addActionListener(e -> {
            new UpahPekerja2(currentUser).setVisible(true);
            this.dispose();
        });
    }
    
    // Other methods remain the same as your original code
    public void displayData(List<Object[]> data) {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (Object[] row : data) {
            Object[] formattedRow = row.clone();
            
            if (row[2] instanceof Date) {
                formattedRow[2] = sdf.format((Date) row[2]);
            }
            
            tableModel.addRow(formattedRow);
        }
    }
    
    public void prepareUpdateForm(String nama, String tanggal, String noTelp, String alamat) {
        txtNama.setText(nama);
        txtMulaiBekerja.setText(tanggal);
        txtNoTelp.setText(noTelp);
        txtAlamat.setText(alamat);
    }
    
    public void clearForm() {
        txtId.setText("");
        txtNama.setText("");
        txtMulaiBekerja.setText("");
        txtNoTelp.setText("");
        txtAlamat.setText("");
    }
    
    public boolean confirmAction(String message) {
        return JOptionPane.showConfirmDialog(this, 
            message, "Konfirmasi", 
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public void showError(String error) {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }
}