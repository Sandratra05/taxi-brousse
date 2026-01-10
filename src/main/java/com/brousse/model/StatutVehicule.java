package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "statut_vehicule")
public class StatutVehicule {
    @EmbeddedId
    private StatutVehiculeId id;

    @MapsId("idVehicule")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vehicule", nullable = false)
    private Vehicule vehicule;

    @MapsId("idVehiculesStatut")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vehicules_statut", nullable = false)
    private VehiculesStatut vehiculesStatut;

    @Column(name = "date_statut", length = 50)
    private String dateStatut;


}