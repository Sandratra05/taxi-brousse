package com.brousse.controller;

import com.brousse.model.MaintenanceVehicule;
import com.brousse.model.StatutVehicule;
import com.brousse.model.Vehicule;
import com.brousse.repository.CategorieRepository;
import com.brousse.repository.VehiculeModeleRepository;
import com.brousse.service.VehiculeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
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
    private final CategorieRepository categorieRepository;
    private final VehiculeModeleRepository vehiculeModeleRepository;

    public VehiculeController(VehiculeService vehiculeService, CategorieRepository categorieRepository, VehiculeModeleRepository vehiculeModeleRepository) {
        this.vehiculeService = vehiculeService;
        this.categorieRepository = categorieRepository;
        this.vehiculeModeleRepository = vehiculeModeleRepository;
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

    @GetMapping("/chiffreAffaire")
    public String chiffreAffaireParVehicule(Model model) {
        model.addAttribute("vehicules", vehiculeService.getChiffreAffaireParVehicule());
        return "vehicules/affaire"; // templates/vehicules/affaire.html
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
        model.addAttribute("categories", categorieRepository.findAll());
        model.addAttribute("vehiculeModeles", vehiculeModeleRepository.findAll());
        return "vehicules/create"; // templates/vehicules/create.html
    }

    // Soumission création (basique via @RequestParam)
    @PostMapping
    public String create(
            @RequestParam String immatriculation,
            @RequestParam BigDecimal consommation,
            @RequestParam Integer categorieId,
            @RequestParam Integer vehiculeModeleId,
            Model model
    ) {
        // Validations basiques
        boolean hasError = false;
        if (immatriculation == null || immatriculation.isBlank()) {
            model.addAttribute("immatriculationError", "Immatriculation obligatoire");
            hasError = true;
        }
        if (consommation == null) {
            model.addAttribute("consommationError", "Consommation obligatoire");
            hasError = true;
        }
        if (categorieId == null) {
            model.addAttribute("categorieIdError", "Catégorie obligatoire");
            hasError = true;
        }
        if (vehiculeModeleId == null) {
            model.addAttribute("vehiculeModeleIdError", "Modèle obligatoire");
            hasError = true;
        }
        if (hasError) {
            model.addAttribute("categories", categorieRepository.findAll());
            model.addAttribute("vehiculeModeles", vehiculeModeleRepository.findAll());
            // Repasser aussi les valeurs saisies pour ne pas vider le formulaire
            model.addAttribute("immatriculation", immatriculation);
            model.addAttribute("consommation", consommation);
            model.addAttribute("categorieId", categorieId);
            model.addAttribute("vehiculeModeleId", vehiculeModeleId);
            return "vehicules/create";
        }
        Vehicule v = vehiculeService.createVehicule(immatriculation, consommation, categorieId, vehiculeModeleId);
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
        model.addAttribute("consommation", vehicule.getConsommationL100km());
        model.addAttribute("categorieId", vehicule.getCategorie().getId());
        model.addAttribute("vehiculeModeleId", vehicule.getVehiculeModele().getId());
        model.addAttribute("categories", categorieRepository.findAll());
        model.addAttribute("vehiculeModeles", vehiculeModeleRepository.findAll());
        return "vehicules/edit"; // templates/vehicules/edit.html
    }

    // Soumission modification (basique via @RequestParam)
    @PostMapping("/{id}")
    public String update(
            @PathVariable Integer id,
            @RequestParam(required = false) String immatriculation,
            @RequestParam(required = false) BigDecimal consommation,
            @RequestParam(required = false) Integer categorieId,
            @RequestParam(required = false) Integer vehiculeModeleId,
            Model model
    ) {
        boolean hasError = false;
        if (immatriculation != null && immatriculation.isBlank()) {
            model.addAttribute("immatriculationError", "Immatriculation obligatoire");
            hasError = true;
        }
        if (hasError) {
            model.addAttribute("vehiculeId", id);
            model.addAttribute("immatriculation", immatriculation);
            model.addAttribute("consommation", consommation);
            model.addAttribute("categorieId", categorieId);
            model.addAttribute("vehiculeModeleId", vehiculeModeleId);
            model.addAttribute("categories", categorieRepository.findAll());
            model.addAttribute("vehiculeModeles", vehiculeModeleRepository.findAll());
            return "vehicules/edit";
        }
        vehiculeService.updateVehicule(id, immatriculation, consommation, categorieId, vehiculeModeleId);
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
        Instant date = null;
        if (dateMaintenance != null && !dateMaintenance.isBlank()) {
            try {
                LocalDate ld = LocalDate.parse(dateMaintenance);
                date = ld.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();
            } catch (DateTimeParseException e) {
                // si invalide, on prend maintenant
                date = Instant.now();
            }
        } else {
            date = Instant.now();
        }
        vehiculeService.mettreEnMaintenance(id,
                date,
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
