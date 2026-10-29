package controller;

import model.Utilisateur;
import service.AuthService;

import java.util.List;

// Contrôleur d'authentification - lien entre LoginView et AuthService
public class AuthController {

    private final AuthService authService;

    public AuthController() {
        this.authService = new AuthService();
    }

    public Utilisateur login(String username, String password) {
        return authService.authenticate(username, password);
    }

    public void initializeDefaultUsers() { authService.initializeDefaultUsers(); }

    public boolean verifyAdminPassword(String adminPassword) {
        return authService.verifyAdminPassword(adminPassword);
    }

    public List<Utilisateur> getAllUsers() {
        return authService.getAllUsers();
    }

    public boolean createUser(Utilisateur u) {
        return authService.createUser(u);
    }

    public boolean updateUser(Utilisateur u) {
        return authService.updateUser(u);
    }

    public boolean deleteUser(int id) {
        return authService.deleteUser(id);
    }

    public Utilisateur getUserById(int id) {
        return authService.getUserById(id);
    }
}
