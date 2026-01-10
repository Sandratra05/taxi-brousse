package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "trajet")
public class Trajet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trajet", nullable = false)
    private Integer id;

    @Column(name = "distance_km", precision = 10, scale = 2)
    private BigDecimal distanceKm;

    @Column(name = "duree_estimee_minutes")
    private Integer dureeEstimeeMinutes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_gare_arrivee", nullable = false)
    private Gare gareArrivee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_gare_depart", nullable = false)
    private Gare gareDepart;

    @OneToMany(mappedBy = "trajet", fetch = FetchType.EAGER)
    private List<Tarif> tarifs = new ArrayList<>();
}