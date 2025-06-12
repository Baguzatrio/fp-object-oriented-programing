package view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import controller.UpahPekerjaController;
import model.User;

public class UpahPekerja2 extends JFrame {

    private JTable tabelUpah;
    private JPanel contentPanel;
    private HamburgerMenu hamburgerMenu;
    private User currentUser;
    private JButton jButton2;
    private JScrollPane jScrollPane1;
    private JPanel tablePanel;

    public UpahPekerja2(User user) {
        this.currentUser = user;
        setTitle("Perhitungan Upah Pekerja");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPanel = new JPanel(new BorderLayout());
        setContentPane(contentPanel);

        // Inisialisasi HamburgerMenu dan tambah ke content
        hamburgerMenu = new HamburgerMenu(contentPanel, currentUser);
        contentPanel.add(hamburgerMenu.getNavBar(), BorderLayout.NORTH);
        contentPanel.add(hamburgerMenu.getDrawerPanel(), BorderLayout.WEST);

        // Panel utama konten
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);

        // Tombol daftar pekerja di kanan atas
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(Color.WHITE);
        jButton2 = new JButton("Daftar Pekerja");
        topPanel.add(jButton2);
        mainContent.add(topPanel, BorderLayout.NORTH);

        // Tabel dan scrollpane
        tabelUpah = new JTable();
        jScrollPane1 = new JScrollPane(tabelUpah);
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(jScrollPane1, BorderLayout.CENTER);
        mainContent.add(tablePanel, BorderLayout.CENTER);

        contentPanel.add(mainContent, BorderLayout.CENTER);

        setupTabelUpah();
    }

    public JButton getBtnDaftarPekerja() {
        return jButton2;
    }

    public JTable getTabelUpah() {
        return tabelUpah;
    }

    private void setupTabelUpah() {
    String[] namaKolom = {"Tanggal", "ID", "Nama", "Produk", "Jumlah", "Upah/Unit", "Total", "Aksi"};
    DefaultTableModel model = new DefaultTableModel(namaKolom, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 7;
        }

        @Override
        public Class<?> getColumnClass(int column) {
            return column == 7 ? JButton.class : Object.class;
        }
    };

    tabelUpah.setModel(model);
    tabelUpah.setRowHeight(30);
    tabelUpah.setGridColor(Color.LIGHT_GRAY); // garis antar sel
    tabelUpah.setShowGrid(true);
    tabelUpah.setIntercellSpacing(new Dimension(1, 1));
    tabelUpah.setFillsViewportHeight(true);

    // Header warna sky blue
    JTableHeader header = tabelUpah.getTableHeader();
    header.setBackground(new Color(135, 206, 250)); // sky blue
    header.setForeground(Color.BLACK);
    header.setFont(new Font("Segoe UI", Font.BOLD, 14));

    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Cek apakah baris ini kosong (semua kolom null atau string kosong)
        boolean isEmptyRow = true;
        for (int col = 0; col < table.getColumnCount() - 1; col++) { // Jangan cek kolom Aksi
            Object cellValue = table.getValueAt(row, col);
            if (cellValue != null && !cellValue.toString().trim().isEmpty()) {
                isEmptyRow = false;
                break;
            }
        }

        // Warnai berdasarkan kondisi isi baris
        if (isEmptyRow) {
            setBackground(new Color(250, 255, 255)); // Baris kosong
        } else {
            setBackground(new Color(230, 245, 255)); // Baris isi
        }

        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return this;
    }
};

    for (int i = 0; i < tabelUpah.getColumnCount() - 1; i++) {
        tabelUpah.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
    }

    // Kolom tombol
    TableColumn buttonColumn = tabelUpah.getColumnModel().getColumn(7);
    buttonColumn.setCellRenderer(new ButtonRenderer());
    buttonColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
    
     model.addRow(new Object[]{"2025-06-11", 101, "Andi", "Bakso Urat", 10, 2000, 20000, "Detail"});
    model.addRow(new Object[]{"2025-06-11", 102, "Budi", "Tahu", 5, 1500, 7500, "Detail"});
    model.addRow(new Object[]{"2025-06-11", 103, "Citra", "Kuah", 8, 1000, 8000, "Detail"});
    
     for (int i = 0; i < 5; i++) {
        model.addRow(new Object[]{"", "", "", "", "", "", "", ""});
    }
}


    public void tampilkanData(List<Object[]> data) {
        DefaultTableModel model = (DefaultTableModel) tabelUpah.getModel();
        model.setRowCount(0);

        for (Object[] row : data) {
            Object[] rowWithButton = new Object[row.length + 1];
            System.arraycopy(row, 0, rowWithButton, 0, row.length);
            rowWithButton[row.length] = "Detail";
            model.addRow(rowWithButton);
        }
    }

    private void tambahkanData() {
        DefaultTableModel model = (DefaultTableModel) tabelUpah.getModel();
        model.addRow(new Object[]{"25-05-2025", "P001", "Jamal", "Tempura", 15, 5000, 75000, "Detail"});
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

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                      boolean isSelected, int row, int column) {
            button.setText("Detail");
            return button;
        }
    }
}
