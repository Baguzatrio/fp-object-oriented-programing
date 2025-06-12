package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import controller.DetailPekerjaController;

public class DetailPekerja2 extends JFrame {
    private JTextField namaField, mulaiField, telpField;
    private JTextPane alamatPane;
    private JTable upahTable;
    private DefaultTableModel tableModel;
    private JButton kembaliButton;
    private int idPekerja;

    public DetailPekerja2(int idPekerja) {
        this.idPekerja = idPekerja;
        setTitle("Detail Pekerja");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initTopPanel();
        initTable();
        initBottomPanel();

        // Load data
        DetailPekerjaController controller = new DetailPekerjaController(this);
        controller.loadData(idPekerja);
    }

        private void initTopPanel() {
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(Color.decode("#ADD8E6")); // light blue
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel namaLabel = new JLabel("Nama");
        JLabel mulaiLabel = new JLabel("Mulai Bekerja");
        JLabel telpLabel = new JLabel("No. Telp.");
        JLabel alamatLabel = new JLabel("Alamat");

        // Ubah warna label
        Color labelColor = Color.decode("#191970"); // midnight blue
        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        for (JLabel label : new JLabel[]{namaLabel, mulaiLabel, telpLabel, alamatLabel}) {
            label.setForeground(labelColor);
            label.setFont(labelFont);
        }

        namaField = new JTextField(15);
        mulaiField = new JTextField(10);
        telpField = new JTextField(12);
        alamatPane = new JTextPane();
        JScrollPane alamatScroll = new JScrollPane(alamatPane);
        alamatScroll.setPreferredSize(new Dimension(200, 40));

        Color inputBg = Color.decode("#FFFFFF"); // putih
        for (JTextField field : new JTextField[]{namaField, mulaiField, telpField}) {
            field.setBackground(inputBg);
            field.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
        alamatPane.setBackground(inputBg);
        alamatPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.0;

        // Kolom kiri: label dan field Nama
        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(namaLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        topPanel.add(namaField, gbc);

        // Kolom kanan: label dan field Telp
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        topPanel.add(telpLabel, gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        topPanel.add(telpField, gbc);

        // Baris kedua: label dan field Mulai
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        topPanel.add(mulaiLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        topPanel.add(mulaiField, gbc);

        // label dan field Alamat
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        topPanel.add(alamatLabel, gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        topPanel.add(alamatScroll, gbc);

        add(topPanel, BorderLayout.NORTH);
    }

    private void initTable() {
        String[] columnNames = {"Tanggal", "Produk", "Jumlah", "Upah/Unit", "Total Upah"};
        tableModel = new DefaultTableModel(columnNames, 0);
        upahTable = new JTable(tableModel);
        upahTable.setFillsViewportHeight(true);
        upahTable.setShowGrid(true); // Menampilkan grid
        upahTable.setGridColor(Color.GRAY); // Warna grid, bisa diganti sesuai selera

        JScrollPane scrollPane = new JScrollPane(upahTable);

        upahTable.setBackground(Color.decode("#FFFFFF"));
        upahTable.setForeground(Color.decode("#191970"));
        upahTable.setGridColor(Color.LIGHT_GRAY);
        upahTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        upahTable.setRowHeight(25);

        // Header styling
        upahTable.getTableHeader().setBackground(Color.decode("#87CEEB")); // sky blue
        upahTable.getTableHeader().setForeground(Color.decode("#191970")); // midnight blue
        upahTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Center alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < upahTable.getColumnCount(); i++) {
            upahTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        add(scrollPane, BorderLayout.CENTER);
    }

    private void initBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.decode("#ADD8E6")); // light blue

        kembaliButton = new JButton("Kembali");
        kembaliButton.setBackground(Color.decode("#191970")); // midnight blue
        kembaliButton.setForeground(Color.WHITE);
        kembaliButton.setFocusPainted(false);
        kembaliButton.setFont(new Font("Segoe UI", Font.BOLD, 12));

        kembaliButton.addActionListener(e -> dispose());

        bottomPanel.add(kembaliButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Method untuk Controller
    public void setPekerjaDetail(String nama, String mulai, String telp, String alamat) {
        namaField.setText(nama);
        mulaiField.setText(mulai);
        telpField.setText(telp);
        alamatPane.setText(alamat);
    }

    public void setUpahTable(List<HashMap<String, String>> upahList) {
        tableModel.setRowCount(0); // reset table
        for (HashMap<String, String> upah : upahList) {
            Object[] row = {
                upah.get("tanggal"),
                upah.get("produk"),
                upah.get("jumlah"),
                upah.get("upah_per_unit"),
                upah.get("total_upah")
            };
            tableModel.addRow(row);
        }
        int rowsToAdd = Math.max(10 - tableModel.getRowCount(), 0);
    for (int i = 0; i < rowsToAdd; i++) {
        tableModel.addRow(new Object[]{"", "", "", "", ""});
    }
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
