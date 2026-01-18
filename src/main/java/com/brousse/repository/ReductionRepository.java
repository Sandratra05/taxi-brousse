package com.brousse.repository;

import com.brousse.model.Reduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReductionRepository extends JpaRepository<Reduction, Integer> {
    Optional<Reduction> findByTypeClient_Id(Integer idTypeClient);
}