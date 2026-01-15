package com.brousse.controller;

import com.brousse.dto.TarifDTO;
import com.brousse.model.Categorie;
import com.brousse.model.PlaceTarif;
import com.brousse.model.Trajet;
import com.brousse.repository.CategorieRepository;
import com.brousse.repository.PlaceTarifRepository;
import com.brousse.repository.TrajetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tarifs")
public class TarifController {

    private final PlaceTarifRepository placeTarifRepository;
    private final TrajetRepository trajetRepository;
    private final CategorieRepository categorieRepository;

    public TarifController(PlaceTarifRepository placeTarifRepository, TrajetRepository trajetRepository, CategorieRepository categorieRepository) {
        this.placeTarifRepository = placeTarifRepository;
        this.trajetRepository = trajetRepository;
        this.categorieRepository = categorieRepository;
    }

    // Liste des tarifs
    @GetMapping
    public String list(Model model) {
        List<PlaceTarif> placeTarifs = placeTarifRepository.findAll();
        Map<String, TarifDTO> groupedTarifs = new HashMap<>();

        for (PlaceTarif pt : placeTarifs) {
            String key = pt.getTrajet().getId() + "_" + pt.getDateTarif().toLocalDate();
            TarifDTO dto = groupedTarifs.computeIfAbsent(key, k -> {
                TarifDTO d = new TarifDTO();
                d.setTrajet(pt.getTrajet());
                d.setDateTarif(pt.getDateTarif());
                return d;
            });

            if (pt.getCategorie().getId() == 1) {
                dto.setTarifStandard(pt.getTarif());
            } else if (pt.getCategorie().getId() == 2) {
                dto.setTarifVip(pt.getTarif());
            } else if (pt.getCategorie().getId() == 3) {
                dto.setTarifPremium(pt.getTarif());
            }
        }

        List<TarifDTO> tarifs = new ArrayList<>(groupedTarifs.values());
        model.addAttribute("tarifs", tarifs);
        return "tarifs/list";
    }

    // Formulaire de création
    @GetMapping("/create")
    public String createForm(Model model) {
        List<Trajet> trajets = trajetRepository.findAll();
        model.addAttribute("trajets", trajets);
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
        LocalDateTime date = LocalDate.parse(dateTarif).atStartOfDay();

        if (trajet != null && tarifvip != null && tarifstandard != null && tarifpremium != null) {

            // Check if already exists for this trajet and date
            List<PlaceTarif> existing = placeTarifRepository.findByTrajet_Id(trajetId);
            boolean exists = existing.stream().anyMatch(pt -> pt.getDateTarif().toLocalDate().equals(date.toLocalDate()));

            if (!exists) {
                // === Enregistrement pour Standard ===
                PlaceTarif placeTarifStandard = new PlaceTarif();
                placeTarifStandard.setTrajet(trajet);
                placeTarifStandard.setDateTarif(date);

                Categorie categorieStandard = categorieRepository.findById(1).orElse(null);
                placeTarifStandard.setCategorie(categorieStandard);
                placeTarifStandard.setTarif(tarifstandard);
                placeTarifRepository.save(placeTarifStandard);

                // === Enregistrement pour VIP ===
                PlaceTarif placeTarifVIP = new PlaceTarif();
                placeTarifVIP.setTrajet(trajet);
                placeTarifVIP.setDateTarif(date);

                Categorie categorieVip = categorieRepository.findById(2).orElse(null);
                placeTarifVIP.setCategorie(categorieVip);
                placeTarifVIP.setTarif(tarifvip);
                placeTarifRepository.save(placeTarifVIP);

                // === Enregistrement pour Premium ===
                PlaceTarif placeTarifPremium = new PlaceTarif();
                placeTarifPremium.setTrajet(trajet);
                placeTarifPremium.setDateTarif(date);

                Categorie categoriePremium = categorieRepository.findById(3).orElse(null);
                placeTarifPremium.setCategorie(categoriePremium);
                placeTarifPremium.setTarif(tarifpremium);
                placeTarifRepository.save(placeTarifPremium);
            }
        }

        return "redirect:/tarifs";
    }

    // Formulaire d'édition
    @GetMapping("/edit/{trajetId}/{dateStr}")
    public String editForm(@PathVariable Integer trajetId, @PathVariable String dateStr, Model model) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Trajet trajet = trajetRepository.findById(trajetId).orElse(null);

        if (trajet == null) {
            return "redirect:/tarifs";
        }

        List<PlaceTarif> placeTarifs = placeTarifRepository.findByTrajet_Id(trajetId);
        TarifDTO dto = new TarifDTO();
        dto.setTrajet(trajet);
        dto.setDateTarif(date.atStartOfDay());

        for (PlaceTarif pt : placeTarifs) {
            if (pt.getDateTarif().toLocalDate().equals(date)) {
                if (pt.getCategorie().getId() == 1) {
                    dto.setTarifStandard(pt.getTarif());
                } else if (pt.getCategorie().getId() == 2) {
                    dto.setTarifVip(pt.getTarif());
                } else if (pt.getCategorie().getId() == 3) {
                    dto.setTarifPremium(pt.getTarif());
                }
            }
        }

        model.addAttribute("tarif", dto);
        return "tarifs/edit";
    }

    // Édition d'un tarif
    @PostMapping("/edit/{trajetId}/{dateStr}")
    public String edit(@PathVariable Integer trajetId, @PathVariable String dateStr,
                       @RequestParam BigDecimal tarifvip,
                       @RequestParam BigDecimal tarifpremium,
                       @RequestParam BigDecimal tarifstandard) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Trajet trajet = trajetRepository.findById(trajetId).orElse(null);

        if (trajet != null && tarifvip != null && tarifstandard != null && tarifpremium != null) {
            List<PlaceTarif> existing = placeTarifRepository.findByTrajet_Id(trajetId);

            for (PlaceTarif pt : existing) {
                if (pt.getDateTarif().toLocalDate().equals(date)) {
                    if (pt.getCategorie().getId() == 1) {
                        pt.setTarif(tarifstandard);
                    } else if (pt.getCategorie().getId() == 2) {
                        pt.setTarif(tarifvip);
                    } else if (pt.getCategorie().getId() == 3) {
                        pt.setTarif(tarifpremium);
                    }
                    placeTarifRepository.save(pt);
                }
            }
        }

        return "redirect:/tarifs";
    }
}
