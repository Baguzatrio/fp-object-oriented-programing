package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Landing extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JButton loginButton;
    private JButton signupButton;
    private JPanel logoPanel;
    private JLabel logoLabel;
    private float opacity = 0f;

    public Landing() {
        initComponents();
        setupWindow();
        setupLogo();
        startFadeInAnimation();
    }

    private void initComponents() {
        // Main panel with enhanced radial gradient
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Radial gradient focused on center
                Point2D center = new Point2D.Float(getWidth()/2, getHeight()/2);
                float radius = Math.max(getWidth(), getHeight()) * 0.8f;
                float[] dist = {0.0f, 0.7f, 1.0f};
                Color[] colors = {
                    new Color(5, 25, 45), 
                    new Color(10, 35, 65), 
                    new Color(15, 40, 70)
                };
                g2d.setPaint(new RadialGradientPaint(center, radius, dist, colors));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Fade-in effect
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            }
        };
        mainPanel.setLayout(null);

        // Title with improved typography
        titleLabel = new JLabel("SELAMAT DATANG DI ILBTRACE!");
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 26));
        titleLabel.setForeground(new Color(230, 245, 255));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 30, 480, 35);

        // Subtitle with perfect centering
        subtitleLabel = new JLabel("Aplikasi Praktis Untuk Memantau Aktivitas Pabrik");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(190, 220, 250));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setBounds(0, 70, 480, 20);

        // Logo panel with enhanced glow effect
        logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Multi-layered glow effect
                for (int i = 0; i < 6; i++) {
                    float ratio = (6 - i) / 6f;
                    g2d.setColor(new Color(0, 174, 239, (int)(70 * ratio)));
                    int offset = 15 - i*2;
                    g2d.fillOval(offset, offset, 
                                getWidth()-2*offset, getHeight()-2*offset);
                }
                
                // White border glow
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawOval(8, 8, getWidth()-16, getHeight()-16);
            }
        };
        logoPanel.setOpaque(false);
        logoPanel.setBounds(150, 100, 180, 180); // Optimal logo size
        logoPanel.setLayout(new BorderLayout());

        // Create professional buttons (height: 36px)
        loginButton = createEnhancedButton("LOG IN", 
            new Color(0, 120, 180),  // #0078B4
            new Color(0, 174, 239)); // #00AEEF
        loginButton.setBounds(130, 270, 220, 36); // Perfectly centered
        
        signupButton = createEnhancedButton("SIGN UP", 
            new Color(0, 100, 160),  // #0064A0
            new Color(0, 150, 210)); // #0096D2
        signupButton.setBounds(130, 316, 220, 36); // 46px gap between buttons

        // Add components with proper z-ordering
        mainPanel.add(logoPanel, Integer.valueOf(0));
        mainPanel.add(titleLabel, Integer.valueOf(1));
        mainPanel.add(subtitleLabel, Integer.valueOf(1));
        mainPanel.add(loginButton, Integer.valueOf(1));
        mainPanel.add(signupButton, Integer.valueOf(1));

        getContentPane().add(mainPanel);
    }

    private JButton createEnhancedButton(String text, Color startColor, Color endColor) {
        JButton button = new JButton(text) {
            private boolean hovered = false;
            private float glowIntensity = 0f;
            private final int BORDER_RADIUS = 18; // Match half of button height
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Button shadow with depth
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRoundRect(2, 3, getWidth()-2, getHeight()-2, BORDER_RADIUS, BORDER_RADIUS);
                
                // Main button gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, hovered ? brighten(startColor, 30) : startColor,
                    0, getHeight(), hovered ? brighten(endColor, 20) : endColor
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth()-2, getHeight()-2, BORDER_RADIUS, BORDER_RADIUS);
                
                // Animated glow border
                if (hovered) {
                    g2.setColor(new Color(255, 255, 255, (int)(80 * glowIntensity)));
                    g2.setStroke(new BasicStroke(1.8f));
                    g2.drawRoundRect(0, 0, getWidth()-2, getHeight()-2, BORDER_RADIUS, BORDER_RADIUS);
                }
                
                // Text rendering with perfect centering
                FontMetrics fm = g2.getFontMetrics();
                Rectangle2D textBounds = fm.getStringBounds(getText(), g2);
                int textX = (getWidth() - (int)textBounds.getWidth()) / 2;
                int textY = (getHeight() - (int)textBounds.getHeight()) / 2 + fm.getAscent();
                
                // Text shadow
                g2.setColor(new Color(0, 0, 0, 80));
                g2.drawString(getText(), textX+1, textY+1);
                
                // Main text
                g2.setColor(Color.WHITE);
                g2.drawString(getText(), textX, textY);
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(220, 36);
            }
            
            @Override
            protected void processMouseEvent(MouseEvent e) {
                switch (e.getID()) {
                    case MouseEvent.MOUSE_ENTERED:
                        hovered = true;
                        animateGlow(0f, 1f);
                        break;
                    case MouseEvent.MOUSE_EXITED:
                        hovered = false;
                        animateGlow(glowIntensity, 0f);
                        break;
                }
                super.processMouseEvent(e);
            }
            
            private void animateGlow(float from, float to) {
                new Thread(() -> {
                    try {
                        float step = (to - from) / 10f;
                        for (int i = 0; i < 10; i++) {
                            glowIntensity = from + step * i;
                            repaint();
                            Thread.sleep(16); // ~60fps animation
                        }
                        glowIntensity = to;
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }
        };
        
        button.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        return button;
    }

    private void startFadeInAnimation() {
        new Thread(() -> {
            try {
                for (int i = 0; i <= 10; i++) {
                    opacity = i / 10f;
                    repaint();
                    Thread.sleep(30);
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private Color brighten(Color color, int amount) {
        return new Color(
            Math.min(255, color.getRed() + amount),
            Math.min(255, color.getGreen() + amount),
            Math.min(255, color.getBlue() + amount)
        );
    }

    private void setupWindow() {
        setSize(480, 420); // Optimal window size
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("ILBTRACE - Factory Monitoring System");
    }

    private void setupLogo() {
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/fppbo/icons/MT.png"));
            Image logoImage = logoIcon.getImage();
            Image scaledLogo = logoImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaledLogo));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            logoPanel.add(logoLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            // Fallback text logo
            logoLabel = new JLabel("ILBTRACE");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            logoLabel.setForeground(new Color(0, 174, 239));
            logoPanel.add(logoLabel, BorderLayout.CENTER);
        }
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addSignupListener(ActionListener listener) {
        signupButton.addActionListener(listener);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Create and display the form
                Landing landingPage = new Landing();
                new controller.page0controller(landingPage);
                landingPage.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error initializing application: " + ex.getMessage(),
                    "Startup Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}