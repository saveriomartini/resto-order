SET DEFINE OFF;

INSERT INTO RESTAURANT(nom, code_postal, localite, rue, num_rue, pays)
    VALUES ('Alpes Et Lac', '2000', 'Neuchâtel', 'Place de La Gare', '2', 'CH');
INSERT INTO RESTAURANT(nom, code_postal, localite, rue, num_rue, pays)
VALUES ('Les Belgeries', '2000', 'Neuchâtel', 'Pl. Blaise-Cendrars', '5', 'CH');
INSERT INTO RESTAURANT(nom, code_postal, localite, rue, num_rue, pays)
VALUES ('Domino''s Pizza', '2000', 'Neuchâtel', 'Espa. de l''Europe', '1/3', 'CH');
COMMIT;

INSERT INTO PRODUIT(fk_resto, prix_unitaire, nom, description)
    VALUES (1, 20, 'Tartare de chevreuil', 'De saison');
INSERT INTO PRODUIT(fk_resto, prix_unitaire, nom, description)
    VALUES (2, 5, 'Frites mini', '150g de frites + sauce au choix');
INSERT INTO PRODUIT(fk_resto, prix_unitaire, nom, description)
    VALUES (2, 7.5, 'Frites normales', '250g de frites + sauce au choix');
INSERT INTO PRODUIT(fk_resto, prix_unitaire, nom, description)
    VALUES (3, 16, 'MARGHERITA', 'Sauce tomate, extra mozzarella (45% MG/ES)');
INSERT INTO PRODUIT(fk_resto, prix_unitaire, nom, description)
    VALUES (3, 18, 'VÉGÉTARIENNE', 'Sauce tomate, mozzarella (45% MG/ES), champignons, poivrons, tomates cherry, olives, oignons rouges');
INSERT INTO PRODUIT(fk_resto, prix_unitaire, nom, description)
    VALUES (3, 21, 'CHEESE & HAM', 'Sauce tomate, mozzarella (45% MG/ES), jambon (CH)');
COMMIT;

INSERT INTO CLIENT(type, email, est_une_femme, nom, prenom, forme_sociale, code_postal, localite, rue, num_rue, pays, telephone)
    VALUES ('P', 'vincent.pazeller@he-arc.ch', 'N', 'Pazeller', 'Vincent', NULL, '2525', 'Le Landeron', 'Rue du test', '2', 'CH', '+41 76 000 00 00');

INSERT INTO CLIENT(type, email, est_une_femme, nom, prenom, forme_sociale, code_postal, localite, rue, num_rue, pays, telephone)
    VALUES ('O', 'info@rhne.ch', NULL, 'Hôpital Pourtales', NULL, 'SA', '2000', 'Neuchâtel', 'Rue du test', '5b', 'CH', '+41 32 000 00 00');
COMMIT;


INSERT INTO VILLES(code_postal, nom_ville) VALUES ('2000', 'Neuch�tel');
COMMIT;