package com.brousse.controller;

import com.brousse.model.Categorie;
import com.brousse.model.Place;
import com.brousse.model.PlaceTarif;
import com.brousse.model.Tarif;
import com.brousse.model.Trajet;
import com.brousse.repository.CategorieRepository;
import com.brousse.repository.PlaceTarifRepository;
import com.brousse.repository.TarifRepository;
import com.brousse.repository.TrajetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/tarifs")
public class TarifController {

    private final TarifRepository tarifRepository;
    private final TrajetRepository trajetRepository;
    private final PlaceTarifRepository placeTarifRepository;

    public TarifController(TarifRepository tarifRepository, TrajetRepository trajetRepository, PlaceTarifRepository placeTarifRepository) {
        this.tarifRepository = tarifRepository;
        this.trajetRepository = trajetRepository;
        this.placeTarifRepository = placeTarifRepository;
    }

    // Liste des tarifs
    @GetMapping
    public String list(Model model) {
        List<Tarif> tarifs = tarifRepository.findAll();
        model.addAttribute("tarifs", tarifs);
        return "tarifs/list";
    }

    // Formulaire de création
    @GetMapping("/create")
    public String createForm(Model model) {
        List<Trajet> trajets = trajetRepository.findAll();
        // List<Categorie> categories = categorieRepository.findAll();
        model.addAttribute("trajets", trajets);
        // model.addAttribute("categories", categories);
        return "tarifs/create";
    }

    // Création d'un tarif
    @PostMapping("/create")
    public String create(@RequestParam Integer trajetId,
                         @RequestParam String dateTarif,
                         @RequestParam BigDecimal tarifvip,
                         @RequestParam BigDecimal tarifpremium,
                         @RequestParam BigDecimal tarifstandard
    ) {
        Trajet trajet = trajetRepository.findById(trajetId).orElse(null);

        if (trajet != null && tarifvip != null && tarifstandard != null) {

            // === Enregistrement pour Standard ===
            PlaceTarif placeTarifStandard = new PlaceTarif();
            placeTarifStandard.setTrajet(trajet);
            placeTarifStandard.setDateTarif(LocalDate.parse(dateTarif).atStartOfDay());

            Categorie categorieStandard = new Categorie();
            categorieStandard.setId(1); // Supposons que l'ID 1 correspond à la catégorie Standard
            placeTarifStandard.setCategorie(categorieStandard);
            placeTarifStandard.setTarif(tarifstandard);
            placeTarifRepository.save(placeTarifStandard);

            // === Enregistrement pour VIP ===
            PlaceTarif placeTarifVIP = new PlaceTarif();
            placeTarifVIP.setTrajet(trajet);
            placeTarifVIP.setDateTarif(LocalDate.parse(dateTarif).atStartOfDay());

            Categorie categorieVip = new Categorie();
            categorieVip.setId(2); // Supposons que l'ID 2 correspond à la catégorie VIP
            placeTarifVIP.setCategorie(categorieVip);
            placeTarifVIP.setTarif(tarifvip);
            placeTarifRepository.save(placeTarifVIP);

        
            PlaceTarif placeTarifPremium = new PlaceTarif();
            placeTarifPremium.setTrajet(trajet);
            placeTarifPremium.setDateTarif(LocalDate.parse(dateTarif).atStartOfDay());

            Categorie categoriePremium = new Categorie();
            categoriePremium.setId(3); // Supposons que l'ID 2 correspond à la catégorie VIP
            placeTarifPremium.setCategorie(categoriePremium);
            placeTarifPremium.setTarif(tarifpremium);
            placeTarifRepository.save(placeTarifPremium);
        }

        return "redirect:/tarifs";
    }
}
