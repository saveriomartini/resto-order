package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Restaurant;
//import ch.hearc.ig.orderresto.persistence.FakeDb;
import ch.hearc.ig.orderresto.persistence.RestaurantDataMapper;
import ch.hearc.ig.orderresto.services.RestaurantServices;

import java.util.Set;

public class RestaurantCLI extends AbstractCLI {

    private RestaurantServices restaurantServices;

    public RestaurantCLI() {
        this.restaurantServices = new RestaurantServices();
    }

    public Restaurant getExistingRestaurant() {
        this.ln("Choisissez un restaurant:");
        Set<Restaurant> allRestaurants = null;

        try {
            allRestaurants = restaurantServices.getExistingRestaurant();
        } catch (Exception e) {
            this.ln("Erreur lors de la récupération des restaurants.");
            e.printStackTrace();
            return null;
        }

        Object[] allRestaurantsArray = allRestaurants.toArray();
        for (int i = 0; i < allRestaurantsArray.length; i++) {
            Restaurant restaurant = (Restaurant) allRestaurantsArray[i];
            this.ln(String.format("%d. %s.", i, restaurant.getName()));
        }
        int index = this.readIntFromUser(allRestaurantsArray.length - 1);
        System.out.println("Je suis passé par là : getExistingRestaurant de RestaurantCLI");

        return (Restaurant) allRestaurantsArray[index];
    }
}