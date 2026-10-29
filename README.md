# GestionEleves — gestion des élèves en Java Swing + JDBC

Application de bureau de **gestion des élèves** (projet de cours JDBC), en architecture
**MVC** : Java Swing pour l'interface, JDBC pour l'accès aux données, authentification
avec mots de passe hachés (jBCrypt) et gestion des utilisateurs.

Deux moteurs de base de données sont supportés et sélectionnés par fichier de configuration :

- **MariaDB** (`resources/database_mariadb.properties`)
- **Microsoft Access** `.accdb` via UCanAccess / Jackcess (`resources/database_access.properties`),
  sans serveur à installer

## Contenu du dépôt

| Dossier | Contenu |
|---|---|
| `GestionEleves/` | Version principale : `src/` (controller, dao, service, view, util), `lib/` (pilotes JDBC), scripts `run`/`compile`/`build`/`setup` pour Linux et Windows, `script.sql` |
| `GestionEleves/Netbeans/` | Variante du projet sous forme de projet NetBeans |
| `JDBC/GestionEleves/` | Variante avec bascule de base de données (`db-switch.sh`) |
| `*.jar` (racine) | Bibliothèques : HSQLDB, Jackcess, commons-lang3, commons-logging |

## Lancement

Prérequis : Java 17+, et MariaDB 10+ si vous utilisez ce moteur.

```bash
cd GestionEleves
./run.sh          # vérifie l'environnement, prépare la base, compile et lance
# ou manuellement
./compile.sh && java -cp "build:lib/*" Main
```

Sous Windows : `setup.bat`, puis `run.bat`.

Une base d'exemple Access est fournie : `resources/gestion_eleves_example.accdb`.
Les bases contenant de vraies données ne sont pas versionnées.
