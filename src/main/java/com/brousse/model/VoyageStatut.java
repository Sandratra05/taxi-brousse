package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "voyage_statut")
public class VoyageStatut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voyage_statut", nullable = false)
    private Integer id;

    @Column(name = "libelle", length = 50)
    private String libelle;


}
