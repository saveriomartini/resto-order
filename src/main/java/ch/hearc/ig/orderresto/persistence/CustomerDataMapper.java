package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.*;
import ch.hearc.ig.orderresto.service.DbUtils;

import java.sql.*;


public class CustomerDataMapper extends AbstractDataMapper {

    private static final String COLUMNS = "(email, telephone, pays, code_postal, localite, rue, num_rue, nom, forme_sociale, prenom, est_une_femme, type)";
    private static String findbyEmailStatement = "SELECT * FROM CLIENT WHERE email = ?";
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
        return "INSERT INTO CLIENT " + COLUMNS + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
        if (customer instanceof PrivateCustomer) {
            stmt.setString(8, ((PrivateCustomer) customer).getFirstName());
            stmt.setString(9, null);
            stmt.setString(10, ((PrivateCustomer) customer).getFirstName());
            stmt.setString(11, ((PrivateCustomer) customer).getGender());
            stmt.setString(12, "P");
        } else {
            stmt.setString(8, ((OrganizationCustomer) customer).getName());
            stmt.setString(9, ((OrganizationCustomer) customer).getLegalForm());
            stmt.setString(10, null);
            stmt.setString(11, null);
            stmt.setString(12, "O");
        }
    }

    @Override
    protected String findStatement() {
        return "SELECT * FROM CLIENT WHERE numero = ?";
    }

    /**
     * @return
     */
    @Override
    protected String findAllStatement() {
        return "";
    }

    public Customer findCustomerByEmail(String email) {
        Customer result = null;
        PreparedStatement findStatement = null;
        try {
            findStatement = DbUtils.getConnection().prepareStatement(findbyEmailStatement);
            findStatement.setString(1, email);
            ResultSet rs = findStatement.executeQuery();
            if (rs.next()) {
                result = (Customer) load(rs);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }






    @Override
    protected RestoObject doLoad(Long id, ResultSet rs) throws SQLException {
        if (rs.getString("type").equals("P")) {
            return new PrivateCustomer(
                    rs.getLong("numero"),
                    rs.getString("email"),
                    rs.getString("telephone"),
                    new Address(
                            rs.getString("pays"),
                            rs.getString("code_postal"),
                            rs.getString("localite"),
                            rs.getString("rue"),
                            rs.getString("num_rue")
                    ),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("est_une_femme")
            );
        } else {
            return new OrganizationCustomer(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("telephone"),
                    new Address(
                            rs.getString("pays"),
                            rs.getString("code_postal"),
                            rs.getString("localite"),
                            rs.getString("rue"),
                            rs.getString("num_rue")
                    ),
                    rs.getString("nom"),
                    rs.getString("forme_sociale")
            );
        }
    }

    public Customer find(long id) {
        return (Customer) abstractFind(id);
    }

}

