package com.brousse.repository;

import com.brousse.model.MethodePaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MethodePaiementRepository extends JpaRepository<MethodePaiement, Integer> {
    Optional<MethodePaiement> findByLibelle(String libelle);
}
