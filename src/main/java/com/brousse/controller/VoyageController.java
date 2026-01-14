package com.brousse.controller;

import com.brousse.dto.VoyageFilterDTO;
import com.brousse.dto.VoyageDTO;
import com.brousse.model.Voyage;
import com.brousse.model.Vehicule;
import com.brousse.model.VehiculeStatut;
import com.brousse.repository.ChauffeurRepository;
import com.brousse.repository.VoyageStatutRepository;
import com.brousse.repository.TrajetRepository;
import com.brousse.repository.VehiculeRepository;
import com.brousse.repository.VehiculeStatutRepository;
import com.brousse.service.BilletService;
import com.brousse.service.VoyageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/voyages")
public class VoyageController {

    private final VoyageService voyageService;
    private final ChauffeurRepository chauffeurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final TrajetRepository trajetRepository;
    private final VoyageStatutRepository voyageStatutRepository;
    private final VehiculeStatutRepository vehiculeStatutRepository;
    private final BilletService billetService;

    public VoyageController(VoyageService voyageService, ChauffeurRepository chauffeurRepository, VehiculeRepository vehiculeRepository, TrajetRepository trajetRepository, VoyageStatutRepository voyageStatutRepository, VehiculeStatutRepository vehiculeStatutRepository, BilletService billetService) {
        this.voyageService = voyageService;
        this.chauffeurRepository = chauffeurRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.trajetRepository = trajetRepository;
        this.voyageStatutRepository = voyageStatutRepository;
        this.vehiculeStatutRepository = vehiculeStatutRepository;
        this.billetService = billetService;
    }

    // ----- Helpers -----
    private void chargerListesForm(Model model, Voyage voyageEnEdition) {
        model.addAttribute("chauffeurs", chauffeurRepository.findAll());
        model.addAttribute("trajets", trajetRepository.findAll());
        model.addAttribute("statuts", voyageStatutRepository.findAll());

        // Trouver l'ID du statut "Disponible"
        VehiculeStatut statutDisponible = vehiculeStatutRepository.findByLibelle("Disponible")
                .orElseThrow(() -> new IllegalStateException("Statut 'Disponible' introuvable"));
        List<Vehicule> vehicules = vehiculeRepository.findByCurrentStatutId(statutDisponible.getId());

        // inclure le véhicule actuel même s'il n'est pas "disponible"
        // (pour ne pas casser le select en mode edit)
        if (voyageEnEdition != null && voyageEnEdition.getVehicule() != null) {
            Integer currentId = voyageEnEdition.getVehicule().getId();

            boolean dejaDansListe = vehicules.stream()
                    .anyMatch(v -> v.getId().equals(currentId));

            if (!dejaDansListe) {
                Vehicule current = vehiculeRepository.findById(currentId).orElse(null);
                if (current != null) {
                    vehicules.add(0, current);
                }
            }
        }

        model.addAttribute("vehicules", vehicules);
    }

    private LocalDateTime parseDatetimeLocalToLdt(String dateDepart) {
        if (dateDepart == null || dateDepart.isBlank()) return null;
        // format HTML datetime-local : yyyy-MM-ddTHH:mm
        return LocalDateTime.parse(dateDepart);
    }

    // ----- LISTE -----
    @GetMapping
    public String liste(
            @RequestParam(required = false) Integer trajetId,
            @RequestParam(required = false) Integer chauffeurId,
            @RequestParam(required = false) Integer vehiculeId,
            @RequestParam(required = false) Integer statutId,
            @RequestParam(required = false) String dateDebut,
            @RequestParam(required = false) String dateFin,
            Model model
    ) {
        List<Voyage> voyages;

        // Si au moins un filtre est fourni
        if (trajetId != null || chauffeurId != null || vehiculeId != null ||
            (statutId != null && !statutId.toString().isBlank()) ||
            (dateDebut != null && !dateDebut.isBlank()) ||
            (dateFin != null && !dateFin.isBlank())) {

            VoyageFilterDTO filtres = new VoyageFilterDTO();
            filtres.setTrajetId(trajetId);
            filtres.setChauffeurId(chauffeurId);
            filtres.setVehiculeId(vehiculeId);
            filtres.setStatutId(statutId);

            // Parser les dates
            if (dateDebut != null && !dateDebut.isBlank()) {
                try {
                    filtres.setDateDebut(parseDatetimeLocalToLdt(dateDebut));
                } catch (DateTimeParseException e) {
                    // Ignorer si invalide
                }
            }
            if (dateFin != null && !dateFin.isBlank()) {
                try {
                    filtres.setDateFin(parseDatetimeLocalToLdt(dateFin));
                } catch (DateTimeParseException e) {
                    // Ignorer si invalide
                }
            }

            voyages = voyageService.listerVoyagesAvecFiltre(filtres);

            // Conserver les valeurs des filtres
            model.addAttribute("trajetId", trajetId);
            model.addAttribute("chauffeurId", chauffeurId);
            model.addAttribute("vehiculeId", vehiculeId);
            model.addAttribute("statutId", statutId);
            model.addAttribute("dateDebut", dateDebut);
            model.addAttribute("dateFin", dateFin);
        } else {
            voyages = voyageService.listerVoyages();
        }

        model.addAttribute("voyages", voyages);

        // Add status to each voyage
        List<Map<String, Object>> voyagesWithStatus = new ArrayList<>();
        for (Voyage v : voyages) {
            Map<String, Object> item = new HashMap<>();
            item.put("voyage", v);
            item.put("statut", voyageService.getCurrentStatusLibelle(v));
            voyagesWithStatus.add(item);
        }
        model.addAttribute("voyagesWithStatus", voyagesWithStatus);

        // Charger les données pour les filtres
        model.addAttribute("trajets", trajetRepository.findAll());
        model.addAttribute("chauffeurs", chauffeurRepository.findAll());
        model.addAttribute("vehicules", vehiculeRepository.findAll());
        model.addAttribute("statuts", voyageStatutRepository.findAll());

        return "voyage/index";
    }

    // ----- CHIFFRE D'AFFAIRES -----
    @GetMapping("/chiffreAffaire")
    public String chiffreAffaire(Model model) {
        List<VoyageDTO> results = voyageService.getVoyageChiffreAffaire();

        // Préparer une liste d'éléments correspondant exactement aux colonnes du thead
        List<Map<String, Object>> voyagesForView = new ArrayList<>();
        for (VoyageDTO dto : results) {
            Map<String, Object> item = new HashMap<>();

            // Date de départ (LocalDateTime ou null)
            item.put("dateDepart", dto.getDateDepart());

            // Trajet sous forme "Départ → Arrivée"
            String trajetStr = "-";
            if (dto.getTrajet() != null && dto.getTrajet().getGareDepart() != null && dto.getTrajet().getGareArrivee() != null) {
                trajetStr = dto.getTrajet().getGareDepart().getNom() + " → " + dto.getTrajet().getGareArrivee().getNom();
            }
            item.put("trajet", trajetStr);

            // Chauffeur (nom + prénom si disponible)
            String chauffeurStr = "-";
            if (dto.getChauffeur() != null) {
                String nom = dto.getChauffeur().getNom() == null ? "" : dto.getChauffeur().getNom();
                String prenom = dto.getChauffeur().getPrenom() == null ? "" : dto.getChauffeur().getPrenom();
                String full = (nom + " " + prenom).trim();
                if (!full.isEmpty()) chauffeurStr = full;
            }
            item.put("chauffeur", chauffeurStr);

            // Véhicule (immatriculation)
            String vehiculeStr = "-";
            if (dto.getVehicule() != null && dto.getVehicule().getImmatriculation() != null) {
                vehiculeStr = dto.getVehicule().getImmatriculation();
            }
            item.put("vehicule", vehiculeStr);

            // Chiffre d'affaires
            item.put("chiffreAffaire", dto.getChiffreAffaire());

            voyagesForView.add(item);
        }
        model.addAttribute("trajets", trajetRepository.findAll());
        model.addAttribute("vehicules", vehiculeRepository.findAll());

        model.addAttribute("voyages", voyagesForView);
        // Calcul du total du chiffre d'affaires
        double totalCa = 0.0;
        for (Map<String, Object> it : voyagesForView) {
            Object ca = it.get("chiffreAffaire");
            if (ca instanceof Number) {
                totalCa += ((Number) ca).doubleValue();
            }
        }
        model.addAttribute("totalChiffreAffaire", totalCa);
        return "voyage/affaire";
    }

    // ----- CHIFFRE D'AFFAIRES FILTRES -----
    @GetMapping("/chiffreAffaire/filtres")
    public String chiffreAffaireFiltres(
            @RequestParam(required = false) Integer trajetId,
            @RequestParam(required = false) Integer vehiculeId,
            @RequestParam(required = false) String dateDebut,
            @RequestParam(required = false) String dateFin,
            Model model
    ) {
        List<VoyageDTO> results = voyageService.getVoyageChiffreAffaire();

        java.time.LocalDateTime debut = parseDatetimeLocalToLdt(dateDebut);
        java.time.LocalDateTime fin = parseDatetimeLocalToLdt(dateFin);

        List<Map<String, Object>> voyagesForView = new ArrayList<>();
        for (VoyageDTO dto : results) {
            // appliquer les filtres
            if (trajetId != null) {
                if (dto.getTrajet() == null || dto.getTrajet().getId() == null || !dto.getTrajet().getId().equals(trajetId)) continue;
            }
            if (vehiculeId != null) {
                if (dto.getVehicule() == null || dto.getVehicule().getId() == null || !dto.getVehicule().getId().equals(vehiculeId)) continue;
            }
            if (debut != null) {
                if (dto.getDateDepart() == null || dto.getDateDepart().isBefore(debut)) continue;
            }
            if (fin != null) {
                if (dto.getDateDepart() == null || dto.getDateDepart().isAfter(fin)) continue;
            }

            Map<String, Object> item = new HashMap<>();
            item.put("dateDepart", dto.getDateDepart());

            String trajetStr = "-";
            if (dto.getTrajet() != null && dto.getTrajet().getGareDepart() != null && dto.getTrajet().getGareArrivee() != null) {
                trajetStr = dto.getTrajet().getGareDepart().getNom() + " → " + dto.getTrajet().getGareArrivee().getNom();
            }
            item.put("trajet", trajetStr);

            String chauffeurStr = "-";
            if (dto.getChauffeur() != null) {
                String nom = dto.getChauffeur().getNom() == null ? "" : dto.getChauffeur().getNom();
                String prenom = dto.getChauffeur().getPrenom() == null ? "" : dto.getChauffeur().getPrenom();
                String full = (nom + " " + prenom).trim();
                if (!full.isEmpty()) chauffeurStr = full;
            }
            item.put("chauffeur", chauffeurStr);

            String vehiculeStr = "-";
            if (dto.getVehicule() != null && dto.getVehicule().getImmatriculation() != null) {
                vehiculeStr = dto.getVehicule().getImmatriculation();
            }
            item.put("vehicule", vehiculeStr);

            item.put("chiffreAffaire", dto.getChiffreAffaire());

            voyagesForView.add(item);
        }

        // transmettre les listes des donnees pour les filtres
        model.addAttribute("trajets", trajetRepository.findAll());
        model.addAttribute("vehicules", vehiculeRepository.findAll());
        model.addAttribute("trajetId", trajetId);
        model.addAttribute("vehiculeId", vehiculeId);
        model.addAttribute("dateDebut", dateDebut);
        model.addAttribute("dateFin", dateFin);

        model.addAttribute("voyages", voyagesForView);

        // Calcul du total du chiffre d'affaires pour les résultats filtrés
        double totalCaFiltres = 0.0;
        for (Map<String, Object> it : voyagesForView) {
            Object ca = it.get("chiffreAffaire");
            if (ca instanceof Number) {
                totalCaFiltres += ((Number) ca).doubleValue();
            }
        }
        model.addAttribute("totalChiffreAffaire", totalCaFiltres);
        return "voyage/affaire";
    }

    

    // ----- CREATE FORM -----
    @GetMapping("/form")
    public String formCreation(Model model) {
        model.addAttribute("mode", "create");
        model.addAttribute("voyage", new Voyage());
        model.addAttribute("dejaPartie", false);

        chargerListesForm(model, null);
        return "voyage/form";
    }

    // ----- CREATE POST -----
    @PostMapping
    public String create(
            @RequestParam("dateDepart") String dateDepart,
            @RequestParam("idChauffeur") Integer idChauffeur,
            @RequestParam("idVehicule") Integer idVehicule,
            @RequestParam("idTrajet") Integer idTrajet,
            @RequestParam("idVoyageStatut") Integer idVoyageStatut,
            Model model
    ) {
        try {
            LocalDateTime ldt = parseDatetimeLocalToLdt(dateDepart);

            Voyage voyage = voyageService.creerVoyage(ldt, idChauffeur, idVehicule, idTrajet, idVoyageStatut);
//            billetService.genererBillet(voyage.getId(), idVehicule);

            return "redirect:/voyages";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("mode", "create");
            model.addAttribute("voyage", new Voyage());
            model.addAttribute("dejaPartie", false);

            chargerListesForm(model, null);
            return "voyage/form";
        }
    }

    // ----- EDIT FORM -----
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Voyage voyage = voyageService.trouverParId(id);

        String currentStatut = voyageService.getCurrentStatusLibelle(voyage);

        boolean canModifyAll = false;
        boolean canModifyStatus = false;

        if ("Terminée".equals(currentStatut) || "Annulée".equals(currentStatut)) {
            canModifyAll = false;
            canModifyStatus = false;
        } else if ("En cours".equals(currentStatut)) {
            canModifyAll = false;
            canModifyStatus = true;
        } else {
            // Prévue
            canModifyAll = true;
            canModifyStatus = true;
        }

        model.addAttribute("mode", "edit");
        model.addAttribute("voyage", voyage);
        model.addAttribute("statut", currentStatut);
        model.addAttribute("canModifyAll", canModifyAll);
        model.addAttribute("canModifyStatus", canModifyStatus);

        chargerListesForm(model, voyage);

        return "voyage/form";
    }

    // ----- UPDATE POST -----
    @PostMapping("/{id}")
    public String update(
            @PathVariable("id") Integer id,
            @RequestParam(value = "dateDepart", required = false) String dateDepart,
            @RequestParam(value = "idChauffeur", required = false) Integer idChauffeur,
            @RequestParam(value = "idVehicule", required = false) Integer idVehicule,
            @RequestParam(value = "idTrajet", required = false) Integer idTrajet,
            @RequestParam("idVoyageStatut") Integer idVoyageStatut,
            Model model
    ) {
        try {
            LocalDateTime ldt = parseDatetimeLocalToLdt(dateDepart);

            voyageService.modifierVoyage(id, ldt, idChauffeur, idVehicule, idTrajet, idVoyageStatut);

            return "redirect:/voyages";
        } catch (Exception e) {
            Voyage voyage = voyageService.trouverParId(id);

            boolean dejaPartie = false;
            if (voyage.getDateDepart() != null) {
                dejaPartie = voyage.getDateDepart().isBefore(LocalDateTime.now().minusMinutes(2));
            }

            model.addAttribute("error", e.getMessage());
            model.addAttribute("mode", "edit");
            model.addAttribute("voyage", voyage);
            model.addAttribute("dejaPartie", dejaPartie);

            chargerListesForm(model, voyage);

            return "voyage/form";
        }
    }
}
