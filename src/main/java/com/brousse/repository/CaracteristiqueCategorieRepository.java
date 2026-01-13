package com.brousse.repository;

import com.brousse.model.CaracteristiqueCategorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaracteristiqueCategorieRepository extends JpaRepository<CaracteristiqueCategorie, Integer> {
    List<CaracteristiqueCategorie> findByCategorie_Id(Integer idCategorie);
    List<CaracteristiqueCategorie> findByCaracteristique_Id(Integer idCaracteristique);
}

