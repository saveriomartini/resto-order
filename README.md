Bachelor of Science en informatique de gestion
62-51.2 Intégration de couches logicielles
1/4
Projet 1
Créer le projet
Veuillez télécharger l’application «resto-order» disponible sur Cyberlearn et l’importer dans votre IDE. Lancez le script de création de la structure des données ainsi que le script d’insertion des données sur votre serveur Oracle.
•
File -> New -> Project from Existing Sources
•
Sélectionnez le fichier pom.xml dans le projet
•
Confirmez en cliquant sur « OK »
•
Note : la version de Java à utiliser est en principe Java 11, mais vous êtes libres d’utiliser des versions plus récentes si vous souhaitez bénéficier de fonctionnalités plus avancées.
Présentation du projet
Voici quelques informations supplémentaires sur l'application :
A la racine de l'archive se trouvent deux scripts SQL : le premier permet de créer la structure (Tables / Séquences / Triggers), tandis que le second permet d'insérer un jeu de données de base.
L'autre élément présent dans l'archive est un projet Maven que vous pouvez importer dans votre IDE. Ce projet est parfaitement exécutable et a pour but d’accueillir les commandes de clients des restaurants de la région. Il est donc possible de passer commande, ou de consulter les commandes existantes.
Naturellement, le projet ne persiste pour l'instant aucune de ses données. Vous allez devoir implémenter complètement la couche de persistance. Cependant, pour que vous puissiez la prendre en main dans un premier temps, le projet de base contient une classe "FakeDb" où sont créés par programmation quelques instances d’objets métier. Il faudra à terme la supprimer pour que toutes les données soient récupérées de la base de données.
Le projet Maven contient quatre packages, à savoir :
-
Package "application" qui contient le main de l’application.
-
Package "business" : contient toutes les classes métier du projet. Le diagramme de classes les présentant toutes se trouve à la troisième page de ce document. Les classes sont toutes des POJO classiques : elles contiennent uniquement leurs attributs, quelques constructeurs, et les getters et setters.
-
Package "persistence" : contient une classe "FakeDb" qui fournit des fausses données à l'application, en attendant que cette dernière se connecte à la base de données.
-
Package "presentation" : contient des classes qui s'occupent de fournir une interface en ligne de commande (CLI). Seuls les « use cases » principaux sont implémentés. Il n’est par exemple pas possible d’ajouter un restaurant ou un produit.
Cette application est uniquement destinée à vous permettre de mettre en pratique rapidement les concepts vus en cours. Certaines simplifications ont été faites pour vous éviter de perdre du temps sur certains détails.
Exercice 1
Veuillez persister le modèle de domaine « restaurants » dans la structure de données fournie. Pour ce faire, veuillez utiliser le design pattern Data Mapper [1] de M. Fowler tout en utilisant les classes à disposition sur Cyberlearn. Pensez aux méthodes d’ajouts, de suppression et de mises à jour, mais également aux méthodes de recherche !
Bachelor of Science en informatique de gestion
62-51.2 Intégration de couches logicielles
2/4
Vous devriez être capable d’apporter des solutions aux questions suivantes à la fin de cet exercice :
•
Comment gérer les connexions JDBC ?
•
Comment générer les identifiants techniques (PK) et faire en sorte qu’ils soient présents dans les objets après leur création ?
•
Comment gérer les relations
•
Que faire dans le Data Mapper lors de la recherche du restaurant (rechercher uniquement le restaurant ? également ses commandes ? où est-ce que l’on s’arrête ?)
•
Doit-il y avoir des relations entre les différents Data Mapper ?
•
Combien d’interactions (requêtes JDBC) sont effectuées à la base de données avec votre code ?
Il n’y a pas toujours de réponses « justes » à ces questions, mais des considérations à prendre en compte qui peuvent se révéler acceptables ou non.
Ne tentez pas de tout gérer en même temps. Commencez simple (classe City par exemple) et construisez petit à petit.
Cet exercice est important, assez long et représente les fondations sur laquelle nous allons construire nos connaissances dans la suite de ce cours.
[1] http://martinfowler.com/eaaCatalog/dataMapper.html
Bachelor of Science en informatique de gestion
62-51.2 Intégration de couches logicielles
3/4
Modèle métier :
Bachelor of Science en informatique de gestion
62-51.2 Intégration de couches logicielles
4/4
Modèle de données :
[Projet1-Exercice 1.pdf](https://github.com/user-attachments/files/17487386/Projet1-Exercice.1.pdf)
