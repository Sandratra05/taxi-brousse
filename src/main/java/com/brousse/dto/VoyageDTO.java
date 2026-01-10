package com.brousse.dto;

import java.time.LocalDateTime;

import com.brousse.model.Chauffeur;
import com.brousse.model.Trajet;
import com.brousse.model.Vehicule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoyageDTO {

    private Integer id;
    private LocalDateTime dateDepart;
    private Chauffeur chauffeur;
    private Vehicule vehicule;
    private Trajet trajet;
    private double chiffreAffaire;
    
}
