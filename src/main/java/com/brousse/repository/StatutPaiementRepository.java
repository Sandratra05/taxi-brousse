package com.brousse.repository;

import com.brousse.model.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutPaiementRepository extends JpaRepository<StatutPaiement, Integer> {
}
