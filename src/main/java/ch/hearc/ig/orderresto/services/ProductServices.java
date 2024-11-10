package ch.hearc.ig.orderresto.services;

import ch.hearc.ig.orderresto.business.*;
import ch.hearc.ig.orderresto.persistence.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class ProductServices {

    public Set<Product> getRestaurantProducts(Long restaurantId) throws SQLException {
        Connection dbConnect = null;
        try {
            dbConnect = ch.hearc.ig.orderresto.service.DbUtils.getConnection();
            dbConnect.setAutoCommit(false);

            ProductDataMapper productDataMapper = ProductDataMapper.getInstance();
            Set<Product> products = (Set<Product>) productDataMapper.findAll(restaurantId);
            System.out.println("Je suis passé par là : getRestaurantProducts de ProductServices");

            dbConnect.commit();
            return products;
        } catch (SQLException e) {
            if (dbConnect != null) {
                dbConnect.rollback();
            }
            throw e;
        } finally {
            if (dbConnect != null) {
                dbConnect.close();
            }
        }
    }
}