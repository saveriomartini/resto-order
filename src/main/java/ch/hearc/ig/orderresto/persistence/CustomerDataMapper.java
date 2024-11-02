package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Address;
import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.OrganizationCustomer;
import ch.hearc.ig.orderresto.business.PrivateCustomer;
import ch.hearc.ig.orderresto.service.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDataMapper {

    public CustomerDataMapper() throws SQLException {
    }

    // Test commit
    public static Customer findCustomerById(Long id) {
        try {
            Connection dbConnect = DbUtils.getConnection();
            try (PreparedStatement ps = dbConnect.prepareStatement("SELECT forme_sociale FROM CLIENT WHERE numero = ?")) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String legalForm = rs.getString("forme_sociale");
                    if (legalForm != null) {
                        return CustomerDataMapper.findOrganizationByID(id);
                    } else {
                        return CustomerDataMapper.findPrivateByID(id);
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

    public static Customer findCustomerByEmail(String email) {
        try {
            Connection dbConnect = DbUtils.getConnection();
            try (PreparedStatement ps = dbConnect.prepareStatement("SELECT * FROM CLIENT WHERE email = ?")) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String emailCustomer = rs.getString("email");
                    Long idCustomer = rs.getLong("numero");
                    if (emailCustomer != null) {
                        return CustomerDataMapper.findOrganizationByID(idCustomer);
                    } else {
                        return CustomerDataMapper.findPrivateByID(idCustomer);
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

    public static Customer findOrganizationByID(Long id) throws SQLException {
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

    public static Customer findPrivateByID(Long id) throws SQLException {
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

        try {
            Connection dbConnect = DbUtils.getConnection();
            try (PreparedStatement ps = dbConnect.prepareStatement("INSERT INTO CLIENT (email, telephone, pays, code_postal, localite, rue, num_rue, nom, forme_sociale, prenom, est_une_femme, type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)")) {
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
                    ps.setString(10, null);
                    ps.setString(11, null);
                    ps.setString(12, "O");
                } else {
                    ps.setString(8, ((PrivateCustomer) customer).getLastName());
                    ps.setString(9, null);
                    ps.setString(10, ((PrivateCustomer) customer).getFirstName());
                    if (((PrivateCustomer) customer).getGender() == "H") {
                        ps.setString(11, "N");
                    } else {
                        ps.setString(11, "O");
                    }
                    ps.setString(12, "P");
                }
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}