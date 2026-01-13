package com.brousse.repository;

import com.brousse.model.DetailsCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailsCommandeRepository extends JpaRepository<DetailsCommande, Integer> {
    List<DetailsCommande> findByCommande_Id(Integer idCommande);
    List<DetailsCommande> findByBillet_Id(Integer idBillet);
}
