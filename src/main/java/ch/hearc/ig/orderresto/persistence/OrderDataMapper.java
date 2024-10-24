package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Order;
import ch.hearc.ig.orderresto.service.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDataMapper {



    public void insert(Order order) throws SQLException{
        try (Connection db = DbUtils.getConnection();
             PreparedStatement dbStatement = db.prepareStatement(
                     "INSERT INTO COMMANDE (fk_client, fk_resto,a_emporter,quand) VALUES (3,1,'O',SYSDATE)"
             );

        ) {
            dbStatement.executeUpdate();
        }
    }
}
