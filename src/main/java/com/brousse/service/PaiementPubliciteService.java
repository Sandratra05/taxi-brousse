package com.brousse.service;

import com.brousse.model.MethodePaiement;
import com.brousse.model.PaiementPublicite;
import com.brousse.model.Publicite;
import com.brousse.repository.MethodePaiementRepository;
import com.brousse.repository.PaiementPubliciteRepository;
import com.brousse.repository.PubliciteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaiementPubliciteService {

    private final PaiementPubliciteRepository paiementPubliciteRepository;
    private final PubliciteRepository publiciteRepository;
    private final MethodePaiementRepository methodePaiementRepository;

    public PaiementPubliciteService(PaiementPubliciteRepository paiementPubliciteRepository,
                                     PubliciteRepository publiciteRepository,
                                     MethodePaiementRepository methodePaiementRepository) {
        this.paiementPubliciteRepository = paiementPubliciteRepository;
        this.publiciteRepository = publiciteRepository;
        this.methodePaiementRepository = methodePaiementRepository;
    }

    public PaiementPublicite save(PaiementPublicite paiement) {
        return paiementPubliciteRepository.save(paiement);
    }

    @Transactional
    public PaiementPublicite create(Integer publiciteId, BigDecimal montant, Integer methodePaiementId) {
        Publicite publicite = publiciteRepository.findById(publiciteId)
                .orElseThrow(() -> new RuntimeException("Publicité non trouvée avec id: " + publiciteId));

        MethodePaiement methodePaiement = methodePaiementRepository.findById(methodePaiementId)
                .orElseThrow(() -> new RuntimeException("Méthode de paiement non trouvée avec id: " + methodePaiementId));

        PaiementPublicite paiement = new PaiementPublicite();
        paiement.setMontant(montant);
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setPublicite(publicite);
        paiement.setMethodePaiement(methodePaiement);

        PaiementPublicite saved = paiementPubliciteRepository.save(paiement);

        // Mettre à jour le statut est_paye de la publicité
        publicite.setEstPaye(true);
        publiciteRepository.save(publicite);

        return saved;
    }

    public Optional<PaiementPublicite> findById(Integer id) {
        return paiementPubliciteRepository.findById(id);
    }

    public List<PaiementPublicite> findAll() {
        return paiementPubliciteRepository.findAll();
    }

    public List<PaiementPublicite> findByPubliciteId(Integer publiciteId) {
        return paiementPubliciteRepository.findByPublicite_Id(publiciteId);
    }

    public List<PaiementPublicite> findBySocieteId(Integer societeId) {
        return paiementPubliciteRepository.findByPublicite_Societe_Id(societeId);
    }

    public BigDecimal getTotalPayeBySocieteId(Integer societeId) {
        return paiementPubliciteRepository.sumMontantBySocieteId(societeId);
    }

    public BigDecimal getTotalPayeByPubliciteId(Integer publiciteId) {
        return paiementPubliciteRepository.sumMontantByPubliciteId(publiciteId);
    }

    public BigDecimal getTotalPayeBySocieteIdAndAnneeMois(Integer societeId, Integer annee, Integer mois) {
        return paiementPubliciteRepository.sumMontantBySocieteIdAndAnneeMois(societeId, annee, mois);
    }

    public void delete(Integer id) {
        paiementPubliciteRepository.deleteById(id);
    }

}

