package view;

import controller.ProdukOlahanController;
import model.ProdukOlahanModel;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ProdukOlahan extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private ProdukOlahanController controller;
    private User currentUser;
    private HamburgerMenu hamburgerMenu;

    public ProdukOlahan(User currentUser) {
        this.currentUser = currentUser;
        this.controller = new ProdukOlahanController();
        initComponents();
        loadData();
    }

     private void initComponents() {
        setLayout(new BorderLayout());

        // 1. Hamburger Menu - gunakan this sebagai parent
        hamburgerMenu = new HamburgerMenu(this, currentUser); // Kirim this (JPanel) sebagai parent
        add(hamburgerMenu.getNavBar(), BorderLayout.NORTH);
        add(hamburgerMenu.getDrawerPanel(), BorderLayout.WEST);

        // 2. Tabel di tengah
        String[] kolom = {"ID", "Nama Produk", "Resep", "Ukuran Kemasan (gram)", "Jumlah Produk"};
        tableModel = new DefaultTableModel(null, kolom);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 3. Tombol di bawah
        JButton tambahBtn = new JButton("Tambah Produk");
        tambahBtn.addActionListener(e -> tambahProduk());
        add(tambahBtn, BorderLayout.SOUTH);
    }

    private void loadData() {
        // Implementasi tetap sama
        tableModel.setRowCount(0);
        List<ProdukOlahanModel> list = controller.getAllProduk();
        for (ProdukOlahanModel p : list) {
            Object[] row = {
                p.getId(),
                p.getNamaProduk(),
                p.getNamaResep(),
                p.getUkuranKemasan(),
                p.getJumlah()
            };
            tableModel.addRow(row);
        }
    }

    private void tambahProduk() {
        // Implementasi tetap sama
        JTextField namaField = new JTextField();
        JTextField ukuranField = new JTextField();
        JTextField jumlahField = new JTextField();

        JComboBox<String> resepCombo = new JComboBox<>();
        Map<String, Integer> resepMap = controller.getResepMap();
        resepMap.keySet().forEach(resepCombo::addItem);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Nama Produk:")); panel.add(namaField);
        panel.add(new JLabel("Resep:")); panel.add(resepCombo);
        panel.add(new JLabel("Ukuran Kemasan (gram):")); panel.add(ukuranField);
        panel.add(new JLabel("Jumlah Produk Jadi:")); panel.add(jumlahField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Tambah Produk Olahan", 
            JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                ProdukOlahanModel produk = new ProdukOlahanModel();
                produk.setNamaProduk(namaField.getText());
                produk.setResepId(resepMap.get(resepCombo.getSelectedItem().toString()));
                produk.setUkuranKemasan(Integer.parseInt(ukuranField.getText()));
                produk.setJumlah(Integer.parseInt(jumlahField.getText()));

                if (controller.tambahProduk(produk)) {
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menambah produk.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Input tidak valid: " + ex.getMessage());
            }
        }
    }
}