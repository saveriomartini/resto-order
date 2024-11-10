package ch.hearc.ig.orderresto.services;

import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.persistence.CustomerDataMapper;

import java.sql.Connection;
import java.sql.SQLException;

public class CustomerServices {

    public Customer createNewCustomer(Customer customer) throws SQLException {
        Connection dbConnect = null;
        try {
            dbConnect = ch.hearc.ig.orderresto.service.DbUtils.getConnection();
            dbConnect.setAutoCommit(false);

            CustomerDataMapper customerDataMapper = CustomerDataMapper.getInstance();
            customerDataMapper.insert(customer);
            System.out.println("Je suis passé par là : creatNewCustomer de CustomerServices");

            dbConnect.commit();
            return customer;
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

    public Customer getExistingCustomerId(Long customerId) throws SQLException {
        System.out.println("Je suis passé par là : getExistingCustomerId de CustomerServices");
        return CustomerDataMapper.getInstance().find(customerId);
    }

    public Customer getExistingCustomerEmail(String email) throws SQLException {
        System.out.println("Je suis passé par là : getExistingCustomerEmail de CustomerServices");
        return CustomerDataMapper.getInstance().findCustomerByEmail(email);
    }
}