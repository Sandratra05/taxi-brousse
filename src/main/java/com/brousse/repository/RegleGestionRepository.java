package com.brousse.repository;

import com.brousse.model.RegleGestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegleGestionRepository extends JpaRepository<RegleGestion, Integer> {
    Optional<RegleGestion> findByLibelle(String libelle);
}

