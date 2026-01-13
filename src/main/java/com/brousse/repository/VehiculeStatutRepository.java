package com.brousse.repository;

import com.brousse.model.VehiculeStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehiculeStatutRepository extends JpaRepository<VehiculeStatut, Integer> {
    Optional<VehiculeStatut> findByLibelle(String libelle);
}
