package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "publicite")
public class Publicite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicite", nullable = false)
    private Integer id;

    @Column(name = "date_diffusion", nullable = false)
    private LocalDateTime dateDiffusion;

    @Column(name = "cout", nullable = false, precision = 15, scale = 2)
    private BigDecimal cout;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vehicule", nullable = false)
    private Vehicule vehicule;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_societe", nullable = false)
    private Societe societe;

    @Column(name = "est_paye", nullable = false)
    private Boolean estPaye;

}
