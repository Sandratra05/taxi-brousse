package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "statut_paiement")
public class StatutPaiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statut_paiement", nullable = false)
    private Integer id;

    @Column(name = "date_statut", nullable = false)
    private Instant dateStatut;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paiement_statut", nullable = false)
    private PaiementStatut paiementStatut;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paiement", nullable = false)
    private Paiement paiement;

}