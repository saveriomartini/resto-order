package ch.hearc.ig.orderresto.services;

import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.persistence.CustomerDataMapper;

import java.sql.Connection;
import java.sql.SQLException;

public class CustomerServices {

    public Customer createNewCustomer(Customer customer) throws SQLException {
        Connection dbConnect = null;
        try {
            dbConnect = DbUtils.getConnection();
            dbConnect.setAutoCommit(false);

            CustomerDataMapper customerDataMapper = CustomerDataMapper.getInstance();
            customerDataMapper.insert(customer);

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
        return CustomerDataMapper.getInstance().findCustomerById(customerId);
    }

    public Customer getExistingCustomerEmail(String email) throws SQLException {
        return CustomerDataMapper.getInstance().findCustomerByEmail(email);
    }
}