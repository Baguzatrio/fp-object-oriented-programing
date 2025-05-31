/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.util.List;
import java.util.Arrays;

public class FormPreviewNotaA4 extends JFrame {

    JPanel panelNota;
    JButton btnCetak;

    public FormPreviewNotaA4(String nomorNota, String nama, String alamat, String tanggal, String total, String bayar, String kembali, List<String[]> daftarBarang) {
        setTitle("Preview Nota");
        setSize(620, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        panelNota = new JPanel();
        panelNota.setPreferredSize(new Dimension(595, 842)); // A4
        panelNota.setBackground(Color.WHITE);
        panelNota.setLayout(null);

        // HEADER
        ImageIcon logo = new ImageIcon("/fppbo/icons/MT.png"); // Path logo
        Image img = logo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(img));
        lblLogo.setBounds(30, 15, 50, 50);
        panelNota.add(lblLogo);

        JLabel lblNamaPabrik = new JLabel("Ikan Laut Barokah");
        lblNamaPabrik.setFont(new Font("Serif", Font.BOLD, 20));
        lblNamaPabrik.setBounds(100, 20, 300, 30);
        panelNota.add(lblNamaPabrik);

        JLabel lblAlamat = new JLabel("Slangkingrejo 1/12, Blaru, Kediri | Telp: 0815-5655-6761");
        lblAlamat.setFont(new Font("Serif", Font.PLAIN, 12));
        lblAlamat.setBounds(150, 50, 400, 20);
        panelNota.add(lblAlamat);

        // GARIS PEMISAH
        JSeparator separator = new JSeparator();
        separator.setBounds(30, 80, 535, 10);
        panelNota.add(separator);

        // INFO CUSTOMER
        JTextArea info = new JTextArea("Nomor Nota : " + nomorNota +
                                       "\nNama Customer : " + nama +
                                       "\nAlamat : " + alamat +
                                       "\nTanggal : " + tanggal);
        info.setEditable(false);
        info.setBounds(30, 90, 400, 60);
        info.setBackground(Color.WHITE);
        panelNota.add(info);

        // HEADER TABEL
        JLabel lblBarang = new JLabel("Nama Barang");
        lblBarang.setBounds(30, 170, 150, 20);
        panelNota.add(lblBarang);

        JLabel lblJumlah = new JLabel("Jumlah");
        lblJumlah.setBounds(200, 170, 50, 20);
        panelNota.add(lblJumlah);

        JLabel lblHarga = new JLabel("Harga");
        lblHarga.setBounds(300, 170, 100, 20);
        panelNota.add(lblHarga);

        JLabel lblSubtotal = new JLabel("Subtotal");
        lblSubtotal.setBounds(420, 170, 100, 20);
        panelNota.add(lblSubtotal);

        // ISI TABEL BARANG
        int y = 200;
        for (String[] item : daftarBarang) {
            JLabel namaBarang = new JLabel(item[0]);
            namaBarang.setBounds(30, y, 150, 20);
            panelNota.add(namaBarang);

            JLabel jumlah = new JLabel(item[1]);
            jumlah.setBounds(200, y, 50, 20);
            panelNota.add(jumlah);

            JLabel harga = new JLabel(item[2]);
            harga.setBounds(300, y, 100, 20);
            panelNota.add(harga);

            JLabel subtotal = new JLabel(item[3]);
            subtotal.setBounds(420, y, 100, 20);
            panelNota.add(subtotal);

            y += 25;
        }

        // TOTAL 
        y += 20;
        panelNota.add(new JLabel("==========================================")).setBounds(30, y, 400, 20); y += 20;

        JLabel lblTotal = new JLabel("Total Bayar : Rp" + total);
        lblTotal.setBounds(320, y, 200, 20);
        panelNota.add(lblTotal); y += 25;

        JLabel lblBayar = new JLabel("Dibayar : Rp" + bayar);
        lblBayar.setBounds(320, y, 200, 20);
        panelNota.add(lblBayar); y += 25;

        JLabel lblKembali = new JLabel("Kembalian : Rp" + kembali);
        lblKembali.setBounds(320, y, 200, 20);
        panelNota.add(lblKembali);

        // PANEL SCROLL
        JScrollPane scroll = new JScrollPane(panelNota);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scroll, BorderLayout.CENTER);

        // TOMBOL CETAK
        btnCetak = new JButton("Cetak Nota");
        add(btnCetak, BorderLayout.SOUTH);

        btnCetak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrinterJob printerJob = PrinterJob.getPrinterJob();
                printerJob.setJobName("Cetak Nota");

                printerJob.setPrintable(new Printable() {
                    @Override
                    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                        if (pageIndex > 0) {
                            return Printable.NO_SUCH_PAGE;
                        }

                        Graphics2D g2 = (Graphics2D) graphics;
                        g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                        double scaleX = pageFormat.getImageableWidth() / panelNota.getWidth();
                        double scaleY = pageFormat.getImageableHeight() / panelNota.getHeight();
                        double scale = Math.min(scaleX, scaleY);
                        g2.scale(scale, scale);

                        panelNota.printAll(g2);
                        return Printable.PAGE_EXISTS;
                    }
                });

                if (printerJob.printDialog()) {
                    try {
                        printerJob.print();
                    } catch (PrinterException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Gagal mencetak nota: " + ex.getMessage());
                    }
                }
            }
        });

        setVisible(true);
    }

    // TEST
    public static void main(String[] args) {
        // Data dummy dari FormTambahDistribusi
        String nomor = "202505251";
        String nama = "Dina";
        String alamat = "Jl. Sukajadi No. 45";
        String tanggal = "25/05/2025";
        String total = "85000";
        String bayar = "100000";
        String kembali = "15000";

        List<String[]> barang = Arrays.asList(
                new String[]{"Bakso Urat", "2", "15000", "30000"},
                new String[]{"Tahu", "5", "10000", "50000"},
                new String[]{"Kuah", "1", "5000", "5000"}
        );

        new FormPreviewNotaA4(nomor, nama, alamat, tanggal, total, bayar, kembali, barang);
    }
}
