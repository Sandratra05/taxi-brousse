package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin", nullable = false)
    private Integer id;

    @Column(name = "nom", nullable = false, length = 50)
    private String nom;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "mot_de_passe", nullable = false, length = 50)
    private String motDePasse;

    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;


}