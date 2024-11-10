package ch.hearc.ig.orderresto.services;

import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.RestoObject;
import ch.hearc.ig.orderresto.persistence.ProductDataMapper;
import ch.hearc.ig.orderresto.persistence.RestaurantDataMapper;

import java.util.List;
import java.util.Set;

public class ProductServices {
    public Set<RestoObject> getAllRestaurantProductsFromCacheOrDB(Long id) {
        return ProductDataMapper.getInstance().findAll(id);
    }




}