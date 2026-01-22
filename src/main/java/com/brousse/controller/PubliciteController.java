package com.brousse.controller;

import com.brousse.model.Publicite;
import com.brousse.model.Societe;
import com.brousse.model.TypeClient;
import com.brousse.repository.PubliciteRepository;
import com.brousse.repository.SocieteRepository;
import com.brousse.service.PubliciteService;
import com.brousse.repository.SocieteRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/publicites")
public class PubliciteController {

    private final PubliciteRepository publiciteRepository;
    private final SocieteRepository societeRepository;
    private final PubliciteService publiciteService;

    public PubliciteController(PubliciteRepository publiciteRepository, SocieteRepository societeRepository, PubliciteService publiciteService) {
        this.publiciteRepository = publiciteRepository;
        this.societeRepository = societeRepository;
        this.publiciteService = publiciteService;
    }

    // Liste des réductions
    @GetMapping
    public String list(
            @RequestParam(required = false) Integer anneeDiffusion,
            @RequestParam(required = false) Integer moisDiffusion,
            Model model) {
        // Récupère toutes les publicités puis applique un filtre année/mois si fournis
        List<Publicite> publicites = publiciteRepository.findAll();
        if (anneeDiffusion != null) {
            publicites = publicites.stream()
                    .filter(p -> p.getDateDiffusion() != null && p.getDateDiffusion().getYear() == anneeDiffusion)
                    .toList();
        }
        if (moisDiffusion != null) {
            publicites = publicites.stream()
                    .filter(p -> p.getDateDiffusion() != null && p.getDateDiffusion().getMonthValue() == moisDiffusion)
                    .toList();
        }

        // Construire la map societeId -> montant total
        Map<Integer, BigDecimal> totalCoutSociete = new HashMap<>();
        for (Publicite p : publicites) {
            if (p.getSociete() == null || p.getCout() == null) continue;
            Integer sid = p.getSociete().getId();

            totalCoutSociete.merge(sid, p.getCout(), BigDecimal::add);
        }

        Map<Integer, BigDecimal> totalCoutSocietePaye = new HashMap<>();
        for (Publicite p : publicites) {
            if (p.getSociete() == null || p.getCout() == null || p.getEstPaye() == null || !p.getEstPaye()) continue;
            Integer sid = p.getSociete().getId();

            totalCoutSocietePaye.merge(sid, p.getCout(), BigDecimal::add);
        }

        Map<Integer, BigDecimal> totalCoutSocieteReste = new HashMap<>();
        
        // Liste des sociétés pour l'affichage
        List<Societe> societes = societeRepository.findAll();

        // Calculer le reste à payer par société = total - payé
        for (Societe s : societes) {
            Integer sid = s.getId();
            BigDecimal total = totalCoutSociete.getOrDefault(sid, BigDecimal.ZERO);
            BigDecimal paye = totalCoutSocietePaye.getOrDefault(sid, BigDecimal.ZERO);
            BigDecimal reste = total.subtract(paye);
            totalCoutSocieteReste.put(sid, reste);
        }
        BigDecimal totalCout = publiciteService.totalCout(publicites);

        model.addAttribute("totalCout", totalCout);
        model.addAttribute("totalCoutSociete", totalCoutSociete);
        model.addAttribute("totalCoutSocietePaye", totalCoutSocietePaye);
        model.addAttribute("totalCoutSocieteReste", totalCoutSocieteReste);
        model.addAttribute("societes", societes);
        return "publicites/list";
    }


    // Formulaire de création
    // @GetMapping("/create")
    // public String createForm(Model model) {
    //     return "Publicites/create";
    // }

    // // Création d'une réduction
    // @PostMapping("/create")
    // public String create(@RequestParam Integer typeClientId,
    //                      @RequestParam BigDecimal Publicite,
    //                      Model model) {
    //     TypeClient typeClient = SocieteRepository.findById(typeClientId).orElse(null);
    //     if (typeClient != null && Publicite != null) {
    //         // Check if already exists for this typeClient
    //         boolean exists = PubliciteRepository.findByTypeClient_Id(typeClientId).isPresent();
    //         if (!exists) {
    //             Publicite newPublicite=new Publicite();
    //             newPublicite.setTypeClient(typeClient);
    //             newPublicite.setPublicite(Publicite);
    //             PubliciteRepository.save(newPublicite);
    //         } else {
    //             model.addAttribute("error", "Une réduction existe déjà pour ce type de client");
    //             return createForm(model);
    //         }
    //     }
    //     return "redirect:/Publicites";
    // }

    // // Formulaire d'édition
    // @GetMapping("/edit/{id}")
    // public String editForm(@PathVariable Integer id, Model model) {
    //     Publicite Publicite = PubliciteRepository.findById(id).orElse(null);
    //     if (Publicite == null) {
    //         return "redirect:/Publicites";
    //     }
    //     List<TypeClient> typeClients = SocieteRepository.findAll();
    //     model.addAttribute("Publicite", Publicite);
    //     model.addAttribute("typeClients", typeClients);
    //     return "Publicites/edit";
    // }

    // // Édition d'une réduction
    // @PostMapping("/edit/{id}")
    // public String edit(@PathVariable Integer id,
    //                    @RequestParam BigDecimal Publicite,
    //                    Model model) {
    //     Publicite existing = PubliciteRepository.findById(id).orElse(null);
    //     if (existing != null && Publicite != null) {
    //         existing.setPublicite(Publicite);
    //         PubliciteRepository.save(existing);
    //     }
    //     return "redirect:/Publicites";
    // }
}