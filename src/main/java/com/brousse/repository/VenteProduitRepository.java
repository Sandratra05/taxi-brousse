package com.brousse.repository;

import com.brousse.model.VenteProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenteProduitRepository extends JpaRepository<VenteProduit, Integer>, JpaSpecificationExecutor<VenteProduit> {
    
    List<VenteProduit> findByVoyageId(Integer voyageId);
    
    List<VenteProduit> findByClientId(Integer clientId);
    
    List<VenteProduit> findByProduitId(Integer produitId);
}
