package com.brousse.repository;

import com.brousse.model.TypeClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeClientRepository extends JpaRepository<TypeClient, Integer> {
}