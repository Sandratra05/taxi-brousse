package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "reduction")
public class Reduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reduction")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_type_client", nullable = false)
    private TypeClient typeClient;

    @Column(name = "reduction", nullable = false, precision = 5, scale = 2)
    private BigDecimal reduction;


}