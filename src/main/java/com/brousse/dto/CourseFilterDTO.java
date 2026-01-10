package com.brousse.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CourseFilterDTO {
    private Integer trajetId;
    private Integer chauffeurId;
    private Integer vehiculeId;
    private String statut;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
}
