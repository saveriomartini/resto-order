package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.business.RestoObject;
import ch.hearc.ig.orderresto.services.RestaurantServices;

import java.util.Set;

public class RestaurantCLI extends AbstractCLI {

    public Restaurant getExistingRestaurant() {
        this.ln("Choisissez un restaurant:");
        RestaurantServices restaurantServices = new RestaurantServices();
        Set<RestoObject> allRestaurants = restaurantServices.getAllRestaurantsFromCacheOrDB();
        Object[] allRestaurantsArray = allRestaurants.toArray();

        for (int i = 0 ; i < allRestaurantsArray.length ; i++) {
            Restaurant restaurant = (Restaurant) allRestaurantsArray[i];
            this.ln(String.format("%d. %s.", i, restaurant.getName()));
        }
        int index = this.readIntFromUser(allRestaurantsArray.length - 1);
        return (Restaurant) allRestaurantsArray[index];
    }
}
