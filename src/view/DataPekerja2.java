package view;

import controller.DataPekerjaController;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import model.User;

public class DataPekerja2 extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private DataPekerjaController controller;
    private User currentUser;
    
    // Form components
    private JTextField txtId, txtNama, txtMulaiBekerja, txtNoTelp, txtSearch;
    private JTextPane txtAlamat;
    private JButton btnTambah, btnUbah, btnHapus, btnDetail, btnKembali, btnSimpan;
    
    public DataPekerja2(User user) {
        this.currentUser = user;
        initializeUI();
        controller = new DataPekerjaController(this);
        setupListeners();
        controller.loadData();
    }

    private void initializeUI() {
        setTitle("Data Pekerja");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JLabel lblTitle = new JLabel("DATA PEKERJA", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        
        // Center content
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        // Form panel
        JPanel formPanel = createFormPanel();
        centerPanel.add(formPanel, BorderLayout.NORTH);
        
        // Table panel
        JPanel tablePanel = createTablePanel();
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Form Data Pekerja"));
        
        // Row 1
        panel.add(new JLabel("ID:"));
        txtId = new JTextField();
        txtId.setEditable(false);
        panel.add(txtId);
        
        panel.add(new JLabel("Nama:"));
        txtNama = new JTextField();
        panel.add(txtNama);
        
        // Row 2
        panel.add(new JLabel("Mulai Bekerja:"));
        txtMulaiBekerja = new JTextField();
        panel.add(txtMulaiBekerja);
        
        panel.add(new JLabel("No. Telp:"));
        txtNoTelp = new JTextField();
        panel.add(txtNoTelp);
        
        // Row 3
        panel.add(new JLabel("Alamat:"));
        JScrollPane scrollPane = new JScrollPane();
        txtAlamat = new JTextPane();
        scrollPane.setViewportView(txtAlamat);
        panel.add(scrollPane);
        
        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        btnTambah = new JButton("Tambah");
        btnSimpan = new JButton("Simpan");
        btnHapus = new JButton("Hapus");
        
        actionPanel.add(btnTambah);
        actionPanel.add(btnSimpan);
        actionPanel.add(btnHapus);
        
        panel.add(new JLabel()); // Empty cell
        panel.add(actionPanel);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Daftar Pekerja"));
        
        // Table setup
        String[] columns = {"ID", "Nama", "Mulai Bekerja", "No. Telp", "Alamat"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(new JLabel("Cari:"));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnUbah = new JButton("Ubah");
        btnDetail = new JButton("Detail");
        btnKembali = new JButton("Kembali ke Upah Pekerja");
        
        panel.add(btnUbah);
        panel.add(btnDetail);
        panel.add(btnKembali);
        
        return panel;
    }
    
    private void setupListeners() {
        btnTambah.addActionListener(e -> controller.tambahPekerja(
            txtNama.getText(),
            txtMulaiBekerja.getText(),
            txtNoTelp.getText(),
            txtAlamat.getText()
        ));
        
        btnSimpan.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                controller.updatePekerja(
                    id,
                    txtNama.getText(),
                    txtMulaiBekerja.getText(),
                    txtNoTelp.getText(),
                    txtAlamat.getText()
                );
            }
        });
        
        btnHapus.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                controller.deletePekerja(id);
            }
        });
        
        btnUbah.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                controller.prepareUpdateForm(id);
            }
        });
        
        btnDetail.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            int id = selectedRow >= 0 ? (int) tableModel.getValueAt(selectedRow, 0) : -1;
            new DetailPekerja2(id).setVisible(true);
        });
        
        btnKembali.addActionListener(e -> {
            new UpahPekerja2(currentUser).setVisible(true);
            this.dispose();
        });
    }
    
    // Other methods remain the same as your original code
    public void displayData(List<Object[]> data) {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (Object[] row : data) {
            Object[] formattedRow = row.clone();
            
            if (row[2] instanceof Date) {
                formattedRow[2] = sdf.format((Date) row[2]);
            }
            
            tableModel.addRow(formattedRow);
        }
    }
    
    public void prepareUpdateForm(String nama, String tanggal, String noTelp, String alamat) {
        txtNama.setText(nama);
        txtMulaiBekerja.setText(tanggal);
        txtNoTelp.setText(noTelp);
        txtAlamat.setText(alamat);
    }
    
    public void clearForm() {
        txtId.setText("");
        txtNama.setText("");
        txtMulaiBekerja.setText("");
        txtNoTelp.setText("");
        txtAlamat.setText("");
    }
    
    public boolean confirmAction(String message) {
        return JOptionPane.showConfirmDialog(this, 
            message, "Konfirmasi", 
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public void showError(String error) {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }
}