package com.brousse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "place_vehicule")
public class PlaceVehicule {
    @Id
    @Column(name = "id_place_vehicule", nullable = false)
    private Integer id;

    @Column(name = "nb_place", nullable = false)
    private Integer nbPlace;


}