package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "bagage")
public class Bagage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bagage", nullable = false)
    private Integer id;

    @Column(name = "nombre_sacs")
    private Integer nombreSacs;

    @Column(name = "poids_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal poidsTotal;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_billet", nullable = false)
    private Billet billet;


}