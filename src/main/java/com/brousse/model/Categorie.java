package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categorie")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categorie", nullable = false)
    private Integer id;

    @Column(name = "code", nullable = false, length = 50, unique = true)
    private String code;

    @Column(name = "nom", nullable = false, length = 50)
    private String nom;

    @Column(name = "ordre", nullable = false)
    private Integer ordre;

    @Column(name = "active", nullable = false)
    private Boolean active;

}

