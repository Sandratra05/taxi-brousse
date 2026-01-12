package com.brousse.repository;

import com.brousse.model.StatutVoyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatutVoyageRepository extends JpaRepository<StatutVoyage, Integer> {
    List<StatutVoyage> findByVoyage_IdOrderByDateStatutDesc(Integer idVoyage);
}
