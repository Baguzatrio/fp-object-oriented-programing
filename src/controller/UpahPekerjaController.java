package controller;

import model.UpahPekerjaModel;
import view.UpahPekerja2;
import view.DetailPekerja2;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import model.User;
import view.DataPekerja2;

public class UpahPekerjaController {
    private UpahPekerjaModel model;
    private UpahPekerja2 view;
    private Connection conn;
    private User user;
    
    public UpahPekerjaController(Connection conn, UpahPekerja2 view) {
        this.conn = conn;
        this.view = view;
        this.model = new UpahPekerjaModel(conn);
        
        initController();
        loadData();
    }
    
    private void initController() {
        // Handle button click
        view.getBtnDaftarPekerja().addActionListener(e -> {
            new view.DataPekerja2(user).setVisible(true);
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
            DetailPekerja2 detailView = new DetailPekerja2(idPekerja);
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