package com.brousse.repository;

import com.brousse.model.PubliciteDiffusion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PubliciteDiffusionRepository extends JpaRepository<PubliciteDiffusion, Integer>, JpaSpecificationExecutor<PubliciteDiffusion> {

    List<PubliciteDiffusion> findByPublicite_Id(Integer publiciteId);

    List<PubliciteDiffusion> findByPublicite_Societe_Id(Integer societeId);

    List<PubliciteDiffusion> findByVoyage_Id(Integer voyageId);

    List<PubliciteDiffusion> findByEstPayeFalse();

    List<PubliciteDiffusion> findByPublicite_Societe_IdAndEstPayeFalse(Integer societeId);

    @Query("SELECT pd FROM PubliciteDiffusion pd WHERE pd.publicite.societe.id = :societeId " +
           "AND EXTRACT(YEAR FROM pd.dateDiffusion) = :annee " +
           "AND EXTRACT(MONTH FROM pd.dateDiffusion) = :mois")
    List<PubliciteDiffusion> findBySocieteIdAndAnneeMois(@Param("societeId") Integer societeId,
                                                         @Param("annee") Integer annee,
                                                         @Param("mois") Integer mois);

    @Query("SELECT COUNT(pd) FROM PubliciteDiffusion pd WHERE pd.publicite.societe.id = :societeId")
    Long countBySocieteId(@Param("societeId") Integer societeId);

    @Query("SELECT COUNT(pd) FROM PubliciteDiffusion pd WHERE pd.publicite.societe.id = :societeId AND pd.estPaye = true")
    Long countBySocieteIdAndEstPayeTrue(@Param("societeId") Integer societeId);

}

