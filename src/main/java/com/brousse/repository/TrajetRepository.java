package com.brousse.repository;

import com.brousse.model.Trajet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TrajetRepository extends JpaRepository<Trajet, Integer>, JpaSpecificationExecutor<Trajet> {

    @Query(value = """
                SELECT
                    t.id_trajet,
                    t.id_gare_depart,
                    t.id_gare_arrivee,
                    t.distance_km,
                    t.duree_estimee_minutes,
                    SUM(b.montant_total) AS chiffreAffaire
                FROM trajet t
                JOIN voyage vo ON t.id_trajet = vo.id_trajet
                JOIN billet b ON vo.id_voyage = b.id_voyage
                GROUP BY
                    t.id_trajet,
                    t.id_gare_depart,
                    t.id_gare_arrivee,
                    t.distance_km,
                    t.duree_estimee_minutes
                ORDER BY t.id_trajet ASC;
        """, nativeQuery = true)
    List<Object[]> findChiffreAffaireParTrajets();
}
