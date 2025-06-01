package controller;

import model.UpahPekerjaModel;
import view.UpahPekerja;
import view.DetailPekerja;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import view.DataPekerja;

public class UpahPekerjaController {
    private UpahPekerjaModel model;
    private UpahPekerja view;
    private Connection conn;
    
    public UpahPekerjaController(Connection conn, UpahPekerja view) {
        this.conn = conn;
        this.view = view;
        this.model = new UpahPekerjaModel(conn);
        
        initController();
        loadData();
    }
    
    private void initController() {
        // Handle button click
        view.getBtnDaftarPekerja().addActionListener(e -> {
            new view.DataPekerja().setVisible(true);
        });
        
        // Handle table button clicks
        view.getTabelUpah().getModel().addTableModelListener(e -> {
            if (e.getColumn() == 7 && e.getType() == TableModelEvent.UPDATE) {
                int idPekerja = (int) view.getTabelUpah().getValueAt(e.getFirstRow(), 1);
                showDetailPekerja(idPekerja);
            }
        });
    }
    
    private void loadData() {
        try {
            List<Object[]> data = model.getDataUpah();
            view.tampilkanData(data);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, 
                "Gagal memuat data: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void showDetailPekerja(int idPekerja) {
    try {
        Object[] DataPekerja = model.getDetailPekerja(idPekerja);
        if (DataPekerja != null) {
            DetailPekerja detailView = new DetailPekerja(idPekerja);
            detailView.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(view, 
                "Data pekerja tidak ditemukan", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(view, 
            "Gagal memuat detail: " + e.getMessage(),
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
}
