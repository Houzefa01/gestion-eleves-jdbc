package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// Configuration base de donnees depuis database_<name>.properties
public class DatabaseConfig {

    private final String fileName;
    private String driver;
    private String url;
    private String username;
    private String password;
    private String type;

    public DatabaseConfig(String configName) {
        this.fileName = "database_" + configName + ".properties";
        Properties props = loadProperties();
        this.type = props.getProperty("db.type", "").trim().toLowerCase();
        String params = props.getProperty("db.params", "");

        if (!type.isEmpty()) {
            switch (type) {
                case "mariadb":
                    this.driver  = "org.mariadb.jdbc.Driver";
                    this.url     = "jdbc:mariadb://" + props.getProperty("db.host", "localhost")
                                 + ":" + props.getProperty("db.port", "3306")
                                 + "/" + props.getProperty("db.name", "gestion_eleves")
                                 + params;
                    break;
                case "mysql":
                    this.driver  = "com.mysql.cj.jdbc.Driver";
                    this.url     = "jdbc:mysql://" + props.getProperty("db.host", "localhost")
                                 + ":" + props.getProperty("db.port", "3306")
                                 + "/" + props.getProperty("db.name", "gestion_eleves")
                                 + params;
                    break;
                case "postgresql":
                    this.driver  = "org.postgresql.Driver";
                    this.url     = "jdbc:postgresql://" + props.getProperty("db.host", "localhost")
                                 + ":" + props.getProperty("db.port", "5432")
                                 + "/" + props.getProperty("db.name", "gestion_eleves")
                                 + params;
                    break;
                case "access":
                    this.driver  = "net.ucanaccess.jdbc.UcanaccessDriver";
                    this.url     = "jdbc:ucanaccess://" + props.getProperty("db.path", "./gestion_eleves.accdb");
                    break;
                default:
                    this.driver  = props.getProperty("db.driver");
                    this.url     = props.getProperty("db.url");
                    break;
            }
            this.username = props.getProperty("db.username", "");
            this.password = props.getProperty("db.password", "");
        } else {
            this.driver   = props.getProperty("db.driver");
            this.url      = props.getProperty("db.url");
            this.username = props.getProperty("db.username", "");
            this.password = props.getProperty("db.password", "");
        }

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

    private Properties loadProperties() {
        Properties props = new Properties();

        try (InputStream in = DatabaseConfig.class.getClassLoader().getResourceAsStream(fileName)) {
            if (in != null) { props.load(in); return props; }
        } catch (IOException ignored) {}

        try (InputStream in = new FileInputStream("resources/" + fileName)) {
            props.load(in); return props;
        } catch (IOException ignored) {}

        try (InputStream in = new FileInputStream(fileName)) {
            props.load(in); return props;
        } catch (IOException ignored) {}

        throw new RuntimeException(
            fileName + " introuvable.\n" +
            "Chemins essayes : classpath:/" + fileName + ", resources/" + fileName + ", ./" + fileName);
    }
}
