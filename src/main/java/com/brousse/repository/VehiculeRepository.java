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
}
