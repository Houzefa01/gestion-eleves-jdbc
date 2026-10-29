# GestionEleves

Application de gestion des élèves développée en **Java Swing** avec **JDBC** et **MariaDB**.

Architecture **MVC** professionnelle. Projet Java simple, sans gestionnaire de build.

---

## Prérequis

- **Java 17** ou supérieur
- **MariaDB 10.x** ou supérieur

---

## Lancement rapide

```bash
./run.sh
```

Le script fait tout automatiquement : vérifications, base de données, compilation, lancement.

---

## Compilation manuelle

```bash
./compile.sh
```

Les `.class` sont générés dans `build/`.

---

## Lancement manuel (après compilation)

```bash
java -cp "build:lib/*" Main
```

---

## Structure du projet

```
GestionEleves/
├── run.sh                         # Script de lancement automatique
├── compile.sh                     # Script de compilation
├── README.md                      # Documentation
├── script.sql                     # Script SQL de création de la base
├── resources/
│   └── database.properties        # Configuration JDBC
├── lib/                           # Dépendances (JARs)
│   ├── mariadb-java-client-3.4.1.jar
│   └── jbcrypt-0.4.jar
└── src/
    ├── Main.java                  # Point d'entrée
    ├── model/
    │   ├── Eleve.java             # Modèle Élève
    │   └── Utilisateur.java       # Modèle Utilisateur
    ├── dao/
    │   ├── EleveDAO.java          # CRUD Élèves (PreparedStatement)
    │   └── UtilisateurDAO.java    # DAO Utilisateurs
    ├── service/
    │   ├── EleveService.java      # Logique métier Élèves
    │   └── AuthService.java       # Authentification + BCrypt
    ├── controller/
    │   ├── EleveController.java   # Contrôleur Élèves
    │   └── AuthController.java    # Contrôleur Authentification
    ├── view/
    │   ├── LoginView.java         # Fenêtre de connexion
    │   ├── MainView.java          # Fenêtre principale (JTable)
    │   └── EleveFormView.java     # Formulaire Ajout/Modification
    └── util/
        └── DBConnection.java      # Connexion JDBC Singleton
```

---

## Utilisateurs par défaut

| Nom d'utilisateur | Mot de passe | Rôle  |
| ----------------- | ------------ | ----- |
| `admin`           | `admin`      | ADMIN |
| `user`            | `user`       | USER  |

---

## Fonctionnalités

- **Authentification** sécurisée (BCrypt)
- **Tableau JTable** avec tous les élèves
- **Recherche dynamique** (nom, prénom, classe)
- **Ajout** d'un élève avec formulaire complet
- **Modification** par double-clic sur une ligne
- **Suppression** avec confirmation
- **Actualisation** du tableau

---

## Architecture MVC

```
View (Swing)  -->  Controller  -->  Service  -->  DAO  -->  MariaDB
     ↑                                                     |
     └────────────────── Model ◄───────────────────────────┘
```

---

## Dépendances (dossier lib/)

| Fichier | Version | Rôle |
|--------|---------|------|
| mariadb-java-client | 3.4.1 | Driver JDBC MariaDB |
| jbcrypt | 0.4 | Hashage BCrypt |
