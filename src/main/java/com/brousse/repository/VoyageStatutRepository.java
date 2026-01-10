package com.brousse.repository;

import com.brousse.model.VoyageStatut;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoyageStatutRepository extends JpaRepository<VoyageStatut, Integer> {
    Optional<VoyageStatut> findByLibelleIgnoreCase(String libelle);
}
