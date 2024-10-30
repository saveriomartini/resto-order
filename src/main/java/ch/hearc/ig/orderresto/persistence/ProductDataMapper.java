package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.service.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ProductDataMapper {

    private Connection connection = DbUtils.getConnection();

    public Product findById(Long id) {

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM produit WHERE numero = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long restaurantId = resultSet.getLong("fk_resto");
                RestaurantDataMapper restaurantDataMapper = new RestaurantDataMapper();
                Restaurant resto = restaurantDataMapper.findById(restaurantId);
                return new Product(
                        resultSet.getLong("numero"),
                        resultSet.getString("nom"),
                        resultSet.getBigDecimal("prix_unitaire"),
                        resultSet.getString("description"),
                        resto
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de récupérer le produit.", e);
        }
        return null;
    }

    public Set<Product> getAllProductsByRestaurant( Long restaurantId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM produit WHERE fk_resto = ?");
            statement.setLong(1, restaurantId);
            ResultSet resultSet = statement.executeQuery();
            Set<Product> products = new HashSet<>();
            while (resultSet.next()) {
                RestaurantDataMapper restaurantDataMapper = new RestaurantDataMapper();
                Restaurant resto = restaurantDataMapper.findById(restaurantId);
                products.add(new Product(
                        resultSet.getLong("numero"),
                        resultSet.getString("nom"),
                        resultSet.getBigDecimal("prix_unitaire"),
                        resultSet.getString("description"),
                        resto
                ));
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de récupérer les produits.", e);
        }
    }
}
