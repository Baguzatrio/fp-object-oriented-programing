package model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class SessionLogModel {
    private final Connection conn;

    // Database table schema
    private static final String CREATE_TABLE_SQL =
        "CREATE TABLE IF NOT EXISTS session_logs (" +
        "log_id VARCHAR(36) PRIMARY KEY," +
        "session_id TEXT NOT NULL," +
        "username TEXT NOT NULL," +
        "module TEXT NOT NULL," +
        "action_type TEXT NOT NULL," +
        "entity_id TEXT," +
        "description TEXT," +
        "timestamp DATETIME NOT NULL," +
        "ip_address TEXT," +
        "user_agent TEXT)";

    public SessionLogModel(Connection conn) throws SQLException {
        this.conn = conn;
        initializeDatabase();
    }

    private void initializeDatabase() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Create session logs table
            stmt.execute(CREATE_TABLE_SQL);
            
            // Add index for faster queries
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_session_user ON session_logs(username)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_session_module ON session_logs(module)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_session_time ON session_logs(timestamp)");
        }
    }

    /**
     * Logs a user activity with full context
     */
    public void logActivity(
        String sessionId,
        String username,
        String module,
        String actionType,
        String entityId,
        String description,
        String ipAddress,
        String userAgent
    ) throws SQLException {
        String logId = UUID.randomUUID().toString();
        String timestamp = LocalDateTime.now().toString();

        String sql = "INSERT INTO session_logs VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, logId);
            pstmt.setString(2, sessionId);
            pstmt.setString(3, username);
            pstmt.setString(4, module);
            pstmt.setString(5, actionType);
            pstmt.setString(6, entityId);
            pstmt.setString(7, description);
            pstmt.setString(8, timestamp);
            pstmt.setString(9, ipAddress);
            pstmt.setString(10, userAgent);
            
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves all activities for a specific session
     */
    public ResultSet getSessionActivities(String sessionId) throws SQLException {
        String sql = "SELECT * FROM session_logs WHERE session_id = ? ORDER BY timestamp";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, sessionId);
        return pstmt.executeQuery();
    }

    /**
     * Gets all activities for a user
     */
    public ResultSet getUserActivities(String username, LocalDateTime start, LocalDateTime end) 
        throws SQLException {
        String sql = "SELECT * FROM session_logs " +
                     "WHERE username = ? AND timestamp BETWEEN ? AND ? " +
                     "ORDER BY timestamp DESC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, start.toString());
        pstmt.setString(3, end.toString());
        return pstmt.executeQuery();
    }

    /**
     * Gets activities by module (e.g., BAHAN_BAKU, PRODUKSI)
     */
    public ResultSet getModuleActivities(String module, int limit) throws SQLException {
        String sql = "SELECT * FROM session_logs WHERE module = ? ORDER BY timestamp DESC LIMIT ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, module);
        pstmt.setInt(2, limit);
        return pstmt.executeQuery();
    }

    /**
     * Finds suspicious activities (e.g., many deletes in short time)
     */
    public ResultSet detectSuspiciousActivities(int actionThreshold, String actionType) 
        throws SQLException {
        String sql = "SELECT username, COUNT(*) as action_count " +
                     "FROM session_logs " +
                     "WHERE action_type = ? AND timestamp >= datetime('now', '-1 hour') " +
                     "GROUP BY username HAVING action_count >= ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, actionType);
        pstmt.setInt(2, actionThreshold);
        return pstmt.executeQuery();
    }

    /**
 * Gets distinct modules that have been logged
 */
public ResultSet getDistinctModules() throws SQLException {
    String sql = "SELECT DISTINCT module FROM session_logs ORDER BY module";
    return conn.createStatement().executeQuery(sql);
}

/**
 * Gets distinct usernames that have activities
 */
public ResultSet getDistinctUsers() throws SQLException {
    String sql = "SELECT DISTINCT username FROM session_logs ORDER BY username";
    return conn.createStatement().executeQuery(sql);
}

/**
 * Gets recent activities with limit
 */
public ResultSet getRecentActivities(int limit) throws SQLException {
    String sql = "SELECT * FROM session_logs ORDER BY timestamp DESC LIMIT ?";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setInt(1, limit);
    return pstmt.executeQuery();
}

/**
 * Gets all activities without limit
 */
public ResultSet getAllActivities() throws SQLException {
    String sql = "SELECT * FROM session_logs ORDER BY timestamp DESC";
    return conn.createStatement().executeQuery(sql);
}
}