package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.*;
import java.sql.*;


public class RestaurantDataMapper extends AbstractDataMapper {

    public static final String COLUMNS = "numero, nom, pays, code_postal, localite, rue, num_rue";
    private static RestaurantDataMapper instanceOfRestaurantDataMapper;

    private RestaurantDataMapper() {
    }

    public static RestaurantDataMapper getInstance() {
        if (instanceOfRestaurantDataMapper == null) {
            instanceOfRestaurantDataMapper = new RestaurantDataMapper();
        }
        return instanceOfRestaurantDataMapper;
    }


    protected String insertStatement() {
        return "INSERT INTO restaurant (nom, pays, code_postal, localite, rue, num_rue) VALUES (?,?,?,?,?,?)";
    }


    protected void doInsert(RestoObject restoObject, PreparedStatement stmt) throws SQLException {
        Restaurant restaurant = (Restaurant) restoObject;
        stmt.setString(1, restaurant.getName());
        stmt.setString(2, restaurant.getAddress().getCountryCode());
        stmt.setString(3, restaurant.getAddress().getPostalCode());
        stmt.setString(4, restaurant.getAddress().getLocality());
        stmt.setString(5, restaurant.getAddress().getStreet());
        stmt.setString(6, restaurant.getAddress().getStreetNumber());
    }


    @Override
    protected String findStatement() {
        return "SELECT * FROM restaurant WHERE numero = ?";
    }

    public Restaurant find(Long id) {
        return (Restaurant) abstractFind(id);
    }


    @Override
    protected RestoObject doLoad(Long id, ResultSet rs) throws SQLException {
        return new Restaurant(
                rs.getLong("numero"),
                rs.getString("nom"),
                new Address(
                        rs.getString("pays"),
                        rs.getString("code_postal"),
                        rs.getString("localite"),
                        rs.getString("rue"),
                        rs.getString("num_rue")
                )
        );
    }



}