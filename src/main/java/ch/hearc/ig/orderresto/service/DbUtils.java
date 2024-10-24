package ch.hearc.ig.orderresto.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Wrapper;

public class DbUtils {
    public static final String DB_URL = "jdbc:oracle:thin:@db.ig.he-arc.ch:1521:ens";
    public static final String DB_USER = "ICL_SM_NC_JD";
    public static final String DB_PASSWORD = "ICL_SM_NC_JD";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
