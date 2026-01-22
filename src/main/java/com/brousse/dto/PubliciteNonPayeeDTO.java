package com.brousse.dto;

import java.time.LocalDateTime;

public class PubliciteNonPayeeDTO {
    private Integer id;
    private LocalDateTime dateDiffusion;
    private String publiciteNom;
    private String voyageInfo;

    public PubliciteNonPayeeDTO() {}

    public PubliciteNonPayeeDTO(Integer id, LocalDateTime dateDiffusion, String publiciteNom, String voyageInfo) {
        this.id = id;
        this.dateDiffusion = dateDiffusion;
        this.publiciteNom = publiciteNom;
        this.voyageInfo = voyageInfo;
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

    public String getPubliciteNom() {
        return publiciteNom;
    }

    public void setPubliciteNom(String publiciteNom) {
        this.publiciteNom = publiciteNom;
    }

    public String getVoyageInfo() {
        return voyageInfo;
    }

    public void setVoyageInfo(String voyageInfo) {
        this.voyageInfo = voyageInfo;
    }
}

