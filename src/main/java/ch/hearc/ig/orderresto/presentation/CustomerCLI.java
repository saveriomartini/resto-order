package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Address;
import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.OrganizationCustomer;
import ch.hearc.ig.orderresto.business.PrivateCustomer;

import ch.hearc.ig.orderresto.persistence.CustomerDataMapper;
//import ch.hearc.ig.orderresto.persistence.FakeDb;

public class CustomerCLI extends AbstractCLI {

    private CustomerDataMapper customerDataMapper;

    public CustomerCLI() {
        try {
            this.customerDataMapper = new CustomerDataMapper();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Customer getExistingCustomer() {
        this.ln("Quelle est votre addresse email?");
        String email = this.readEmailFromUser();
        Customer customer = customerDataMapper.findCustomerByEmail(email);
        if (customer != null && customer.getEmail().equals(email)) {
            return customer;
        } else {
            return null;
        }
    }

    /*public Customer getPrivateCustomerByID(Long id) {
        try {
            return CustomerDataMapper.findPrivateByID(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Customer getOrganizationCustomerById(Long id) {
        try {
            return CustomerDataMapper.findOrganizationByID(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public void getCustomerById(Long id) {
        Customer customer = customerDataMapper.findCustomerById(id);
        if (customer != null) {
            this.ln("Le client est le suivant : " + customer);
        } else {
            this.ln("Client non trouvé.");
        }
    }

    public void getCustomerByEmail(String email) {
        Customer customer = customerDataMapper.findCustomerByEmail(email);
        if (customer != null) {
            this.ln("Le client est le suivant : " + customer);
        } else {
            this.ln("Client non trouvé.");
        }
    }

    public Customer createNewCustomer() {
        Customer customer = null;
        this.ln("Êtes-vous un client privé ou une organisation?");
        this.ln("0. Annuler");
        this.ln("1. Un client privé");
        this.ln("2. Une organisation");
        int customerTypeChoice = this.readIntFromUser(2);
        if (customerTypeChoice == 0) {
            return null;
        }
        this.ln("Quelle est votre addresse email?");
        String email = this.readEmailFromUser();
        this.ln("Quelle est votre numéro de téléphone?");
        String phone = this.readStringFromUser();

        if (customerTypeChoice == 1) {
            this.ln("Êtes-vous un homme ou une femme (H/F)?");
            String gender = this.readChoicesFromUser(new String[]{"H", "F"});
            this.ln("Quel est votre prénom?");
            String firstName = this.readStringFromUser();
            this.ln("Quel est votre nom?");
            String lastName = this.readStringFromUser();
            Address address = (new AddressCLI()).getNewAddress();
            customer = new PrivateCustomer(null, phone, email, address, gender, firstName, lastName);
        } else {
            this.ln("Quel est le nom de votre organisation?");
            String name = this.readStringFromUser();
            this.ln(String.format("%s est une société anonyme (SA)?, une association (A) ou une fondation (F)?", name));
            String legalForm = this.readChoicesFromUser(new String[]{"SA", "A", "F"});
            Address address = (new AddressCLI()).getNewAddress();
            customer = new OrganizationCustomer(null, phone, email, address, name, legalForm);
        }

        return customer;
    }
}
