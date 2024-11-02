package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Restaurant;
//import ch.hearc.ig.orderresto.persistence.FakeDb;
import ch.hearc.ig.orderresto.persistence.RestaurantDataMapper;

import java.util.Set;

public class RestaurantCLI extends AbstractCLI {

    public Restaurant getExistingRestaurant() {
        this.ln("Choisissez un restaurant:");
        RestaurantDataMapper restaurantDataMapper = new RestaurantDataMapper();
        Set<Restaurant> allRestaurants = restaurantDataMapper.getAllRestaurants();
         Object [] allRestaurantsArray = allRestaurants.toArray();
        for (int i = 0 ; i < allRestaurantsArray.length ; i++) {
            Restaurant restaurant = (Restaurant) allRestaurantsArray[i];
            this.ln(String.format("%d. %s.", i, restaurant.getName()));
        }
        int index = this.readIntFromUser(allRestaurantsArray.length - 1);

        return (Restaurant) allRestaurantsArray[index];
    }
}
