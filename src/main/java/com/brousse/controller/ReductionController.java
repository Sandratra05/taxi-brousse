package com.brousse.controller;

import com.brousse.model.Reduction;
import com.brousse.model.TypeClient;
import com.brousse.repository.ReductionRepository;
import com.brousse.repository.TypeClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/reductions")
public class ReductionController {

    private final ReductionRepository reductionRepository;
    private final TypeClientRepository typeClientRepository;

    public ReductionController(ReductionRepository reductionRepository, TypeClientRepository typeClientRepository) {
        this.reductionRepository = reductionRepository;
        this.typeClientRepository = typeClientRepository;
    }

    // Liste des réductions
    @GetMapping
    public String list(Model model) {
        List<Reduction> reductions = reductionRepository.findAll();
        model.addAttribute("reductions", reductions);
        return "reductions/list";
    }

    // Formulaire de création
    @GetMapping("/create")
    public String createForm(Model model) {
        List<TypeClient> typeClients = typeClientRepository.findAll();
        model.addAttribute("typeClients", typeClients);
        return "reductions/create";
    }

    // Création d'une réduction
    @PostMapping("/create")
    public String create(@RequestParam Integer typeClientId,
                         @RequestParam BigDecimal reduction,
                         Model model) {
        TypeClient typeClient = typeClientRepository.findById(typeClientId).orElse(null);
        if (typeClient != null && reduction != null) {
            // Check if already exists for this typeClient
            boolean exists = reductionRepository.findByTypeClient_Id(typeClientId).isPresent();
            if (!exists) {
                Reduction newReduction=new Reduction();
                newReduction.setTypeClient(typeClient);
                newReduction.setReduction(reduction);
                reductionRepository.save(newReduction);
            } else {
                model.addAttribute("error", "Une réduction existe déjà pour ce type de client");
                return createForm(model);
            }
        }
        return "redirect:/reductions";
    }

    // Formulaire d'édition
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Reduction reduction = reductionRepository.findById(id).orElse(null);
        if (reduction == null) {
            return "redirect:/reductions";
        }
        List<TypeClient> typeClients = typeClientRepository.findAll();
        model.addAttribute("reduction", reduction);
        model.addAttribute("typeClients", typeClients);
        return "reductions/edit";
    }

    // Édition d'une réduction
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Integer id,
                       @RequestParam BigDecimal reduction,
                       Model model) {
        Reduction existing = reductionRepository.findById(id).orElse(null);
        if (existing != null && reduction != null) {
            existing.setReduction(reduction);
            reductionRepository.save(existing);
        }
        return "redirect:/reductions";
    }
}