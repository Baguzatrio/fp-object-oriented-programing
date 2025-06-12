package view;

import model.*;
import controller.*;
import view.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Produksi extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private ProduksiController controller;
    private User currentUser;
    private HamburgerMenu hamburgerMenu;

    private JPanel inputPanel;
    private JButton toggleInputBtn;

    public Produksi(User currentUser) {
        this.currentUser = currentUser;
        this.controller = new ProduksiController();
        hamburgerMenu = new HamburgerMenu(this, currentUser);
        setLayout(new BorderLayout());

        // Navbar dan Drawer
        add(hamburgerMenu.getNavBar(), BorderLayout.NORTH);
        add(hamburgerMenu.getDrawerPanel(), BorderLayout.WEST);

        // Main Panel (isi konten)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel Tabel
        JPanel panelTabel = new JPanel(new BorderLayout());
        String[] kolom = {"ID", "Tanggal", "Produk", "Jumlah Batch", "Total Kg", "Jumlah Kemasan"};
        tableModel = new DefaultTableModel(null, kolom);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panelTabel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(panelTabel);

        // Ganti kode floating button dengan ini:
toggleInputBtn = new JButton("+") {
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillOval(0, 0, getWidth(), getHeight());
        super.paintComponent(g2);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, 50);
    }
};

// Style tombol
toggleInputBtn.setBackground(new Color(70, 130, 180));
toggleInputBtn.setForeground(Color.WHITE);
toggleInputBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
toggleInputBtn.setBorder(BorderFactory.createEmptyBorder());
toggleInputBtn.setContentAreaFilled(false);
toggleInputBtn.setFocusPainted(false);
toggleInputBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

// Posisikan tombol di atas input panel
inputPanel = createInputPanel();
JLayeredPane layeredPane = new JLayeredPane();
layeredPane.setPreferredSize(new Dimension(300, 600)); // Sesuaikan dengan ukuran inputPanel
layeredPane.add(inputPanel, JLayeredPane.DEFAULT_LAYER);
layeredPane.add(toggleInputBtn, JLayeredPane.PALETTE_LAYER);

// Tambahkan ke layout
JPanel eastPanel = new JPanel(new BorderLayout());
eastPanel.add(layeredPane, BorderLayout.CENTER);
add(eastPanel, BorderLayout.EAST);

        toggleInputBtn.addActionListener(e -> showInputPanel());

        // Layered Pane untuk menumpuk tombol di pojok kanan bawah
// Panel utama dengan tombol ➕ mengambang
JPanel contentPanel = new JPanel(new BorderLayout());
contentPanel.add(mainPanel, BorderLayout.CENTER);

// Panel tombol ➕ di kanan bawah
JPanel floatingButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
floatingButtonPanel.setOpaque(false); // transparan
floatingButtonPanel.add(toggleInputBtn);

contentPanel.add(floatingButtonPanel, BorderLayout.SOUTH);
add(contentPanel, BorderLayout.CENTER);


        // Panel input produksi dari kanan
        
        add(inputPanel, BorderLayout.EAST);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
    new LineBorder(new Color(200, 200, 200)), // border luar (line)
    new EmptyBorder(20, 30, 20, 20)           // border dalam (padding)
));

        panel.setBackground(new Color(240, 248, 255)); // biru muda
        panel.setPreferredSize(new Dimension(300, 600));
        panel.setVisible(false);

        JLabel title = new JLabel("Input Produksi");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JComboBox<String> resepCombo = new JComboBox<>();
        Map<String, Integer> resepMap = controller.loadResep(resepCombo);
        JTextField batchField = new JTextField();
        JTextField kgField = new JTextField();

        JButton simpanBtn = new JButton("Simpan");
        JButton closeBtn = new JButton("✖️");
        closeBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        closeBtn.addActionListener(e -> inputPanel.setVisible(false));

        simpanBtn.addActionListener(e -> {
            try {
                String produk = (String) resepCombo.getSelectedItem();
                int resepId = resepMap.get(produk);
                int batch = Integer.parseInt(batchField.getText());
                double kg = Double.parseDouble(kgField.getText());

                boolean success = controller.prosesInputProduksi(resepId, produk, batch, kg);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Produksi berhasil disimpan.");
                    inputPanel.setVisible(false);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Stok bahan baku tidak mencukupi.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Input tidak valid.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan produksi: " + ex.getMessage());
            }
        });

        // Rangkai komponen
        panel.add(closeBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        int fieldHeight = 30;

JLabel labelProduk = new JLabel("Produk:");
labelProduk.setAlignmentX(Component.LEFT_ALIGNMENT);
resepCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, fieldHeight));

JLabel labelBatch = new JLabel("Jumlah Batch:");
labelBatch.setAlignmentX(Component.LEFT_ALIGNMENT);
batchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, fieldHeight));

JLabel labelKg = new JLabel("Total Kilogram:");
labelKg.setAlignmentX(Component.LEFT_ALIGNMENT);
kgField.setMaximumSize(new Dimension(Integer.MAX_VALUE, fieldHeight));

simpanBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
simpanBtn.setPreferredSize(new Dimension(100, 35));

panel.add(labelProduk);
panel.add(Box.createVerticalStrut(5));
panel.add(resepCombo);
panel.add(Box.createVerticalStrut(15));

panel.add(labelBatch);
panel.add(Box.createVerticalStrut(5));
panel.add(batchField);
panel.add(Box.createVerticalStrut(15));

panel.add(labelKg);
panel.add(Box.createVerticalStrut(5));
panel.add(kgField);
panel.add(Box.createVerticalStrut(20));

panel.add(simpanBtn);

        return panel;
    }

    private void showInputPanel() {
    inputPanel.setVisible(true);
    
    // Animasi slide dan snap tombol
    Timer timer = new Timer(10, new ActionListener() {
        int xPos = -25; // Posisi akhir (separuh masuk)
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (toggleInputBtn.getX() < xPos) {
                toggleInputBtn.setLocation(
                    toggleInputBtn.getX() + 2, 
                    toggleInputBtn.getY()
                );
            } else {
                ((Timer)e.getSource()).stop();
            }
        }
    });
    timer.start();
}

    public void loadData() {
        tableModel.setRowCount(0);
        List<ProduksiModel> list = controller.getAllProduksi();
        for (ProduksiModel p : list) {
            Object[] row = {
                p.getId(), p.getTanggal(), p.getProduk(),
                p.getJumlahBatch(), p.getTotalKg(), p.getJumlahKemasan()
            };
            tableModel.addRow(row);
        }
    }

}
