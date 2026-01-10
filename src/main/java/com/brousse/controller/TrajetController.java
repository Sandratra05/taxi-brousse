package com.brousse.controller;

import com.brousse.dto.TrajetFilterDTO;
import com.brousse.model.Gare;
import com.brousse.model.Trajet;
import com.brousse.repository.GareRepository;
import com.brousse.service.TrajetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/trajets")
public class TrajetController {
    
    private final TrajetService trajetService;
    private final GareRepository gareRepository;

    public TrajetController(TrajetService trajetService, GareRepository gareRepository) {
        this.trajetService = trajetService;
        this.gareRepository = gareRepository;
    }

    // Liste des trajets avec filtres optionnels
    @GetMapping
    public String list(
            @RequestParam(required = false) Integer gareDepart,
            @RequestParam(required = false) Integer gareArrivee,
            @RequestParam(required = false) BigDecimal distanceMin,
            @RequestParam(required = false) BigDecimal distanceMax,
            @RequestParam(required = false) Integer dureeMin,
            @RequestParam(required = false) Integer dureeMax,
            Model model
    ) {
        List<Trajet> trajets;
        
        // Si au moins un filtre est fourni, utiliser la recherche filtrée
        if (gareDepart != null || gareArrivee != null || 
            distanceMin != null || distanceMax != null || 
            dureeMin != null || dureeMax != null) {
            
            TrajetFilterDTO filtres = new TrajetFilterDTO();
            filtres.setGareDepart(gareDepart);
            filtres.setGareArrivee(gareArrivee);
            filtres.setDistanceMin(distanceMin);
            filtres.setDistanceMax(distanceMax);
            filtres.setDureeMin(dureeMin);
            filtres.setDureeMax(dureeMax);
            trajets = trajetService.findWithFilter(filtres);
            
            // Conserver les valeurs des filtres pour l'affichage
            model.addAttribute("gareDepart", gareDepart);
            model.addAttribute("gareArrivee", gareArrivee);
            model.addAttribute("distanceMin", distanceMin);
            model.addAttribute("distanceMax", distanceMax);
            model.addAttribute("dureeMin", dureeMin);
            model.addAttribute("dureeMax", dureeMax);
        } else {
            trajets = trajetService.findAll();
        }
        
        model.addAttribute("trajets", trajets);
        model.addAttribute("gares", gareRepository.findAll());
        return "trajets/list";
    }

    // Détail d'un trajet
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Trajet trajet = trajetService.findById(id).orElse(null);
        if (trajet == null) {
            return "redirect:/trajets";
        }
        model.addAttribute("trajet", trajet);
        model.addAttribute("tarif", trajetService.getTarifForTrajet(id));
        return "trajets/detail";
    }

    // Formulaire de création
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("gares", gareRepository.findAll());
        return "trajets/create";
    }

    // Soumission création
    @PostMapping
    public String create(
            @RequestParam Integer gareDepartId,
            @RequestParam Integer gareArriveeId,
            @RequestParam BigDecimal distance,
            @RequestParam Integer duree,
            @RequestParam(required = false) BigDecimal prixBase,
            @RequestParam(required = false) BigDecimal prixBagage,
            Model model
    ) {
        boolean hasError = false;
        
        if (gareDepartId == null) {
            model.addAttribute("gareDepartError", "Gare de départ obligatoire");
            hasError = true;
        }
        if (gareArriveeId == null) {
            model.addAttribute("gareArriveeError", "Gare d'arrivée obligatoire");
            hasError = true;
        }
        if (gareDepartId != null && gareArriveeId != null && gareDepartId.equals(gareArriveeId)) {
            model.addAttribute("gareArriveeError", "La gare d'arrivée doit être différente de la gare de départ");
            hasError = true;
        }
        if (distance == null || distance.compareTo(BigDecimal.ZERO) <= 0) {
            model.addAttribute("distanceError", "Distance obligatoire et doit être positive");
            hasError = true;
        }
        if (duree == null || duree <= 0) {
            model.addAttribute("dureeError", "Durée obligatoire et doit être positive");
            hasError = true;
        }
        if (prixBase != null && prixBase.compareTo(BigDecimal.ZERO) < 0) {
            model.addAttribute("prixBaseError", "Le prix de base ne peut pas être négatif");
            hasError = true;
        }
        if (prixBagage != null && prixBagage.compareTo(BigDecimal.ZERO) < 0) {
            model.addAttribute("prixBagageError", "Le prix bagage ne peut pas être négatif");
            hasError = true;
        }

        if (hasError) {
            model.addAttribute("gares", gareRepository.findAll());
            model.addAttribute("gareDepartId", gareDepartId);
            model.addAttribute("gareArriveeId", gareArriveeId);
            model.addAttribute("distance", distance);
            model.addAttribute("duree", duree);
            model.addAttribute("prixBase", prixBase);
            model.addAttribute("prixBagage", prixBagage);
            return "trajets/create";
        }
        
        Trajet trajet = new Trajet();
        trajet.setGareDepart(gareRepository.findById(gareDepartId).orElse(null));
        trajet.setGareArrivee(gareRepository.findById(gareArriveeId).orElse(null));
        trajet.setDistanceKm(distance);
        trajet.setDureeEstimeeMinutes(duree);
        
        Trajet saved = trajetService.create(trajet);

        // Créer le tarif si les prix sont fournis
        if (prixBase != null) {
            trajetService.createOrUpdateTarif(saved, prixBase, prixBagage);
        }

        return "redirect:/trajets/" + saved.getId();
    }

    // Formulaire de modification
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Trajet trajet = trajetService.findById(id).orElse(null);
        if (trajet == null) {
            return "redirect:/trajets";
        }
        model.addAttribute("trajetId", id);
        model.addAttribute("gareDepartId", trajet.getGareDepart().getId());
        model.addAttribute("gareArriveeId", trajet.getGareArrivee().getId());
        model.addAttribute("distance", trajet.getDistanceKm());
        model.addAttribute("duree", trajet.getDureeEstimeeMinutes());
        model.addAttribute("gares", gareRepository.findAll());

        // Charger le tarif existant
        var tarif = trajetService.getTarifForTrajet(id);
        if (tarif != null) {
            model.addAttribute("prixBase", tarif.getPrixBase());
            model.addAttribute("prixBagage", tarif.getPrixBagage());
        }

        return "trajets/edit";
    }

    // Soumission modification
    @PostMapping("/{id}")
    public String update(
            @PathVariable Integer id,
            @RequestParam Integer gareDepartId,
            @RequestParam Integer gareArriveeId,
            @RequestParam BigDecimal distance,
            @RequestParam Integer duree,
            @RequestParam(required = false) BigDecimal prixBase,
            @RequestParam(required = false) BigDecimal prixBagage,
            Model model
    ) {
        boolean hasError = false;
        
        if (gareDepartId == null) {
            model.addAttribute("gareDepartError", "Gare de départ obligatoire");
            hasError = true;
        }
        if (gareArriveeId == null) {
            model.addAttribute("gareArriveeError", "Gare d'arrivée obligatoire");
            hasError = true;
        }
        if (gareDepartId != null && gareArriveeId != null && gareDepartId.equals(gareArriveeId)) {
            model.addAttribute("gareArriveeError", "La gare d'arrivée doit être différente de la gare de départ");
            hasError = true;
        }
        if (distance == null || distance.compareTo(BigDecimal.ZERO) <= 0) {
            model.addAttribute("distanceError", "Distance obligatoire et doit être positive");
            hasError = true;
        }
        if (duree == null || duree <= 0) {
            model.addAttribute("dureeError", "Durée obligatoire et doit être positive");
            hasError = true;
        }
        if (prixBase != null && prixBase.compareTo(BigDecimal.ZERO) < 0) {
            model.addAttribute("prixBaseError", "Le prix de base ne peut pas être négatif");
            hasError = true;
        }
        if (prixBagage != null && prixBagage.compareTo(BigDecimal.ZERO) < 0) {
            model.addAttribute("prixBagageError", "Le prix bagage ne peut pas être négatif");
            hasError = true;
        }

        if (hasError) {
            model.addAttribute("trajetId", id);
            model.addAttribute("gares", gareRepository.findAll());
            model.addAttribute("gareDepartId", gareDepartId);
            model.addAttribute("gareArriveeId", gareArriveeId);
            model.addAttribute("distance", distance);
            model.addAttribute("duree", duree);
            model.addAttribute("prixBase", prixBase);
            model.addAttribute("prixBagage", prixBagage);
            return "trajets/edit";
        }
        
        Trajet trajetDetails = new Trajet();
        trajetDetails.setGareDepart(gareRepository.findById(gareDepartId).orElse(null));
        trajetDetails.setGareArrivee(gareRepository.findById(gareArriveeId).orElse(null));
        trajetDetails.setDistanceKm(distance);
        trajetDetails.setDureeEstimeeMinutes(duree);
        
        Trajet updated = trajetService.update(id, trajetDetails);

        // Créer ou mettre à jour le tarif si les prix sont fournis
        if (prixBase != null) {
            trajetService.createOrUpdateTarif(updated, prixBase, prixBagage);
        }

        return "redirect:/trajets/" + id;
    }

    // Suppression
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        trajetService.delete(id);
        return "redirect:/trajets";
    }
}
