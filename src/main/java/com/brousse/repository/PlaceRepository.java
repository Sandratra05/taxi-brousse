package com.brousse.repository;

import com.brousse.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {
    
    /**
     * Récupère toutes les places d'une configuration de véhicule
     */
    List<Place> findByVehicule_Id(Integer idVehicule);
}
