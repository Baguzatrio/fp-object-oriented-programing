package view;

import controller.BahanBakuController;
import controller.ResepDetailController;
import java.awt.BorderLayout;
import java.awt.Color;
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
import model.ResepModel;
import model.BahanBakuModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

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
        tableModel = new DefaultTableModel(null, kolom) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setBackground(new Color(173, 216, 230));
        table.setForeground(Color.BLACK);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(135, 206, 250));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        gradientPanel.add(scrollPane, BorderLayout.CENTER);

        JButton tambahBtn = new JButton("➕ Tambah Bahan ke Resep");
        tambahBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tambahBtn.setBackground(new Color(100, 149, 237));
        tambahBtn.setForeground(Color.WHITE);
        tambahBtn.setFocusPainted(false);
        tambahBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        tambahBtn.addActionListener(e -> tambahDetail());

        JButton hapusBtn = new JButton("🗑️ Hapus Bahan");
        hapusBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        hapusBtn.setBackground(new Color(220, 20, 60));
        hapusBtn.setForeground(Color.WHITE);
        hapusBtn.setFocusPainted(false);
        hapusBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        hapusBtn.addActionListener(e -> hapusDetail());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(tambahBtn);
        bottomPanel.add(hapusBtn);
        gradientPanel.add(bottomPanel, BorderLayout.SOUTH);

        gradientPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                table.clearSelection();
            }
        });

        setContentPane(gradientPanel);
        loadDetail();
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
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(25, 25, 112);
                Color color2 = new Color(173, 216, 230);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                setOpaque(false);
                super.paintComponent(g);
            }
        };
        gradientPanel.setLayout(new BorderLayout());
        gradientPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Recipe info display
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Resep ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JLabel(String.valueOf(resepId)), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Nama Produk:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JLabel(namaProduk), gbc);

        // Ingredients selection
        BahanBakuController bahanCtrl = new BahanBakuController();
        List<BahanBakuModel> bahanList = bahanCtrl.getAllBahan("");
        Map<String, Integer> bahanMap = new HashMap<>();
        List<String> namaBahanList = new ArrayList<>();

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setEditable(true);
        for (BahanBakuModel b : bahanList) {
            comboBox.addItem(b.getNama());
            bahanMap.put(b.getNama(), b.getId());
            namaBahanList.add(b.getNama());
        }
        setupAutoComplete(comboBox, namaBahanList);

        JTextField jumlahField = new JTextField();
        JComboBox<String> satuanCombo = new JComboBox<>(new String[]{"gram", "kg", "ml", "liter", "pcs"});

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Bahan Baku:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        formPanel.add(comboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Jumlah:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(jumlahField, gbc);

        gbc.gridx = 2;
        formPanel.add(satuanCombo, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        JButton okButton = new JButton("Simpan");
        JButton cancelButton = new JButton("Batal");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        okButton.addActionListener(e -> {
            try {
                String selectedNama = (String) comboBox.getEditor().getItem();
                if (!bahanMap.containsKey(selectedNama)) {
                    JOptionPane.showMessageDialog(dialog, "Pilih bahan baku yang valid!");
                    return;
                }
                
                int bahanId = bahanMap.get(selectedNama);
                double jumlah = Double.parseDouble(jumlahField.getText());
                String satuan = (String) satuanCombo.getSelectedItem();

                if (jumlah <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Jumlah harus lebih dari 0");
                    return;
                }

                boolean sukses = controller.tambahDetail(resepId, bahanId, jumlah, satuan);
                if (sukses) {
                    loadDetail();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, "Bahan berhasil ditambahkan ke resep");
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menambahkan bahan ke resep");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Jumlah harus berupa angka");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        gradientPanel.add(formPanel, BorderLayout.CENTER);
        gradientPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setContentPane(gradientPanel);
        dialog.setVisible(true);
    }

    private void hapusDetail() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Pilih bahan yang akan dihapus terlebih dahulu!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus bahan ini dari resep?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            int detailId = (int) tableModel.getValueAt(selectedRow, 0);
            boolean sukses = controller.hapusDetail(detailId);
            if (sukses) {
                loadDetail();
                JOptionPane.showMessageDialog(this, "Bahan berhasil dihapus dari resep");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus bahan dari resep");
            }
        }
    }

    public void showAvailableReseps() {
        JDialog resepDialog = new JDialog(this, "Daftar Resep", true);
        resepDialog.setSize(500, 400);
        resepDialog.setLocationRelativeTo(this);
        
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
        
        String[] kolom = {"ID", "Nama Produk", "Deskripsi"};
        DefaultTableModel tableModel = new DefaultTableModel(null, kolom);
        JTable table = new JTable(tableModel);
        
        table.setRowHeight(25);
        table.setBackground(new Color(173, 216, 230));
        table.setForeground(Color.BLACK);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(135, 206, 250));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        List<ResepModel> reseps = controller.getAllReseps();
        for (ResepModel r : reseps) {
            Object[] row = {r.getId(), r.getNamaProduk(), r.getDeskripsi()};
            tableModel.addRow(row);
        }
        
        gradientPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JButton selectButton = new JButton("Pilih Resep");
        selectButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int resepId = (int) tableModel.getValueAt(selectedRow, 0);
                String namaProduk = (String) tableModel.getValueAt(selectedRow, 1);
                resepDialog.dispose();
                new ResepDetail(resepId, namaProduk).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(resepDialog, "Pilih resep terlebih dahulu!");
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(selectButton);
        gradientPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        resepDialog.setContentPane(gradientPanel);
        resepDialog.setVisible(true);
    }
}