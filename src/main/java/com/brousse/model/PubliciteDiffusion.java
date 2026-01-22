package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "publicite_diffusion")
public class PubliciteDiffusion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicite_diffusion", nullable = false)
    private Integer id;

    @Column(name = "date_diffusion", nullable = false)
    private LocalDateTime dateDiffusion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_publicite", nullable = false)
    private Publicite publicite;

    @Column(name = "est_paye", nullable = false)
    private Boolean estPaye = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_voyage", nullable = false)
    private Voyage voyage;

    @OneToMany(mappedBy = "publiciteDiffusion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaiementPublicite> paiements = new ArrayList<>();

}

