package com.brousse.controller;

import com.brousse.model.Chauffeur;
import com.brousse.repository.ChauffeurRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chauffeurs")
public class ChauffeurController {

    private final ChauffeurRepository chauffeurRepository;

    public ChauffeurController(ChauffeurRepository chauffeurRepository) {
        this.chauffeurRepository = chauffeurRepository;
    }

    // Liste des chauffeurs
    @GetMapping
    public String list(Model model) {
        List<Chauffeur> chauffeurs = chauffeurRepository.findAll();
        model.addAttribute("chauffeurs", chauffeurs);
        return "chauffeurs/list";
    }

    // Formulaire de création
    @GetMapping("/create")
    public String createForm() {
        return "chauffeurs/create";
    }

    // Création d'un chauffeur
    @PostMapping("/create")
    public String create(@RequestParam String nom,
                         @RequestParam String prenom,
                         @RequestParam String telephone) {
        Chauffeur chauffeur = new Chauffeur();
        chauffeur.setNom(nom);
        chauffeur.setPrenom(prenom);
        chauffeur.setTelephone(telephone);
        chauffeurRepository.save(chauffeur);
        return "redirect:/chauffeurs";
    }
}
