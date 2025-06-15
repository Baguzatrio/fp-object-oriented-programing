package controller;

import model.SessionLogModel;
import view.SessionLogView;
import javax.swing.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SessionLogController {
    private final SessionLogModel model;
    private final SessionLogView view;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SessionLogController(SessionLogModel model, SessionLogView view) {
        this.model = model;
        this.view = view;
        
        initialize();
        setupEventHandlers();
        loadInitialData();
    }

    private void initialize() {
        view.setVisible(true);
        view.getModuleFilter().addItem("ALL MODULES");
    }

    private void setupEventHandlers() {
        // Refresh button
        view.getRefreshBtn().addActionListener(e -> refreshData());

        // Export button
        view.getExportBtn().addActionListener(e -> exportToCSV());

        // Module filter
        view.getModuleFilter().addActionListener(e -> {
            String module = (String) view.getModuleFilter().getSelectedItem();
            if (!module.equals("ALL MODULES")) {
                filterByModule(module);
            } else {
                refreshData();
            }
        });

        // User filter
        view.getUserFilter().addActionListener(e -> {
            String user = (String) view.getUserFilter().getSelectedItem();
            if (user != null && !user.equals("ALL USERS")) {
                filterByUser(user);
            } else {
                refreshData();
            }
        });
    }

    private void loadInitialData() {
        try {
            // Load modules
            ResultSet modules = model.getDistinctModules();
            while (modules.next()) {
                view.getModuleFilter().addItem(modules.getString("module"));
            }

            // Load users
            view.getUserFilter().addItem("ALL USERS");
            ResultSet users = model.getDistinctUsers();
            while (users.next()) {
                view.getUserFilter().addItem(users.getString("username"));
            }

            // Load last 500 activities
            refreshData();
            
        } catch (SQLException ex) {
            showError("Failed to initialize: " + ex.getMessage());
        }
    }

    private void refreshData() {
        try {
            ResultSet logs = model.getRecentActivities(500);
            view.loadData(logs);
        } catch (SQLException ex) {
            showError("Failed to refresh data: " + ex.getMessage());
        }
    }

    private void filterByModule(String module) {
        try {
            ResultSet logs = model.getModuleActivities(module, 500);
            view.loadData(logs);
        } catch (SQLException ex) {
            showError("Failed to filter by module: " + ex.getMessage());
        }
    }

    private void filterByUser(String username) {
        try {
            LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
            ResultSet logs = model.getUserActivities(
                username, 
                oneWeekAgo, 
                LocalDateTime.now()
            );
            view.loadData(logs);
        } catch (SQLException ex) {
            showError("Failed to filter by user: " + ex.getMessage());
        }
    }

    private void exportToCSV() {
        try {
            ResultSet logs = model.getAllActivities();
            String csvData = resultSetToCSV(logs);
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Activity Log");
            
            if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                java.nio.file.Path path = fileChooser.getSelectedFile().toPath();
                java.nio.file.Files.write(path, csvData.getBytes());
                JOptionPane.showMessageDialog(view, "Exported successfully!");
            }
        } catch (Exception ex) {
            showError("Export failed: " + ex.getMessage());
        }
    }

    private String resultSetToCSV(ResultSet rs) throws SQLException {
    StringBuilder sb = new StringBuilder();
    ResultSetMetaData meta = rs.getMetaData();
    int columnCount = meta.getColumnCount();

    // 1. Build CSV Header
    for (int i = 1; i <= columnCount; i++) {
        sb.append(escapeForCSV(meta.getColumnName(i)));
        if (i < columnCount) sb.append(",");
    }
    sb.append("\n");

    // 2. Process Data Rows
    while (rs.next()) {
        for (int i = 1; i <= columnCount; i++) {
            Object value = rs.getObject(i);
            sb.append(escapeForCSV(value != null ? value.toString() : ""));
            if (i < columnCount) sb.append(",");
        }
        sb.append("\n");
    }

    return sb.toString();
}

private String escapeForCSV(String input) {
    if (input == null) return "";
    
    // Escape quotes by doubling them
    String escaped = input.replace("\"", "\"\"");
    
    // Wrap in quotes if contains special characters
    if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
        return "\"" + escaped + "\"";
    }
    return escaped;
}

    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Call this from other controllers to log actions
    public void logUserAction(String sessionId, String username, String module, 
                            String actionType, String entityId, String description) {
        try {
            model.logActivity(
                sessionId,
                username,
                module,
                actionType,
                entityId,
                description,
                "127.0.0.1", // Replace with actual IP
                "JavaApp"    // Replace with actual user agent
            );
        } catch (SQLException ex) {
            System.err.println("Failed to log action: " + ex.getMessage());
        }
    }
}