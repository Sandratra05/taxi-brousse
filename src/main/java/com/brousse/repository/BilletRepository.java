package com.brousse.repository;

import com.brousse.model.Billet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BilletRepository extends JpaRepository<Billet, Integer> {
    
    /**
     * Vérifie si un billet existe pour une place et un voyage donnés
     *
     * @param idPlace ID de la place
     * @param idVoyage ID du voyage
     * @return true si un billet existe, false sinon
     */
    boolean existsByPlace_IdAndVoyage_Id(Integer idPlace, Integer idVoyage);

    /**
     * Récupère tous les billets d'un voyage
     */
    List<Billet> findByVoyage_Id(Integer idVoyage);

}
