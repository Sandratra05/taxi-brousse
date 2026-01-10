package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "statut_voyage")
public class StatutVoyage {
    @EmbeddedId
    private StatutVoyageId id;

    @MapsId("idVoyage")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_voyage", nullable = false)
    private Voyage voyage;

    @MapsId("idVoyageStatut")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_voyage_statut", nullable = false)
    private VoyageStatut voyageStatut;

    @Column(name = "date_statut", length = 50)
    private String dateStatut;

}
