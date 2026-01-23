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

//     List<PaiementPublicite> findByPubliciteDiffusion_Id(Integer diffusionId);

//     List<PaiementPublicite> findByPubliciteDiffusion_Publicite_Societe_Id(Integer societeId);

//     @Query("SELECT COALESCE(SUM(pp.montant), 0) FROM PaiementPublicite pp WHERE pp.publiciteDiffusion.publicite.societe.id = :societeId")
//     BigDecimal sumMontantBySocieteId(@Param("societeId") Integer societeId);

//     @Query("SELECT COALESCE(SUM(pp.montant), 0) FROM PaiementPublicite pp WHERE pp.publiciteDiffusion.id = :diffusionId")
//     BigDecimal sumMontantByDiffusionId(@Param("diffusionId") Integer diffusionId);

//     @Query("SELECT COALESCE(SUM(pp.montant), 0) FROM PaiementPublicite pp " +
//            "WHERE pp.societe.id = :societeId " +
//            "AND EXTRACT(YEAR FROM pp.publiciteDiffusion.dateDiffusion) = :annee " +
//            "AND EXTRACT(MONTH FROM pp.publiciteDiffusion.dateDiffusion) = :mois")
//     BigDecimal sumMontantBySocieteIdAndAnneeMois(@Param("societeId") Integer societeId,
//                                                   @Param("annee") Integer annee,
//                                                   @Param("mois") Integer mois);

    @Query("SELECT COALESCE(SUM(pp.montant), 0) FROM PaiementPublicite pp WHERE pp.societe.id = :societeId")
    BigDecimal sumMontantBySocieteId(@Param("societeId") Integer societeId);

}
