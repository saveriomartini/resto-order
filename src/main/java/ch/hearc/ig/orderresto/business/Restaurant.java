package ch.hearc.ig.orderresto.business;

import ch.hearc.ig.orderresto.persistence.ProductDataMapper;

import java.util.HashSet;
import java.util.Set;

public class Restaurant {

    private Long id;
    private String name;
    private Set<Order> orders;
    private Address address;
    private Set<Product> productsCatalog;

    public Restaurant(Long id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.orders = new HashSet<>();
        this.productsCatalog = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public Address getAddress() {
        return address;
    }

    public Set<Product> getProductsCatalog() {
        if (productsCatalog == null) {
            ProductDataMapper productDataMapper = ProductDataMapper.getInstance();
            productsCatalog = productDataMapper.getAllProductsByRestaurant(id);
        }
        return productsCatalog;
    }

    public void registerProduct(Product p) {
        if (p.getRestaurant() != this) {
            throw new RuntimeException("Restaurant mismatch!");
        }
        this.productsCatalog.add(p);
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }
}