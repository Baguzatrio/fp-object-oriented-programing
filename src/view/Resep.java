package view;

import controller.ResepController;
import model.ResepModel;
import model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class Resep extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private ResepController controller;
    private HamburgerMenu hamburgerMenu;
    private User currentUser;

    public Resep(User user) {
        this.currentUser = user;
        this.controller = new ResepController();

        setTitle("Manajemen Resep");
        setSize(900, 550); // Ukuran lebih besar
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set warna background utama
        getContentPane().setBackground(new Color(245, 245, 245));
        
        initComponents();
        loadResep();
    }

    private void initComponents() {
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    mainPanel.setBackground(new Color(245, 245, 245));
    setContentPane(mainPanel);

    // Hamburger Menu
    hamburgerMenu = new HamburgerMenu(mainPanel, currentUser);
    mainPanel.add(hamburgerMenu.getNavBar(), BorderLayout.NORTH);
    mainPanel.add(hamburgerMenu.getDrawerPanel(), BorderLayout.WEST);

    // Konten utama
    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBackground(Color.WHITE);
    contentPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(230, 230, 230)),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));

    // Header
    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    headerPanel.setBackground(Color.WHITE);
    JLabel titleLabel = new JLabel("DAFTAR RESEP");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    titleLabel.setForeground(new Color(70, 70, 70));
    headerPanel.add(titleLabel);
    contentPanel.add(headerPanel, BorderLayout.NORTH);

    // Tabel
    String[] kolom = {"ID", "NAMA PRODUK"};
    tableModel = new DefaultTableModel(null, kolom) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    table = new JTable(tableModel);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setRowHeight(35);
    table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    table.setGridColor(new Color(200, 200, 200)); // grid color
    table.setShowGrid(true);
    table.setShowHorizontalLines(true);
    table.setShowVerticalLines(true);
    table.setIntercellSpacing(new Dimension(1, 1));

    // Header style
    JTableHeader header = table.getTableHeader();
    header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    header.setBackground(new Color(135, 206, 250)); // sky blue
    header.setForeground(Color.BLACK);
    header.setReorderingAllowed(false);
    header.setResizingAllowed(false);

    // Hindari efek hover pada header
    header.setDefaultRenderer(new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel(value.toString());
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));
            label.setBackground(new Color(135, 206, 250)); // sky blue
            label.setForeground(Color.BLACK);
            label.setOpaque(true);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        }
    });

    // Isi tabel light blue + zebra stripe
    table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, 
                    isSelected, hasFocus, row, column);
            if (isSelected) {
                c.setBackground(new Color(100, 180, 255)); // seleksi
            } else {
                c.setBackground(row % 2 == 0 ? new Color(224, 242, 255) : new Color(204, 232, 255)); // light blue stripes
            }
            return c;
        }
    });

    // Klik di luar tabel = deselect
    table.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {
            Point point = evt.getPoint();
            int row = table.rowAtPoint(point);
            if (row == -1) {
                table.clearSelection();
            }
        }
    });

    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    contentPanel.add(scrollPane, BorderLayout.CENTER);

    // Tombol
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

    JButton tambahBtn = createFlatButton("+ Tambah Resep", new Color(0, 150, 136));
    JButton detailBtn = createFlatButton("Lihat Detail", new Color(96, 125, 139));

    tambahBtn.addActionListener(e -> tambahResep());
    detailBtn.addActionListener(e -> bukaDetailResep());

    buttonPanel.add(detailBtn);
    buttonPanel.add(tambahBtn);
    contentPanel.add(buttonPanel, BorderLayout.SOUTH);

    mainPanel.add(contentPanel, BorderLayout.CENTER);
}

   private JButton createFlatButton(String text, Color bgColor) {
    JButton button = new JButton(text);
    button.setFont(new Font("Segoe UI", Font.BOLD, 13));
    button.setBackground(bgColor);
    button.setForeground(Color.black); 
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
    Color hoverColor = bgColor.darker(); // Ubah ke warna lebih gelap untuk hover
    
    button.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            button.setBackground(hoverColor);
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            button.setBackground(bgColor);
        }
    });
    
    return button;
}

    private void loadResep() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                tableModel.setRowCount(0);
                List<ResepModel> list = controller.getAllResep();
                for (ResepModel r : list) {
                    Object[] row = {r.getId(), r.getNamaProduk()};
                    tableModel.addRow(row);
                }
                return null;
            }
        };
        worker.execute();
    }

    private void tambahResep() {
        JTextField namaField = new JTextField();
        namaField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("Nama Produk:"));
        inputPanel.add(namaField);
        
        int result = JOptionPane.showConfirmDialog(this, inputPanel, 
            "Tambah Resep Baru", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION && !namaField.getText().isEmpty()) {
            if (controller.tambahResep(namaField.getText())) {
                loadResep();
                JOptionPane.showMessageDialog(this, 
                    "<html><b>Resep berhasil ditambahkan!</b></html>", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "<html>Gagal menambahkan resep.<br>Pastikan nama tidak duplikat.</html>", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void bukaDetailResep() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "<html><b>Pilih resep terlebih dahulu!</b></html>", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resepId = (int) tableModel.getValueAt(selectedRow, 0);
        String namaProduk = (String) tableModel.getValueAt(selectedRow, 1);

        // Buka frame detail
        SwingUtilities.invokeLater(() -> {
            ResepDetail detailFrame = new ResepDetail(resepId, namaProduk);
            detailFrame.setLocationRelativeTo(Resep.this);
            detailFrame.setVisible(true);
        });
    }
    
}