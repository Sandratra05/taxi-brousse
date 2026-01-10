package com.brousse.repository;

import com.brousse.model.PlaceVehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceVehiculeRepository extends JpaRepository<PlaceVehicule, Integer> {
}
