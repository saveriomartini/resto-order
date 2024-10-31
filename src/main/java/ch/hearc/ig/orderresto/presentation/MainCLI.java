package ch.hearc.ig.orderresto.presentation;

import ch.hearc.ig.orderresto.business.Order;
import ch.hearc.ig.orderresto.persistence.FakeDb;

public class MainCLI extends AbstractCLI {

    CustomerCLI customerCLI;

    public MainCLI() {
        this.customerCLI = new CustomerCLI();
    }

    public void run() {
        this.ln("======================================================");
        this.ln("Que voulez-vous faire ?");
        this.ln("0. Quitter l'application");
        this.ln("1. Faire une nouvelle commande");
        this.ln("2. Consulter une commande");
        this.ln("3. Rechercher un client par ID");
        this.ln("4. Rechercher un client par email");
        int userChoice = this.readIntFromUser(4);
        this.handleUserChoice(userChoice);
    }

    private void handleUserChoice(int userChoice) {
        if (userChoice == 0) {
            this.ln("Good bye!");
            return;
        }
        OrderCLI orderCLI = new OrderCLI();
        if (userChoice == 1) {
            Order newOrder = orderCLI.createNewOrder();
            FakeDb.getOrders().add(newOrder);
        } else if (userChoice == 2) {
            Order existingOrder = orderCLI.selectOrder();
            if (existingOrder != null) {
                orderCLI.displayOrder(existingOrder);
            }
        } else if (userChoice == 3) {
            CustomerCLI customerCLI = new CustomerCLI();
            this.ln("Entrez l'ID du client :");
            Long id = this.readLongFromUser();
            customerCLI.getCustomerById(id);
        } else if (userChoice == 4) {
            CustomerCLI customerCLI = new CustomerCLI();
            this.ln("Entrez l'email du client :");
            String email = this.readStringFromUser(1, 255, null);
            customerCLI.getCustomerByEmail(email);
        }
        this.run();
    }
}
