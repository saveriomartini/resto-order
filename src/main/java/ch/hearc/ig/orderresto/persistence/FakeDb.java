package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class FakeDb {
    private static Set<Customer> customers;
    private static Set<Restaurant> restaurants;
    private static Set<Order> orders;

    public static Set<Customer> getCustomers() {
        if (FakeDb.customers == null) {
            FakeDb.customers = new HashSet<>();

            Address address1 = new Address("CH", "2525", "Le Landeron", "Rue du test", "2");
            PrivateCustomer customer1 = new PrivateCustomer(null, "+41 76 000 00 00", "vincent.pazeller@he-arc.ch", address1, "M", "Vincent", "Pazeller");
            FakeDb.customers.add(customer1);

            Address address2 = new Address("CH", "2000", "Neuchâtel", "Rue du test", "5b");
            OrganizationCustomer customer2 = new OrganizationCustomer(null, "+41 32 000 00 00", "info@rhne.ch", address2, "Hôpital Pourtales", "SA");
            FakeDb.customers.add(customer2);
        }
        return FakeDb.customers;
    }

    public static void addCustomer(Customer customer) {
        FakeDb.getCustomers().add(customer);
    }

    public static Set<Restaurant> getRestaurants() {
        if (FakeDb.restaurants == null) {
            FakeDb.restaurants = new HashSet<>();

            Address address1 = new Address("CH", "2000", "Neuchâtel", "Place de La Gare", "2");
            Restaurant r1 = new Restaurant(null, "Alpes Et Lac");
            new Product(null, "Tartare de chevreuil", new BigDecimal(20),  "De saison", r1);
            FakeDb.restaurants.add(r1);

            Address address2 = new Address("CH", "2000", "Neuchâtel", "Pl. Blaise-Cendrars", "5");
            Restaurant r2 = new Restaurant(null, "Les Belgeries");
            new Product(null, "Frites mini", new BigDecimal("5"),  "150g de frites + sauce au choix", r2);
            new Product(null, "Frites normales", new BigDecimal("7.5"),  "250g de frites + sauce au choix", r2);
            FakeDb.restaurants.add(r2);

            Address address3 = new Address("CH", "2000", "Neuchâtel", "Espa. de l'Europe", "1/3");
            Restaurant r3 = new Restaurant(null, "Domino's Pizza");
            new Product(null, "MARGHERITA", new BigDecimal("16"),  "Sauce tomate, extra mozzarella (45% MG/ES)", r3);
            new Product(null, "VÉGÉTARIENNE", new BigDecimal("18"),  "Sauce tomate, mozzarella (45% MG/ES), champignons, poivrons, tomates cherry, olives, oignons rouges", r3);
            new Product(null, "CHEESE & HAM", new BigDecimal("21"),  "Sauce tomate, mozzarella (45% MG/ES), jambon (CH)", r3);
            FakeDb.restaurants.add(r3);

        }
        return FakeDb.restaurants;
    }

    public static Set<Order> getOrders() {
        if (FakeDb.orders == null) {
            FakeDb.orders = new HashSet<>();
        }
        return FakeDb.orders;
    }
    
}
