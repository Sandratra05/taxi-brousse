package com.brousse.repository;

import com.brousse.model.Billet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BilletRepository extends JpaRepository<Billet, Integer>, JpaSpecificationExecutor<Billet> {

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

    @Query(value = """
         SELECT b.*,
            FROM billet b 
            JOIN details_commande dc ON b.id_billet = dc.id_billet
            JOIN commande c ON dc.id_commande = c.id_commande
            JOIN paiement p ON c.id_commande = p.id_commande
            JOIN statut_paiement sp ON p.id_paiement = sp.id_paiement
            JOIN paiement_statut ps ON sp.id_paiement_statut = ps.id_paiement_statut
    """, nativeQuery = true)
    List<Billet> getBilletPayee();

    @Query(value = """
         SELECT b.id_billet, b.code_billet, p.numero AS numero_place, pt.tarif AS prix_calcule
            FROM billet b
            JOIN place p ON b.id_place = p.id_place
            JOIN voyage v ON b.id_voyage = v.id_voyage
            JOIN place_tarif pt ON p.id_categorie = :id_categorie AND v.id_trajet = :id_trajet AND pt.id_type_client = :id_type_client
            WHERE pt.date_tarif = (
                SELECT MAX(pt2.date_tarif)
                FROM place_tarif pt2
                WHERE pt2.id_categorie = p.id_categorie
                AND pt2.id_trajet = v.id_trajet
                AND pt2.id_type_client = :id_type_client
                AND pt2.date_tarif <= v.date_depart
            );
    """, nativeQuery = true)
    List<Object[]> getPrixBilletPourChaquePlace(@Param("id_categorie") Integer idCategorie,
                                                 @Param("id_trajet") Integer idTrajet,
                                                 @Param("id_type_client") Integer idTypeClient);

}
