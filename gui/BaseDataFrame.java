package gui.base;

import db.Koneksi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public abstract class BaseDataFrame extends JFrame {
    protected JTable table;
    protected DefaultTableModel tableModel;
    protected String[] columnNames;
    
    public BaseDataFrame(String title, String[] columns) {
        this.columnNames = columns;
        setTitle(title);
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        loadData();
    }
    
    protected void initComponents() {
        tableModel = new DefaultTableModel(null, columnNames);
        table = new JTable(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(table);
        JButton tambahBtn = new JButton(getAddButtonText());
        tambahBtn.addActionListener(e -> tambahData());
        
        add(scrollPane, BorderLayout.CENTER);
        add(tambahBtn, BorderLayout.SOUTH);
    }
    

    protected abstract void loadData();
    protected abstract void tambahData();
    protected abstract String getAddButtonText();
    

    protected void showError(String message, Exception e) {
        JOptionPane.showMessageDialog(this, message + ": " + e.getMessage());
    }
    

    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}