package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.business.RestoObject;
import ch.hearc.ig.orderresto.services.ProductServices;

import java.util.Set;

public class ProductCLI extends AbstractCLI {

    public Product getRestaurantProduct(Restaurant restaurant) {
        this.ln(String.format("Bienvenue chez %s. Choisissez un de nos produits:", restaurant.getName()));
        ProductServices productServices = new ProductServices();
        Set<RestoObject> products = productServices.getAllRestaurantProductsFromCacheOrDB(restaurant.getId());
        Object[] productsArray = products.toArray();

        for (int i = 0; i < productsArray.length; i++) {
            Product product = (Product) productsArray[i];
            this.ln(String.format("%d. %s.", i, product.getName()));
        }
        int index = this.readIntFromUser(productsArray.length - 1);
        return (Product) productsArray[index];
    }
}