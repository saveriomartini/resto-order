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

    private final IdentityMap<Product> identityMapProduct = new IdentityMap<>();
    private static ProductDataMapper instanceOfProductDataMapper;

    public ProductDataMapper() {
    }

    // Singleton pour récupération, si elle existe, ou nouvelle instanciation si elle n'existe pas de ProductDataMapper
    public static ProductDataMapper getInstance() {
        if (instanceOfProductDataMapper == null) {
            instanceOfProductDataMapper = new ProductDataMapper();
        }
        return instanceOfProductDataMapper;
    }

    // Récupération du produit, en fonction de l'ID, dans l'IdentityMap de Product si il existe
    // Sinon on va chercher le produit dans la DB et on l'ajoute dans l'IdentityMap
    // Nécessaire car utilisé dans la création de commande dans OrderDataMapper
    public IdentityMap<Product> getIdentityMapProduct() {
        return identityMapProduct;
    }


    // Récupération du produit, en fonction de l'ID, dans la DB
    // N'est appelée que si le produit n'est pas dans l'IdentityMap de Product
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
    // Récupération de tous les produits d'un restaurant en fonction de l'ID du restaurant saisi
    public Set<Product> getAllProductsByRestaurant(Long restaurantId) {
        Set<Product> products = new HashSet<>();

        // Vérification si les produits sont déjà dans l'identityMap
        for (Product product : instanceOfProductDataMapper.identityMapProduct.values()) {
            if (product.getRestaurant().getId().equals(restaurantId)) {
                products.add(product);
            }
        }

        // Si les produits ne sont pas dans l'identityMap, récupération dans la base de données
        if (products.isEmpty()) {
            try {
                Connection connection = DbUtils.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM produit WHERE fk_resto = ?");
                statement.setLong(1, restaurantId);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    RestaurantDataMapper restaurantDataMapper = new RestaurantDataMapper();
                    Restaurant resto = restaurantDataMapper.findById(restaurantId);
                    Product product = new Product(
                            resultSet.getLong("numero"),
                            resultSet.getString("nom"),
                            resultSet.getBigDecimal("prix_unitaire"),
                            resultSet.getString("description"),
                            resto
                    );
                    products.add(product);
                    identityMapProduct.put(product.getId(), product);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Impossible de récupérer les produits.", e);
            }
        }
        return products;
    }
}