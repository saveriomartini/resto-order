package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Address;

import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.services.DbUtils;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

public class RestaurantDataMapper {

    protected IdentityMap<Restaurant> identityMapRestaurant = new IdentityMap<>();
    private static RestaurantDataMapper instanceOfRestaurantDataMapper;

    public RestaurantDataMapper() {
    }

    public static RestaurantDataMapper getInstance() {
        if (instanceOfRestaurantDataMapper == null) {
            instanceOfRestaurantDataMapper = new RestaurantDataMapper();
        }
        return instanceOfRestaurantDataMapper;
    }

    public Restaurant findById(Long id) {

        Restaurant restaurant = null;
        if (identityMapRestaurant.contains(id)) {
            return identityMapRestaurant.get(id);
        }
        try {
            Connection connection = DbUtils.getConnection();
            java.sql.PreparedStatement statement = connection.prepareStatement("SELECT * FROM restaurant WHERE numero = ?");
            statement.setLong(1, id);
            java.sql.ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                restaurant= new Restaurant(
                        resultSet.getLong("numero"),
                        resultSet.getString("nom"),
                        new Address(
                                resultSet.getString("pays"),
                                resultSet.getString("code_postal"),
                                resultSet.getString("localite"),
                                resultSet.getString("rue"),
                                resultSet.getString("num_rue")
                        )
                );
            }
            identityMapRestaurant.put(id, restaurant);
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Impossible de récupérer le restaurant.", e);
        }
        return restaurant;
    }

    public Set<Restaurant>getAllRestaurants() {

        Set<Restaurant> restaurants = new HashSet<>();
        try {
            Connection connection = DbUtils.getConnection();
            java.sql.PreparedStatement statement = connection.prepareStatement("SELECT * FROM restaurant");
            java.sql.ResultSet resultSet = statement.executeQuery();
            Restaurant restaurant;
            while (resultSet.next()) {
                restaurant= new Restaurant(
                        resultSet.getLong("numero"),
                        resultSet.getString("nom"),
                        new Address(
                                resultSet.getString("pays"),
                                resultSet.getString("code_postal"),
                                resultSet.getString("localite"),
                                resultSet.getString("rue"),
                                resultSet.getString("num_rue")
                        )
                );
                restaurants.add(restaurant);
            }
            return restaurants;
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Impossible de récupérer les restaurants.", e);
        }
    }

}