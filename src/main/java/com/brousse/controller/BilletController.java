package com.brousse.controller;

import com.brousse.model.Billet;
import com.brousse.model.Voyage;
import com.brousse.model.Place;
import com.brousse.model.Vehicule;
import com.brousse.repository.ClientRepository;
import com.brousse.repository.VoyageRepository;
import com.brousse.repository.PlaceRepository;
import com.brousse.repository.MethodePaiementRepository;
import com.brousse.service.BilletService;
import com.brousse.service.PlaceService;
import com.brousse.service.VoyageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/billets")
public class BilletController {
    
    @Autowired
    private BilletService billetService;
    
    @Autowired
    private PlaceService placeService;
    
    @Autowired
    private VoyageRepository voyageRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MethodePaiementRepository methodePaiementRepository;

    @Autowired
    private VoyageService voyageService;

    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) Integer idVoyage,
            Model model
    ) {
        List<Billet> billets = new ArrayList<>();

        List<Voyage> voyages = voyageService.listerVoyagesPrevus();

        for (Voyage v : voyages) {
            List<Billet> billetsDuVoyage = billetService.getBilletsByVoyage(v.getId());
            billets.addAll(billetsDuVoyage);
        }

        // Filter in memory
        if (statut != null && !statut.isBlank()) {
            billets = billets.stream()
                    .filter(b -> statut.equals(b.getStatut()))
                    .collect(java.util.stream.Collectors.toList());
        }

        if (idVoyage != null) {
            billets = billets.stream()
                    .filter(b -> idVoyage.equals(b.getVoyage().getId()))
                    .collect(java.util.stream.Collectors.toList());
        }

        model.addAttribute("billets", billets);
        model.addAttribute("voyages", voyages);
        model.addAttribute("statut", statut);
        model.addAttribute("idVoyage", idVoyage);
        return "billets/list";
    }

    /**
     * Affiche les places d'un voyage avec leur disponibilité
     */
    @GetMapping("/voyage/{idVoyage}/places")
    public String voirPlacesVoyage(@PathVariable Integer idVoyage, Model model) {
        Voyage voyage = voyageRepository.findById(idVoyage)
                .orElseThrow(() -> new IllegalArgumentException("Voyage introuvable"));

        // Récupérer toutes les places de la configuration du véhicule
        List<Place> toutesLesPlaces = placeService.getPlacesByVehicule(voyage.getVehicule().getId());
        
        // Créer une map des places avec leurs infos
        List<Map<String, Object>> placesInfo = new ArrayList<>();
        
        for (Place place : toutesLesPlaces) {
            Map<String, Object> info = new HashMap<>();
            info.put("place", place);
            
            boolean disponible = placeService.isPlaceDisponible(place.getId(), idVoyage);
            info.put("disponible", disponible);
            
            if (!disponible) {
                // Récupérer le billet associé
                Optional<Billet> billet = billetService.getBilletByPlaceAndVoyage(place.getId(), idVoyage);
                billet.ifPresent(b -> {
                    info.put("billet", b);
                });
            }
            
            placesInfo.add(info);
        }
        
        model.addAttribute("voyage", voyage);
        model.addAttribute("placesInfo", placesInfo);
        
        return "billets/places-voyage";
    }

    /**
     * Formulaire d'achat de billet
     */
    @GetMapping("/voyage/{idVoyage}/place/{idPlace}/acheter")
    public String showAcheterForm(
            @PathVariable Integer idVoyage,
            @PathVariable Integer idPlace,
            Model model
    ) {
        // Vérifier la disponibilité
        if (!placeService.isPlaceDisponible(idPlace, idVoyage)) {
            model.addAttribute("error", "Cette place n'est plus disponible");
            return "redirect:/billets/voyage/" + idVoyage + "/places";
        }
        
        Voyage voyage = voyageRepository.findById(idVoyage).orElse(null);
        Place place = placeRepository.findById(idPlace).orElse(null);
        
        if (voyage == null || place == null) {
            return "redirect:/voyages";
        }
        
        model.addAttribute("voyage", voyage);
        model.addAttribute("place", place);
        model.addAttribute("clients", clientRepository.findAll());
        
        return "billets/acheter-form";
    }

    /**
     * Traitement de l'achat de billet
     */
    @PostMapping("/voyage/{idVoyage}/place/{idPlace}/acheter")
    public String acheterBillet(
            @PathVariable Integer idVoyage,
            @PathVariable Integer idPlace,
            @RequestParam Integer clientId,
            Model model
    ) {
        try {
            Billet billet = billetService.acheterBillet(idVoyage, idPlace, clientId);
            return "redirect:/billets/" + billet.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("voyage", voyageRepository.findById(idVoyage).orElse(null));
            model.addAttribute("place", placeRepository.findById(idPlace).orElse(null));
            model.addAttribute("clients", clientRepository.findAll());
            model.addAttribute("clientId", clientId);
            return "billets/acheter-form";
        }
    }

    /**
     * Détail d'un billet
     */
    @GetMapping("/{id}")
    public String detailBillet(@PathVariable Integer id, Model model) {
        Billet billet = billetService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Billet introuvable"));
        
        model.addAttribute("billet", billet);
        return "billets/detail";
    }

    @GetMapping("/formPayer/{id}")
    public String showPayerForm(@PathVariable Integer id, Model model) {
        Billet billet = billetService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Billet introuvable"));
        model.addAttribute("billet", billet);
        model.addAttribute("methodesPaiement", methodePaiementRepository.findAll());
        return "billets/formPayer";
    }

    /**
     * Payer un billet
     */
    @PostMapping("/{id}/payer")
    public String payerBillet(@PathVariable Integer id, @RequestParam String datePaiement, @RequestParam Integer idMethodePaiement, Model model) {
        try {
            billetService.payerBillet(id, datePaiement, idMethodePaiement);
            return "redirect:/billets/" + id;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            Billet billet = billetService.findById(id).orElse(null);
            model.addAttribute("billet", billet);
            return "billets/detail";
        }
    }

    @GetMapping("/form-voyage")
    public String showVoyagesPrevues(Model model) {
        List<Voyage> voyages = voyageService.listerVoyages();
        List<Voyage> voyagesPrevues = voyages.stream()
                .filter(v -> "Prévue".equals(voyageService.getCurrentStatusLibelle(v)))
                .collect(java.util.stream.Collectors.toList());
        model.addAttribute("voyages", voyagesPrevues);
        return "billets/form-voyage";
    }
}
