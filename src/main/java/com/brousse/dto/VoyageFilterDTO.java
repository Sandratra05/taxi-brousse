package com.brousse.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class VoyageFilterDTO {
    private Integer trajetId;
    private Integer chauffeurId;
    private Integer vehiculeId;
    private Integer statutId;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private BigDecimal valeurMaximale;
}
