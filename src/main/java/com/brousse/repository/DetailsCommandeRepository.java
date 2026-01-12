package com.brousse.repository;

import com.brousse.model.DetailsCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailsCommandeRepository extends JpaRepository<DetailsCommande, Integer> {
}
