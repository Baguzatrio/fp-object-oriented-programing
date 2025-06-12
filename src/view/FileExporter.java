package view;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.io.FileWriter;
import javax.swing.JOptionPane;

public class FileExporter {
    public static void exportToCSV(JTable table, String filename) {
        try (FileWriter csv = new FileWriter(filename)) {
            TableModel model = table.getModel();

            for (int i = 0; i < model.getColumnCount(); i++) {
                csv.write(model.getColumnName(i) + ",");
            }
            csv.write("\n");

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    csv.write(model.getValueAt(i, j).toString() + ",");
                }
                csv.write("\n");
            }

            JOptionPane.showMessageDialog(null, "Data berhasil di-export ke " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
