package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.services.ProductServices;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ProductCLI extends AbstractCLI {

    private ProductServices productServices;

    public ProductCLI() {
        this.productServices = new ProductServices();
    }

    public Product getRestaurantProduct(Restaurant restaurant) {
        this.ln(String.format("Bienvenue chez %s. Choisissez un de nos produits:", restaurant.getName()));

        Set<Product> productSet = null;
        try {
            productSet = productServices.getRestaurantProducts(restaurant.getId());
        } catch (SQLException e) {
            this.ln("Erreur lors de la récupération des produits.");
            e.printStackTrace();
            return null;
        }

        Object[] products = productSet.toArray();
        for (int i = 0; i < products.length; i++) {
            Product product = (Product) products[i];
            this.ln(String.format("%d. %s", i, product));
        }

        int index = this.readIntFromUser(products.length - 1);
        System.out.println("Je suis passé par là : getRestaurantProduct de ProductCLI");
        return (Product) products[index];
    }
}