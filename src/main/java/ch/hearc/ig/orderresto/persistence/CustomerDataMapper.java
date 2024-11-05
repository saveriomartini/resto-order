package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Address;
import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.OrganizationCustomer;
import ch.hearc.ig.orderresto.business.PrivateCustomer;
import ch.hearc.ig.orderresto.service.DbUtils;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleType;
import oracle.jdbc.OracleTypes;

import java.sql.*;

public class CustomerDataMapper {

    public Customer findCustomerById(Long id) {
        try {
            Connection dbConnect = DbUtils.getConnection();
            try (PreparedStatement ps = dbConnect.prepareStatement("SELECT forme_sociale FROM CLIENT WHERE numero = ?")) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String legalForm = rs.getString("forme_sociale");
                    if (legalForm != null) {
                        return findOrganizationByID(id);
                    } else {
                        return findPrivateByID(id);
                    }
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Customer findCustomerByEmail(String email) {
        try {
            Connection dbConnect = DbUtils.getConnection();
            try (PreparedStatement ps = dbConnect.prepareStatement("SELECT * FROM CLIENT WHERE email = ?")) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Long idCustomer = rs.getLong("numero");
                    String legalForm = rs.getString("forme_sociale");
                    if (legalForm != null) {
                        return findOrganizationByID(idCustomer);
                    } else {
                        return findPrivateByID(idCustomer);
                    }
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Customer findOrganizationByID(Long id) throws SQLException {
        Connection dbConnect = DbUtils.getConnection();
        try (PreparedStatement ps = dbConnect.prepareStatement("SELECT * FROM CLIENT WHERE numero = ?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Address address = new Address(
                        rs.getString("pays"),
                        rs.getString("code_postal"),
                        rs.getString("localite"),
                        rs.getString("rue"),
                        rs.getString("num_rue")
                );
                return new OrganizationCustomer(
                        rs.getLong("numero"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        address,
                        rs.getString("nom"),
                        rs.getString("forme_sociale")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Customer findPrivateByID(Long id) throws SQLException {
        Connection dbConnect = DbUtils.getConnection();
        try (PreparedStatement ps = dbConnect.prepareStatement("SELECT * FROM CLIENT WHERE numero = ?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Address address = new Address(
                        rs.getString("pays"),
                        rs.getString("code_postal"),
                        rs.getString("localite"),
                        rs.getString("rue"),
                        rs.getString("num_rue")
                );
                return new PrivateCustomer(
                        rs.getLong("numero"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        address,
                        rs.getString("est_une_femme"),
                        rs.getString("prenom"),
                        rs.getString("nom")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long insert(Customer customer) {

        long idCustomer = -1;
        try {
            Connection dbConnect = DbUtils.getConnection();
            String sql = "INSERT INTO CLIENT (email, telephone, pays, code_postal, localite, rue, num_rue, nom, forme_sociale, prenom, est_une_femme, type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?) returning numero into ?";
            try (OraclePreparedStatement ps = (OraclePreparedStatement) dbConnect.prepareStatement(sql)) {
                ps.setString(1, customer.getEmail());
                ps.setString(2, customer.getPhone());
                ps.setString(3, customer.getAddress().getCountryCode());
                ps.setString(4, customer.getAddress().getPostalCode());
                ps.setString(5, customer.getAddress().getLocality());
                ps.setString(6, customer.getAddress().getStreet());
                ps.setString(7, customer.getAddress().getStreetNumber());
                if (customer instanceof OrganizationCustomer) {
                    ps.setString(8, ((OrganizationCustomer) customer).getName());
                    ps.setString(9, ((OrganizationCustomer) customer).getLegalForm());
                    ps.setNull(10, Types.VARCHAR);
                    ps.setNull(11, Types.CHAR);
                    ps.setString(12, "O");
                } else {
                    ps.setString(8, ((PrivateCustomer) customer).getLastName());
                    ps.setNull(9, Types.VARCHAR);
                    ps.setString(10, ((PrivateCustomer) customer).getFirstName());
                    ps.setString(11, ((PrivateCustomer) customer).getGender().equals("H") ? "N" : "O");
                    ps.setString(12, "P");
                }
                ps.registerReturnParameter(13, OracleTypes.NUMBER);
                ps.executeUpdate();
                try (ResultSet rs = ps.getReturnResultSet()) {
                    if (rs.next()) {
                        idCustomer = rs.getLong(1);
                        System.out.println("Customer inserted with id: " + idCustomer);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idCustomer;
    }
}