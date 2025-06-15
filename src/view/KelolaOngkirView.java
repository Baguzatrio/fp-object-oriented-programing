package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import model.OngkirModel;
import java.sql.*;

public class KelolaOngkirView extends JPanel {
    private JTable table;
    private JTextField areaField, biayaField;
    private JButton tambahButton;
    private OngkirModel model;

    public KelolaOngkirView() {
        try {
            this.model = new OngkirModel();
            initUI();
            loadData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Form input
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        formPanel.add(new JLabel("Area:"));
        areaField = new JTextField();
        formPanel.add(areaField);
        formPanel.add(new JLabel("Biaya (Rp):"));
        biayaField = new JTextField();
        formPanel.add(biayaField);

        tambahButton = new JButton("Tambah Data");
        tambahButton.addActionListener(e -> tambahData());

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(formPanel, BorderLayout.CENTER);
        inputPanel.add(tambahButton, BorderLayout.SOUTH);

        // Table
        table = new JTable(new DefaultTableModel(new Object[]{"ID", "Area", "Biaya"}, 0));
        JScrollPane scrollPane = new JScrollPane(table);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() throws SQLException {
    DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
    tableModel.setRowCount(0);
    
    for (OngkirModel.Ongkir ongkir : this.model.getAllOngkir()) {
        tableModel.addRow(new Object[]{ongkir.getId(), ongkir.getArea(), ongkir.getBiaya()});
    }
}

    private void tambahData() {
        try {
            String area = areaField.getText();
            int biaya = Integer.parseInt(biayaField.getText());
            
            model.tambahOngkir(area, biaya);
            loadData();
            
            areaField.setText("");
            biayaField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}