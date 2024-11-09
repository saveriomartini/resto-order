package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.service.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class ProductDataMapper {

    private static ProductDataMapper instanceOfProductDataMapper;

    private ProductDataMapper() {
    }

    public static ProductDataMapper getInstance() {
        if (instanceOfProductDataMapper == null) {
            instanceOfProductDataMapper = new ProductDataMapper();
        }
        return instanceOfProductDataMapper;
    }

    protected IdentityMap<Product> identityMapProduct = new IdentityMap<>();

    public Product findById(Long id) {
        if (identityMapProduct.contains(id)) {
            return identityMapProduct.get(id);
        }
        try {
            Connection connection = DbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM produit WHERE numero = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long restaurantId = resultSet.getLong("fk_resto");
                RestaurantDataMapper restaurantDataMapper = RestaurantDataMapper.getInstance();
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

        RestaurantDataMapper restaurantDataMapper = RestaurantDataMapper.getInstance();
        System.out.println("Checking if restaurantId is in identityMapRestaurant: " + restaurantId);
        System.out.println("Current identityMapRestaurant: " + restaurantDataMapper.identityMapRestaurant.toString());
        if (restaurantDataMapper.identityMapRestaurant.contains(restaurantId)) {
            System.out.println("Restaurant found in identityMapRestaurant");
            return restaurantDataMapper.identityMapRestaurant.get(restaurantId).getProductsCatalog();
        } else {
            System.out.println("Restaurant not found in identityMapRestaurant, fetching from database");
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
                    identityMapProduct.put(productId, product);
                    resto.registerProduct(product);
                }
                restaurantDataMapper.identityMapRestaurant.put(resto.getId(), resto);
                System.out.println("Updated identityMapRestaurant: " + restaurantDataMapper.identityMapRestaurant.toString());
            } catch (SQLException e) {
                throw new RuntimeException("Impossible de récupérer les produits.", e);
            }
            return resto.getProductsCatalog();
        }
    }

}