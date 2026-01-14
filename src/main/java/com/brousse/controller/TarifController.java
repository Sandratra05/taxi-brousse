package com.brousse.controller;

import com.brousse.model.Categorie;
import com.brousse.model.Tarif;
import com.brousse.model.Trajet;
import com.brousse.repository.CategorieRepository;
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
    private final CategorieRepository categorieRepository;

    public TarifController(TarifRepository tarifRepository, TrajetRepository trajetRepository, CategorieRepository categorieRepository) {
        this.tarifRepository = tarifRepository;
        this.trajetRepository = trajetRepository;
        this.categorieRepository = categorieRepository;
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
        List<Categorie> categories = categorieRepository.findAll();
        model.addAttribute("trajets", trajets);
        model.addAttribute("categories", categories);
        return "tarifs/create";
    }

    // Création d'un tarif
    @PostMapping("/create")
    public String create(@RequestParam Integer trajetId,
                         @RequestParam Integer categorieId,
                         @RequestParam String dateTarif,
                         @RequestParam BigDecimal tarif) {
        Trajet trajet = trajetRepository.findById(trajetId).orElse(null);
        Categorie categorie = categorieRepository.findById(categorieId).orElse(null);

        if (trajet != null && categorie != null) {
            Tarif newTarif = new Tarif();
            newTarif.setTrajet(trajet);
            newTarif.setCategorie(categorie);
            newTarif.setDateTarif(LocalDate.parse(dateTarif));
            newTarif.setTarif(tarif);
            tarifRepository.save(newTarif);
        }

        return "redirect:/tarifs";
    }
}
