package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.Order;
import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.service.DbUtils;
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public void insertOrderProducts(Order order) {
        try {
            Connection dbConnect = DbUtils.getConnection();
            String sql = "INSERT INTO produit_commande (FK_commande, FK_produit) VALUES (?, ?)";
            try (PreparedStatement ps = dbConnect.prepareStatement(sql)) {
                for (Product product : order.getProducts()) {
                    ps.setLong(1, order.getId());
                    ps.setLong(2, product.getId());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*public Set<Order> findAllOrdersByCustomer(Customer customer) throws SQLException {
        Connection db = DbUtils.getConnection();
        try (PreparedStatement dbStatement = db.prepareStatement(
                "SELECT * FROM COMMANDE WHERE fk_client = ?"
        )) {
            dbStatement.setLong(1, customer.getId());
            ResultSet dbResult = dbStatement.executeQuery();
            Set<Order> ordersFound = new HashSet<>();
            while (dbResult.next()) {
                Restaurant restaurant = new RestaurantDataMapper().findById(dbResult.getLong("fk_resto"));
                Order currentOrder = new Order(
                        dbResult.getLong("numero"),
                        customer,
                        restaurant,// TODO: restaurant
                        dbResult.getString("a_emporter").equals("O"), // Assuming 'O' for take-away, 'N' otherwise
                        dbResult.getTimestamp("quand").toLocalDateTime()
                );
                ordersFound.add(currentOrder);
            }
            return ordersFound;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/

    public Set<Order> findAllOrdersByCustomer(Customer customer) throws SQLException {
        Set<Order> orders = new HashSet<>();
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

                Restaurant restaurant = new RestaurantDataMapper().findById(restaurantId);
                Order order = new Order(orderId, customer, restaurant, takeAway, when);

                Long productId = rs.getLong("product_id");
                String productName = rs.getString("nom");
                BigDecimal productPrice = rs.getBigDecimal("prix_unitaire");
                String productDescription = rs.getString("description");

                Product product = new Product(productId, productName, productPrice, productDescription, restaurant);
                order.addProduct(product);

                orders.add(order);
            }
        }
        return orders;
    }


}