package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// Configuration base de donnees depuis database.properties
public class DatabaseConfig {

    private static final String FILE_NAME = "database.properties";
    private String driver;
    private String url;
    private String username;
    private String password;
    private String type;

    public DatabaseConfig() {
        Properties props = loadProperties();
        this.type = props.getProperty("db.type", "").trim().toLowerCase();

        if (!type.isEmpty()) {
            // Mode preset : utilisation des valeurs pour ce SGBD
            switch (type) {
                case "mariadb":
                    this.driver  = "org.mariadb.jdbc.Driver";
                    this.url     = "jdbc:mariadb://" + props.getProperty("db.host", "localhost")
                                 + ":" + props.getProperty("db.port", "3306")
                                 + "/" + props.getProperty("db.name", "gestion_eleves");
                    break;
                case "mysql":
                    this.driver  = "com.mysql.cj.jdbc.Driver";
                    this.url     = "jdbc:mysql://" + props.getProperty("db.host", "localhost")
                                 + ":" + props.getProperty("db.port", "3306")
                                 + "/" + props.getProperty("db.name", "gestion_eleves")
                                 + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
                    break;
                case "postgresql":
                    this.driver  = "org.postgresql.Driver";
                    this.url     = "jdbc:postgresql://" + props.getProperty("db.host", "localhost")
                                 + ":" + props.getProperty("db.port", "5432")
                                 + "/" + props.getProperty("db.name", "gestion_eleves");
                    break;
                case "access":
                    this.driver  = "net.ucanaccess.jdbc.UcanaccessDriver";
                    this.url     = "jdbc:ucanaccess://" + props.getProperty("db.path", "./gestion_eleves.accdb")
                                 + ";newDatabaseVersion=V2010;memory=false;singleConnection=true";
                    break;
                default:
                    // Type inconnu : fallback sur les propriétés directes
                    this.driver  = props.getProperty("db.driver");
                    this.url     = props.getProperty("db.url");
                    break;
            }
            this.username = props.getProperty("db.username", "");
            this.password = props.getProperty("db.password", "");
        } else {
            // Mode manuel : utilisation directe des propriétés
            this.driver   = props.getProperty("db.driver");
            this.url      = props.getProperty("db.url");
            this.username = props.getProperty("db.username", "");
            this.password = props.getProperty("db.password", "");
        }

        // Chargement du driver
        try {
            Class.forName(this.driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC introuvable : " + this.driver
                    + "\nAjoutez le JAR correspondant dans le dossier lib/", e);
        }
    }

    public String getDriver()   { return driver; }
    public String getUrl()      { return url; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getType()     { return type; }

    private static Properties loadProperties() {
        Properties props = new Properties();

        // 1. Classpath (JAR, build/)
        try (InputStream in = DatabaseConfig.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (in != null) { props.load(in); return props; }
        } catch (IOException ignored) {}

        // 2. resources/ (IDE, dev)
        try (InputStream in = new FileInputStream("resources/" + FILE_NAME)) {
            props.load(in); return props;
        } catch (IOException ignored) {}

        // 3. Racine projet
        try (InputStream in = new FileInputStream(FILE_NAME)) {
            props.load(in); return props;
        } catch (IOException ignored) {}

        throw new RuntimeException(
            "database.properties introuvable.\n" +
            "Chemins essayes : classpath:/" + FILE_NAME + ", resources/" + FILE_NAME + ", ./" + FILE_NAME);
    }
}
