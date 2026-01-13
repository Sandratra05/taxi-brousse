package com.brousse.repository;

import com.brousse.model.Vehicule;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Integer>, JpaSpecificationExecutor<Vehicule> {
//    List<Vehicule> findByStatutIgnoreCase(String statut);

    @Query("SELECT v FROM Vehicule v WHERE EXISTS (" +
           "SELECT sv FROM StatutVehicule sv WHERE sv.vehicule = v " +
           "AND sv.vehiculeStatut.id = :statutId)")
    List<Vehicule> findByCurrentStatutId(@Param("statutId") Integer statutId);

    @Query(value = """
        SELECT
            v.id_vehicule,
            v.immatriculation,
            CONCAT(vm.marque, ' ', vm.modele) as modele,
            SUM(b.montant_total) AS chiffreAffaire
        FROM vehicule v
        JOIN vehicule_modele vm ON v.id_vehicule_modele = vm.id_vehicule_modele
        JOIN voyage vo ON v.id_vehicule = vo.id_vehicule
        JOIN billet b ON vo.id_voyage = b.id_voyage
        GROUP BY
            v.id_vehicule,
            v.immatriculation,
            vm.marque,
            vm.modele
        ORDER BY v.id_vehicule ASC;
    """, nativeQuery = true)
    List<Object[]> findChiffreAffaireParVehicule();
}
