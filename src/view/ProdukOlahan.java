package view;

import controller.ProdukOlahanController;
import model.ProdukOlahanModel;
import model.User;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdukOlahan extends JFrame {
    // Color Palette
    private static final Color MIDNIGHT_BLUE = new Color(25, 42, 86);
    private static final Color SKY_BLUE = new Color(135, 206, 235);
    private static final Color LIGHT_BLUE = new Color(173, 216, 230);
    private static final Color WHITE = Color.WHITE;
    private static final Color BLACK = Color.BLACK;
    private static final Color TABLE_ROW = new Color(240, 248, 255);
    
    private JTable table;
    private DefaultTableModel tableModel;
    private ProdukOlahanController controller;
    private JPanel formPanel;
    private HamburgerMenu hamburgerMenu;
    private User currentUser;
    private Map<String, JTextField> formFields = new HashMap<>(); 

    public ProdukOlahan(User currentUser) {
        this.controller = new ProdukOlahanController();
        this.currentUser = currentUser;
        setTitle("Produk Olahan");
        setSize(900, 600);
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
        
        JLabel titleLabel = new JLabel("PRODUK OLAHAN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(MIDNIGHT_BLUE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Table setup with stok column
        String[] kolom = {"ID", "Nama Produk", "Ukuran (gr)", "Stok", "Harga", "Upah"};
        tableModel = new DefaultTableModel(null, kolom) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        styleTable();
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(MIDNIGHT_BLUE, 1, true));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Circle add button
        JButton addButton = new CircleButton("+");
        addButton.addActionListener(e -> toggleFormPanel());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(LIGHT_BLUE);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 10, 10));
        buttonPanel.add(addButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Compact form panel (not full height)
        formPanel = createFormPanel();
        formPanel.setVisible(false);
        mainPanel.add(formPanel, BorderLayout.EAST);

        getContentPane().add(mainPanel);
    }

    private void styleTable() {
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setGridColor(MIDNIGHT_BLUE);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Midnight blue header
        JTableHeader header = table.getTableHeader();
        header.setBackground(MIDNIGHT_BLUE);
        header.setForeground(MIDNIGHT_BLUE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false);
        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(LIGHT_BLUE);
                c.setForeground(BLACK);
                
                if (column >= 3) {
                    ((JLabel)c).setHorizontalAlignment(JLabel.RIGHT);
                }
                if (isSelected) {
                    c.setBackground(SKY_BLUE);
                    c.setForeground(BLACK);
                }
                return c;
            }
        });
        table.setOpaque(true);
        table.setFillsViewportHeight(true);
    }

    // Circle Button Class
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

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(250, 400)); // Fixed height, not full
        panel.setBackground(LIGHT_BLUE);
        panel.setBorder(new MatteBorder(0, 1, 0, 0, MIDNIGHT_BLUE));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Title
        JLabel title = new JLabel("Tambah Produk");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(MIDNIGHT_BLUE);
        title.setBorder(new EmptyBorder(15, 15, 15, 15));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        
        // Compact form fields
        panel.add(createCompactField("Nama Produk"));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createCompactField("Ukuran (gram)"));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createCompactField("Harga"));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createCompactField("Upah"));
        panel.add(Box.createVerticalGlue());
        
        // Submit button
        JButton submitBtn = new JButton("SIMPAN");
        submitBtn.setBackground(SKY_BLUE);
        submitBtn.setForeground(MIDNIGHT_BLUE);
        submitBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        submitBtn.setBorder(new EmptyBorder(5, 15, 5, 15));
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(LIGHT_BLUE);
        btnPanel.add(submitBtn);
        panel.add(btnPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JTextField namaField = ((JTextField)((JPanel)panel.getComponent(1)).getComponent(1));
        JTextField ukuranField = ((JTextField)((JPanel)panel.getComponent(3)).getComponent(1));
        JTextField hargaField = ((JTextField)((JPanel)panel.getComponent(5)).getComponent(1));
        JTextField upahField = ((JTextField)((JPanel)panel.getComponent(7)).getComponent(1));

    submitBtn.addActionListener(e -> {
        try {
            // Validasi input
            String nama = namaField.getText().trim();
            int ukuran = Integer.parseInt(ukuranField.getText().trim());
            int harga = Integer.parseInt(hargaField.getText().trim());
            int upah = Integer.parseInt(upahField.getText().trim());
            
            if (nama.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama produk tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Simpan ke database via controller
            boolean success = controller.tambahProduk(nama, ukuran, harga, upah);
            
            if (success) {
                // Clear field
                namaField.setText("");
                ukuranField.setText("");
                hargaField.setText("");
                upahField.setText("");
                
                // Refresh tabel
                loadData();
                
                // Sembunyikan form
                formPanel.setVisible(false);
                revalidate();
                
                JOptionPane.showMessageDialog(this, "Produk berhasil ditambahkan", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan produk", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Input angka tidak valid", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    });
        return panel;
    }

    private JPanel createCompactField(String label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BLUE);
        panel.setBorder(new EmptyBorder(0, 15, 0, 15));
        panel.setMaximumSize(new Dimension(200, 70));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(MIDNIGHT_BLUE);
        
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(150, 25));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MIDNIGHT_BLUE, 1),
            BorderFactory.createEmptyBorder(3, 5, 3, 5)
        ));
        formFields.put(label.toLowerCase(), field);
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void toggleFormPanel() {
    if (!formPanel.isVisible()) {
        // Clear semua field saat form ditampilkan
        formFields.forEach((key, field) -> field.setText(""));
    }
    formPanel.setVisible(!formPanel.isVisible());
    revalidate();
}

    private void loadData() {
        tableModel.setRowCount(0);
        List<ProdukOlahanModel> list = controller.getAllProduk();
        for (ProdukOlahanModel p : list) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getNamaProduk(),
                p.getUkuranKemasan() + " gr",
                p.getStok() + " pcs",  // Stok column
                "Rp " + String.format("%,d", p.getHarga()),
                "Rp " + String.format("%,d", p.getUpah())
            });
        }
    }
}