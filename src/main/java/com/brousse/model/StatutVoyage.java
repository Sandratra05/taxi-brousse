package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "statut_voyage")
public class StatutVoyage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statut_voyage", nullable = false)
    private Integer id;

    @Column(name = "date_statut", nullable = false)
    private Instant dateStatut;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_voyage_statut", nullable = false)
    private VoyageStatut voyageStatut;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_voyage", nullable = false)
    private Voyage voyage;

}
