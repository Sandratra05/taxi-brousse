package com.brousse.controller;

import com.brousse.dto.VoyageFilterDTO;
import com.brousse.model.VehiculesStatut;
import com.brousse.model.Voyage;
import com.brousse.model.Vehicule;
import com.brousse.repository.ChauffeurRepository;
import com.brousse.repository.VoyageStatutRepository;
import com.brousse.repository.TrajetRepository;
import com.brousse.repository.VehiculeRepository;
import com.brousse.repository.VehiculesStatutRepository;
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

    @Autowired
    private VoyageService voyageService;

    @Autowired
    private ChauffeurRepository chauffeurRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private TrajetRepository trajetRepository;

    @Autowired
    private VoyageStatutRepository voyageStatutRepository;

    @Autowired
    private VehiculesStatutRepository vehiculesStatutRepository;

    // ----- Helpers -----
    private void chargerListesForm(Model model, Voyage voyageEnEdition) {
        model.addAttribute("chauffeurs", chauffeurRepository.findAll());
        model.addAttribute("trajets", trajetRepository.findAll());
        model.addAttribute("statuts", voyageStatutRepository.findAll());

        // Trouver l'ID du statut "Disponible"
        VehiculesStatut statutDisponible = vehiculesStatutRepository.findByLibelle("Disponible")
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

            voyageService.creerVoyage(ldt, idChauffeur, idVehicule, idTrajet, idVoyageStatut);

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
