package service;

import dao.EleveDAO;
import model.Eleve;

import java.util.List;

// Service métier pour la gestion des élèves.
public class EleveService {

    private final EleveDAO eleveDAO;

    public EleveService() {
        this.eleveDAO = new EleveDAO();
    }

    // Récupère la liste complète des élèves.
    public List<Eleve> getAllEleves() {
        return eleveDAO.findAll();
    }

    // Recherche un élève par son ID.
    public Eleve getEleveById(int id) {
        return eleveDAO.findById(id);
    }

    // Recherche des élèves par mot-clé.
    public List<Eleve> searchEleves(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllEleves();
        }
        return eleveDAO.search(keyword.trim());
    }

    // Ajoute un nouvel élève après validation.
    public boolean addEleve(Eleve eleve) {
        validateEleve(eleve);
        return eleveDAO.insert(eleve);
    }

    // Met à jour un élève existant après validation.
    public boolean updateEleve(Eleve eleve) {
        validateEleve(eleve);
        return eleveDAO.update(eleve);
    }

    // Supprime un élève par son ID.
    public boolean deleteEleve(int id) {
        return eleveDAO.delete(id);
    }

    // Valide les champs obligatoires d'un élève.
    private void validateEleve(Eleve eleve) {
        if (eleve.getNom() == null || eleve.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire.");
        }
        if (eleve.getPrenom() == null || eleve.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom est obligatoire.");
        }
        if (eleve.getDateNaissance() == null) {
            throw new IllegalArgumentException("La date de naissance est obligatoire.");
        }
        if (eleve.getSexe() == null || eleve.getSexe().trim().isEmpty()) {
            throw new IllegalArgumentException("Le sexe est obligatoire.");
        }
        if (eleve.getClasse() == null || eleve.getClasse().trim().isEmpty()) {
            throw new IllegalArgumentException("La classe est obligatoire.");
        }
    }
}
