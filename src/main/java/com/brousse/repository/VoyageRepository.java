package com.brousse.repository;

import com.brousse.model.Voyage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VoyageRepository extends JpaRepository<Voyage, Integer>, JpaSpecificationExecutor<Voyage> {

    boolean existsByVehicule_IdAndDateDepartBetween(Integer vehiculeId, LocalDateTime from, LocalDateTime to);

    boolean existsByVehicule_IdAndDateDepartBetweenAndIdNot(
            Integer vehiculeId, LocalDateTime from, LocalDateTime to, Integer id
    );

    // ----- POUR VEHICULE -----
    List<Voyage> findByVehicule_IdAndDateDepartLessThanOrderByDateDepartDesc(
            Integer vehiculeId, LocalDateTime date, Pageable pageable
    );

    List<Voyage> findByVehicule_IdAndDateDepartGreaterThanOrderByDateDepartAsc(
            Integer vehiculeId, LocalDateTime date, Pageable pageable
    );

    // ----- POUR CHAUFFEUR -----
    List<Voyage> findByChauffeur_IdAndDateDepartLessThanOrderByDateDepartDesc(
            Integer chauffeurId, LocalDateTime date, Pageable pageable
    );

    List<Voyage> findByChauffeur_IdAndDateDepartGreaterThanOrderByDateDepartAsc(
            Integer chauffeurId, LocalDateTime date, Pageable pageable
    );

    @Query(value = """
            SELECT
                    vo.*,
                    SUM(b.montant_total * vm.place) AS chiffreAffaire
            FROM vehicule v
            JOIN voyage vo ON v.id_vehicule = vo.id_vehicule
            JOIN vehicule_modele vm ON v.id_vehicule_modele = vm.id_vehicule_modele
            JOIN billet b ON vo.id_voyage = b.id_voyage
            GROUP BY
                    vo.id_voyage,
                    vo.date_depart,
                    vo.id_chauffeur,
                    vo.id_vehicule,
                    vo.id_trajet 
            ORDER BY id_voyage ASC;
        """, nativeQuery = true)
    List<Object[]> findVoyageChiffreAffaire();
}
