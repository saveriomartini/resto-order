package ch.hearc.ig.orderresto.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
}
