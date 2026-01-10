package com.brousse.repository;

import com.brousse.model.StatutVehicule;
import com.brousse.model.StatutVehiculeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatutVehiculeRepository extends JpaRepository<StatutVehicule, StatutVehiculeId> {
    @Query("SELECT sv FROM StatutVehicule sv WHERE sv.vehicule.id = :idVehicule ORDER BY sv.dateStatut DESC")
    List<StatutVehicule> findByVehiculeIdOrderByDateStatutDesc(@Param("idVehicule") Integer idVehicule);
}