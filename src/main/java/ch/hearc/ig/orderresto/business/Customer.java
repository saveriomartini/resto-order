package ch.hearc.ig.orderresto.business;

import java.util.HashSet;
import java.util.Set;

public abstract class Customer {

    private Long id;
    private String phone;
    private String email;
    private Set<Order> orders;
    private Address address;

    protected Customer(Long id, String phone, String email, Address address) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.orders = new HashSet<>();
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
    public void setId(long aLong) {
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public Address getAddress() {
        return address;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }
}