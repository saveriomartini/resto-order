package ch.hearc.ig.orderresto.services;

import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.persistence.ProductDataMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class ProductServices {

    public Set<Product> getRestaurantProducts(Long restaurantId) throws SQLException {
        Connection dbConnect = null;
        try {
            dbConnect = DbUtils.getConnection();
            dbConnect.setAutoCommit(false);

            ProductDataMapper productDataMapper = ProductDataMapper.getInstance();
            Set<Product> products = productDataMapper.getAllProductsByRestaurant(restaurantId);
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