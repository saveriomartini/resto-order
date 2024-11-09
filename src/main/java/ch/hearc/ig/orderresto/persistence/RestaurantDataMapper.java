package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Address;
import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.service.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RestaurantDataMapper extends AbstractRestoMapper {


    private static RestaurantDataMapper instanceOfRestaurantDataMapper;

    private RestaurantDataMapper() {
    }

    public static RestaurantDataMapper getInstance() {
        if (instanceOfRestaurantDataMapper == null) {
            instanceOfRestaurantDataMapper = new RestaurantDataMapper();
        }
        return instanceOfRestaurantDataMapper;
    }

    public Restaurant findById(Long id) {

        Restaurant restaurant = null;
        try {
            Connection connection = DbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM restaurant WHERE numero = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

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
                        //restaurant.getProductsCatalog().add() a finir !!


                );
            }
            cache.put(id, restaurant);
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de récupérer le restaurant.", e);
        }
        return restaurant;
    }

    public Set<Restaurant>getAllRestaurants() {

        Set<Restaurant> restaurants = new HashSet<>();
        try {
            Connection connection = DbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM restaurant");
            ResultSet resultSet = statement.executeQuery();
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
                cache.put(restaurant.getId(), restaurant);
            }
            return restaurants;
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de récupérer les restaurants.", e);
        }
    }

/*    public Set<Product> findProductsByRestaurantId(Long restaurantId) {
        Set<Product> products = new HashSet<>();
        Restaurant restaurant = findById(restaurantId);
        try {
            Connection connection = DbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM product WHERE restaurant_id = ?"
            );
            statement.setLong(1, restaurantId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                products.add(new Product(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getBigDecimal("unit_price"),
                        resultSet.getString("description"),
                        restaurant
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de récupérer les produits.", e);
        }
        return products;
    }*/
}