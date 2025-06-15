package view;

import controller.PengeluaranLainController;
import model.Pengeluaran;
import model.User;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PengeluaranLain extends JFrame {
    // Color Palette (same as ProdukOlahan)
    private static final Color MIDNIGHT_BLUE = new Color(25, 42, 86);
    private static final Color SKY_BLUE = new Color(135, 206, 235);
    private static final Color LIGHT_BLUE = new Color(173, 216, 230);
    private static final Color WHITE = Color.WHITE;
    private static final Color BLACK = Color.BLACK;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private PengeluaranLainController controller;
    private JPanel formPanel;
    private HamburgerMenu hamburgerMenu;
    private User currentUser;

    public PengeluaranLain(User currentUser, Connection conn) {
        try {
        this.controller = new PengeluaranLainController(conn);
        this.currentUser = currentUser;
        setTitle("Pengeluaran Lain");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Inisialisasi controller: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
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
        
        JLabel titleLabel = new JLabel("PENGELUARAN LAIN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(MIDNIGHT_BLUE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Table setup
        String[] kolom = {"Tanggal", "Jenis", "Kategori", "Jumlah", "Keterangan", "Aksi"};
        tableModel = new DefaultTableModel(null, kolom) {
            @Override 
            public boolean isCellEditable(int row, int column) { 
                return column == 5; // Hanya kolom aksi yang editable
            }
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

        // Form panel
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
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(MIDNIGHT_BLUE);
        header.setForeground(WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Cell styling
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? WHITE : new Color(240, 248, 255));
                c.setForeground(BLACK);
                
                if (column == 3) { // Kolom Jumlah
                    ((JLabel)c).setHorizontalAlignment(JLabel.RIGHT);
                }
                if (isSelected) {
                    c.setBackground(SKY_BLUE);
                }
                return c;
            }
        });

        // Kolom Aksi
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    // Custom button renderer
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(MIDNIGHT_BLUE);
            setForeground(WHITE);
            setText("Hapus");
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom button editor
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                int id = (int) table.getValueAt(currentRow, 0);
                hapusData(id);
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            return button;
        }
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(250, 400));
        panel.setBackground(LIGHT_BLUE);
        panel.setBorder(new MatteBorder(0, 1, 0, 0, MIDNIGHT_BLUE));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Title
        JLabel title = new JLabel("Tambah Pengeluaran");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(MIDNIGHT_BLUE);
        title.setBorder(new EmptyBorder(15, 15, 15, 15));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        
        // Form fields
        panel.add(createFormField("Tanggal", new JTextField(LocalDate.now().toString())));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createFormField("Jenis", new JComboBox<>(new String[]{"Operasional", "Peralatan", "Lainnya"})));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createFormField("Kategori", new JTextField()));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createFormField("Jumlah", new JTextField()));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createFormField("Keterangan", new JTextField()));
        panel.add(Box.createVerticalGlue());
        
        // Submit button
        JButton submitBtn = new JButton("SIMPAN");
        submitBtn.setBackground(SKY_BLUE);
        submitBtn.setForeground(MIDNIGHT_BLUE);
        submitBtn.addActionListener(e -> simpanData());
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(submitBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        return panel;
    }

    private JPanel createFormField(String label, JComponent input) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BLUE);
        panel.setBorder(new EmptyBorder(0, 15, 0, 15));
        panel.setMaximumSize(new Dimension(230, 60));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(MIDNIGHT_BLUE);
        
        input.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (input instanceof JTextField) {
            input.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MIDNIGHT_BLUE, 1),
                BorderFactory.createEmptyBorder(3, 5, 3, 5)
            ));
        }
        
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(input, BorderLayout.CENTER);
        return panel;
    }

    private void toggleFormPanel() {
        formPanel.setVisible(!formPanel.isVisible());
        revalidate();
    }

    private void loadData() {
        try {
            tableModel.setRowCount(0);
            List<Pengeluaran> data = controller.getAllPengeluaran();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            
            for (Pengeluaran p : data) {
                tableModel.addRow(new Object[]{
                    p.getTanggal().format(formatter),
                    p.getJenis(),
                    p.getKategori(),
                    "Rp " + String.format("%,d", p.getJumlah()),
                    p.getKeterangan(),
                    p.getId() // Untuk operasi hapus
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void simpanData() {
        try {
            // Ambil data dari form
            LocalDate tanggal = LocalDate.now(); // Implementasi date picker bisa ditambahkan
            String jenis = ((JComboBox<?>)formPanel.getComponent(3)).getSelectedItem().toString();
            String kategori = ((JTextField)((JPanel)formPanel.getComponent(5)).getComponent(1)).getText();
            int jumlah = Integer.parseInt(((JTextField)((JPanel)formPanel.getComponent(7)).getComponent(1)).getText());
            String keterangan = ((JTextField)((JPanel)formPanel.getComponent(9)).getComponent(1)).getText();

            // Simpan ke database
            controller.tambahPengeluaran(tanggal, jenis, kategori, jumlah, keterangan);
            
            // Refresh tabel
            loadData();
            formPanel.setVisible(false);
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                "Gagal Simpan", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusData(int id) {
        try {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                controller.hapusPengeluaran(id);
                loadData();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Circle Button Class (same as ProdukOlahan)
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
}