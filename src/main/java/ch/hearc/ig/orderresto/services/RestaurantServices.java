package ch.hearc.ig.orderresto.services;

import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.persistence.RestaurantDataMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

public class RestaurantServices {

    public Set<Restaurant> getExistingRestaurant() throws SQLException {
        Connection dbConnect = null;
        try {
            dbConnect = DbUtils.getConnection();
            dbConnect.setAutoCommit(false);

            RestaurantDataMapper restaurantDataMapper = RestaurantDataMapper.getInstance();
            Set<Restaurant> restaurants = restaurantDataMapper.getAllRestaurants();

            dbConnect.commit();
            return restaurants;
        } catch (SQLException e) {
            if (dbConnect != null) {
                dbConnect.rollback();
            }
            throw e;
        } finally {
            if (dbConnect != null) {
                dbConnect.close();
            };
        }
    }
}
