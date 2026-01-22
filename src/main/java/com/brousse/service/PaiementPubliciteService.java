package com.brousse.service;

import com.brousse.model.MethodePaiement;
import com.brousse.model.PaiementPublicite;
import com.brousse.model.PubliciteDiffusion;
import com.brousse.repository.MethodePaiementRepository;
import com.brousse.repository.PaiementPubliciteRepository;
import com.brousse.repository.PubliciteDiffusionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaiementPubliciteService {

    private final PaiementPubliciteRepository paiementPubliciteRepository;
    private final PubliciteDiffusionRepository publiciteDiffusionRepository;
    private final MethodePaiementRepository methodePaiementRepository;

    public PaiementPubliciteService(PaiementPubliciteRepository paiementPubliciteRepository,
                                     PubliciteDiffusionRepository publiciteDiffusionRepository,
                                     MethodePaiementRepository methodePaiementRepository) {
        this.paiementPubliciteRepository = paiementPubliciteRepository;
        this.publiciteDiffusionRepository = publiciteDiffusionRepository;
        this.methodePaiementRepository = methodePaiementRepository;
    }

    public PaiementPublicite save(PaiementPublicite paiement) {
        return paiementPubliciteRepository.save(paiement);
    }

    @Transactional
    public PaiementPublicite create(Integer diffusionId, BigDecimal montant, Integer methodePaiementId) {
        PubliciteDiffusion diffusion = publiciteDiffusionRepository.findById(diffusionId)
                .orElseThrow(() -> new RuntimeException("Diffusion non trouvée avec id: " + diffusionId));

        MethodePaiement methodePaiement = methodePaiementRepository.findById(methodePaiementId)
                .orElseThrow(() -> new RuntimeException("Méthode de paiement non trouvée avec id: " + methodePaiementId));

        PaiementPublicite paiement = new PaiementPublicite();
        paiement.setMontant(montant);
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setPubliciteDiffusion(diffusion);
        paiement.setMethodePaiement(methodePaiement);

        PaiementPublicite saved = paiementPubliciteRepository.save(paiement);

        // Mettre à jour le statut est_paye de la diffusion
        diffusion.setEstPaye(true);
        publiciteDiffusionRepository.save(diffusion);

        return saved;
    }

    public Optional<PaiementPublicite> findById(Integer id) {
        return paiementPubliciteRepository.findById(id);
    }

    public List<PaiementPublicite> findAll() {
        return paiementPubliciteRepository.findAll();
    }

    public List<PaiementPublicite> findByDiffusionId(Integer diffusionId) {
        return paiementPubliciteRepository.findByPubliciteDiffusion_Id(diffusionId);
    }

    public List<PaiementPublicite> findBySocieteId(Integer societeId) {
        return paiementPubliciteRepository.findByPubliciteDiffusion_Publicite_Societe_Id(societeId);
    }

    public BigDecimal getTotalPayeBySocieteId(Integer societeId) {
        return paiementPubliciteRepository.sumMontantBySocieteId(societeId);
    }

    public BigDecimal getTotalPayeByDiffusionId(Integer diffusionId) {
        return paiementPubliciteRepository.sumMontantByDiffusionId(diffusionId);
    }

    public BigDecimal getTotalPayeBySocieteIdAndAnneeMois(Integer societeId, Integer annee, Integer mois) {
        return paiementPubliciteRepository.sumMontantBySocieteIdAndAnneeMois(societeId, annee, mois);
    }

    public void delete(Integer id) {
        paiementPubliciteRepository.deleteById(id);
    }

}

