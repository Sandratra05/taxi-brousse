package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "vehicule")
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicule", nullable = false)
    private Integer id;

    @Column(name = "immatriculation", nullable = false, length = 50, unique = true)
    private String immatriculation;

    @Column(name = "consommation_l_100km", nullable = false, precision = 15, scale = 2)
    private BigDecimal consommationL100km;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vehicule_modele", nullable = false)
    private VehiculeModele vehiculeModele;

}