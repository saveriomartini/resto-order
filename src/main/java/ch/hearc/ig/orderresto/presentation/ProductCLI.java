package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.persistence.ProductDataMapper;

import java.util.List;

public class ProductCLI extends AbstractCLI {

    private ProductDataMapper productDataMapper = ProductDataMapper.getInstance();

    public Product getRestaurantProduct(Restaurant restaurant) {
        this.ln(String.format("Bienvenue chez %s. Choisissez un de nos produits:", restaurant.getName()));

        List<Product> products = (List<Product>) restaurant.getProductsCatalog();

        for (int i = 0; i < products.size(); i++) {
            this.ln(String.format("%d. %s", i, products.get(i).getName()));
        }
        return null;
    }
}