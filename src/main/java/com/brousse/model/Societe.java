package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "societe")
public class Societe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_societe", nullable = false)
    private Integer id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "adresse", length = 200)
    private String adresse;

    @Column(name = "telephone", length = 50)
    private String telephone;

    @Column(name = "email", length = 100)
    private String email;

}
