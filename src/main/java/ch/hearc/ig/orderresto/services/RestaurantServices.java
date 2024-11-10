package ch.hearc.ig.orderresto.services;

import ch.hearc.ig.orderresto.business.RestoObject;
import ch.hearc.ig.orderresto.persistence.RestaurantDataMapper;

import java.util.Set;

public class RestaurantServices {

    public Set<RestoObject> getAllRestaurantsFromCacheOrDB() {
        return RestaurantDataMapper.getInstance().findAll();
    }


}
