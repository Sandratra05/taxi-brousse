package com.brousse.controller;

import com.brousse.dto.PubliciteNonPayeeDTO;
import com.brousse.model.MethodePaiement;
import com.brousse.model.PaiementPublicite;
import com.brousse.model.Publicite;
import com.brousse.model.Societe;
import com.brousse.repository.MethodePaiementRepository;
import com.brousse.repository.PubliciteRepository;
import com.brousse.repository.SocieteRepository;
import com.brousse.repository.PaiementPubliciteRepository;
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

    private final PubliciteRepository publiciteRepository;
    private final SocieteRepository societeRepository;
    private final PaiementPubliciteRepository paiementPubliciteRepository;
    private final TarifPubliciteService tarifPubliciteService;
    private final MethodePaiementRepository methodePaiementRepository;

    public PubliciteController(PubliciteRepository publiciteRepository,
                               SocieteRepository societeRepository,
                               PaiementPubliciteRepository paiementPubliciteRepository,
                               TarifPubliciteService tarifPubliciteService,
                               MethodePaiementRepository methodePaiementRepository) {
        this.publiciteRepository = publiciteRepository;
        this.societeRepository = societeRepository;
        this.paiementPubliciteRepository = paiementPubliciteRepository;
        this.tarifPubliciteService = tarifPubliciteService;
        this.methodePaiementRepository = methodePaiementRepository;
    }

    // Liste des publicités avec montants payés par société
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

        // Liste des sociétés pour l'affichage
        List<Societe> societes = societeRepository.findAll();

        // Récupérer le tarif actuel
        BigDecimal tarifActuel = tarifPubliciteService.getMontantTarifActuel();

        // Construire la map societeId -> montant total payé (depuis paiement_publicite)
        Map<Integer, BigDecimal> totalPayeSociete = new HashMap<>();

        // Construire la map societeId -> nombre de publicités
        Map<Integer, Long> nombrePublicitesSociete = new HashMap<>();

        // Construire la map societeId -> nombre de publicités payées (est_paye = true)
        Map<Integer, Long> nombrePublicitesPayeesSociete = new HashMap<>();

        // Construire la map societeId -> montant total à payer (nombre de pubs * tarif)
        Map<Integer, BigDecimal> totalAPayerSociete = new HashMap<>();

        // Construire la map societeId -> reste à payer
        Map<Integer, BigDecimal> resteAPayerSociete = new HashMap<>();

        // Variable pour calculer le CA total
        long nombreTotalPubs = 0;

        for (Societe s : societes) {
            Integer sid = s.getId();

            // Filtrer les publicités de cette société selon les critères année/mois
            List<Publicite> publicitesFiltered = publicites.stream()
                    .filter(p -> p.getSociete() != null && p.getSociete().getId().equals(sid))
                    .toList();

            // Nombre total de publicités pour cette société
            long nombrePubs = publicitesFiltered.size();
            nombrePublicitesSociete.put(sid, nombrePubs);
            nombreTotalPubs += nombrePubs;

            // Nombre de publicités payées (est_paye = true)
            long nombrePayees = publicitesFiltered.stream()
                    .filter(p -> Boolean.TRUE.equals(p.getEstPaye()))
                    .count();
            nombrePublicitesPayeesSociete.put(sid, nombrePayees);

            // Montant total à payer = nombre de pubs * tarif actuel
            BigDecimal totalAPayer = tarifActuel.multiply(BigDecimal.valueOf(nombrePubs));
            totalAPayerSociete.put(sid, totalAPayer);

            // Calculer le montant total payé depuis paiement_publicite
            BigDecimal totalPaye = BigDecimal.ZERO;
            for (Publicite p : publicitesFiltered) {
                BigDecimal montantPaye = paiementPubliciteRepository.sumMontantByPubliciteId(p.getId());
                totalPaye = totalPaye.add(montantPaye != null ? montantPaye : BigDecimal.ZERO);
            }
            totalPayeSociete.put(sid, totalPaye);

            // Reste à payer = total à payer - total payé
            BigDecimal resteAPayer = totalAPayer.subtract(totalPaye);
            resteAPayerSociete.put(sid, resteAPayer.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : resteAPayer);
        }

        // CA = nombre total de publicités * tarif
        BigDecimal chiffreAffaires = tarifActuel.multiply(BigDecimal.valueOf(nombreTotalPubs));

        // Générer la liste des années (de 2020 à l'année actuelle + 1)
        int currentYear = LocalDateTime.now().getYear();
        List<Integer> annees = IntStream.rangeClosed(2020, currentYear + 1)
                .boxed()
                .sorted((a, b) -> b - a) // Tri décroissant
                .collect(Collectors.toList());

        model.addAttribute("annees", annees);
        model.addAttribute("tarifActuel", tarifActuel);
        model.addAttribute("chiffreAffaires", chiffreAffaires);
        model.addAttribute("totalPayeSociete", totalPayeSociete);
        model.addAttribute("totalAPayerSociete", totalAPayerSociete);
        model.addAttribute("resteAPayerSociete", resteAPayerSociete);
        model.addAttribute("nombrePublicitesSociete", nombrePublicitesSociete);
        model.addAttribute("nombrePublicitesPayeesSociete", nombrePublicitesPayeesSociete);
        model.addAttribute("societes", societes);
        model.addAttribute("anneeDiffusion", anneeDiffusion);
        model.addAttribute("moisDiffusion", moisDiffusion);

        return "publicites/list";
    }

    // Formulaire de paiement des publicités
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

    // API pour récupérer les publicités non payées d'une société
    @GetMapping("/api/non-payees/{societeId}")
    @ResponseBody
    public ResponseEntity<List<PubliciteNonPayeeDTO>> getPublicitesNonPayees(@PathVariable Integer societeId) {
        List<Publicite> publicites = publiciteRepository.findBySociete_IdAndEstPayeFalse(societeId);

        List<PubliciteNonPayeeDTO> dtos = publicites.stream()
                .map(p -> new PubliciteNonPayeeDTO(
                        p.getId(),
                        p.getDateDiffusion(),
                        p.getVehicule().getImmatriculation()
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    // Traitement du paiement des publicités
    @PostMapping("/paiement")
    public String processPaiement(
            @RequestParam Integer societeId,
            @RequestParam List<Integer> publiciteIds,
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

            // Traiter chaque publicité sélectionnée
            for (Integer publiciteId : publiciteIds) {
                Publicite publicite = publiciteRepository.findById(publiciteId)
                        .orElseThrow(() -> new RuntimeException("Publicité non trouvée: " + publiciteId));

                // Créer le paiement
                PaiementPublicite paiement = new PaiementPublicite();
                paiement.setMontant(tarifActuel);
                paiement.setDatePaiement(dateTime);
                paiement.setPublicite(publicite);
                paiement.setMethodePaiement(methodePaiement);
                paiementPubliciteRepository.save(paiement);

                // Mettre à jour le statut de la publicité
                publicite.setEstPaye(true);
                publiciteRepository.save(publicite);
            }

            redirectAttributes.addFlashAttribute("success",
                    publiciteIds.size() + " publicité(s) payée(s) avec succès");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur lors du paiement: " + e.getMessage());
        }

        return "redirect:/publicites/paiement";
    }

}