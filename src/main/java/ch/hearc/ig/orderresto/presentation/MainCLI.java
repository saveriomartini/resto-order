package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Customer;
import ch.hearc.ig.orderresto.business.Order;
import ch.hearc.ig.orderresto.business.Product;
//import ch.hearc.ig.orderresto.persistence.FakeDb;
import ch.hearc.ig.orderresto.persistence.OrderDataMapper;
import ch.hearc.ig.orderresto.persistence.ProductDataMapper;

import java.sql.SQLException;

public class MainCLI extends AbstractCLI {
    public void run() throws SQLException {
        this.ln("======================================================");
        this.ln("Que voulez-vous faire ?");
        this.ln("0. Quitter l'application");
        this.ln("1. Faire une nouvelle commande");
        this.ln("2. Consulter une commande");
        this.ln("3. Consulter un client");
        int userChoice = this.readIntFromUser(3);
        this.handleUserChoice(userChoice);
    }

    private void handleUserChoice(int userChoice) throws SQLException {
        if (userChoice == 0) {
            this.ln("Good bye!");
            return;
        }
        OrderCLI orderCLI = new OrderCLI();
        if (userChoice == 1) {
            Order newOrder = orderCLI.createNewOrder();
            OrderDataMapper odm = new OrderDataMapper();
            odm.insert(newOrder);
            orderCLI.displayOrder(newOrder);
        } else if (userChoice == 2) {
            Order existingOrder = orderCLI.selectOrder();
            if (existingOrder != null) {
                orderCLI.displayOrder(existingOrder);
            }
        } else if (userChoice == 3) {
            Customer getExistingCustomer = new CustomerCLI().getExistingCustomer();
            if (getExistingCustomer != null) {
                this.ln("Le client est le suivant : " + getExistingCustomer);
            } else {
                this.ln("Client non trouvé.");
            }
        }
        this.run();
    }
}
