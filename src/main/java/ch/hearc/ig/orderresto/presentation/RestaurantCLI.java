package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Restaurant;

import ch.hearc.ig.orderresto.services.RestaurantServices;

import java.util.Set;

public class RestaurantCLI extends AbstractCLI {

    private final RestaurantServices restaurantServices;

    // Initialisation de l'instance de RestaurantServices
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

        return (Restaurant) allRestaurantsArray[index];
    }
}