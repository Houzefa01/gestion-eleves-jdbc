package controller;

import model.Eleve;
import service.EleveService;

import java.util.List;

// Contrôleur pour la gestion des élèves.
public class EleveController {

    private final EleveService eleveService;

    public EleveController() {
        this.eleveService = new EleveService();
    }

    // Récupère tous les élèves.
    public List<Eleve> getAllEleves() {
        return eleveService.getAllEleves();
    }

    // Recherche un élève par ID.
    public Eleve getEleveById(int id) {
        return eleveService.getEleveById(id);
    }

    // Recherche des élèves par mot-clé.
    public List<Eleve> searchEleves(String keyword) {
        return eleveService.searchEleves(keyword);
    }

    // Ajoute un nouvel élève.
    public boolean addEleve(Eleve eleve) {
        return eleveService.addEleve(eleve);
    }

    // Met à jour un élève existant.
    public boolean updateEleve(Eleve eleve) {
        return eleveService.updateEleve(eleve);
    }

    // Supprime un élève par son ID.
    public boolean deleteEleve(int id) {
        return eleveService.deleteEleve(id);
    }
}
