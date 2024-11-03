package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.Order;
import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.service.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class OrderDataMapper {


    public Order insertOrder(Order order) {
        try {
            Connection dbConnect = DbUtils.getConnection();
            String sql = "INSERT INTO COMMANDE (FK_client, FK_resto, A_emporter, Quand) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = dbConnect.prepareStatement(sql, new String[] {"numero"})) {
                ps.setLong(1, order.getCustomer().getId());
                ps.setLong(2, order.getRestaurant().getId());
                ps.setString(3, order.getTakeAway() ? "O" : "N");
                ps.setTimestamp(4, java.sql.Timestamp.valueOf(order.getWhen()));
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        order.setId(rs.getLong(1));
                        System.out.println("Order inserted with id: " + order.getId());
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

    public Set<Order> findAllOrdersByCustomer(Customer customer) throws SQLException {
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



    }



}


