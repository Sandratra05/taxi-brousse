package com.brousse.repository;

import com.brousse.model.Publicite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PubliciteRepository extends JpaRepository<Publicite, Integer>, JpaSpecificationExecutor<Publicite> {

    List<Publicite> findBySociete_Id(Integer societeId);

}
