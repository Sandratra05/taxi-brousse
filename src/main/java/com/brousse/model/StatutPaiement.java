package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "statut_paiement")
public class StatutPaiement {
    @EmbeddedId
    private StatutPaiementId id;

    @MapsId("idPaiement")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paiement", nullable = false)
    private Paiement paiement;

    @MapsId("idPaiementStatut")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paiement_statut", nullable = false)
    private PaiementStatut paiementStatut;

    @Column(name = "date_statut", nullable = false)
    private LocalDate dateStatut;


}