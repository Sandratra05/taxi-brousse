package com.brousse.repository;

import com.brousse.model.PaiementPublicite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaiementPubliciteRepository extends JpaRepository<PaiementPublicite, Integer>, JpaSpecificationExecutor<PaiementPublicite> {

    List<PaiementPublicite> findByPublicite_Id(Integer idPublicite);

    List<PaiementPublicite> findByPublicite_Societe_Id(Integer idSociete);

    @Query("SELECT COALESCE(SUM(pp.montant), 0) FROM PaiementPublicite pp WHERE pp.publicite.societe.id = :societeId")
    BigDecimal sumMontantBySocieteId(@Param("societeId") Integer societeId);

    @Query("SELECT COALESCE(SUM(pp.montant), 0) FROM PaiementPublicite pp WHERE pp.publicite.id = :publiciteId")
    BigDecimal sumMontantByPubliciteId(@Param("publiciteId") Integer publiciteId);

    @Query("SELECT COALESCE(SUM(pp.montant), 0) FROM PaiementPublicite pp " +
           "WHERE pp.publicite.societe.id = :societeId " +
           "AND EXTRACT(YEAR FROM pp.publicite.dateDiffusion) = :annee " +
           "AND EXTRACT(MONTH FROM pp.publicite.dateDiffusion) = :mois")
    BigDecimal sumMontantBySocieteIdAndAnneeMois(@Param("societeId") Integer societeId,
                                                  @Param("annee") Integer annee,
                                                  @Param("mois") Integer mois);

}

