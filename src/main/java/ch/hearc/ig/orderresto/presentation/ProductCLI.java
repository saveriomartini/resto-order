package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Order;
import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.persistence.ProductDataMapper;
import ch.hearc.ig.orderresto.persistence.RestaurantDataMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProductCLI extends AbstractCLI {

    private ProductDataMapper productDataMapper = new ProductDataMapper();

    public Product getRestaurantProduct(Restaurant restaurant) {
        this.ln(String.format("Bienvenue chez %s. Choisissez un de nos produits:", restaurant.getName()));

        Object[] products = productDataMapper.getAllProductsByRestaurant(restaurant.getId()).toArray();
        for (int i = 0 ; i < products.length ; i++) {
            Product product = (Product) products[i];
            this.ln(String.format("%d. %s", i, product));
        }
        int index = this.readIntFromUser(products.length - 1);
        return (Product) products[index];
    }
}