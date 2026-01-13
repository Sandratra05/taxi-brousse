package com.brousse.repository;

import com.brousse.model.Unite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniteRepository extends JpaRepository<Unite, Integer> {
    Optional<Unite> findByCode(String code);
}

