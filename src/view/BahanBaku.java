package view;

import controller.BahanBakuController;
import model.BahanBakuModel;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class BahanBaku extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private BahanBakuController controller;
    private User currentUser;
    private HamburgerMenu hamburgerMenu;
    private JPanel inputPanel;
    private JButton toggleInputBtn;

    public BahanBaku(User currentUser) {
        this.currentUser = currentUser;
        this.controller = new BahanBakuController();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 1. Hamburger Menu
        hamburgerMenu = new HamburgerMenu(this, currentUser);
        add(hamburgerMenu.getNavBar(), BorderLayout.NORTH);
        add(hamburgerMenu.getDrawerPanel(), BorderLayout.WEST);

        // 2. Main Content
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Table setup
        String[] columns = {"ID", "Nama Bahan", "Stok", "Satuan", "Harga", "Tgl Beli", "Tgl Kadaluwarsa"};
        tableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 3. Floating Action Button
        toggleInputBtn = new JButton("+") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 150, 136)); // Teal color
                g2.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g2);
            }
        };
        toggleInputBtn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        toggleInputBtn.setForeground(Color.WHITE);
        toggleInputBtn.setBorderPainted(false);
        toggleInputBtn.setContentAreaFilled(false);
        toggleInputBtn.setFocusPainted(false);
        toggleInputBtn.setPreferredSize(new Dimension(56, 56));
        toggleInputBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Position FAB
        JPanel fabPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        fabPanel.setOpaque(false);
        fabPanel.add(toggleInputBtn);
        fabPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 20));
        
        // 4. Input Panel (RIGHT SIDEBAR)
        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(new Color(250, 250, 250));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(224, 224, 224)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        inputPanel.setPreferredSize(new Dimension(320, getHeight()));
        inputPanel.setVisible(false);

        // Input Panel Components
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Tambah Bahan Baku");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        JButton closeBtn = new JButton("×");
        closeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setForeground(new Color(100, 100, 100));
        closeBtn.addActionListener(e -> toggleInputPanel());
        
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(closeBtn, BorderLayout.EAST);
        inputPanel.add(headerPanel);
        inputPanel.add(Box.createVerticalStrut(20));

        // Form fields
        JTextField namaField = new JTextField();
        JTextField stokField = new JTextField();
        JTextField satuanField = new JTextField();
        JTextField hargaField = new JTextField();
        JTextField tglBeliField = new JTextField("YYYY-MM-DD");
        JTextField tglKadaluarsaField = new JTextField("YYYY-MM-DD");

        inputPanel.add(createFormField("Nama Bahan", namaField));
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(createFormField("Stok", stokField));
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(createFormField("Satuan", satuanField));
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(createFormField("Harga/Unit", hargaField));
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(createFormField("Tanggal Beli", tglBeliField));
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(createFormField("Tanggal Kadaluwarsa", tglKadaluarsaField));
        inputPanel.add(Box.createVerticalStrut(30));

        // Submit button
        JButton submitBtn = new JButton("SIMPAN");
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.setBackground(new Color(0, 150, 136)); // Matching teal
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setBorderPainted(false);
        submitBtn.setFocusPainted(false);
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.setMaximumSize(new Dimension(200, 40));
        submitBtn.addActionListener(e -> saveBahanBaku(
            namaField.getText(),
            stokField.getText(),
            satuanField.getText(),
            hargaField.getText(),
            tglBeliField.getText(),
            tglKadaluarsaField.getText()
        ));

        inputPanel.add(submitBtn);
        inputPanel.add(Box.createVerticalGlue());

        // Layered layout
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new BorderLayout());
        layeredPane.add(mainPanel, BorderLayout.CENTER);
        layeredPane.add(fabPanel, BorderLayout.SOUTH);
        
        add(layeredPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.EAST);

        // Button action
        toggleInputBtn.addActionListener(e -> toggleInputPanel());
    }

    private JPanel createFormField(String label, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(250, 250, 250));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        component.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        component.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        
        if (component instanceof JTextField) {
            ((JTextField)component).setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }
        
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(5));
        panel.add(component);

        return panel;
    }

    private void toggleInputPanel() {
        inputPanel.setVisible(!inputPanel.isVisible());
        revalidate();
        repaint();
    }

    private void saveBahanBaku(String nama, String stok, String satuan, String harga, String tglBeli, String tglKadaluarsa) {
        try {
            BahanBakuModel bahan = new BahanBakuModel();
            bahan.setNama(nama);
            bahan.setStok(Double.parseDouble(stok));
            bahan.setSatuan(satuan);
            bahan.setHargaPerUnit(Double.parseDouble(harga));
            bahan.setTanggalBeli(tglBeli);
            bahan.setTanggalKadaluarsa(tglKadaluarsa);

            if (controller.tambahBahan(bahan)) {
                JOptionPane.showMessageDialog(this, "Bahan baku berhasil ditambahkan");
                toggleInputPanel();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambah bahan baku");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Format angka tidak valid");
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<BahanBakuModel> list = controller.getAllBahan();
        for (BahanBakuModel b : list) {
            tableModel.addRow(new Object[]{
                b.getId(),
                b.getNama(),
                b.getStok(),
                b.getSatuan(),
                b.getHargaPerUnit(),
                b.getTanggalBeli(),
                b.getTanggalKadaluarsa()
            });
        }
    }
}