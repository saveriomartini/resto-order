package ch.hearc.ig.orderresto.service;

import java.sql.Connection;

public class DbUtils {


    public static Connection getConnection() {

        String jdbcUrl = "jdbc:oracle:thin:@db.ig.he-arc.ch:1521:ens";
        String username = "nilo_castillo";
        String password = "nilo_castillo";

        try {
            return java.sql.DriverManager.getConnection(jdbcUrl, username, password);
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Impossible de se connecter à la base de données.", e);
        }
    }
}
