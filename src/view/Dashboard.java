package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import controller.page3controller;
import model.User;

public class Dashboard extends JFrame {
    private HamburgerMenu hamburgerMenu;
    private User currentUser;
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JPanel iconContainer;
    private JPanel titlePanel;
    private JLabel titleLabel;
    private RoundedPanel[] iconPanels;
    private JLabel[] iconLabels;
    private JLabel[] textLabels;

    public Dashboard(User user) {
        this.currentUser = user;
        initializeComponents();
        setupLayout();
        addIcons();
        new page3controller(this, currentUser);
        
        // Handle window resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeIcons();
            }
        });
    }

    private void initializeComponents() {
        // Main frame setup
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Main panel with hamburger menu
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Hamburger menu setup
        hamburgerMenu = new HamburgerMenu(mainPanel, currentUser);
        JPanel navContainer = new JPanel(new BorderLayout());
        navContainer.setBackground(Color.WHITE);
        navContainer.add(hamburgerMenu.getNavBar(), BorderLayout.CENTER);
        mainPanel.add(navContainer, BorderLayout.NORTH);
        
        // Content area
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        
        // Title panel
        titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        titleLabel = new JLabel("Produksi Lancar, Data Tercatat, Bisnis Terkontrol");
        titleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        titleLabel.setForeground(new Color(51, 51, 255));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        titlePanel.add(titleLabel);
        
        // Icon container
        iconContainer = new JPanel(new GridLayout(1, 3, 30, 0)); // 30px horizontal gap
        iconContainer.setOpaque(false);
        iconContainer.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(51, 0, 204), 2, true),
            new EmptyBorder(20, 20, 20, 20) // Inner padding
        ));
        
        // Initialize icon panels
        String[] iconTexts = {"Daftar Produksi", "Produksi", "Distribusi"};
        iconPanels = new RoundedPanel[3];
        iconLabels = new JLabel[3];
        textLabels = new JLabel[3];
        
        for (int i = 0; i < 3; i++) {
            JPanel container = new JPanel(new BorderLayout());
            container.setBackground(Color.WHITE);
            
            iconPanels[i] = new RoundedPanel(20);
            iconPanels[i].setBackground(new Color(153, 255, 255));
            iconPanels[i].setLayout(new GridBagLayout());
            
            textLabels[i] = new JLabel(iconTexts[i]);
            JPanel labelPanel = new JPanel();
            labelPanel.setOpaque(false);
            labelPanel.add(textLabels[i]);
            
            container.add(iconPanels[i], BorderLayout.CENTER);
            container.add(labelPanel, BorderLayout.SOUTH);
            
            iconContainer.add(container);
        }
        
        // Add components to content panel
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        contentPanel.add(iconContainer, BorderLayout.CENTER);
        
        // Add content to main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Set content pane
        setContentPane(mainPanel);
    }

    private void setupLayout() {
        // Set appropriate sizes and margins
        Dimension iconSize = new Dimension(150, 120); // Base size for icons
        
        for (RoundedPanel panel : iconPanels) {
            panel.setPreferredSize(iconSize);
            panel.setMinimumSize(iconSize);
        }
    }

    private void addIcons() {
        String[] iconPaths = {
            "/fppbo/icons/factory.png",
            "/fppbo/icons/package.png",
            "/fppbo/icons/truck.png"
        };
        
        for (int i = 0; i < iconPanels.length; i++) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(iconPaths[i]));
                if (icon.getImage() != null) {
                    iconLabels[i] = new JLabel();
                    iconPanels[i].add(iconLabels[i]);
                    resizeIcon(i);
                } else {
                    addPlaceholderIcon(i);
                }
            } catch (Exception e) {
                System.err.println("Error loading icon: " + e.getMessage());
                addPlaceholderIcon(i);
            }
        }
    }

    private void resizeIcons() {
        for (int i = 0; i < iconPanels.length; i++) {
            if (iconLabels[i] != null && iconLabels[i].getIcon() != null) {
                resizeIcon(i);
            }
        }
    }

    private void resizeIcon(int index) {
        // Calculate size based on panel size but keep reasonable bounds
        int panelSize = Math.min(iconPanels[index].getWidth(), iconPanels[index].getHeight());
        int iconSize = Math.max(40, panelSize * 2 / 3); // Between 40px and 2/3 of panel
        
        ImageIcon originalIcon = (ImageIcon) iconLabels[index].getIcon();
        if (originalIcon == null) {
            originalIcon = new ImageIcon(getClass().getResource(
                index == 0 ? "/fppbo/icons/factory.png" :
                index == 1 ? "/fppbo/icons/package.png" : "/fppbo/icons/truck.png"
            ));
        }
        
        Image scaled = originalIcon.getImage().getScaledInstance(
            iconSize, iconSize, Image.SCALE_SMOOTH
        );
        iconLabels[index].setIcon(new ImageIcon(scaled));
    }

    private void addPlaceholderIcon(int index) {
        iconPanels[index].removeAll();
        JLabel errorLabel = new JLabel("Icon " + (index + 1));
        iconPanels[index].add(errorLabel);
        iconPanels[index].revalidate();
        iconPanels[index].repaint();
    }

    // Getters for the icon panels (if needed by controller)
    public JPanel getDaftarProduksiPanel() {
        return (JPanel) iconPanels[0].getParent();
    }

    public JPanel getProduksiPanel() {
        return (JPanel) iconPanels[1].getParent();
    }

    public JPanel getDistribusiPanel() {
        return (JPanel) iconPanels[2].getParent();
    }
}

// RoundedPanel class (if not already defined elsewhere)
class RoundedPanel extends JPanel {
    private int cornerRadius;

    public RoundedPanel(int radius) {
        cornerRadius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);
        int width = getWidth();
        int height = getHeight();
        
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw rounded border
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);
    }
}