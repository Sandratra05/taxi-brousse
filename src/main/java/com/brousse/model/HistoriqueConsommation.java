package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "historique_consommation")
public class HistoriqueConsommation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historique_consommation", nullable = false)
    private Integer id;

    @Column(name = "date_conso", nullable = false)
    private LocalDate dateConso;

    @Column(name = "consommation_l_100km", nullable = false, precision = 15, scale = 2)
    private BigDecimal consommationL100km;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vehicule", nullable = false)
    private Vehicule vehicule;

}

