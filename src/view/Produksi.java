package view;

import model.*;
import controller.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

public class Produksi extends JFrame {
    // Color Palette
    private static final Color MIDNIGHT_BLUE = new Color(25, 42, 86);
    private static final Color SKY_BLUE = new Color(135, 206, 235);
    private static final Color LIGHT_BLUE = new Color(173, 216, 230);
    private static final Color WHITE = Color.WHITE;
    private static final Color BLACK = Color.BLACK;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private ProduksiController controller;
    private JPanel formPanel;
    private JButton toggleInputBtn;
    private JComboBox<String> produkCombo;
    private JComboBox<String> pekerjaCombo;
    private JTextField batchField;
    private JTextField kgField;
    private Map<String, Integer> produkMap;
    private Map<String, Integer> pekerjaMap;
    private HamburgerMenu hamburgerMenu;
    private User currentUser;

    public Produksi(User currentUser) {
        this.controller = new ProduksiController();
        this.currentUser = currentUser;
        setTitle("Produksi");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        loadData();
    }

    private void initComponents() {
        // Main panel setup
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BLUE);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

         // Navbar dan drawer
        hamburgerMenu = new HamburgerMenu(this, currentUser);
        getContentPane().add(hamburgerMenu.getNavBar(), BorderLayout.NORTH);

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(LIGHT_BLUE);
        titlePanel.setBorder(new EmptyBorder(0, 10, 15, 0));
        
        JLabel titleLabel = new JLabel("PRODUKSI");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(MIDNIGHT_BLUE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Table setup
        String[] kolom = {"ID", "Tanggal", "Produk", "Pegawai", "Jumlah Batch", "Total Kg", "Jumlah Kemasan"};
        tableModel = new DefaultTableModel(null, kolom) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        styleTable();
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(MIDNIGHT_BLUE, 1, true));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Circle add button
        toggleInputBtn = new CircleButton("+");
        toggleInputBtn.addActionListener(e -> toggleFormPanel());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(LIGHT_BLUE);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 10, 10));
        buttonPanel.add(toggleInputBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Form panel (compact size)
        formPanel = createFormPanel();
        formPanel.setVisible(false);
        mainPanel.add(formPanel, BorderLayout.EAST);

        getContentPane().add(mainPanel);
    }
    
    private JPanel createFormPanel() {
    JPanel panel = new JPanel();
    panel.setPreferredSize(new Dimension(300, 350)); // Slightly wider but still compact
    panel.setBackground(LIGHT_BLUE);
    panel.setBorder(new MatteBorder(0, 1, 0, 0, MIDNIGHT_BLUE));
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
    // Title with close button
    JLabel title = new JLabel("Tambah Produksi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(MIDNIGHT_BLUE);
        title.setBorder(new EmptyBorder(15, 15, 15, 15));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
    
    // Form content panel
    JPanel formContent = new JPanel();
    formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
    formContent.setBackground(LIGHT_BLUE);
    formContent.setBorder(new EmptyBorder(5, 15, 5, 15));
    
    // Load combobox data
    produkCombo = new JComboBox<>();
    produkMap = controller.loadResep(produkCombo);
    styleComboBox(produkCombo);
    
    pekerjaCombo = new JComboBox<>();
    pekerjaMap = controller.loadPekerja(pekerjaCombo);
    styleComboBox(pekerjaCombo);
    
    batchField = new JTextField();
    styleTextField(batchField);
    batchField.getDocument().addDocumentListener(new DocumentListener() {
        @Override public void insertUpdate(DocumentEvent e) { updateKgValue(); }
        @Override public void removeUpdate(DocumentEvent e) { updateKgValue(); }
        @Override public void changedUpdate(DocumentEvent e) { updateKgValue(); }
    });
    
    kgField = new JTextField("0 kg");
    kgField.setEditable(false);
    styleTextField(kgField);
    
    // Add form components with proper spacing
    formContent.add(createFormLabel("Produk:"));
    formContent.add(Box.createRigidArea(new Dimension(0, 5)));
    formContent.add(produkCombo);
    formContent.add(Box.createRigidArea(new Dimension(0, 10)));
    
    formContent.add(createFormLabel("Pekerja:"));
    formContent.add(Box.createRigidArea(new Dimension(0, 5)));
    formContent.add(pekerjaCombo);
    formContent.add(Box.createRigidArea(new Dimension(0, 10)));
    
    formContent.add(createFormLabel("Jumlah Batch:"));
    formContent.add(Box.createRigidArea(new Dimension(0, 5)));
    formContent.add(batchField);
    formContent.add(Box.createRigidArea(new Dimension(0, 10)));
    
    formContent.add(createFormLabel("Total Kg:"));
    formContent.add(Box.createRigidArea(new Dimension(0, 5)));
    formContent.add(kgField);
    
    panel.add(formContent);
    panel.add(Box.createVerticalGlue());
    
    // Submit button
    JButton submitBtn = new JButton("Simpan Produksi");
    submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    submitBtn.setMaximumSize(new Dimension(200, 35));
    submitBtn.setBackground(SKY_BLUE);
    submitBtn.setForeground(MIDNIGHT_BLUE);
    submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
    submitBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
    submitBtn.addActionListener(e -> {
        try {
            String produk = (String) produkCombo.getSelectedItem();
            String pekerja = (String) pekerjaCombo.getSelectedItem();
            int batch = Integer.parseInt(batchField.getText());
            double kg = batch * 12; // 1 batch = 12 kg
            int idPekerja = pekerjaMap.get(pekerja);
            
            if (controller.prosesInputProduksi(produkMap.get(produk), produk, batch, kg, idPekerja)) {
                JOptionPane.showMessageDialog(this, "Produksi berhasil disimpan!");
                toggleFormPanel();
                loadData();
                clearForm();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    });
    
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(LIGHT_BLUE);
    buttonPanel.setBorder(new EmptyBorder(10, 0, 15, 0));
    buttonPanel.add(submitBtn);
    panel.add(buttonPanel);
    
    return panel;
}

private JLabel createFormLabel(String text) {
    JLabel label = new JLabel(text);
    label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    label.setForeground(MIDNIGHT_BLUE);
    label.setAlignmentX(Component.LEFT_ALIGNMENT);
    return label;
}

private void styleComboBox(JComboBox<String> combo) {
    combo.setMaximumSize(new Dimension(250, 30));
    combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    combo.setBackground(WHITE);
    combo.setForeground(MIDNIGHT_BLUE);
    combo.setBorder(BorderFactory.createCompoundBorder(
        new LineBorder(MIDNIGHT_BLUE, 1),
        new EmptyBorder(0, 8, 0, 8)
    ));
}

private void styleTextField(JTextField field) {
    field.setMaximumSize(new Dimension(250, 30));
    field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    field.setBackground(WHITE);
    field.setForeground(MIDNIGHT_BLUE);
    field.setBorder(BorderFactory.createCompoundBorder(
        new LineBorder(MIDNIGHT_BLUE, 1),
        new EmptyBorder(0, 8, 0, 8)
    ));
}

// New compact form row creator
private JPanel createCompactFormRow(String label, JComponent field) {
    JPanel panel = new JPanel(new BorderLayout(5, 0)); // Reduced horizontal gap
    panel.setBackground(LIGHT_BLUE);
    panel.setBorder(new EmptyBorder(0, 15, 0, 15)); // Reduced side padding
    
    JLabel lbl = new JLabel(label);
    lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    lbl.setForeground(MIDNIGHT_BLUE);
    lbl.setPreferredSize(new Dimension(70, 20)); // Fixed label width
    
    panel.add(lbl, BorderLayout.WEST);
    panel.add(field, BorderLayout.CENTER);
    
    return panel;
}

private void updateKgValue() {
    try {
        int batch = Integer.parseInt(batchField.getText());
        kgField.setText((batch * 12) + " kg"); // 1 batch = 12 kg
    } catch (NumberFormatException e) {
        kgField.setText("0 kg");
    }
}

    private void clearForm() {
        batchField.setText("");
        kgField.setText("0 kg");
        produkCombo.setSelectedIndex(0);
        pekerjaCombo.setSelectedIndex(0);
    }

    private void styleTable() {
        table.setShowGrid(true);
        table.setGridColor(MIDNIGHT_BLUE);
        table.setRowHeight(30);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(MIDNIGHT_BLUE);
        header.setForeground(MIDNIGHT_BLUE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Cell styling
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, 
                    isSelected, hasFocus, row, column);
                c.setBackground(LIGHT_BLUE);
                c.setForeground(BLACK);
                if (column >= 3) { // Right-align numeric columns
                    ((JLabel)c).setHorizontalAlignment(JLabel.RIGHT);
                }
                return c;
            }
        });
        table.setBackground(WHITE);
        table.setFillsViewportHeight(true);
    }
    
    class CircleButton extends JButton {
        public CircleButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setBackground(MIDNIGHT_BLUE);
            setForeground(WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 20));
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
    
    private JPanel createFormRow(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(LIGHT_BLUE);
        panel.setBorder(new EmptyBorder(0, 20, 0, 20));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(MIDNIGHT_BLUE);
        
        field.setPreferredSize(new Dimension(150, 25));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (field instanceof JTextField) {
            ((JTextField)field).setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(MIDNIGHT_BLUE, 1),
                new EmptyBorder(3, 8, 3, 8)
            ));
        }
        
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void toggleFormPanel() {
        formPanel.setVisible(!formPanel.isVisible());
        revalidate();
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<ProduksiModel> list = controller.getAllProduksi();
        for (ProduksiModel p : list) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getTanggal(),
                p.getProduk(),
                p.getPekerja(),
                p.getJumlahBatch(),
                String.format("%.2f kg", p.getTotalKg()),
                p.getJumlahKemasan() + " pcs"
            });
        }
    }
}