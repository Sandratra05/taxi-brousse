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
public class StatutVehiculeId implements Serializable {
    private static final long serialVersionUID = -8636154270048783353L;
    @Column(name = "id_vehicule", nullable = false)
    private Integer idVehicule;

    @Column(name = "id_vehicules_statut", nullable = false)
    private Integer idVehiculesStatut;


}