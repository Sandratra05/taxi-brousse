package com.brousse.controller;

import com.brousse.model.Billet;
import com.brousse.model.Voyage;
import com.brousse.model.Place;
import com.brousse.model.Commande;
import com.brousse.model.DetailsCommande;
import com.brousse.model.Client;
import com.brousse.repository.ClientRepository;
import com.brousse.repository.VoyageRepository;
import com.brousse.repository.PlaceRepository;
import com.brousse.repository.MethodePaiementRepository;
import com.brousse.repository.CommandeRepository;
import com.brousse.repository.DetailsCommandeRepository;
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

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private DetailsCommandeRepository detailsCommandeRepository;

    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) Integer idVoyage,
            Model model
    ) {
        // Récupérer toutes les commandes
        List<Commande> commandes = commandeRepository.findAll();

        // Filtrer par voyage si spécifié
        if (idVoyage != null) {
            commandes = commandes.stream()
                    .filter(c -> {
                        List<DetailsCommande> details = detailsCommandeRepository.findByCommande_Id(c.getId());
                        return details.stream().anyMatch(d -> d.getBillet().getVoyage().getId().equals(idVoyage));
                    })
                    .collect(java.util.stream.Collectors.toList());
        }

        // Créer une liste d'informations de commandes
        List<Map<String, Object>> commandesInfo = new ArrayList<>();
        for (Commande commande : commandes) {
            List<DetailsCommande> details = detailsCommandeRepository.findByCommande_Id(commande.getId());

            Map<String, Object> info = new HashMap<>();
            info.put("commande", commande);
            info.put("details", details);
            info.put("nombreBillets", details.size());
            info.put("montantTotal", commande.getMontantTotal());

            // Vérifier si tous les billets sont payés
            boolean tousPayes = details.stream().allMatch(d -> "Payé".equals(d.getBillet().getStatut()));
            info.put("tousPayes", tousPayes);

            // Filtrer par statut si spécifié
            if (statut != null && !statut.isBlank()) {
                if ("Payé".equals(statut) && !tousPayes) continue;
                if ("Non Payé".equals(statut) && tousPayes) continue;
            }

            commandesInfo.add(info);
        }

        List<Voyage> voyages = voyageService.listerVoyagesPrevus();

        model.addAttribute("commandesInfo", commandesInfo);
        model.addAttribute("voyages", voyages);
        model.addAttribute("statut", statut);
        model.addAttribute("idVoyage", idVoyage);
        return "billets/list";
    }

    /**
     * Affiche les places d'un voyage avec leur disponibilité
     */
//    @GetMapping("/voyage/{idVoyage}/places")
//    public String voirPlacesVoyage(@PathVariable Integer idVoyage, Model model) {
//        Voyage voyage = voyageRepository.findById(idVoyage)
//                .orElseThrow(() -> new IllegalArgumentException("Voyage introuvable"));
//
//        // Récupérer toutes les places de la configuration du véhicule
//        List<Place> toutesLesPlaces = placeService.getPlacesByVehicule(voyage.getVehicule().getId());
//
//        // Créer une map des places avec leurs infos
//        List<Map<String, Object>> placesInfo = new ArrayList<>();
//
//        for (Place place : toutesLesPlaces) {
//            Map<String, Object> info = new HashMap<>();
//            info.put("place", place);
//
//            boolean disponible = placeService.isPlaceDisponible(place.getId(), idVoyage);
//            info.put("disponible", disponible);
//
//            if (!disponible) {
//                // Récupérer le billet associé
//                Optional<Billet> billet = billetService.getBilletByPlaceAndVoyage(place.getId(), idVoyage);
//                billet.ifPresent(b -> {
//                    info.put("billet", b);
//                });
//            }
//
//            placesInfo.add(info);
//        }
//
//        model.addAttribute("voyage", voyage);
//        model.addAttribute("placesInfo", placesInfo);
//
//        return "billets/places-voyage";
//    }

    /**
     * Formulaire d'achat de billets avec sélection visuelle des places
     */
    @GetMapping("/voyage/{idVoyage}/acheter")
    public String showAcheterForm(
            @PathVariable Integer idVoyage,
            Model model
    ) {
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
        model.addAttribute("clients", clientRepository.findAll());
        
        return "billets/acheter-form";
    }

    /**
     * Traitement de l'achat de billets multiples
     */
    @PostMapping("/voyage/{idVoyage}/acheter")
    public String acheterBillets(
            @PathVariable Integer idVoyage,
            @RequestParam(value = "placesIds", required = false) List<Integer> placesIds,
            @RequestParam Integer clientId,
            Model model
    ) {
        try {
            if (placesIds == null || placesIds.isEmpty()) {
                throw new IllegalArgumentException("Veuillez sélectionner au moins une place");
            }

            // Créer une commande pour tous les billets
            Integer commandeId = billetService.acheterBilletsEnCommande(idVoyage, placesIds, clientId);

            return "redirect:/billets/commande/" + commandeId;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return showAcheterForm(idVoyage, model);
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

    /**
     * Afficher les détails d'une commande
     */
    @GetMapping("/commande/{id}")
    public String detailCommande(@PathVariable Integer id, @RequestParam(required = false) String paiement, Model model) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Commande introuvable"));

        List<DetailsCommande> detailsCommande = detailsCommandeRepository.findByCommande_Id(id);

        // Vérifier si tous les billets sont payés
        boolean tousPayes = detailsCommande.stream().allMatch(d -> "Payé".equals(d.getBillet().getStatut()));

        model.addAttribute("commande", commande);
        model.addAttribute("detailsCommande", detailsCommande);
        model.addAttribute("paiementSuccess", "success".equals(paiement));
        model.addAttribute("tousPayes", tousPayes);

        return "billets/commande-detail";
    }

    /**
     * Formulaire de paiement d'une commande
     */
    @GetMapping("/commande/{id}/payer")
    public String showPayerCommandeForm(@PathVariable Integer id, Model model) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Commande introuvable"));

        List<DetailsCommande> detailsCommande = detailsCommandeRepository.findByCommande_Id(id);

        model.addAttribute("commande", commande);
        model.addAttribute("detailsCommande", detailsCommande);
        model.addAttribute("methodesPaiement", methodePaiementRepository.findAll());

        return "billets/commande-payer";
    }

    /**
     * Payer une commande
     */
    @PostMapping("/commande/{id}/payer")
    public String payerCommande(@PathVariable Integer id, @RequestParam String datePaiement, @RequestParam Integer idMethodePaiement, Model model) {
        try {
            billetService.payerCommande(id, datePaiement, idMethodePaiement);
            return "redirect:/billets/commande/" + id + "?paiement=success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            Commande commande = commandeRepository.findById(id).orElse(null);
            List<DetailsCommande> detailsCommande = detailsCommandeRepository.findByCommande_Id(id);
            model.addAttribute("commande", commande);
            model.addAttribute("detailsCommande", detailsCommande);
            model.addAttribute("methodesPaiement", methodePaiementRepository.findAll());
            return "billets/commande-payer";
        }
    }
}
