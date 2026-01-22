package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "publicite")
public class Publicite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicite", nullable = false)
    private Integer id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_societe", nullable = false)
    private Societe societe;

    @OneToMany(mappedBy = "publicite", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PubliciteDiffusion> diffusions = new ArrayList<>();

}
