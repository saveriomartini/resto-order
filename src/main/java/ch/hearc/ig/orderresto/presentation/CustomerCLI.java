package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Address;
import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.OrganizationCustomer;
import ch.hearc.ig.orderresto.business.PrivateCustomer;

import ch.hearc.ig.orderresto.services.CustomerServices;

import java.sql.SQLException;

public class CustomerCLI extends AbstractCLI {

    private final CustomerServices customerServices;

    // Initialisation de l'instance de CustomerServices
    public CustomerCLI() {
        this.customerServices = new CustomerServices();
    }

    public Customer getExistingCustomer() {
        this.ln("Quelle est votre addresse email?");
        String email = this.readEmailFromUser();
        Customer customer = null;
        try {
            customer = customerServices.getExistingCustomerEmail(email);
            if (customer != null) {
                Long customerId = customer.getId();
                customer = customerServices.getExistingCustomerId(customerId);
            }
        } catch (SQLException e) {
            this.ln("Erreur lors de la récupération du client.");
            e.printStackTrace();
        }
        if (customer != null && customer.getEmail().equals(email)) {
            return customer;
        } else {
            return null;
        }
    }

    // Création d'un nouveau client, privé ou organisation, en fonction du choix de l'utilisateur
    // Le client est créé par l'instance de CustomerServices et les méthodes de celle-ci
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

        try {
            customer = customerServices.createNewCustomer(customer);
        } catch (SQLException e) {
            this.ln("Erreur lors de la création du client.");
            e.printStackTrace();
        }

        return customer;
    }
}
