package model;

// Modèle représentant un utilisateur.
public class Utilisateur {

    private int id;
    private String nom;
    private String username;
    private String password;
    private String role;

    // Constructeur par défaut.
    public Utilisateur() {
    }

    // Constructeur complet.
    public Utilisateur(int id, String nom, String username, String password, String role) {
        this.id = id;
        this.nom = nom;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Constructeur pour insertion.
    public Utilisateur(String nom, String username, String password, String role) {
        this.nom = nom;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // =========================================================================
    // Getters et Setters
    // =========================================================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
