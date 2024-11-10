package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.*;
import java.sql.*;



public class OrderDataMapper extends AbstractDataMapper {

    public static final String COLUMNS = "(fk_client, fk_resto, a_emporter, quand)";
    private static OrderDataMapper instanceOfOrderdataMapper;

    private OrderDataMapper() {
    }

    public static OrderDataMapper getInstance() {
        if (instanceOfOrderdataMapper == null) {
            instanceOfOrderdataMapper = new OrderDataMapper();
        }
        return instanceOfOrderdataMapper;
    }

    protected String insertStatement() {
        return "INSERT INTO commande" +COLUMNS+ "VALUES (?,?,?,?)";
    }

    @Override
    protected void doInsert(RestoObject restoObject, PreparedStatement stmt) throws SQLException {
        Order order = (Order) restoObject;
        stmt.setLong(1, order.getCustomer().getId());
        stmt.setLong(2, order.getRestaurant().getId());
        stmt.setString(3, order.getTakeAway() ? "O" : "N");
        stmt.setTimestamp(4, Timestamp.valueOf(order.getWhen()));
    }


    @Override
    protected String findStatement() {
        return "SELECT * FROM commande WHERE numero = ?";
    }

    /**
     * @return
     */
    @Override
    protected String findAllStatement() {
        return "";
    }


    @Override
    protected RestoObject doLoad(Long id, ResultSet rs) throws SQLException {
        return new Order(
                rs.getLong("numero"),
                CustomerDataMapper.getInstance().find(rs.getLong("fk_client")),
                RestaurantDataMapper.getInstance().find(rs.getLong("fk_resto")),
                rs.getString("a_emporter").equals("O"),
                rs.getTimestamp("quand").toLocalDateTime()
        );
    }

    protected Order find(Long id) {
        return (Order) abstractFind(id);
    }
}