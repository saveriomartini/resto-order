package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.business.RestoObject;
import ch.hearc.ig.orderresto.service.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class ProductDataMapper extends AbstractDataMapper {

    private static ProductDataMapper instanceOfProductDataMapper;

    private ProductDataMapper() {
    }

    public static ProductDataMapper getInstance() {
        if (instanceOfProductDataMapper == null) {
            instanceOfProductDataMapper = new ProductDataMapper();
        }
        return instanceOfProductDataMapper;
    }


    @Override
    protected String findStatement() {
        return "";
    }

    @Override
    protected void doInsert(RestoObject restoObject, PreparedStatement stmt) throws SQLException {

    }

    @Override
    protected String insertStatement() {
        return "";
    }

    public Product abstractFind(Long id) {
        if (cache.containsKey(id)) {
            return (Product) cache.get(id);
        }
        try {
            Connection connection = DbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM produit WHERE numero = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long restaurantId = resultSet.getLong("fk_resto");
                RestaurantDataMapper restaurantDataMapper = RestaurantDataMapper.getInstance();
                Restaurant resto = (Restaurant) restaurantDataMapper.abstractFind(restaurantId);
                Product product = new Product(
                        resultSet.getLong("numero"),
                        resultSet.getString("nom"),
                        resultSet.getBigDecimal("prix_unitaire"),
                        resultSet.getString("description"),
                        resto
                );
                cache.put(id, product);
                return product;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de récupérer le produit.", e);
        }
        return null;
    }

    @Override
    protected RestoObject doLoad(Long id, ResultSet rs) throws SQLException {
        return null;
    }

    public Set<Product> getAllProductsByRestaurant(Long restaurantId) {

        RestaurantDataMapper restaurantDataMapper = RestaurantDataMapper.getInstance();
        System.out.println("Checking if restaurantId is in identityMapRestaurant: " + restaurantId);
        System.out.println("Current identityMapRestaurant: " + restaurantDataMapper.cache.toString());
        if (restaurantDataMapper.cache.containsKey(restaurantId)) {
            System.out.println("Restaurant found in identityMapRestaurant");
            Restaurant resto = (Restaurant) restaurantDataMapper.cache.get(restaurantId);
            return resto.getProductsCatalog();
        } else {
            System.out.println("Restaurant not found in identityMapRestaurant, fetching from database");
            Restaurant resto = (Restaurant) restaurantDataMapper.abstractFind(restaurantId);
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
                    cache.put(productId, product);
                    resto.registerProduct(product);
                }
                restaurantDataMapper.cache.put(resto.getId(), resto);
                System.out.println("Updated identityMapRestaurant: " + restaurantDataMapper.cache.toString());
            } catch (SQLException e) {
                throw new RuntimeException("Impossible de récupérer les produits.", e);
            }
            return resto.getProductsCatalog();
        }
    }

}