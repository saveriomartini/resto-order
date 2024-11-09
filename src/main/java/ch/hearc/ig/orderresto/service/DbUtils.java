package ch.hearc.ig.orderresto.service;

import java.sql.*;

public class DbUtils {

    private static Connection connection;
    private static final String DB_URL = "jdbc:oracle:thin:@db.ig.he-arc.ch:1521:ens";
    private static final String DB_USER = "ICL_SM_NC_JD";
    private static final String DB_PASSWORD = "ICL_SM_NC_JD";

    private DbUtils() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            synchronized (DbUtils.class) {
                if (connection == null || connection.isClosed()) {
                    connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                }
            }
        }
        return connection;
    }


    public static void cleanUp(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void cleanUp(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void cleanUp(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void cleanUp(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void cleanUp(Connection conn, Statement stmt, ResultSet rs) {
        cleanUp(rs);
        cleanUp(stmt);
        cleanUp(conn);
    }
}

