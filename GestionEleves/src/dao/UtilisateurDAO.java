package dao;

import model.Utilisateur;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// DAO pour la table "utilisateurs"
public class UtilisateurDAO {

    public Utilisateur findByUsername(String username) {
        String sql = "SELECT id, nom, username, password, role FROM utilisateurs WHERE username = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToUtilisateur(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur : " + e.getMessage());
        }

        return null;
    }

    public boolean insert(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateurs (nom, username, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getUsername());
            stmt.setString(3, utilisateur.getPassword());
            stmt.setString(4, utilisateur.getRole());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de l'utilisateur : " + e.getMessage());
        }

        return false;
    }

    public boolean hasUsers() {
        String sql = "SELECT COUNT(*) FROM utilisateurs";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des utilisateurs : " + e.getMessage());
        }

        return false;
    }

    public List<Utilisateur> findAll() {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT id, nom, username, password, role FROM utilisateurs ORDER BY id";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapToUtilisateur(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des utilisateurs : " + e.getMessage());
        }
        return list;
    }

    public boolean update(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateurs SET nom = ?, username = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getUsername());
            stmt.setString(3, utilisateur.getPassword());
            stmt.setString(4, utilisateur.getRole());
            stmt.setInt(5, utilisateur.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
        return false;
    }

    public Utilisateur findById(int id) {
        String sql = "SELECT id, nom, username, password, role FROM utilisateurs WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapToUtilisateur(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur : " + e.getMessage());
        }
        return null;
    }

    private Utilisateur mapToUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(rs.getInt("id"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setUsername(rs.getString("username"));
        utilisateur.setPassword(rs.getString("password"));
        utilisateur.setRole(rs.getString("role"));
        return utilisateur;
    }
}
