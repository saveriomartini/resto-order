package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Address;
import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.OrganizationCustomer;
import ch.hearc.ig.orderresto.business.PrivateCustomer;
import ch.hearc.ig.orderresto.services.DbUtils;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

import java.sql.*;

public class CustomerDataMapper {

    private final IdentityMap<Customer> identityMapCustomer = new IdentityMap<>();
    private static CustomerDataMapper instanceCustomerDataMapper;

    private CustomerDataMapper() {
    }

    // Singleton pour récupération, si elle existe, ou nouvelle instanciation si elle n'existe pas de CustomerDataMapper
    public static CustomerDataMapper getInstance() {
        if (instanceCustomerDataMapper == null) {
            instanceCustomerDataMapper = new CustomerDataMapper();
        }
        return instanceCustomerDataMapper;
    }

    // Récupération du client, en fonction de l'ID, dans l'IdentityMap de Customer si il existe
    // Sinon on va chercher le client dans la DB et on l'ajoute dans l'IdentityMap
    // La méthode recherche si le client est une organisation ou un client privé
    public Customer findCustomerById(Long id) {

        if (identityMapCustomer.contains(id)) {
            return identityMapCustomer.get(id);
        }
        try {
            Connection dbConnect = DbUtils.getConnection();
            try (PreparedStatement ps = dbConnect.prepareStatement("SELECT forme_sociale FROM CLIENT WHERE numero = ?")) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String legalForm = rs.getString("forme_sociale");
                    Customer customer;
                    if (legalForm != null) {
                        customer = findOrganizationByID(id);
                    } else {
                        customer = findPrivateByID(id);
                    }
                    identityMapCustomer.put(id, customer);
                    return customer;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Récupération du client dans l'IdentityMap de Customer si il existe
    // Sinon on va chercher le client dans la DB et on l'ajoute dans l'IdentityMap
    // La méthode recherche si le client est une organisation ou un client privé
    public Customer findCustomerByEmail(String email) {

        for (Customer customer : identityMapCustomer.values()) {

            if (customer.getEmail().equals(email)) {
                System.out.println("Customer found in identity map");
                return customer;
            }
        }
        try {
            Connection dbConnect = DbUtils.getConnection();
            try (PreparedStatement ps = dbConnect.prepareStatement("SELECT * FROM CLIENT WHERE email = ?")) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Long idCustomer = rs.getLong("numero");
                    String legalForm = rs.getString("forme_sociale");
                    Customer customer;
                    if (legalForm != null) {
                        customer = findOrganizationByID(idCustomer);
                    } else {
                        customer = findPrivateByID(idCustomer);
                    }
                    identityMapCustomer.put(idCustomer, customer);
                    return customer;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Récupération du client organisation et de ses spécificités dans l'IdentityMap de Customer si il existe
    // Sinon on va chercher le client dans la DB et on l'ajoute dans l'IdentityMap
    // La méthode ne traite que les clients de type organisation
    public Customer findOrganizationByID(Long id) throws SQLException {

        if (identityMapCustomer.contains(id)) {
            return identityMapCustomer.get(id);
        }

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

    // Récupération du client privé et de ses spécificités dans l'IdentityMap de Customer si il existe
    // Sinon on va chercher le client dans la DB et on l'ajoute dans l'IdentityMap
    // La méthode ne traite que les clients de type privé
    public Customer findPrivateByID(Long id) throws SQLException {

        if (identityMapCustomer.contains(id)) {
            return identityMapCustomer.get(id);
        }
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

    // Insertion du client dans la DB et ajout dans l'IdentityMap de Customer
    // La méthode gère si le client est de type organisation ou privé
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
                        customer.setId(idCustomer);
                        identityMapCustomer.put(idCustomer, customer);
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