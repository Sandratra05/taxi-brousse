package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "vehicule_maintenance")
public class MaintenanceVehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicule_maintenance", nullable = false)
    private Integer id;

    @Column(name = "date_maintenance", nullable = false)
    private Instant dateMaintenance;

    @Column(name = "date_fin")
    private Instant dateFin;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @Column(name = "cout", precision = 15, scale = 2)
    private BigDecimal cout;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vehicule", nullable = false)
    private Vehicule vehicule;


}