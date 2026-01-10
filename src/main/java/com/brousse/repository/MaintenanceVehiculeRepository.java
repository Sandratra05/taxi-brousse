package com.brousse.repository;

import com.brousse.model.MaintenanceVehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceVehiculeRepository extends JpaRepository<MaintenanceVehicule, Integer> {
    @Query("SELECT m FROM MaintenanceVehicule m WHERE m.vehicule.id = :idVehicule ORDER BY m.dateMaintenance DESC")
    List<MaintenanceVehicule> findByVehiculeIdOrderByDateMaintenanceDesc(@Param("idVehicule") Integer idVehicule);
}