package com.brousse.dto;

import java.time.LocalDateTime;

public class PubliciteNonPayeeDTO {
    private Integer id;
    private LocalDateTime dateDiffusion;
    private String vehiculeImmatriculation;

    public PubliciteNonPayeeDTO() {}

    public PubliciteNonPayeeDTO(Integer id, LocalDateTime dateDiffusion, String vehiculeImmatriculation) {
        this.id = id;
        this.dateDiffusion = dateDiffusion;
        this.vehiculeImmatriculation = vehiculeImmatriculation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateDiffusion() {
        return dateDiffusion;
    }

    public void setDateDiffusion(LocalDateTime dateDiffusion) {
        this.dateDiffusion = dateDiffusion;
    }

    public String getVehiculeImmatriculation() {
        return vehiculeImmatriculation;
    }

    public void setVehiculeImmatriculation(String vehiculeImmatriculation) {
        this.vehiculeImmatriculation = vehiculeImmatriculation;
    }
}

