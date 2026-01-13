package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "statut_billet")
public class StatutBillet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statut_billet", nullable = false)
    private Integer id;

    @Column(name = "date_statut", nullable = false)
    private Instant dateStatut;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_billet_statut", nullable = false)
    private BilletStatut billetStatut;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_billet", nullable = false)
    private Billet billet;

}

