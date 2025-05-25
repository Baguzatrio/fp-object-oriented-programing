/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fppbo;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import javax.swing.JPanel;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Lenovo
 */
public class RoundedPanel1 extends JPanel{
    private int arc;

    public RoundedPanel1(int arc) {
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

    int arc = this.arc; // radius lengkungan

    Path2D path = new Path2D.Double();
    // Mulai dari kiri bawah
    path.moveTo(0, h);
    // ke kiri atas dengan lengkungan
    path.lineTo(0, arc);
    path.quadTo(0, 0, arc, 0); // lengkung kiri atas
    // ke kanan atas dengan lengkungan
    path.lineTo(w - arc, 0);
    path.quadTo(w, 0, w, arc); // lengkung kanan atas
    // ke kanan bawah
    path.lineTo(w, h);
    // tutup path ke titik awal
    path.closePath();

    g2.setColor(getBackground());
    g2.fill(path);

    g2.dispose();
}

}