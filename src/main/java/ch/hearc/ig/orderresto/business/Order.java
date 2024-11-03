package ch.hearc.ig.orderresto.business;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Order {

    private Long id;
    private Customer customer;
    private Restaurant restaurant;
    private Set<Product> products;
    private Boolean takeAway;
    private LocalDateTime when;
    private BigDecimal totalAmount;

    public Order(Long id, Customer customer, Restaurant restaurant, Boolean takeAway, LocalDateTime when) {
        this.id = id;
        this.customer = customer;
        this.restaurant = restaurant;
        this.products = new HashSet<>();
        this.takeAway = takeAway;
        this.totalAmount = new BigDecimal(0);
        this.when = when;
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Boolean getTakeAway() {
        return takeAway;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void addProduct(Product product) {
        this.products.add(product);
        this.totalAmount = this.totalAmount.add(product.getUnitPrice());
    }

    public void setId(long aLong) {
        this.id = aLong;
    }
}