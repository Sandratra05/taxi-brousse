package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "maintenance_vehicule")
public class MaintenanceVehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_maintenance", nullable = false)
    private Integer id;

    @Column(name = "date_maintenance", nullable = false)
    private LocalDate dateMaintenance;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "cout", precision = 10, scale = 2)
    private BigDecimal cout;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vehicule", nullable = false)
    private Vehicule vehicule;


}