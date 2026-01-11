package com.brousse.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrajetDTO {
    private Integer id_trajet;
    private Integer gareDepart;
    private Integer gareArrivee;
    private Integer dureeEstimeeMinutes;
    private BigDecimal distanceKm;
    private Double chiffreAffaire;

}
