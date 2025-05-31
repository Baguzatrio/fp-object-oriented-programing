/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
public class UpahPekerja extends javax.swing.JFrame {

    private javax.swing.JTable tabelUpah;
    public UpahPekerja() {
        initComponents();
        setupTabelUpah();
    }

    private void setupTabelUpah() {
    // Kolom ditambah "Aksi" untuk tombol
    String[] namaKolom = {"Tanggal", "ID", "Nama", "Produk", "Jumlah", "Upah/Unit", "Total", "Aksi"};
    DefaultTableModel model = new DefaultTableModel(namaKolom, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 7; // Hanya kolom "Aksi" yang editable
        }
        
        @Override
        public Class<?> getColumnClass(int column) {
            return column == 7 ? JButton.class : Object.class; // Kolom 7 berisi button
        }
    };
    
    tabelUpah = new javax.swing.JTable(model);
        tabelUpah.setAutoCreateRowSorter(true);
        tabelUpah.setFillsViewportHeight(true);
        tabelUpah.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabelUpah.getColumnModel().getColumn(1).setPreferredWidth(50);
        tabelUpah.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelUpah.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelUpah.getColumnModel().getColumn(2).setPreferredWidth(50);
        tabelUpah.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabelUpah.getColumnModel().getColumn(2).setPreferredWidth(100);
        jScrollPane1.setViewportView(tabelUpah);
        tambahkanData();
    
    // Tambahkan tombol ke kolom terakhir
    TableColumnModel columnModel = tabelUpah.getColumnModel();
    TableColumn buttonColumn = columnModel.getColumn(7);
    buttonColumn.setCellRenderer(new ButtonRenderer());
    buttonColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText("Detail");
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String idPekerja;
    
    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Pindah ke halaman detail
                new DetailPekerja().setVisible(true);
                ((JFrame)SwingUtilities.getWindowAncestor(button)).dispose();
            }
        });
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        idPekerja = table.getValueAt(row, 1).toString(); // Ambil ID dari kolom 2
        button.setText("Detail");
        return button;
    }
}
    
    private void tambahkanData() {
        DefaultTableModel model = (DefaultTableModel) tabelUpah.getModel();
        model.addRow(new Object[] {"25-05-2025", "P001", "Jamal", "Tempura", 15, 5000, 75000});
    }
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jButton2.setText("Daftar Pekerja");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(371, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(UpahPekerja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpahPekerja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpahPekerja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpahPekerja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UpahPekerja().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
