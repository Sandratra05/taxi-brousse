package com.brousse.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TrajetFilterDTO {
    private Integer gareDepart;
    private Integer gareArrivee;
    private BigDecimal distanceMin;
    private BigDecimal distanceMax;
    private Integer dureeMin;
    private Integer dureeMax;
}
