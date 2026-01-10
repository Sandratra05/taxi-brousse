package com.brousse.controller;

import com.brousse.model.MaintenanceVehicule;
import com.brousse.model.StatutVehicule;
import com.brousse.model.Vehicule;
import com.brousse.model.PlaceVehicule;
import com.brousse.service.VehiculeService;
import com.brousse.repository.PlaceVehiculeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/vehicules")
public class VehiculeController {
    private final VehiculeService vehiculeService;
    private final PlaceVehiculeRepository placeVehiculeRepository;

    public VehiculeController(VehiculeService vehiculeService, PlaceVehiculeRepository placeVehiculeRepository) {
        this.vehiculeService = vehiculeService;
        this.placeVehiculeRepository = placeVehiculeRepository;
    }

    // Liste des véhicules
    @GetMapping
    public String list(Model model) {
        List<Vehicule> vehicules = vehiculeService.listVehicules();
        List<Map<String, Object>> vehiculesWithStatus = new ArrayList<>();
        for (Vehicule v : vehicules) {
            Map<String, Object> item = new HashMap<>();
            item.put("vehicule", v);
            item.put("statut", vehiculeService.getCurrentStatusLibelle(v));
            vehiculesWithStatus.add(item);
        }
        model.addAttribute("vehiculesWithStatus", vehiculesWithStatus);
        return "vehicules/list"; // templates/vehicules/list.html
    }

    // Détail d'un véhicule
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Vehicule vehicule = vehiculeService.getVehicule(id)
                .orElse(null);
        if (vehicule == null) {
            return "redirect:/vehicules";
        }
        model.addAttribute("vehicule", vehicule);
        model.addAttribute("statut", vehiculeService.getCurrentStatusLibelle(vehicule));
        return "vehicules/detail"; // templates/vehicules/detail.html
    }

    // Formulaire de création
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        List<PlaceVehicule> configs = placeVehiculeRepository.findAll();
        model.addAttribute("placeVehiculeConfigs", configs);
        return "vehicules/create"; // templates/vehicules/create.html
    }

    // Soumission création (basique via @RequestParam)
    @PostMapping
    public String create(
            @RequestParam String immatriculation,
            @RequestParam String modele,
            @RequestParam Integer placeVehiculeId,
            Model model
    ) {
        // Validations basiques
        boolean hasError = false;
        if (immatriculation == null || immatriculation.isBlank()) {
            model.addAttribute("immatriculationError", "Immatriculation obligatoire");
            hasError = true;
        }
        if (modele == null || modele.isBlank()) {
            model.addAttribute("modeleError", "Modèle obligatoire");
            hasError = true;
        }
        if (placeVehiculeId == null) {
            model.addAttribute("placeVehiculeIdError", "Configuration de places obligatoire");
            hasError = true;
        }
        if (hasError) {
            model.addAttribute("placeVehiculeConfigs", placeVehiculeRepository.findAll());
            // Repasser aussi les valeurs saisies pour ne pas vider le formulaire
            model.addAttribute("immatriculation", immatriculation);
            model.addAttribute("modele", modele);
            model.addAttribute("placeVehiculeId", placeVehiculeId);
            return "vehicules/create";
        }
        Vehicule v = vehiculeService.createVehicule(immatriculation, modele, placeVehiculeId);
        return "redirect:/vehicules/" + v.getId();
    }

    // Formulaire de modification
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Vehicule vehicule = vehiculeService.getVehicule(id).orElse(null);
        if (vehicule == null) {
            return "redirect:/vehicules";
        }
        model.addAttribute("vehiculeId", id);
        model.addAttribute("immatriculation", vehicule.getImmatriculation());
        model.addAttribute("modele", vehicule.getModele());
        model.addAttribute("placeVehiculeId", vehicule.getPlaceVehicule().getId());
        model.addAttribute("placeVehiculeConfigs", placeVehiculeRepository.findAll());
        return "vehicules/edit"; // templates/vehicules/edit.html
    }

    // Soumission modification (basique via @RequestParam)
    @PostMapping("/{id}")
    public String update(
            @PathVariable Integer id,
            @RequestParam(required = false) String immatriculation,
            @RequestParam(required = false) String modele,
            @RequestParam(required = false) Integer placeVehiculeId,
            Model model
    ) {
        boolean hasError = false;
        if (immatriculation != null && immatriculation.isBlank()) {
            model.addAttribute("immatriculationError", "Immatriculation obligatoire");
            hasError = true;
        }
        if (modele != null && modele.isBlank()) {
            model.addAttribute("modeleError", "Modèle obligatoire");
            hasError = true;
        }
        if (hasError) {
            model.addAttribute("vehiculeId", id);
            model.addAttribute("immatriculation", immatriculation);
            model.addAttribute("modele", modele);
            model.addAttribute("placeVehiculeId", placeVehiculeId);
            model.addAttribute("placeVehiculeConfigs", placeVehiculeRepository.findAll());
            return "vehicules/edit";
        }
        vehiculeService.updateVehicule(id, immatriculation, modele, placeVehiculeId);
        return "redirect:/vehicules/" + id;
    }

    // Suppression
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        vehiculeService.deleteVehicule(id);
        return "redirect:/vehicules";
    }

    // Actions de statut métier
    @PostMapping("/{id}/maintenance")
    public String maintenance(
            @PathVariable Integer id,
            @RequestParam(required = false) String dateMaintenance,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) BigDecimal cout
    ) {
        LocalDate date = null;
        if (dateMaintenance != null && !dateMaintenance.isBlank()) {
            try {
                date = LocalDate.parse(dateMaintenance);
            } catch (DateTimeParseException e) {
                // si invalide, on prend aujourd'hui
                date = LocalDate.now();
            }
        }
        vehiculeService.mettreEnMaintenance(id,
                date != null ? date : LocalDate.now(),
                description,
                cout);
        return "redirect:/vehicules/" + id;
    }

    @PostMapping("/{id}/activer")
    public String activer(@PathVariable Integer id) {
        vehiculeService.activer(id);
        return "redirect:/vehicules/" + id;
    }

    @PostMapping("/{id}/hors-service")
    public String horsService(@PathVariable Integer id) {
        vehiculeService.mettreHorsService(id);
        return "redirect:/vehicules/" + id;
    }

    // Historique des statuts
    @GetMapping("/{id}/historique-statuts")
    public String historiqueStatuts(@PathVariable Integer id, Model model) {
        List<StatutVehicule> statuts = vehiculeService.historiqueStatuts(id);
        model.addAttribute("statuts", statuts);
        model.addAttribute("vehiculeId", id);
        return "vehicules/historique-statuts"; // templates/vehicules/historique-statuts.html
    }

    // Historique des maintenances
    @GetMapping("/{id}/historique-maintenance")
    public String historiqueMaintenance(@PathVariable Integer id, Model model) {
        List<MaintenanceVehicule> maintenances = vehiculeService.historiqueMaintenances(id);
        model.addAttribute("maintenances", maintenances);
        model.addAttribute("vehiculeId", id);
        return "vehicules/historique-maintenance"; // templates/vehicules/historique-maintenance.html
    }
}
