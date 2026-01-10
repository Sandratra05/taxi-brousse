package com.brousse.repository;

import com.brousse.model.Vehicule;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Integer> {
//    List<Vehicule> findByStatutIgnoreCase(String statut);

    @Query("SELECT v FROM Vehicule v WHERE EXISTS (" +
           "SELECT sv FROM StatutVehicule sv WHERE sv.id.idVehicule = v.id " +
           "AND sv.id.idVehiculesStatut = :statutId)")
    List<Vehicule> findByCurrentStatutId(@Param("statutId") Integer statutId);

    @Query(value = """
                SELECT
            v.id_vehicule,
            v.immatriculation,
            v.modele,
            SUM(b.montant_total * pv.nb_place) AS chiffreAffaire
        FROM vehicule v
        JOIN voyage vo ON v.id_vehicule = vo.id_vehicule
        JOIN billet b ON vo.id_voyage = b.id_voyage
        JOIN place_vehicule pv ON v.id_place_vehicule = pv.id_place_vehicule
        GROUP BY
            v.id_vehicule,
            v.immatriculation,
            v.modele
        ORDER BY v.id_vehicule ASC;
        """, nativeQuery = true)
    List<Object[]> findChiffreAffaireParVehicule();
}
