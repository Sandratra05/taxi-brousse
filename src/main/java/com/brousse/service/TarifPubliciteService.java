package com.brousse.service;

import com.brousse.model.TarifPublicite;
import com.brousse.repository.TarifPubliciteRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TarifPubliciteService {

    private final TarifPubliciteRepository tarifPubliciteRepository;

    public TarifPubliciteService(TarifPubliciteRepository tarifPubliciteRepository) {
        this.tarifPubliciteRepository = tarifPubliciteRepository;
    }

    public TarifPublicite save(TarifPublicite tarif) {
        return tarifPubliciteRepository.save(tarif);
    }

    public TarifPublicite create(BigDecimal montant) {
        TarifPublicite tarif = new TarifPublicite();
        tarif.setMontant(montant);
        tarif.setDateTarif(LocalDateTime.now());
        return tarifPubliciteRepository.save(tarif);
    }

    public Optional<TarifPublicite> findById(Integer id) {
        return tarifPubliciteRepository.findById(id);
    }

    public List<TarifPublicite> findAll() {
        return tarifPubliciteRepository.findAll();
    }

    /**
     * Récupère le tarif actuel (le plus récent)
     */
    public Optional<TarifPublicite> getTarifActuel() {
        return tarifPubliciteRepository.findLatestTarif();
    }

    /**
     * Récupère le montant du tarif actuel, ou 0 si aucun tarif n'existe
     */
    public BigDecimal getMontantTarifActuel() {
        return getTarifActuel()
                .map(TarifPublicite::getMontant)
                .orElse(BigDecimal.ZERO);
    }

    public void delete(Integer id) {
        tarifPubliciteRepository.deleteById(id);
    }

}

