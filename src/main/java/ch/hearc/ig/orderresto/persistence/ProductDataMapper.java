package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.*;
import java.sql.*;
import java.util.List;

public class ProductDataMapper extends AbstractDataMapper {


    public static final String COLUMNS = "(fk_resto, nom, prix_unitaire, description)";
    private static ProductDataMapper instanceOfProductDataMapper;

    private ProductDataMapper() {
    }

    public static ProductDataMapper getInstance() {
        if (instanceOfProductDataMapper == null) {
            instanceOfProductDataMapper = new ProductDataMapper();
        }
        return instanceOfProductDataMapper;
    }

    @Override
    protected String insertStatement() {
        return "INSERT INTO produit" + COLUMNS + "VALUES (?,?,?,?)";
    }

    protected void doInsert(RestoObject restoObject, PreparedStatement stmt) throws SQLException {
        Product product = (Product) restoObject;
        stmt.setLong(1, product.getRestaurant().getId());
        stmt.setString(2, product.getName());
        stmt.setBigDecimal(3, product.getUnitPrice());
        stmt.setString(4, product.getDescription());
    }

    @Override
    protected String findStatement() {
        return "SELECT * FROM produit WHERE numero = ?";
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
        return new Product(
                rs.getLong("numero"),
                rs.getString("nom"),
                rs.getBigDecimal("prix_unitaire"),
                rs.getString("description"),
                RestaurantDataMapper.getInstance().find(rs.getLong("fk_resto"))
        );
    }

    protected Product find(Long id) {
        return (Product) abstractFind(id);
    }

}