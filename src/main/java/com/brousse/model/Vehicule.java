package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vehicule")
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicule", nullable = false)
    private Integer id;

    @Column(name = "immatriculation", nullable = false, length = 50)
    private String immatriculation;

    @Column(name = "modele", length = 100)
    private String modele;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_place_vehicule", nullable = false)
    private PlaceVehicule placeVehicule;


}