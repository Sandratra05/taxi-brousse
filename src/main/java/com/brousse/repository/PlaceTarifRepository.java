package com.brousse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brousse.model.PlaceTarif;

public interface PlaceTarifRepository extends JpaRepository<PlaceTarif , Integer> {
    // Récupère les tarifs (VIP/Standard) pour un trajet donné
    java.util.List<PlaceTarif> findByTrajet_Id(Integer idTrajet);
    
    // Trouve le tarif le plus récent pour un trajet, catégorie et type client donnés
    java.util.Optional<PlaceTarif> findFirstByTrajet_IdAndCategorie_IdAndTypeClient_IdOrderByDateTarifDesc(Integer trajetId, Integer categorieId, Integer typeClientId);
}