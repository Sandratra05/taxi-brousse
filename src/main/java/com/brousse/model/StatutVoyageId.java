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
public class StatutVoyageId implements Serializable {
    private static final long serialVersionUID = -8636154270048783353L;
    @Column(name = "id_voyage", nullable = false)
    private Integer idVoyage;

    @Column(name = "id_voyage_statut", nullable = false)
    private Integer idVoyageStatut;

    public StatutVoyageId() {}

    public StatutVoyageId(Integer idVoyage, Integer idVoyageStatut) {
        this.idVoyage = idVoyage;
        this.idVoyageStatut = idVoyageStatut;
    }
}
