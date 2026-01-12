package com.brousse.repository;

import com.brousse.model.Caracteristique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaracteristiqueRepository extends JpaRepository<Caracteristique, Integer> {
}

