package view;

import controller.DistribusiController;
import controller.FormEditDistribusiController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import model.User;

public class Distribusi2 extends JFrame {
    private static final Color MIDNIGHT_BLUE = new Color(25, 42, 86);
    private static final Color SKY_BLUE = new Color(135, 206, 235);
    private static final Color LIGHT_BLUE = new Color(173, 216, 230);
    private static final Color WHITE = Color.WHITE;
    private static final Color BLACK = Color.BLACK;
    
    private JTable tabelDistribusi;
    private HamburgerMenu hamburgerMenu;
    private User currentUser;
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JButton buttonTambah;
    private JButton buttonCustomer;
    private DistribusiController controller;
    private JLabel titleLabel;

    public Distribusi2(User user) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        this.currentUser = user;
        initializeComponents();
        setupLayout();
    }

    public void refreshData() {
        if (controller != null) {
            controller.loadData();
        }
    }
    
    private void initializeComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(LIGHT_BLUE);

        // Navbar dan drawer
        hamburgerMenu = new HamburgerMenu(this, currentUser);
        getContentPane().add(hamburgerMenu.getNavBar(), BorderLayout.NORTH);
        
        titleLabel = new JLabel("Distribusi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(MIDNIGHT_BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));

        DefaultTableModel model = new DefaultTableModel(
            new Object[][]{},
            new String[]{"No Nota", "Nama Customer", "Lokasi", "Status", "Tanggal", "Total Harga", "Aksi"}
        );
        
        tabelDistribusi = new JTable(model);
        tabelDistribusi.setRowHeight(28);
        tabelDistribusi.setShowGrid(true);
        tabelDistribusi.setGridColor(MIDNIGHT_BLUE);
        tabelDistribusi.setBackground(WHITE);
        tabelDistribusi.setForeground(MIDNIGHT_BLUE);
        tabelDistribusi.setBorder(new LineBorder(MIDNIGHT_BLUE));

        // Table cell styling
        tabelDistribusi.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

                if (column == 6) {
                    JButton button = new JButton("Lihat");
                    button.setBackground(WHITE);
                    button.setForeground(MIDNIGHT_BLUE);
                    button.setFocusPainted(false);
                    button.setBorderPainted(true);
                    button.setOpaque(true);
                    button.setBorder(BorderFactory.createLineBorder(MIDNIGHT_BLUE));
                    return button;
                }
                super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                
                setBackground(WHITE);
                setForeground(MIDNIGHT_BLUE);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, MIDNIGHT_BLUE));
                return this;
            }
        });
        
        tabelDistribusi.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int column = tabelDistribusi.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY() / tabelDistribusi.getRowHeight();
                
                if(row < tabelDistribusi.getRowCount() && row >= 0 && column == 6) {
                    String noNota = (String) tabelDistribusi.getValueAt(row, 0);
                    bukaFormDetailDistribusi(noNota);
                }
            }
        });

        // Header styling (unchanged)
        tabelDistribusi.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

                super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

                setBackground(MIDNIGHT_BLUE);
                setForeground(WHITE);
                
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 1, WHITE),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                return this;
            }
        });

        scrollPane = new JScrollPane(tabelDistribusi);
        scrollPane.getViewport().setBackground(WHITE);

        // Buttons setup with hover effects
        buttonTambah = createHoverButton("TAMBAH");
        buttonCustomer = createHoverButton("DATA CUSTOMER");
    }
    
    private void bukaFormDetailDistribusi(String noNota) {
            FormTambahDistribusi2 form = new FormTambahDistribusi2();
            form.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    refreshData();
                }
            });
            new FormEditDistribusiController(form, currentUser, this, noNota);
            form.setVisible(true);
    }

    private JButton createHoverButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(72, 61, 139));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        
        // Hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 149, 237)); // Light blue on hover
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(72, 61, 139)); // Original color
            }
        });
        
        return button;
    }

    private void setupLayout() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(mainPanel.getBackground());
        
        JPanel titleButtonPanel = new JPanel(new BorderLayout());
        titleButtonPanel.setBackground(mainPanel.getBackground());
        titleButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 40, 5, 0));
        titleButtonPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonsPanel.setBackground(mainPanel.getBackground());
        buttonsPanel.add(buttonCustomer);
        buttonsPanel.add(buttonTambah);

        titleButtonPanel.add(buttonsPanel, BorderLayout.EAST);
        buttonPanel.add(titleButtonPanel, BorderLayout.NORTH);
        
        // Content panel setup
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(mainPanel.getBackground());
        contentPanel.add(buttonPanel, BorderLayout.NORTH);
        
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(mainPanel.getBackground());
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // top, left, bottom, right
        tableWrapper.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(tableWrapper, BorderLayout.CENTER);

        // Add components to main panel
        mainPanel.add(hamburgerMenu.getNavBar(), BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Frame setup
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    // Action listeners
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