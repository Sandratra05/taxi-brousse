package com.brousse.service;

import com.brousse.model.PaiementDiffusion;
import com.brousse.model.PubliciteDiffusion;
import com.brousse.repository.PaiementDiffusionRepository;
import com.brousse.repository.PubliciteDiffusionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PaiementDiffusionService {

    private final PaiementDiffusionRepository paiementDiffusionRepository;
    private final PubliciteDiffusionRepository publiciteDiffusionRepository;
    private final TarifPubliciteService tarifPubliciteService;

    public PaiementDiffusionService(
            PaiementDiffusionRepository paiementDiffusionRepository,
            PubliciteDiffusionRepository publiciteDiffusionRepository,
            TarifPubliciteService tarifPubliciteService) {
        this.paiementDiffusionRepository = paiementDiffusionRepository;
        this.publiciteDiffusionRepository = publiciteDiffusionRepository;
        this.tarifPubliciteService = tarifPubliciteService;
    }

    /**
     * Crée un paiement pour une publicité diffusion et vérifie si le paiement est complet
     */
    @Transactional
    public PaiementDiffusion create(BigDecimal montant, Integer idPubliciteDiffusion) {
        PubliciteDiffusion diffusion = publiciteDiffusionRepository.findById(idPubliciteDiffusion)
                .orElseThrow(() -> new RuntimeException("PubliciteDiffusion non trouvée avec id: " + idPubliciteDiffusion));

        PaiementDiffusion paiement = new PaiementDiffusion();
        paiement.setMontant(montant);
        paiement.setPubliciteDiffusion(diffusion);

        PaiementDiffusion saved = paiementDiffusionRepository.save(paiement);

        // Vérifier si le paiement total est atteint
        updatePayeTotalement(saved);

        return saved;
    }

    /**
     * Met à jour le flag payeTotalement selon la somme des paiements vs le montant total dû
     * Montant total = nb_diffusion * tarif_publicite.montant
     */
    @Transactional
    public void updatePayeTotalement(PaiementDiffusion paiement) {
        PubliciteDiffusion diffusion = paiement.getPubliciteDiffusion();
        
        // Calcul du montant total dû : nb_diffusion * tarif actuel
        BigDecimal tarifMontant = tarifPubliciteService.getMontantTarifActuel();
        BigDecimal montantTotal = tarifMontant.multiply(BigDecimal.valueOf(diffusion.getNbDiffusion()));

        // Somme des paiements effectués pour cette diffusion
        BigDecimal sommePaiements = paiementDiffusionRepository.sumMontantByPubliciteDiffusionId(diffusion.getId());

        // Si somme >= montant total, on met payeTotalement = true
        boolean estPayeTotalement = sommePaiements.compareTo(montantTotal) >= 0;
        
        // Mettre à jour tous les paiements de cette diffusion
        List<PaiementDiffusion> paiements = paiementDiffusionRepository.findByPubliciteDiffusion_Id(diffusion.getId());
        for (PaiementDiffusion pd : paiements) {
            paiementDiffusionRepository.save(pd);
        }

        // Mettre à jour aussi le flag est_paye de la PubliciteDiffusion
        diffusion.setEstPaye(estPayeTotalement);
        publiciteDiffusionRepository.save(diffusion);
    }

    public Optional<PaiementDiffusion> findById(Integer id) {
        return paiementDiffusionRepository.findById(id);
    }

    public List<PaiementDiffusion> findAll() {
        return paiementDiffusionRepository.findAll();
    }

    public List<PaiementDiffusion> findByPubliciteDiffusionId(Integer idPubliciteDiffusion) {
        return paiementDiffusionRepository.findByPubliciteDiffusion_Id(idPubliciteDiffusion);
    }

    /**
     * Calcule le montant restant à payer pour une PubliciteDiffusion
     */
    public BigDecimal getMontantRestant(Integer idPubliciteDiffusion) {
        PubliciteDiffusion diffusion = publiciteDiffusionRepository.findById(idPubliciteDiffusion)
                .orElseThrow(() -> new RuntimeException("PubliciteDiffusion non trouvée avec id: " + idPubliciteDiffusion));

        BigDecimal tarifMontant = tarifPubliciteService.getMontantTarifActuel();
        BigDecimal montantTotal = tarifMontant.multiply(BigDecimal.valueOf(diffusion.getNbDiffusion()));
        BigDecimal sommePaiements = paiementDiffusionRepository.sumMontantByPubliciteDiffusionId(idPubliciteDiffusion);

        return montantTotal.subtract(sommePaiements).max(BigDecimal.ZERO);
    }

    public void delete(Integer id) {
        paiementDiffusionRepository.deleteById(id);
    }

}
