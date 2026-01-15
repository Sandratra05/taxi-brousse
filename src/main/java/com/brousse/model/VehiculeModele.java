package com.brousse.model;

import java.math.BigDecimal;

import lombok.Setter;
import lombok.Getter;
import jakarta.persistence.*;

@Table(name = "vehicule_modele")
@Entity
@Setter
@Getter
public class VehiculeModele {
    @Column(name = "id_vehicule_modele", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "consommation_l_100km", precision = 15, scale = 2)
    private BigDecimal consommationL100km;

    @Column(name = "modele", nullable = false, length = 50)
    private String modele;

    @Column(name = "marque", nullable = false, length = 50)
    private String marque;

    @Column(name = "place", nullable = false)
    private Integer place;

}
