package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vehicule", nullable = false)
    private Vehicule vehicule;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_societe", nullable = false)
    private Societe societe;

    @Column(name = "est_paye", nullable = false)
    private Boolean estPaye = false;

    @OneToMany(mappedBy = "publicite", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaiementPublicite> paiements = new ArrayList<>();

}
