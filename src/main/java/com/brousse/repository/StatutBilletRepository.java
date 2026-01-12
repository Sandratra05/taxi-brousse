package com.brousse.repository;

import com.brousse.model.StatutBillet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatutBilletRepository extends JpaRepository<StatutBillet, Integer> {
    List<StatutBillet> findByBillet_IdOrderByDateStatutDesc(Integer idBillet);
}

