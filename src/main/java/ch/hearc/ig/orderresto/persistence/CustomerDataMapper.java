package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.*;
import java.sql.*;


public class CustomerDataMapper extends AbstractDataMapper {


    private static CustomerDataMapper instanceCustomerDataMapper;

    private CustomerDataMapper() {
    }

    public static CustomerDataMapper getInstance() {
        if (instanceCustomerDataMapper == null) {
            instanceCustomerDataMapper = new CustomerDataMapper();
        }
        return instanceCustomerDataMapper;
    }

    protected String insertStatement() {
        return "INSERT INTO CLIENT (email, telephone, pays, code_postal, localite, rue, num_rue, nom, forme_sociale, prenom, est_une_femme, type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

    }

    protected void doInsert(RestoObject restoObject, PreparedStatement stmt) throws SQLException {
        Customer customer = (Customer) restoObject;
        stmt.setString(1, customer.getEmail());
        stmt.setString(2, customer.getPhone());
        stmt.setString(3, customer.getAddress().getCountryCode());
        stmt.setString(4, customer.getAddress().getPostalCode());
        stmt.setString(5, customer.getAddress().getLocality());
        stmt.setString(6, customer.getAddress().getStreet());
        stmt.setString(7, customer.getAddress().getStreetNumber());

    }

    @Override
    protected String findStatement() {
        return "";
    }

    public Customer find(Long id) {
        return (Customer) abstractFind(id);
    }



    @Override
    protected RestoObject doLoad(Long id, ResultSet rs) throws SQLException {

        return null;
    }
}