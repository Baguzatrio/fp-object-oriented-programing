package view;

import controller.BahanBakuController;
import controller.ResepDetailController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import model.ResepDetailModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import model.BahanBakuModel;

public class ResepDetail extends JFrame {
    private int resepId;
    private String namaProduk;
    private JTable table;
    private DefaultTableModel tableModel;
    private ResepDetailController controller;

    public ResepDetail(int resepId, String namaProduk) {
    this.resepId = resepId;
    this.namaProduk = namaProduk;
    this.controller = new ResepDetailController();

    setTitle("Komposisi Resep: " + namaProduk);
    setSize(650, 400);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    JPanel gradientPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            Color color1 = new Color(0, 0, 80);
            Color color2 = new Color(135, 206, 250);
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    };
    gradientPanel.setLayout(new BorderLayout(10, 10));
    gradientPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JLabel labelJudul = new JLabel("Komposisi Resep: " + namaProduk);
    labelJudul.setFont(new Font("SansSerif", Font.BOLD, 18));
    labelJudul.setForeground(Color.WHITE);
    gradientPanel.add(labelJudul, BorderLayout.NORTH);

    String[] kolom = {"ID", "Bahan Baku", "Jumlah", "Satuan"};
    tableModel = new DefaultTableModel(null, kolom);
    table = new JTable(tableModel);
    table.setRowHeight(25);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setRowSelectionAllowed(true);
    table.setColumnSelectionAllowed(false);
    table.setBackground(new Color(173, 216, 230));
    table.setForeground(Color.BLACK);
    table.setFont(new Font("SansSerif", Font.PLAIN, 13));

    JTableHeader header = table.getTableHeader();
    header.setBackground(new Color(135, 206, 250));
    header.setForeground(Color.BLACK);
    header.setFont(new Font("SansSerif", Font.BOLD, 14));
    header.setOpaque(true);

    JScrollPane scrollPane = new JScrollPane(table);
    gradientPanel.add(scrollPane, BorderLayout.CENTER);

    JButton tambahBtn = new JButton("➕ Tambah Bahan ke Resep");
    tambahBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
    tambahBtn.setBackground(new Color(100, 149, 237));
    tambahBtn.setForeground(Color.WHITE);
    tambahBtn.setFocusPainted(false);
    tambahBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    tambahBtn.addActionListener(e -> tambahDetail());

    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    bottomPanel.setOpaque(false);
    bottomPanel.add(tambahBtn);
    gradientPanel.add(bottomPanel, BorderLayout.SOUTH);

    gradientPanel.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            table.clearSelection();
        }
    });

    setContentPane(gradientPanel);
    loadDetail(); // <--- Pastikan ini memanggil controller DAO yang sudah terhubung ke database
}

    private void loadDetail() {
        tableModel.setRowCount(0);
        List<ResepDetailModel> list = controller.getDetailByResepId(resepId);
        for (ResepDetailModel r : list) {
            Object[] row = {r.getId(), r.getNamaBahan(), r.getJumlah(), r.getSatuan()};
            tableModel.addRow(row);
        }
    }
    
    private void setupAutoComplete(JComboBox<String> comboBox, List<String> items) {
    JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();
    editor.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent e) {
            String input = editor.getText();
            comboBox.removeAllItems();
            for (String item : items) {
                if (item.toLowerCase().contains(input.toLowerCase())) {
                    comboBox.addItem(item);
                }
            }
            editor.setText(input);
            comboBox.setPopupVisible(true);
        }
    });
}

private void tambahDetail() {
    JDialog dialog = new JDialog(this, "Tambah Komposisi", true);
    dialog.setSize(400, 250);
    dialog.setLocationRelativeTo(this);
    dialog.setUndecorated(false);

    // Panel dengan background gradasi
    JPanel gradientPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            Color color1 = new Color(25, 25, 112);  // midnight blue
            Color color2 = new Color(173, 216, 230); // light blue
            GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            setOpaque(false); // Supaya gradasi tampil
            super.paintComponent(g);
        }
    };
    gradientPanel.setLayout(new BorderLayout());
    gradientPanel.setOpaque(false);

    // Form input
    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setOpaque(false);
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.anchor = GridBagConstraints.WEST;

    // Ambil data bahan baku
    BahanBakuController bahanCtrl = new BahanBakuController();
    List<BahanBakuModel> bahanList = bahanCtrl.getAllBahan();
    Map<String, Integer> bahanMap = new HashMap<>();
    List<String> namaBahanList = new ArrayList<>();

    // ComboBox bahan baku dengan auto complete
    JComboBox<String> comboBox = new JComboBox<>();
    comboBox.setEditable(true);
    for (BahanBakuModel b : bahanList) {
        comboBox.addItem(b.getNama());
        bahanMap.put(b.getNama(), b.getId());
        namaBahanList.add(b.getNama());
    }
    setupAutoComplete(comboBox, namaBahanList);

    JTextField jumlahField = new JTextField();
    comboBox.setPreferredSize(new Dimension(200, 25));
    jumlahField.setPreferredSize(new Dimension(200, 25));

    // Label dan Field: Bahan Baku
    gbc.gridx = 0;
    gbc.gridy = 0;
    formPanel.add(new JLabel("Bahan Baku:"), gbc);
    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    formPanel.add(comboBox, gbc);

    // Label dan Field: Jumlah
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    formPanel.add(new JLabel("Jumlah:"), gbc);
    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    formPanel.add(jumlahField, gbc);

    // Tombol
    JPanel buttonPanel = new JPanel();
    buttonPanel.setOpaque(false);
    JButton okButton = new JButton("OK");
    JButton cancelButton = new JButton("Batal");
    buttonPanel.add(okButton);
    buttonPanel.add(cancelButton);

    // Aksi tombol
    okButton.addActionListener(e -> {
        try {
            String selectedNama = (String) comboBox.getEditor().getItem();
            if (!bahanMap.containsKey(selectedNama)) {
                JOptionPane.showMessageDialog(dialog, "Bahan baku tidak valid!");
                return;
            }
            int bahanId = bahanMap.get(selectedNama);
            double jumlah = Double.parseDouble(jumlahField.getText());

            boolean sukses = controller.tambahDetail(resepId, bahanId, jumlah);
            if (sukses) {
                loadDetail();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Berhasil menambahkan detail resep");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan detail resep");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Jumlah harus berupa angka");
        }
    });

    cancelButton.addActionListener(e -> dialog.dispose());

    // Gabung semua ke panel utama
    gradientPanel.add(formPanel, BorderLayout.CENTER);
    gradientPanel.add(buttonPanel, BorderLayout.SOUTH);

    dialog.setContentPane(gradientPanel);
    dialog.setVisible(true);
}

}
