package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.Order;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.service.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class OrderDataMapper {

    ProductDataMapper productDataMapper = new ProductDataMapper();

    public void insert(Order currentOrder) {
        try (Connection db = DbUtils.getConnection();
             PreparedStatement dbStatement = db.prepareStatement(
                     "INSERT INTO COMMANDE (fk_client, fk_resto, a_emporter, quand) VALUES (?, ?, ?, SYSDATE)"
             )) {

            dbStatement.setLong(1, currentOrder.getCustomer().getId());
            dbStatement.setLong(2, currentOrder.getRestaurant().getId());
            dbStatement.setString(3, currentOrder.getTakeAway() ? "O" : "N"); // Assuming 'O' for take-away, 'N' otherwise


                throw new RuntimeException("Insertion failed, no rows affected.");
            } catch (SQLException e) {
            throw new RuntimeException(e);
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


