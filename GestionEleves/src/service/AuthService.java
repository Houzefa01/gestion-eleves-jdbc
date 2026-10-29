package service;

import dao.UtilisateurDAO;
import model.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

// Service d'authentification et gestion des utilisateurs
public class AuthService {

    private final UtilisateurDAO utilisateurDAO;

    public AuthService() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    public Utilisateur authenticate(String username, String plainPassword) {
        Utilisateur utilisateur = utilisateurDAO.findByUsername(username);

        if (utilisateur == null) {
            return null;
        }

        // Vérification du mot de passe avec BCrypt
        if (BCrypt.checkpw(plainPassword, utilisateur.getPassword())) {
            return utilisateur;
        }

        return null;
    }

    public void initializeDefaultUsers() {
        if (utilisateurDAO.hasUsers()) {
            return;
        }

        String adminHash = BCrypt.hashpw("admin", BCrypt.gensalt(12));
        String userHash = BCrypt.hashpw("user", BCrypt.gensalt(12));

        Utilisateur admin = new Utilisateur("Administrateur", "admin", adminHash, "ADMIN");
        Utilisateur user = new Utilisateur("Utilisateur Standard", "user", userHash, "USER");

        utilisateurDAO.insert(admin);
        utilisateurDAO.insert(user);

        System.out.println("Utilisateurs par défaut créés : admin/admin et user/user");
    }

    public boolean verifyAdminPassword(String adminPassword) {
        Utilisateur admin = utilisateurDAO.findByUsername("admin");
        if (admin == null) return false;
        return BCrypt.checkpw(adminPassword, admin.getPassword());
    }

    public List<Utilisateur> getAllUsers() {
        return utilisateurDAO.findAll();
    }

    public boolean createUser(Utilisateur u) {
        u.setPassword(BCrypt.hashpw(u.getPassword(), BCrypt.gensalt(12)));
        return utilisateurDAO.insert(u);
    }

    public boolean updateUser(Utilisateur u) {
        return utilisateurDAO.update(u);
    }

    public boolean deleteUser(int id) {
        return utilisateurDAO.delete(id);
    }

    public Utilisateur getUserById(int id) {
        return utilisateurDAO.findById(id);
    }
}
