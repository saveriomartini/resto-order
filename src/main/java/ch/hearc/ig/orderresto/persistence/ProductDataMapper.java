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

    private IdentityMap<Product> identityMapProduct = new IdentityMap<>();

    public Product findById(Long id) {
        try {
            Connection connection = DbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM produit WHERE numero = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long restaurantId = resultSet.getLong("fk_resto");
                RestaurantDataMapper restaurantDataMapper = new RestaurantDataMapper();
                Restaurant resto = restaurantDataMapper.findById(restaurantId);
                Product product = new Product(
                        resultSet.getLong("numero"),
                        resultSet.getString("nom"),
                        resultSet.getBigDecimal("prix_unitaire"),
                        resultSet.getString("description"),
                        resto
                );
                identityMapProduct.put(id, product);
                return product;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de récupérer le produit.", e);
        }
        return null;
    }

    public Set<Product> getAllProductsByRestaurant(Long restaurantId) {
        RestaurantDataMapper restaurantDataMapper = new RestaurantDataMapper();

        if (restaurantDataMapper.identityMapRestaurant.contains(restaurantId)) {
            System.out.println(restaurantDataMapper.identityMapRestaurant.get(restaurantId).getProductsCatalog());
            System.out.println("coucou");
            return restaurantDataMapper.identityMapRestaurant.get(restaurantId).getProductsCatalog();
        } else {
            Restaurant resto = restaurantDataMapper.findById(restaurantId);
            try {
                Connection connection = DbUtils.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM produit WHERE fk_resto = ?");
                statement.setLong(1, restaurantId);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    long productId = resultSet.getLong("numero");
                    Product product = new Product(
                            resultSet.getLong("numero"),
                            resultSet.getString("nom"),
                            resultSet.getBigDecimal("prix_unitaire"),
                            resultSet.getString("description"),
                            resto
                    );
                    resto.registerProduct(product);
                    identityMapProduct.put(productId, product);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Impossible de récupérer les produits.", e);
            }
            return resto.getProductsCatalog();
        }
    }

}