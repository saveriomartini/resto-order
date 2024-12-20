package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.Order;
import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.services.DbUtils;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class OrderDataMapper {

    private final IdentityMap<Order> identityMapOrder = new IdentityMap<>();

    private static OrderDataMapper instanceOfOrderDataMapper;

    private OrderDataMapper() {
    }

    // Singleton, pour récupération si elle existe ou nouvelle instanciation si elle n'existe pas de OrderDataMapper
    public static OrderDataMapper getInstance() {
        if (instanceOfOrderDataMapper == null) {
            instanceOfOrderDataMapper = new OrderDataMapper();
        }
        return instanceOfOrderDataMapper;
    }

    // Récupération de toutes les commandes d'un client en fonction du client saisi
   public Set<Order> findAllOrdersByCustomer(Customer customer) {

       Set<Order> orderSet = new HashSet<>();

        // Vérification si la commande est déjà dans l'IdentityMap de l'instance
       for (Order order : OrderDataMapper.instanceOfOrderDataMapper.identityMapOrder.values()) {
           if (order.getCustomer().getId().equals(customer.getId())) {
               orderSet.add(order);
           }
       }

       // Si la map est vide et donc orderSet aussi, on va chercher les commandes dans la DB grâce à une jointure des tables commande, produit_commande, produit et restaurant
       // Ces jointures permettent de récupérer les informations nécessaires pour instancier les objets Order, Product et Restaurant et garantir les relations fk
       if (orderSet.isEmpty()) {
           try {
               Connection dbConnect = DbUtils.getConnection();
               try (PreparedStatement ps = dbConnect.prepareStatement(
                       "SELECT c.numero AS order_id, c.quand, c.a_emporter, p.numero AS product_id, p.nom, p.prix_unitaire, p.description, r.numero AS restaurant_id, r.nom AS restaurant_name FROM COMMANDE c JOIN PRODUIT_COMMANDE pc ON c.numero = pc.fk_commande JOIN PRODUIT p ON pc.fk_produit = p.numero JOIN RESTAURANT r ON c.fk_resto = r.numero WHERE c.fk_client = ?")) {
                   ps.setLong(1, customer.getId());
                   ResultSet rs = ps.executeQuery();
                   while (rs.next()) {
                       Long orderId = rs.getLong("order_id");
                       LocalDateTime when = rs.getTimestamp("quand").toLocalDateTime();
                       Boolean takeAway = rs.getBoolean("a_emporter");
                       Long restaurantId = rs.getLong("restaurant_id");

                       Restaurant restaurant = RestaurantDataMapper.getInstance().findById(restaurantId);
                       Order order = OrderDataMapper.instanceOfOrderDataMapper.identityMapOrder.get(orderId);
                       if (order == null) {
                           order = new Order(orderId, customer, restaurant, takeAway, when);
                           OrderDataMapper.instanceOfOrderDataMapper.identityMapOrder.put(orderId, order);
                       }

                       Long productId = rs.getLong("product_id");
                       String productName = rs.getString("nom");
                       BigDecimal productPrice = rs.getBigDecimal("prix_unitaire");
                       String productDescription = rs.getString("description");

                       Product product = ProductDataMapper.getInstance().getIdentityMapProduct().get(productId);
                       if (product == null) {
                           product = new Product(productId, productName, productPrice, productDescription, restaurant);
                           ProductDataMapper.getInstance().getIdentityMapProduct().put(productId, product);
                       }

                       order.addProduct(product);
                       orderSet.add(order);
                   }
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
        return orderSet;
    }

    // Insertion d'une commande dans la DB et ajout dans l'IdentityMap
    public Order insertOrder(Order order) {
        try {
            Connection dbConnect = DbUtils.getConnection();
            String sql = "INSERT INTO COMMANDE (FK_client, FK_resto, A_emporter, Quand) VALUES (?, ?, ?, ?) RETURNING numero INTO ?";
            try (OraclePreparedStatement ps = (OraclePreparedStatement) dbConnect.prepareStatement(sql)) {
                ps.setLong(1, order.getCustomer().getId());
                ps.setLong(2, order.getRestaurant().getId());
                ps.setString(3, order.getTakeAway() ? "O" : "N");
                ps.setTimestamp(4, java.sql.Timestamp.valueOf(order.getWhen()));
                ps.registerReturnParameter(5, OracleTypes.NUMBER);
                ps.executeUpdate();

                try (ResultSet rs = ps.getReturnResultSet()) {
                    if (rs.next()) {

                        order.setId(rs.getLong(1));
                        System.out.println("Order id: " + order.getId());
                    }
                }
                OrderDataMapper.instanceOfOrderDataMapper.identityMapOrder.put(order.getId(), order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    //Insertion des produits liés une commande dans la DB et ajout dans l'IdentityMap si nécessaire
    public void insertOrderProducts(Order order) {

        try {
            Connection dbConnect = DbUtils.getConnection();
            String sql = "INSERT INTO produit_commande (FK_commande, FK_produit) VALUES (?, ?)";
            try (PreparedStatement ps = dbConnect.prepareStatement(sql)) {
                for (Product product : order.getProducts()) {
                    ps.setLong(1, order.getId());
                    ps.setLong(2, product.getId());
                    ps.addBatch();
                    OrderDataMapper.instanceOfOrderDataMapper.identityMapOrder.put(order.getId(), order);
                }
                ps.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}