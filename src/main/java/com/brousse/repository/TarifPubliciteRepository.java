package com.brousse.repository;

import com.brousse.model.TarifPublicite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TarifPubliciteRepository extends JpaRepository<TarifPublicite, Integer> {

    /**
     * Récupère le tarif le plus récent (basé sur date_tarif)
     */
    @Query("SELECT t FROM TarifPublicite t ORDER BY t.dateTarif DESC LIMIT 1")
    Optional<TarifPublicite> findLatestTarif();

    /**
     * Récupère le premier tarif (le plus ancien)
     */
    Optional<TarifPublicite> findFirstByOrderByDateTarifAsc();

}

