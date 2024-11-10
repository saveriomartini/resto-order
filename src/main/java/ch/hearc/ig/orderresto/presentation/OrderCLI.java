package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.*;
//import ch.hearc.ig.orderresto.persistence.FakeDb;
import ch.hearc.ig.orderresto.persistence.CustomerDataMapper;
import ch.hearc.ig.orderresto.persistence.OrderDataMapper;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class OrderCLI extends AbstractCLI {

    public Order createNewOrder() throws SQLException {
        this.ln("======================================================");
        Restaurant restaurant = (new RestaurantCLI()).getExistingRestaurant();

        this.ln("======================================================");
        this.ln("0. Annuler");
        this.ln("1. Je suis un client existant");
        this.ln("2. Je suis un nouveau client");

        int userChoice = this.readIntFromUser(2);
        if (userChoice == 0) {
            (new MainCLI()).run();
            return null;
        }
        CustomerCLI customerCLI = new CustomerCLI();
        Customer customer = null;
        CustomerDataMapper customerDataMapper = CustomerDataMapper.getInstance();
        if (userChoice == 1) {
            customer = customerCLI.getExistingCustomer();
        } else {
            customer = customerCLI.createNewCustomer();
            customerDataMapper.insert(customer);
            System.out.println("Nouveau client créé : " + customer.getId());
        }

        Order order = new Order(null, customer, restaurant, false, LocalDateTime.now());
        Product product = (new ProductCLI()).getRestaurantProduct(restaurant);
        order.addProduct(product);

        OrderDataMapper orderDataMapper = OrderDataMapper.getInstance();

        orderDataMapper.insert(order);
        orderDataMapper.insert(product);

        product.addOrder(order);
        restaurant.addOrder(order);
        customer.addOrder(order);

        this.ln("Merci pour votre commande!");
        return order;
    }
    public void displayOrders() throws SQLException {
        Customer customer = (new CustomerCLI()).getExistingCustomer();
        if (customer == null) {
            this.ln("Désolé, nous ne connaissons pas cette personne.");
        } else if (customer.getOrders().isEmpty()) {
            this.ln("Vous n'avez pas encore passé de commande.");
        }

        this.ln("Voici les commandes pour " + customer.getEmail() + ":");

        Set<Order> allOrders = customer.getOrders();
        for (Order order : allOrders) {
            LocalDateTime when = order.getWhen();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy à HH:mm");
            this.ln(String.format("Commande %.2f, le %s chez %s.", order.getTotalAmount(), when.format(formatter), order.getRestaurant().getName()));
        }
    }
}