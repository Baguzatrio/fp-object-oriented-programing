package fppbo;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.DefaultCellEditor;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.sql.*;
import javax.swing.event.*;

public class FormTambahDistribusi extends javax.swing.JFrame {

    private JComboBox<String> comboBoxNama;
    DefaultTableModel tableModel;
    JTable table;
    Map<String, Integer> produkMap = new LinkedHashMap<>();
    private Map<String, Customer> customerMap = new HashMap<>();
    
    private class Customer {
    String id;
    String alamat;

    public Customer(String id, String alamat) {
        this.id = id;
        this.alamat = alamat;
    }
    }
    
    private Distribusi distribusi;
    
    public FormTambahDistribusi() {
        this.distribusi = distribusi;
        initComponents();
        comboBoxNama = new JComboBox<>();
        setupTabelDistribusi();
        jButton2.setEnabled(false);
        jTextField6.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
    public void insertUpdate(javax.swing.event.DocumentEvent e) {
        updateTotalDanKembalian();
    }

    public void removeUpdate(javax.swing.event.DocumentEvent e) {
        updateTotalDanKembalian();
    }

    public void changedUpdate(javax.swing.event.DocumentEvent e) {
        updateTotalDanKembalian();
    }
});
        loadCustomerData();
        setupAutocomplete();
    }
    
    private void loadCustomerData() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT id_customer, nama_customer, alamat_customer FROM customer";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            customerMap.clear();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            while (rs.next()) {
                String id = rs.getString("id_customer");
                String nama = rs.getString("nama_customer");
                String alamat = rs.getString("alamat_customer");
                model.addElement(nama);
                customerMap.put(nama, new Customer(id,alamat));
            }
            comboBoxNama.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load data customer: " + e.getMessage());
        }
    }
    
    private void setupAutocomplete() {
    JTextField field = jTextField1;
    JPopupMenu suggestionPopup = new JPopupMenu();

    field.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) { showSuggestions(); }
        public void removeUpdate(DocumentEvent e) { showSuggestions(); }
        public void changedUpdate(DocumentEvent e) {}

        private void showSuggestions() {
            String input = field.getText().trim().toLowerCase();
            suggestionPopup.setVisible(false);
            suggestionPopup.removeAll();

            if (input.isEmpty()) return;

            for (String nama : customerMap.keySet()) {
                if (nama.toLowerCase().startsWith(input)) {
                    JMenuItem item = new JMenuItem(nama);
                    item.addActionListener(e -> {
                        field.setText(nama);
                        suggestionPopup.setVisible(false);
                        isiDataCustomer(nama); // isi otomatis id, alamat, tanggal, nota
                    });
                    suggestionPopup.add(item);
                }
            }

            if (suggestionPopup.getComponentCount() > 0) {
                suggestionPopup.show(field, 0, field.getHeight());
            }
        }
    });
}

    private void isiDataCustomer(String nama) {
    Customer c = customerMap.get(nama);
    if (c != null) {
        jTextField2.setText(c.id);           // ID customer
        jTextPane1.setText(c.alamat);        // Alamat
        isiTanggalDanNoNota();               // Tanggal & Nomor Nota otomatis
    }
}

    private void isiTanggalDanNoNota() {
    try {
        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date today = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(today.getTime());
        
        jTextField4.setText(displayFormat.format(today));
        String idCustomer = jTextField2.getText().trim();
        if (!idCustomer.isEmpty()) {
            int urutan = hitungNotaHariIni(Integer.parseInt(idCustomer));
            String nomorNota = dbFormat.format(today) + "-" + idCustomer + "-" + String.format("%03d", urutan);
            jTextField3.setText(nomorNota);
        }
    }catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}

    private int hitungNotaHariIni(int idCustomer) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) as jumlah FROM distribusi WHERE id_customer = ? AND tanggal = CURDATE()";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCustomer);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("jumlah") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return 1;
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField7 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        jTextField7.setText("jTextField1");
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("TAMBAH PENJUALAN");

        jLabel2.setText("Customer");

        jLabel3.setText("ID Customer");

        jLabel4.setText("No. Nota");

        jTextField1.setText("jTextField1");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.setText("jTextField1");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3.setText("jTextField1");

        jLabel5.setText("Tanggal");

        jLabel6.setText("Alamat");

        jTextField4.setText("jTextField4");

        jScrollPane1.setViewportView(jTextPane1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(177, 177, 177)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(34, 34, 34)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(61, 61, 61)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(6, 6, 6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())))
        );

        jLabel7.setText("TOTAL");

        jLabel8.setText("KEMBALI");

        jLabel9.setText("BAYAR");

        jTextField5.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jTextField5.setText("jTextField1");
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jTextField6.setText("jTextField1");
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jTextField8.setText("jTextField1");
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jButton2.setText("SIMPAN");
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("KEMBALI");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setText("CETAK");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3)
                .addGap(12, 12, 12)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 17, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton5)
                            .addComponent(jButton3))))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

public void setupTabelDistribusi() {
    // 1. Data produk
    produkMap.put("Tempura", 10000);
    produkMap.put("Bakso Ikan", 9000);
    produkMap.put("Otak-otak Ikan", 8000);

    // 2. Model tabel
    String[] kolom = {"No.", "Nama Produk", "Harga Satuan", "Jumlah", "Subtotal"};
    tableModel = new DefaultTableModel(null, kolom) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 1 || column == 3;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 2 || columnIndex == 3 || columnIndex == 4) {
                return Integer.class;
            }
            return String.class;
        }
    };

    // 3. Tambah baris kosong
    for (int i = 0; i < 5; i++) {
        tableModel.addRow(new Object[]{i + 1, "", 0, 0, 0});
    }

    // 4. Buat tabel
    table = new JTable(tableModel);

    // 5. Dropdown nama produk
    JComboBox<String> comboBox = new JComboBox<>(produkMap.keySet().toArray(new String[0]));
    table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));

    // 6. Perhitungan otomatis
    table.getModel().addTableModelListener(e -> {
        int row = e.getFirstRow();
        int col = e.getColumn();

        if (col == 1) { // nama produk dipilih
            String nama = (String) table.getValueAt(row, 1);
            Integer harga = produkMap.getOrDefault(nama, 0);
            table.setValueAt(harga, row, 2);
            Integer jumlah = (Integer) table.getValueAt(row, 3);
            table.setValueAt(harga * jumlah, row, 4);
        }

        if (col == 3) { // jumlah diubah
            Integer jumlah = (Integer) table.getValueAt(row, 3);
            Integer harga = (Integer) table.getValueAt(row, 2);
            table.setValueAt(harga * jumlah, row, 4);
        }
        updateTotalDanKembalian();
    });

    // 7. Masukkan ke JScrollPane2
    jScrollPane2.setViewportView(table);
}

public void updateTotalDanKembalian() {
    int total = 0;

    for (int i = 0; i < tableModel.getRowCount(); i++) {
        Object val = tableModel.getValueAt(i, 4);
        if (val instanceof Integer) {
            total += (Integer) val;
        }
    }

    jTextField5.setText(String.valueOf(total)); // total harga

    try {
        int dibayar = Integer.parseInt(jTextField6.getText());
        int kembalian = dibayar - total;
        jTextField8.setText(String.valueOf(kembalian));
    } catch (NumberFormatException e) {
        jTextField8.setText("0"); // jika belum diisi atau salah
    }
    jButton2.setEnabled(total > 0);
}

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try {
            String nomorNota = jTextField3.getText();
            String nama = (String) comboBoxNama.getSelectedItem();
            String idCustomer = jTextField2.getText();
            String alamat = jTextPane1.getText();
            String tanggal = jTextField4.getText();
            int total = Integer.parseInt(jTextField5.getText());
            int dibayar = Integer.parseInt(jTextField6.getText());
            int kembalian = dibayar - total;
            String status = kembalian >= 0 ? "Lunas" : "Belum Lunas";
            
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO distribusi (no_nota, id_customer, nama_customer, alamat, status, tanggal, total) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomorNota);
            stmt.setString(2, idCustomer);
            stmt.setString(3, nama);
            stmt.setString(4, alamat);
            stmt.setString(5, status);
            stmt.setString(6, tanggal);
            stmt.setInt(7, total);
            stmt.setInt(8, dibayar);
            stmt.setInt(9, kembalian);
            stmt.executeUpdate();
            
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String produk = (String) tableModel.getValueAt(i, 1);
                Integer harga = (Integer) tableModel.getValueAt(i, 2);
                Integer jumlah = (Integer) tableModel.getValueAt(i, 3);
                Integer subtotal = (Integer) tableModel.getValueAt(i, 4);
                
                if (produk != null && !produk.isEmpty() && jumlah > 0) {
                    String detailSql = "INSERT INTO detail_distribusi (no_nota, nama_produk, harga, jumlah, subtotal) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement detailStmt = conn.prepareStatement(detailSql);
                    detailStmt.setString(1, nomorNota);
                    detailStmt.setString(2, produk);
                    detailStmt.setInt(3, harga);
                    detailStmt.setInt(4, jumlah);
                    detailStmt.setInt(5, subtotal);
                    detailStmt.executeUpdate();
                }
            }
            JOptionPane.showMessageDialog(this, "Nota berhasil disimpan!");
            this.dispose();
            new Distribusi().setVisible(true);
        }catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
new Distribusi().setVisible(true); // buka form input distribusi
    this.setVisible(false);         // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jTextField1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormTambahDistribusi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormTambahDistribusi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormTambahDistribusi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormTambahDistribusi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormTambahDistribusi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
