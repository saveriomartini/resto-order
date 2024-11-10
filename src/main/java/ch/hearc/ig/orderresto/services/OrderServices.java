package ch.hearc.ig.orderresto.services;

import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.Order;
import ch.hearc.ig.orderresto.business.Product;
import ch.hearc.ig.orderresto.business.Restaurant;
import ch.hearc.ig.orderresto.persistence.OrderDataMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

public class OrderServices {

    public Order createNewOrder(Customer customer, Restaurant restaurant, Set<Product> products) throws SQLException {
        Connection dbConnect = null;
        try {
            dbConnect = DbUtils.getConnection();
            dbConnect.setAutoCommit(false);

            Order order = new Order(null, customer, restaurant, false, LocalDateTime.now());
            for (Product product : products){
                order.addProduct(product);
            }


            OrderDataMapper orderDataMapper = OrderDataMapper.getInstance();
            orderDataMapper.insertOrder(order);
            orderDataMapper.insertOrderProducts(order);

            restaurant.addOrder(order);
            customer.addOrder(order);

            dbConnect.commit();
            return order;
        } catch (SQLException e) {
            if (dbConnect != null) {
                dbConnect.rollback();
            }
            throw e;
        } finally {
            if (dbConnect != null) {
                dbConnect.close();
            }
        }
    }

    public Set<Order> findAllOrdersByCustomer(Customer customer) throws SQLException {
        return OrderDataMapper.getInstance().findAllOrdersByCustomer(customer);
    }
}
