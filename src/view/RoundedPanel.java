/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author Lenovo
 */
import java.awt.*;
import java.awt.geom.Path2D;
import javax.swing.*;

public class RoundedPanel extends JPanel {
    private int arc;

    public RoundedPanel(int arc) {
        this.arc = arc;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Path2D path = new Path2D.Double();
            path.moveTo(0,0); // kiri atas
            path.lineTo(w, 0); //kanan atas
            path.lineTo(w, h - arc); //kanan bawah sebelum lengkung
            path.quadTo(w, h, w - arc, h); //lengkung kanan bawah
            path.lineTo(arc, h); // dasar bawah
            path.quadTo(0, h, 0, h - arc); // lengkung kiri bawah
            path.closePath();
            
            g2.setColor(getBackground());
            g2.fill(path);
            g2.dispose();
        }
}


