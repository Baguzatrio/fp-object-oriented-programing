package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import controller.DataCustomerController;
import model.DataCustomerModel;
import model.User;

public class DataCustomer2 extends JFrame {
    // Components
    private JTable tableCustomer;
    private JTextField txtId, txtNama, txtNoTelp, txtAlamat, txtSearch;
    private JButton btnSimpan, btnHapus, btnCari, btnUbah, btnKembali;
    
    public DataCustomer2() {
        initializeUI();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    private void initializeUI() {
        setTitle("Data Customer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(240, 248, 255); // AliceBlue
                Color color2 = new Color(230, 230, 250); // Lavender
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Title
        JLabel lblTitle = new JLabel("DATA CUSTOMER", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(70, 130, 180)); // SteelBlue
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        
        // Center content panel
        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        
        // Form panel
        JPanel formPanel = createFormPanel();
        centerPanel.add(formPanel, BorderLayout.WEST);
        
        // Table panel
        JPanel tablePanel = createTablePanel();
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Bottom button panel
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new CompoundBorder(
            new TitledBorder(
                new LineBorder(new Color(100, 149, 237), 2), 
                "Form Customer",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(70, 130, 180)
            ),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setOpaque(false);
        
        // Form fields
        JPanel[] rows = new JPanel[4];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            rows[i].setOpaque(false);
        }
        
        // ID
        JLabel lblId = new JLabel("ID:");
        lblId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtId = createStyledTextField(false);
        rows[0].add(lblId);
        rows[0].add(txtId);
        
        // Nama
        JLabel lblNama = new JLabel("Nama:");
        lblNama.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNama = createStyledTextField(true);
        rows[1].add(lblNama);
        rows[1].add(txtNama);
        
        // No. Telp
        JLabel lblNoTelp = new JLabel("No. Telp:");
        lblNoTelp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNoTelp = createStyledTextField(true);
        rows[2].add(lblNoTelp);
        rows[2].add(txtNoTelp);
        
        // Alamat
        JLabel lblAlamat = new JLabel("Alamat:");
        lblAlamat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtAlamat = createStyledTextField(true);
        rows[3].add(lblAlamat);
        rows[3].add(txtAlamat);
        
        // Add rows to form
        for (JPanel row : rows) {
            panel.add(row);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setOpaque(false);
        
        btnSimpan = createStyledButton("Simpan", new Color(34, 139, 34)); // ForestGreen
        btnHapus = createStyledButton("Hapus", new Color(178, 34, 34)); // FireBrick
        
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnHapus);
        
        panel.add(buttonPanel);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new CompoundBorder(
            new TitledBorder(
                new LineBorder(new Color(100, 149, 237), 2),
                "Daftar Customer",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(70, 130, 180)
            ),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setOpaque(false);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setOpaque(false);
        
        txtSearch = createStyledTextField(true);
        txtSearch.setPreferredSize(new Dimension(200, 30));
        
        btnCari = createStyledButton("Cari", new Color(70, 130, 180)); // SteelBlue
        searchPanel.add(new JLabel("Pencarian:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnCari);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Table
        tableCustomer = new JTable();
        tableCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableCustomer.setRowHeight(25);
        tableCustomer.setSelectionBackground(new Color(135, 206, 250)); // LightSkyBlue
        tableCustomer.setSelectionForeground(Color.BLACK);
        tableCustomer.setGridColor(new Color(200, 200, 200));
        
        JScrollPane scrollPane = new JScrollPane(tableCustomer);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setOpaque(false);
        
        btnUbah = createStyledButton("Ubah", new Color(218, 165, 32)); // GoldenRod
        btnKembali = createStyledButton("Kembali", new Color(119, 136, 153)); // LightSlateGray
        
        panel.add(btnUbah);
        panel.add(btnKembali);
        
        return panel;
    }
    
    private JTextField createStyledTextField(boolean editable) {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(new CompoundBorder(
            new LineBorder(new Color(150, 150, 150)),
            new EmptyBorder(5, 5, 5, 5)
        ));
        textField.setEditable(editable);
        return textField;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
            new LineBorder(bgColor.darker(), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
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
    
    // Maintain all original getter methods
    public JTextField getTextField1() { return txtId; }
    public JTextField getTextField2() { return txtNama; }
    public JTextField getTextField3() { return txtNoTelp; }
    public JTextField getTextField4() { return txtAlamat; }
    public JTextField getTextField5() { return txtSearch; }
    public JTable getTableCustomer() { return tableCustomer; }
    public JScrollPane getTableScrollPane() { return (JScrollPane)tableCustomer.getParent().getParent(); }
    
    // Maintain all original listener methods
    public void addAddButtonListener(ActionListener listener) {
        btnSimpan.addActionListener(listener);
    }
    
    public void addDeleteButtonListener(ActionListener listener) {
        btnHapus.addActionListener(listener);
    }
    
    public void addSearchButtonListener(ActionListener listener) {
        btnCari.addActionListener(listener);
    }
    
    public void addEditButtonListener(ActionListener listener) {
        btnUbah.addActionListener(listener);
    }
    
    public void addBackButtonListener(ActionListener listener) {
        btnKembali.addActionListener(listener);
    }
    
    // Maintain all original dialog methods
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Informasi", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public int showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Konfirmasi", 
            JOptionPane.YES_NO_OPTION);
    }
    
    public javax.swing.JTextField getSearchField() {
    return txtSearch;
    }
}