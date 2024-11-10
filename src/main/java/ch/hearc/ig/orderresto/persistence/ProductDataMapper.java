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

        Set<Product> products = new HashSet<>();
        for (Product product : identityMapProduct.values()) {
            if (product.getRestaurant().getId().equals(restaurantId)) {
                products.add(product);
            }
        }

        if (!products.isEmpty()) {
            System.out.println("Products from identity map");
            return products;
        }

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
                        RestaurantDataMapper.getInstance().findById(restaurantId)
                );
                identityMapProduct.put(productId, product);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de récupérer les produits.", e);
        }
        return products;
    }

}