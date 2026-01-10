package com.brousse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class StatutPaiementId implements Serializable {
    private static final long serialVersionUID = 5326075694405380766L;
    @Column(name = "id_paiement", nullable = false)
    private Integer idPaiement;

    @Column(name = "id_paiement_statut", nullable = false)
    private Integer idPaiementStatut;

    public StatutPaiementId() {}

    public StatutPaiementId(Integer idPaiement, Integer idPaiementStatut) {
        this.idPaiement = idPaiement;
        this.idPaiementStatut = idPaiementStatut;
    }
}