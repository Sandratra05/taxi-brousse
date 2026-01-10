package com.brousse.repository;

import com.brousse.model.VehiculesStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehiculesStatutRepository extends JpaRepository<VehiculesStatut, Integer> {
    Optional<VehiculesStatut> findByLibelle(String libelle);
}