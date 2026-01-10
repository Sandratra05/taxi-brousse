package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "tarif")
public class Tarif {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarif", nullable = false)
    private Integer id;

    @Column(name = "prix_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixBase;

    @ColumnDefault("0")
    @Column(name = "prix_bagage", precision = 10, scale = 2)
    private BigDecimal prixBagage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_trajet", nullable = false)
    private Trajet trajet;


}