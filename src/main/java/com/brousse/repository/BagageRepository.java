package com.brousse.repository;

import com.brousse.model.Bagage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BagageRepository extends JpaRepository<Bagage, Integer> {
}
