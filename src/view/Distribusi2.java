package view;

import controller.DistribusiController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import model.User;

public class Distribusi2 extends JFrame {
    private JTable tabelDistribusi;
    private JButton buttonTambah;
    private JButton buttonCustomer;
    private JPanel mainPanel;

    public Distribusi2(User user) {
        // OPTIONAL: Set Look and Feel agar warna tidak ditimpa LAF lain
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        // Main panel setup
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Table setup
        DefaultTableModel model = new DefaultTableModel(
            new Object[][]{},
            new String[]{"No Nota", "Nama Customer", "Lokasi", "Status", "Tanggal", "Total Harga", "Aksi"}
        );
        
        tabelDistribusi = new JTable(model);
        styleTable(); // Method untuk styling tabel

        // Scroll pane for table
        JScrollPane scrollPane = new JScrollPane(tabelDistribusi);
        scrollPane.getViewport().setBackground(new Color(240, 248, 255)); // light blue

        // Buttons setup
        buttonTambah = new JButton("TAMBAH");
        buttonCustomer = new JButton("DATA CUSTOMER");

        // Layout setup
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(mainPanel.getBackground());
        buttonPanel.add(buttonCustomer);
        buttonPanel.add(buttonTambah);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(mainPanel.getBackground());
        contentPanel.add(buttonPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }

    private void setupLayout() {
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void styleTable() {
        tabelDistribusi.getTableHeader().setBackground(new Color(72, 61, 139)); // midnight blue
        tabelDistribusi.getTableHeader().setForeground(Color.WHITE);
        tabelDistribusi.setRowHeight(28);
        tabelDistribusi.setBackground(new Color(240, 248, 255)); // light blue
        tabelDistribusi.setForeground(Color.DARK_GRAY);
    }

    // Action listeners (to be set by controller)
    public void addTambahButtonListener(java.awt.event.ActionListener listener) {
        buttonTambah.addActionListener(listener);
    }

    public void addCustomerButtonListener(java.awt.event.ActionListener listener) {
        buttonCustomer.addActionListener(listener);
    }

    // Getters for components
    public JButton getButtonTambah() {
        return buttonTambah;
    }

    public JButton getButtonCustomer() {
        return buttonCustomer;
    }

    public JTable getTabelDistribusi() {
        return tabelDistribusi;
    }

    public void setTableModel(TableModel model) {
        tabelDistribusi.setModel(model);
    }
}
