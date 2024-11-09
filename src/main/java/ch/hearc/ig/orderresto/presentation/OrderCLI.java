package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.Order;
import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.persistence.CustomerDataMapper;
import ch.hearc.ig.orderresto.services.OrderServices;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class OrderCLI extends AbstractCLI {

    private OrderServices orderServices;

    public OrderCLI() {
        this.orderServices = new OrderServices();
    }

    public Order createNewOrder() throws SQLException {
        this.ln("======================================================");
        Restaurant restaurant = (new RestaurantCLI()).getExistingRestaurant();
        Product product = (new ProductCLI()).getRestaurantProduct(restaurant);

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
        if (userChoice == 1) {
            customer = customerCLI.getExistingCustomer();
        } else {
            customer = customerCLI.createNewCustomer();
            CustomerDataMapper.getInstance().insert(customer);
            System.out.println("Nouveau client créé : " + customer.getId());
        }

        Order order = orderServices.createNewOrder(customer, restaurant, product);
        this.ln("Merci pour votre commande!");
        System.out.println("Je suis passé par là : createNewOrder de OrderCLI");
        return order;
    }

    public void displayOrders() throws SQLException {
        Customer customer = (new CustomerCLI()).getExistingCustomer();
        if (customer == null) {
            this.ln("Désolé, nous ne connaissons pas cette personne.");
            return;
        }

        Set<Order> allOrders = orderServices.findAllOrdersByCustomer(customer);
        if (allOrders.isEmpty()) {
            this.ln(String.format("Désolé, il n'y a aucune commande pour %s", customer.getEmail()));
            return;
        }

        System.out.println("Je suis passé par là : displayOrders de OrderCLI");
        this.ln("Voici les commandes pour " + customer.getEmail() + ":");

        for (Order order : allOrders) {
            LocalDateTime when = order.getWhen();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy à HH:mm");
            this.ln(String.format("Commande %.2f, le %s chez %s.", order.getTotalAmount(), when.format(formatter), order.getRestaurant().getName()));
        }
    }
}