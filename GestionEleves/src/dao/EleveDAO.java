package dao;

import model.Eleve;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// DAO pour la table "eleves".
public class EleveDAO {

    // Récupère tous les élèves.
    public List<Eleve> findAll() {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT id, nom, prenom, date_naissance, sexe, adresse, telephone, email, classe FROM eleves ORDER BY nom, prenom";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                eleves.add(mapToEleve(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des élèves : " + e.getMessage());
        }

        return eleves;
    }

    // Recherche un élève par ID.
    public Eleve findById(int id) {
        String sql = "SELECT id, nom, prenom, date_naissance, sexe, adresse, telephone, email, classe FROM eleves WHERE id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToEleve(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'élève par ID : " + e.getMessage());
        }

        return null;
    }

    // Recherche des élèves par mot-clé.
    public List<Eleve> search(String keyword) {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT id, nom, prenom, date_naissance, sexe, adresse, telephone, email, classe FROM eleves "
                   + "WHERE LOWER(nom) LIKE ? OR LOWER(prenom) LIKE ? OR LOWER(classe) LIKE ? "
                   + "ORDER BY nom, prenom";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likePattern = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, likePattern);
            stmt.setString(2, likePattern);
            stmt.setString(3, likePattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    eleves.add(mapToEleve(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche d'élèves : " + e.getMessage());
        }

        return eleves;
    }

    // Insère un nouvel élève.
    public boolean insert(Eleve eleve) {
        String sql = "INSERT INTO eleves (nom, prenom, date_naissance, sexe, adresse, telephone, email, classe) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, eleve.getNom());
            stmt.setString(2, eleve.getPrenom());
            stmt.setDate(3, Date.valueOf(eleve.getDateNaissance()));
            stmt.setString(4, eleve.getSexe());
            stmt.setString(5, eleve.getAdresse());
            stmt.setString(6, eleve.getTelephone());
            stmt.setString(7, eleve.getEmail());
            stmt.setString(8, eleve.getClasse());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        eleve.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de l'élève : " + e.getMessage());
        }

        return false;
    }

    // Met à jour un élève existant.
    public boolean update(Eleve eleve) {
        String sql = "UPDATE eleves SET nom = ?, prenom = ?, date_naissance = ?, sexe = ?, "
                   + "adresse = ?, telephone = ?, email = ?, classe = ? WHERE id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, eleve.getNom());
            stmt.setString(2, eleve.getPrenom());
            stmt.setDate(3, Date.valueOf(eleve.getDateNaissance()));
            stmt.setString(4, eleve.getSexe());
            stmt.setString(5, eleve.getAdresse());
            stmt.setString(6, eleve.getTelephone());
            stmt.setString(7, eleve.getEmail());
            stmt.setString(8, eleve.getClasse());
            stmt.setInt(9, eleve.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'élève : " + e.getMessage());
        }

        return false;
    }

    // Supprime un élève par son ID.
    public boolean delete(int id) {
        String sql = "DELETE FROM eleves WHERE id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'élève : " + e.getMessage());
        }

        return false;
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM eleves";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Erreur count eleves : " + e.getMessage());
        }
        return 0;
    }

    // Convertit une ligne ResultSet en objet Eleve.
    private Eleve mapToEleve(ResultSet rs) throws SQLException {
        Eleve eleve = new Eleve();
        eleve.setId(rs.getInt("id"));
        eleve.setNom(rs.getString("nom"));
        eleve.setPrenom(rs.getString("prenom"));

        Date dateSql = rs.getDate("date_naissance");
        if (dateSql != null) {
            eleve.setDateNaissance(dateSql.toLocalDate());
        }

        eleve.setSexe(rs.getString("sexe"));
        eleve.setAdresse(rs.getString("adresse"));
        eleve.setTelephone(rs.getString("telephone"));
        eleve.setEmail(rs.getString("email"));
        eleve.setClasse(rs.getString("classe"));
        return eleve;
    }
}
