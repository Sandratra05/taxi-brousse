package com.brousse.repository;

import com.brousse.model.HistoriqueConsommation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueConsommationRepository extends JpaRepository<HistoriqueConsommation, Integer> {
    List<HistoriqueConsommation> findByVehicule_IdOrderByDateConsoDesc(Integer idVehicule);
}

