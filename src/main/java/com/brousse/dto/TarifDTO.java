package com.brousse.dto;

import com.brousse.model.Trajet;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TarifDTO {
    private Trajet trajet;
    private LocalDateTime dateTarif;
    private BigDecimal tarifStandard;
    private BigDecimal tarifVip;
    private BigDecimal tarifPremium;
}
