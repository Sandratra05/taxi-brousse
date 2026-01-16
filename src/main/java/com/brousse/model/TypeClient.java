package com.brousse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "type_client")
public class TypeClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_client", nullable = false)
    private Integer id;

    @Column(name = "libelle", nullable = false, length = 50)
    private String libelle;


}
