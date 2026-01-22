package com.brousse.controller;

import com.brousse.dto.PubliciteNonPayeeDTO;
import com.brousse.model.MethodePaiement;
import com.brousse.model.PaiementPublicite;
import com.brousse.model.PubliciteDiffusion;
import com.brousse.model.Societe;
import com.brousse.repository.MethodePaiementRepository;
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
    private final TarifPubliciteService tarifPubliciteService;
    private final MethodePaiementRepository methodePaiementRepository;

    public PubliciteController(SocieteRepository societeRepository,
                               PubliciteDiffusionRepository publiciteDiffusionRepository,
                               PaiementPubliciteRepository paiementPubliciteRepository,
                               TarifPubliciteService tarifPubliciteService,
                               MethodePaiementRepository methodePaiementRepository) {
        this.societeRepository = societeRepository;
        this.publiciteDiffusionRepository = publiciteDiffusionRepository;
        this.paiementPubliciteRepository = paiementPubliciteRepository;
        this.tarifPubliciteService = tarifPubliciteService;
        this.methodePaiementRepository = methodePaiementRepository;
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

            // Nombre total de diffusions pour cette société
            long nombreDiffusions = diffusionsFiltered.size();
            nombreDiffusionsSociete.put(sid, nombreDiffusions);
            nombreTotalDiffusions += nombreDiffusions;

            // Nombre de diffusions payées (est_paye = true)
            long nombrePayees = diffusionsFiltered.stream()
                    .filter(d -> Boolean.TRUE.equals(d.getEstPaye()))
                    .count();
            nombreDiffusionsPayeesSociete.put(sid, nombrePayees);

            // Montant total à payer = nombre de diffusions * tarif actuel
            BigDecimal totalAPayer = tarifActuel.multiply(BigDecimal.valueOf(nombreDiffusions));
            totalAPayerSociete.put(sid, totalAPayer);

            // Calculer le montant total payé depuis paiement_publicite
            BigDecimal totalPaye = BigDecimal.ZERO;
            for (PubliciteDiffusion d : diffusionsFiltered) {
                BigDecimal montantPaye = paiementPubliciteRepository.sumMontantByDiffusionId(d.getId());
                totalPaye = totalPaye.add(montantPaye != null ? montantPaye : BigDecimal.ZERO);
            }
            totalPayeSociete.put(sid, totalPaye);

            // Reste à payer = total à payer - total payé
            BigDecimal resteAPayer = totalAPayer.subtract(totalPaye);
            resteAPayerSociete.put(sid, resteAPayer.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : resteAPayer);
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
        List<MethodePaiement> methodesPaiement = methodePaiementRepository.findAll();
        BigDecimal tarifActuel = tarifPubliciteService.getMontantTarifActuel();

        model.addAttribute("societes", societes);
        model.addAttribute("methodesPaiement", methodesPaiement);
        model.addAttribute("tarifActuel", tarifActuel);

        return "publicites/paiement-form";
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
            @RequestParam List<Integer> diffusionIds,
            @RequestParam String datePaiement,
            @RequestParam Integer methodePaiementId,
            RedirectAttributes redirectAttributes) {

        try {
            // Récupérer le tarif actuel
            BigDecimal tarifActuel = tarifPubliciteService.getMontantTarifActuel();

            // Récupérer la méthode de paiement
            MethodePaiement methodePaiement = methodePaiementRepository.findById(methodePaiementId)
                    .orElseThrow(() -> new RuntimeException("Méthode de paiement non trouvée"));

            // Parser la date
            LocalDateTime dateTime = LocalDateTime.parse(datePaiement);

            // Traiter chaque diffusion sélectionnée
            for (Integer diffusionId : diffusionIds) {
                PubliciteDiffusion diffusion = publiciteDiffusionRepository.findById(diffusionId)
                        .orElseThrow(() -> new RuntimeException("Diffusion non trouvée: " + diffusionId));

                // Créer le paiement
                PaiementPublicite paiement = new PaiementPublicite();
                paiement.setMontant(tarifActuel);
                paiement.setDatePaiement(dateTime);
                paiement.setPubliciteDiffusion(diffusion);
                paiement.setMethodePaiement(methodePaiement);
                paiementPubliciteRepository.save(paiement);

                // Mettre à jour le statut de la diffusion
                diffusion.setEstPaye(true);
                publiciteDiffusionRepository.save(diffusion);
            }

            redirectAttributes.addFlashAttribute("success",
                    diffusionIds.size() + " diffusion(s) payée(s) avec succès");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur lors du paiement: " + e.getMessage());
        }

        return "redirect:/publicites/paiement";
    }

}