package com.brousse.repository;

import com.brousse.model.StatutVoyage;
import com.brousse.model.StatutVoyageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatutVoyageRepository extends JpaRepository<StatutVoyage, StatutVoyageId> {
    List<StatutVoyage> findById_IdVoyageOrderByDateStatutDesc(Integer idVoyage);
}
