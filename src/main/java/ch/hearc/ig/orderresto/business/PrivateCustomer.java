package ch.hearc.ig.orderresto.business;

import java.math.BigDecimal;

public class PrivateCustomer extends Customer {

    private String gender;
    private String firstName;
    private String lastName;

    public PrivateCustomer(Long id, String phone, String email, Address address, String gender, String firstName, String lastName) {
        super(id, phone, email, address);
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "ID No. " + getId() +
                ", Client privé" +
                ", Nom : '" + lastName + '\'' +
                ", Prénom : '" + firstName + '\'' +
                ", Addresse : " + getAddress() +
                ", Telephone : '" + getPhone() + '\'' +
                ", Email : '" + getEmail() + '\'' +
                ", Genre : '" + (gender == null ? "Femme" : "Homme") + '\'';
    }
}