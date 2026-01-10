package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vehicules_statut")
public class VehiculesStatut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicules_statut", nullable = false)
    private Integer id;

    @Column(name = "libelle", length = 50)
    private String libelle;


}