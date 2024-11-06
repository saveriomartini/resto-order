package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Address;
import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.service.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class RestaurantDataMapper {



    public Restaurant findById(Long id) {

        try {
            Connection connection = DbUtils.getConnection();
            java.sql.PreparedStatement statement = connection.prepareStatement("SELECT * FROM restaurant WHERE numero = ?");
            statement.setLong(1, id);
            java.sql.ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Restaurant(
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
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Impossible de récupérer le restaurant.", e);
        }
        return null;
    }

    public Set<Restaurant>getAllRestaurants() {

        Set<Restaurant> restaurants = new HashSet<>();
        try {
            Connection connection = DbUtils.getConnection();
            java.sql.PreparedStatement statement = connection.prepareStatement("SELECT * FROM restaurant");
            java.sql.ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                restaurants.add(new Restaurant(
                        resultSet.getLong("numero"),
                        resultSet.getString("nom"),
                        new Address(
                                resultSet.getString("pays"),
                                resultSet.getString("code_postal"),
                                resultSet.getString("localite"),
                                resultSet.getString("rue"),
                                resultSet.getString("num_rue")
                        )
                ));
            }
            return restaurants;
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Impossible de récupérer les restaurants.", e);
        }
    }

    public Set<Product> findProductsByRestaurantId(Long restaurantId) {
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
    }

    /*public void insert (Restaurant restaurant) {

        Address address = new Address("CH", "1000", "Lausanne", "Rue de la Grotte", "1");

        String name = restaurant.getName();
        String code_postal = address.getPostalCode();
        String localite = address.getLocality();
        String rue = address.getStreet();
        String num_rue = address.getStreetNumber();
        String pays = address.getCountryCode();

        try {
            java.sql.PreparedStatement statement = connection.prepareStatement("INSERT INTO restaurant (name, code_postal, localite, rue, num_rue, pays ) VALUES (name, code_postal, localite, rue, num_rue, pays)");
            statement.executeUpdate();
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Impossible d'insérer le restaurant.", e);
        }
    }*/



}