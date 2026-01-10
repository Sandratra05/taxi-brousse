package com.brousse.dto;

import com.brousse.model.PlaceVehicule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehiculeDTO {
    private Integer id;
    private String immatriculation;
    private String modele;
    private PlaceVehicule placeVehicule;
    private Double chiffreAffaire;
    
}
