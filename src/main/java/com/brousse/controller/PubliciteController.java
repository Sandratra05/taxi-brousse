package com.brousse.controller;

import com.brousse.dto.PubliciteNonPayeeDTO;
import com.brousse.model.PaiementDiffusion;
import com.brousse.model.PaiementPublicite;
import com.brousse.model.PubliciteDiffusion;
import com.brousse.model.Societe;
import com.brousse.repository.PaiementDiffusionRepository;
import com.brousse.repository.PaiementPubliciteRepository;
import com.brousse.repository.PubliciteDiffusionRepository;
import com.brousse.repository.SocieteRepository;
import com.brousse.service.TarifPubliciteService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/publicites")
public class PubliciteController {

    private final SocieteRepository societeRepository;
    private final PubliciteDiffusionRepository publiciteDiffusionRepository;
    private final PaiementPubliciteRepository paiementPubliciteRepository;
    private final PaiementDiffusionRepository paiementDiffusionRepository;
    private final TarifPubliciteService tarifPubliciteService;

    public PubliciteController(SocieteRepository societeRepository,
                               PubliciteDiffusionRepository publiciteDiffusionRepository,
                               PaiementPubliciteRepository paiementPubliciteRepository,
                               PaiementDiffusionRepository paiementDiffusionRepository,
                               TarifPubliciteService tarifPubliciteService) {
        this.societeRepository = societeRepository;
        this.publiciteDiffusionRepository = publiciteDiffusionRepository;
        this.paiementPubliciteRepository = paiementPubliciteRepository;
        this.paiementDiffusionRepository = paiementDiffusionRepository;
        this.tarifPubliciteService = tarifPubliciteService;
    }

    // Liste des diffusions avec montants payés par société
    @GetMapping
    public String list(
            @RequestParam(required = false) Integer anneeDiffusion,
            @RequestParam(required = false) Integer moisDiffusion,
            Model model) {

        // Récupère toutes les diffusions puis applique un filtre année/mois si fournis
        List<PubliciteDiffusion> diffusions = publiciteDiffusionRepository.findAll();
        if (anneeDiffusion != null) {
            diffusions = diffusions.stream()
                    .filter(d -> d.getDateDiffusion() != null && d.getDateDiffusion().getYear() == anneeDiffusion)
                    .toList();
        }
        if (moisDiffusion != null) {
            diffusions = diffusions.stream()
                    .filter(d -> d.getDateDiffusion() != null && d.getDateDiffusion().getMonthValue() == moisDiffusion)
                    .toList();
        }

        // Liste des sociétés pour l'affichage
        List<Societe> societes = societeRepository.findAll();

        // Récupérer le tarif actuel
        BigDecimal tarifActuel = tarifPubliciteService.getMontantTarifActuel();

        // Construire les maps par société
        Map<Integer, BigDecimal> totalPayeSociete = new HashMap<>();
        Map<Integer, Long> nombreDiffusionsSociete = new HashMap<>();
        Map<Integer, Long> nombreDiffusionsPayeesSociete = new HashMap<>();
        Map<Integer, BigDecimal> totalAPayerSociete = new HashMap<>();
        Map<Integer, BigDecimal> resteAPayerSociete = new HashMap<>();

        // Variable pour calculer le CA total
        long nombreTotalDiffusions = 0;

        for (Societe s : societes) {
            Integer sid = s.getId();

            // Filtrer les diffusions de cette société selon les critères année/mois
            List<PubliciteDiffusion> diffusionsFiltered = diffusions.stream()
                    .filter(d -> d.getPublicite() != null &&
                                 d.getPublicite().getSociete() != null &&
                                 d.getPublicite().getSociete().getId().equals(sid))
                    .toList();

            // Nombre total de diffusions pour cette société (somme des nb_diffusion)
            long nombreDiffusions = diffusionsFiltered.stream()
                    .mapToLong(d -> d.getNbDiffusion() != null ? d.getNbDiffusion() : 0)
                    .sum();
            nombreDiffusionsSociete.put(sid, nombreDiffusions);
            nombreTotalDiffusions += nombreDiffusions;

            // Nombre de diffusions payées (est_paye = true) - somme des nb_diffusion des diffusions payées
            long nombrePayees = diffusionsFiltered.stream()
                    .filter(d -> Boolean.TRUE.equals(d.getEstPaye()))
                    .mapToLong(d -> d.getNbDiffusion() != null ? d.getNbDiffusion() : 0)
                    .sum();
            nombreDiffusionsPayeesSociete.put(sid, nombrePayees);

            // Montant total à payer = somme de (nb_diffusion * tarif) pour chaque diffusion
            BigDecimal totalAPayer = BigDecimal.ZERO;
            for (PubliciteDiffusion d : diffusionsFiltered) {
                int nbDiff = d.getNbDiffusion() != null ? d.getNbDiffusion() : 0;
                totalAPayer = totalAPayer.add(tarifActuel.multiply(BigDecimal.valueOf(nbDiff)));
            }
            totalAPayerSociete.put(sid, totalAPayer);

            // Montant total payé par la société (depuis PaiementPublicite)
            BigDecimal totalPaye = paiementPubliciteRepository.sumMontantBySocieteId(sid);
            if (totalPaye == null) {
                totalPaye = BigDecimal.ZERO;
            }
            totalPayeSociete.put(sid, totalPaye);

            // Reste à payer = total à payer - total payé
            BigDecimal resteAPayer = totalAPayer.subtract(totalPaye);
            if (resteAPayer.compareTo(BigDecimal.ZERO) < 0) {
                resteAPayer = BigDecimal.ZERO;
            }
            resteAPayerSociete.put(sid, resteAPayer);
        }

        // CA = nombre total de diffusions * tarif
        BigDecimal chiffreAffaires = tarifActuel.multiply(BigDecimal.valueOf(nombreTotalDiffusions));

        // Générer la liste des années (de 2020 à l'année actuelle + 1)
        int currentYear = LocalDateTime.now().getYear();
        List<Integer> annees = IntStream.rangeClosed(2020, currentYear + 1)
                .boxed()
                .sorted((a, b) -> b - a)
                .collect(Collectors.toList());

        model.addAttribute("annees", annees);
        model.addAttribute("tarifActuel", tarifActuel);
        model.addAttribute("chiffreAffaires", chiffreAffaires);
        model.addAttribute("totalPayeSociete", totalPayeSociete);
        model.addAttribute("totalAPayerSociete", totalAPayerSociete);
        model.addAttribute("resteAPayerSociete", resteAPayerSociete);
        model.addAttribute("nombreDiffusionsSociete", nombreDiffusionsSociete);
        model.addAttribute("nombreDiffusionsPayeesSociete", nombreDiffusionsPayeesSociete);
        model.addAttribute("societes", societes);
        model.addAttribute("anneeDiffusion", anneeDiffusion);
        model.addAttribute("moisDiffusion", moisDiffusion);

        return "publicites/list";
    }

    // Formulaire de paiement des diffusions
    @GetMapping("/paiement")
    public String paiementForm(Model model) {
        List<Societe> societes = societeRepository.findAll();
        BigDecimal tarifActuel = tarifPubliciteService.getMontantTarifActuel();

        model.addAttribute("societes", societes);
        model.addAttribute("tarifActuel", tarifActuel);

        return "publicites/paiement-form";
    }

    // API pour récupérer les infos de paiement d'une société
    @GetMapping("/api/info-societe/{societeId}")
    @ResponseBody
    public ResponseEntity<Map<String, BigDecimal>> getInfoSociete(@PathVariable Integer societeId) {
        BigDecimal tarifActuel = tarifPubliciteService.getMontantTarifActuel();
        
        // Récupérer toutes les diffusions de la société
        List<PubliciteDiffusion> diffusions = publiciteDiffusionRepository.findByPublicite_Societe_Id(societeId);
        
        // Montant total = somme de (nb_diffusion * tarif) pour chaque diffusion
        BigDecimal montantTotal = BigDecimal.ZERO;
        for (PubliciteDiffusion d : diffusions) {
            BigDecimal montantDiffusion = tarifActuel.multiply(BigDecimal.valueOf(d.getNbDiffusion()));
            montantTotal = montantTotal.add(montantDiffusion);
        }
        
        // Montant total payé par la société (depuis PaiementPublicite)
        BigDecimal montantPaye = paiementPubliciteRepository.sumMontantBySocieteId(societeId);
        if (montantPaye == null) {
            montantPaye = BigDecimal.ZERO;
        }
        
        // Reste à payer
        BigDecimal montantReste = montantTotal.subtract(montantPaye);
        if (montantReste.compareTo(BigDecimal.ZERO) < 0) {
            montantReste = BigDecimal.ZERO;
        }
        
        Map<String, BigDecimal> result = new HashMap<>();
        result.put("montantTotal", montantTotal);
        result.put("montantPaye", montantPaye);
        result.put("montantReste", montantReste);
        
        return ResponseEntity.ok(result);
    }

    // API pour récupérer les diffusions non payées d'une société
    @GetMapping("/api/non-payees/{societeId}")
    @ResponseBody
    public ResponseEntity<List<PubliciteNonPayeeDTO>> getDiffusionsNonPayees(@PathVariable Integer societeId) {
        List<PubliciteDiffusion> diffusions = publiciteDiffusionRepository.findByPublicite_Societe_IdAndEstPayeFalse(societeId);

        List<PubliciteNonPayeeDTO> dtos = diffusions.stream()
                .map(d -> new PubliciteNonPayeeDTO(
                        d.getId(),
                        d.getDateDiffusion(),
                        d.getPublicite().getNom(),
                        d.getVoyage().getVehicule().getImmatriculation() + " - " +
                        d.getVoyage().getTrajet().getGareDepart().getVille().getLibelle() + " → " +
                        d.getVoyage().getTrajet().getGareArrivee().getVille().getLibelle()
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    // Traitement du paiement des diffusions
    @PostMapping("/paiement")
    public String processPaiement(
            @RequestParam Integer societeId,
            @RequestParam BigDecimal montant,
            @RequestParam String datePaiement,
            RedirectAttributes redirectAttributes) {

        try {
            // Récupérer le tarif actuel
            BigDecimal tarifActuel = tarifPubliciteService.getMontantTarifActuel();

            // Recuperer la société
            Societe societe = societeRepository.findById(societeId)
                    .orElseThrow(() -> new RuntimeException("Société non trouvée"));

            // Parser la date
            LocalDateTime dateTime = LocalDateTime.parse(datePaiement);

            // 1. Insérer dans PaiementPublicite
            PaiementPublicite paiement = new PaiementPublicite();
            paiement.setMontant(montant);
            paiement.setDatePaiement(dateTime);
            paiement.setSociete(societe);
            paiementPubliciteRepository.save(paiement);

            // 2. Récupérer toutes les diffusions de la société
            List<PubliciteDiffusion> diffusions = publiciteDiffusionRepository.findByPublicite_Societe_Id(societeId);

            // 3. Calculer le montant total que la société doit payer
            BigDecimal montantTotalDu = BigDecimal.ZERO;
            for (PubliciteDiffusion d : diffusions) {
                BigDecimal montantDiffusion = tarifActuel.multiply(BigDecimal.valueOf(d.getNbDiffusion()));
                montantTotalDu = montantTotalDu.add(montantDiffusion);
            }

            // 4. Calculer le rapport proportionnel : montant payé / montant total
            if (montantTotalDu.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal rapport = montant.divide(montantTotalDu, 10, RoundingMode.HALF_UP);

                // 5. Répartir proportionnellement sur chaque diffusion
                for (PubliciteDiffusion d : diffusions) {
                    // Montant total pour cette diffusion = nb_diffusion * tarif
                    BigDecimal montantDiffusion = tarifActuel.multiply(BigDecimal.valueOf(d.getNbDiffusion()));
                    
                    // Montant à attribuer = rapport * montant de la diffusion
                    BigDecimal montantAttribue = rapport.multiply(montantDiffusion).setScale(2, RoundingMode.HALF_UP);

                    // Insérer dans PaiementDiffusion
                    if (montantAttribue.compareTo(BigDecimal.ZERO) > 0) {
                        PaiementDiffusion paiementDiffusion = new PaiementDiffusion();
                        paiementDiffusion.setMontant(montantAttribue);
                        paiementDiffusion.setPubliciteDiffusion(d);
                        paiementDiffusionRepository.save(paiementDiffusion);
                    }
                }
            }

            redirectAttributes.addFlashAttribute("success",
                    "Paiement de " + montant + " AR effectué avec succès pour " + societe.getNom());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur lors du paiement: " + e.getMessage());
        }

        return "redirect:/publicites/paiement";
    }

}