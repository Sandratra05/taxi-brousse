CREATE TABLE admin(
                      id_admin SERIAL,
                      nom VARCHAR(100)  NOT NULL,
                      email VARCHAR(150)  NOT NULL,
                      mot_de_passe VARCHAR(255)  NOT NULL,
                      date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      PRIMARY KEY(id_admin),
                      UNIQUE(email)
);

CREATE TABLE gare(
                     id_gare SERIAL,
                     nom VARCHAR(100)  NOT NULL,
                     adresse TEXT,
                     ville VARCHAR(100) ,
                     PRIMARY KEY(id_gare)
);

CREATE TABLE trajet(
                       id_trajet SERIAL,
                       distance_km NUMERIC(6,2)  ,
                       duree_estimee_minutes INTEGER,
                       id_gare_arrivee INTEGER NOT NULL,
                       id_gare_depart INTEGER NOT NULL,
                       PRIMARY KEY(id_trajet),
                       FOREIGN KEY(id_gare_arrivee) REFERENCES gare(id_gare),
                       FOREIGN KEY(id_gare_depart) REFERENCES gare(id_gare)
);

CREATE TABLE chauffeur(
                          id_chauffeur SERIAL,
                          nom VARCHAR(100)  NOT NULL,
                          prenom VARCHAR(100)  NOT NULL,
                          telephone VARCHAR(50) ,
                          PRIMARY KEY(id_chauffeur)
);

CREATE TABLE tarif(
                      id_tarif SERIAL,
                      prix_base NUMERIC(10,2)   NOT NULL,
                      prix_bagage NUMERIC(10,2)   DEFAULT 0,
                      id_trajet INTEGER NOT NULL,
                      PRIMARY KEY(id_tarif),
                      FOREIGN KEY(id_trajet) REFERENCES trajet(id_trajet)
);

CREATE TABLE vehicules_statut(
                                 id_vehicules_statut SERIAL,
                                 libelle VARCHAR(50) ,
                                 PRIMARY KEY(id_vehicules_statut)
);

CREATE TABLE client(
                       id_client SERIAL,
                       nom VARCHAR(50) ,
                       prenom VARCHAR(50) ,
                       telephone VARCHAR(50) ,
                       email VARCHAR(50) ,
                       PRIMARY KEY(id_client)
);

CREATE TABLE voyage_statut(
                              id_voyage_statut SERIAL,
                              libelle VARCHAR(50) ,
                              PRIMARY KEY(id_voyage_statut)
);

CREATE TABLE place_vehicule(
                               id_place_vehicule SERIAL,
                               nb_place INTEGER NOT NULL,
                               PRIMARY KEY(id_place_vehicule)
);

CREATE TABLE place(
                      id_place SERIAL,
                      numero VARCHAR(50) ,
                      id_place_vehicule INTEGER NOT NULL,
                      PRIMARY KEY(id_place),
                      FOREIGN KEY(id_place_vehicule) REFERENCES place_vehicule(id_place_vehicule)
);

CREATE TABLE paiement_statut(
                                id_paiement_statut SERIAL,
                                libelle VARCHAR(50)  NOT NULL,
                                PRIMARY KEY(id_paiement_statut)
);

CREATE TABLE methode_paiement(
                                 id_methode_paiement SERIAL,
                                 libelle VARCHAR(50) ,
                                 PRIMARY KEY(id_methode_paiement)
);

CREATE TABLE vehicule(
                         id_vehicule SERIAL,
                         immatriculation VARCHAR(50)  NOT NULL,
                         modele VARCHAR(100) ,
                         id_place_vehicule INTEGER NOT NULL,
                         PRIMARY KEY(id_vehicule),
                         UNIQUE(immatriculation),
                         FOREIGN KEY(id_place_vehicule) REFERENCES place_vehicule(id_place_vehicule)
);

CREATE TABLE maintenance_vehicule(
                                     id_maintenance SERIAL,
                                     date_maintenance DATE NOT NULL,
                                     description TEXT,
                                     cout NUMERIC(10,2)  ,
                                     id_vehicule INTEGER NOT NULL,
                                     PRIMARY KEY(id_maintenance),
                                     FOREIGN KEY(id_vehicule) REFERENCES vehicule(id_vehicule)
);

CREATE TABLE voyage(
                       id_voyage SERIAL,
                       date_depart TIMESTAMP NOT NULL,
                       id_chauffeur INTEGER NOT NULL,
                       id_vehicule INTEGER NOT NULL,
                       id_trajet INTEGER NOT NULL,
                       PRIMARY KEY(id_voyage),
                       FOREIGN KEY(id_chauffeur) REFERENCES chauffeur(id_chauffeur),
                       FOREIGN KEY(id_vehicule) REFERENCES vehicule(id_vehicule),
                       FOREIGN KEY(id_trajet) REFERENCES trajet(id_trajet)
);

CREATE TABLE billet(
                       id_billet SERIAL,
                       montant_total NUMERIC(10,2)   NOT NULL,
                       statut VARCHAR(50) ,
                       code_billet VARCHAR(100) ,
                       id_place INTEGER NOT NULL,
                       id_client INTEGER NOT NULL,
                       id_voyage INTEGER NOT NULL,
                       PRIMARY KEY(id_billet),
                       UNIQUE(code_billet),
                       FOREIGN KEY(id_place) REFERENCES place(id_place),
                       FOREIGN KEY(id_client) REFERENCES client(id_client),
                       FOREIGN KEY(id_voyage) REFERENCES voyage(id_voyage)
);

CREATE TABLE paiement(
                         id_paiement SERIAL,
                         montant NUMERIC(10,2)   NOT NULL,
                         id_methode_paiement INTEGER NOT NULL,
                         id_billet INTEGER NOT NULL,
                         PRIMARY KEY(id_paiement),
                         FOREIGN KEY(id_methode_paiement) REFERENCES methode_paiement(id_methode_paiement),
                         FOREIGN KEY(id_billet) REFERENCES billet(id_billet)
);

CREATE TABLE bagage(
                       id_bagage SERIAL,
                       nombre_sacs INTEGER,
                       poids_total NUMERIC(6,2)  ,
                       cout NUMERIC(10,2)  ,
                       id_billet INTEGER NOT NULL,
                       PRIMARY KEY(id_bagage),
                       FOREIGN KEY(id_billet) REFERENCES billet(id_billet)
);

CREATE TABLE statut_vehicule(
                                id_vehicule INTEGER,
                                id_vehicules_statut INTEGER,
                                date_statut VARCHAR(50) ,
                                PRIMARY KEY(id_vehicule, id_vehicules_statut),
                                FOREIGN KEY(id_vehicule) REFERENCES vehicule(id_vehicule),
                                FOREIGN KEY(id_vehicules_statut) REFERENCES vehicules_statut(id_vehicules_statut)
);

CREATE TABLE statut_voyage(
                              id_voyage INTEGER,
                              id_voyage_statut INTEGER,
                              date_statut VARCHAR(50) ,
                              PRIMARY KEY(id_voyage, id_voyage_statut),
                              FOREIGN KEY(id_voyage) REFERENCES voyage(id_voyage),
                              FOREIGN KEY(id_voyage_statut) REFERENCES voyage_statut(id_voyage_statut)
);

CREATE TABLE statut_paiement(
                                id_paiement INTEGER,
                                id_paiement_statut INTEGER,
                                date_statut DATE NOT NULL,
                                PRIMARY KEY(id_paiement, id_paiement_statut),
                                FOREIGN KEY(id_paiement) REFERENCES paiement(id_paiement),
                                FOREIGN KEY(id_paiement_statut) REFERENCES paiement_statut(id_paiement_statut)
);
