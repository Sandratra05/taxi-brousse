package com.brousse.repository;

import com.brousse.model.PaiementDiffusion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaiementDiffusionRepository extends JpaRepository<PaiementDiffusion, Integer> {

    List<PaiementDiffusion> findByPubliciteDiffusion_Id(Integer idPubliciteDiffusion);

    @Query("SELECT COALESCE(SUM(pd.montant), 0) FROM PaiementDiffusion pd WHERE pd.publiciteDiffusion.id = :idPubliciteDiffusion")
    BigDecimal sumMontantByPubliciteDiffusionId(@Param("idPubliciteDiffusion") Integer idPubliciteDiffusion);

}
