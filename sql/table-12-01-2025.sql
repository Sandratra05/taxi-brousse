-- \c postgres
-- drop database taxi_brousse;
-- create database taxi_brousse;
-- \c taxi_brousse
-- Script de mise à jour : table-12-01-2025.sql
-- Supprimer les tables obsolètes
DROP TABLE IF EXISTS statut_paiement;
DROP TABLE IF EXISTS statut_voyage;
DROP TABLE IF EXISTS statut_vehicule;
DROP TABLE IF EXISTS bagage;
DROP TABLE IF EXISTS paiement;
DROP TABLE IF EXISTS billet;
DROP TABLE IF EXISTS voyage;
DROP TABLE IF EXISTS maintenance_vehicule;
DROP TABLE IF EXISTS vehicule;
DROP TABLE IF EXISTS methode_paiement;
DROP TABLE IF EXISTS paiement_statut;
DROP TABLE IF EXISTS place;
DROP TABLE IF EXISTS place_vehicule;
DROP TABLE IF EXISTS vehicules_statut;
DROP TABLE IF EXISTS voyage_statut;
DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS tarif;
DROP TABLE IF EXISTS trajet;
DROP TABLE IF EXISTS chauffeur;
DROP TABLE IF EXISTS gare;
DROP TABLE IF EXISTS admin;

-- Créer les nouvelles tables ou versions modifiées
CREATE TABLE admin(
   id_admin SERIAL,
   nom VARCHAR(50) NOT NULL,
   email VARCHAR(50) NOT NULL,
   mot_de_passe VARCHAR(50) NOT NULL,
   date_creation TIMESTAMP NOT NULL,
   PRIMARY KEY(id_admin)
);

CREATE TABLE vehicule_modele(
   id_vehicule_modele SERIAL,
   marque VARCHAR(50) NOT NULL,
   modele VARCHAR(50) NOT NULL,
   consommation_l_100km NUMERIC(15,2),
   place INTEGER NOT NULL,
   PRIMARY KEY(id_vehicule_modele)
);

CREATE TABLE categorie(
   id_categorie SERIAL,
   code VARCHAR(50) NOT NULL,
   nom VARCHAR(50) NOT NULL,
   ordre INTEGER NOT NULL,
   active BOOLEAN NOT NULL,
   PRIMARY KEY(id_categorie),
   UNIQUE(code)
);

CREATE TABLE unite(
   id_unite SERIAL,
   nom VARCHAR(50) NOT NULL,
   code VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_unite)
);

CREATE TABLE vehicule(
   id_vehicule SERIAL,
   immatriculation VARCHAR(50) NOT NULL,
   consommation_l_100km NUMERIC(15,2) NOT NULL,
   id_vehicule_modele INTEGER NOT NULL,
   PRIMARY KEY(id_vehicule),
   UNIQUE(immatriculation),
   FOREIGN KEY(id_vehicule_modele) REFERENCES vehicule_modele(id_vehicule_modele)
);

CREATE TABLE historique_consommation(
   id_historique_consommation SERIAL,
   date_conso DATE NOT NULL,
   consommation_l_100km NUMERIC(15,2) NOT NULL,
   id_vehicule INTEGER NOT NULL,
   PRIMARY KEY(id_historique_consommation),
   FOREIGN KEY(id_vehicule) REFERENCES vehicule(id_vehicule)
);

CREATE TABLE vehicule_statut(
   id_vehicule_statut SERIAL,
   libelle VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_vehicule_statut)
);

CREATE TABLE statut_vehicule(
   id_statut_vehicule SERIAL,
   date_statut DATE NOT NULL,
   id_vehicule_statut INTEGER NOT NULL,
   id_vehicule INTEGER NOT NULL,
   PRIMARY KEY(id_statut_vehicule),
   FOREIGN KEY(id_vehicule_statut) REFERENCES vehicule_statut(id_vehicule_statut),
   FOREIGN KEY(id_vehicule) REFERENCES vehicule(id_vehicule)
);

CREATE TABLE place(
   id_place SERIAL,
   numero INTEGER NOT NULL,
   id_vehicule INTEGER NOT NULL,
   id_categorie INTEGER NOT NULL,
   PRIMARY KEY(id_place),
   FOREIGN KEY (id_categorie) REFERENCES categorie(id_categorie),
   FOREIGN KEY(id_vehicule) REFERENCES vehicule(id_vehicule)
);

CREATE TABLE vehicule_maintenance(
   id_vehicule_maintenance SERIAL,
   date_maintenance TIMESTAMP NOT NULL,
   date_fin TIMESTAMP,
   description VARCHAR(100) NOT NULL,
   cout NUMERIC(15,2),
   id_vehicule INTEGER NOT NULL,
   PRIMARY KEY(id_vehicule_maintenance),
   FOREIGN KEY(id_vehicule) REFERENCES vehicule(id_vehicule)
);

CREATE TABLE voyage_statut(
   id_voyage_statut SERIAL,
   libelle VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_voyage_statut)
);

CREATE TABLE billet_statut(
   id_billet_statut SERIAL,
   libelle VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_billet_statut)
);

CREATE TABLE client(
   id_client SERIAL,
   nom VARCHAR(50) NOT NULL,
   prenom VARCHAR(50) NOT NULL,
   telephone VARCHAR(50) NOT NULL,
   email VARCHAR(50),
   PRIMARY KEY(id_client)
);

CREATE TABLE type_client(
   id_type_client SERIAL,
   libelle VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_type_client)
); 

CREATE TABLE methode_paiement(
   id_methode_paiement SERIAL,
   libelle VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_methode_paiement)
);

CREATE TABLE commande(
   id_commande SERIAL,
   montant_total NUMERIC(15,2) NOT NULL,
   date_ TIMESTAMP NOT NULL,
   id_client INTEGER NOT NULL,
   PRIMARY KEY(id_commande),
   FOREIGN KEY(id_client) REFERENCES client(id_client)
);

CREATE TABLE paiement_statut(
   id_paiement_statut SERIAL,
   libelle VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_paiement_statut)
);

CREATE TABLE regle_gestion(
   id_regle_gestion SERIAL,
   libelle VARCHAR(50) NOT NULL,
   valeur VARCHAR(50) NOT NULL,
   id_unite INTEGER NOT NULL,
   PRIMARY KEY(id_regle_gestion),
   FOREIGN KEY(id_unite) REFERENCES unite(id_unite)
);

CREATE TABLE chauffeur(
   id_chauffeur SERIAL,
   nom VARCHAR(50) NOT NULL,
   prenom VARCHAR(50) NOT NULL,
   telephone VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_chauffeur)
);

CREATE TABLE ville(
   id_ville SERIAL,
   libelle VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_ville)
);

CREATE TABLE caracteristique(
   id_caracteristique SERIAL,
   libelle VARCHAR(50) NOT NULL,
   id_unite INTEGER NOT NULL,
   PRIMARY KEY(id_caracteristique),
   FOREIGN KEY(id_unite) REFERENCES unite(id_unite)
);

CREATE TABLE caracteristique_categorie(
   id_caracteristique_categorie SERIAL,
   valeur VARCHAR(50) NOT NULL,
   id_categorie INTEGER NOT NULL,
   id_caracteristique INTEGER NOT NULL,
   PRIMARY KEY(id_caracteristique_categorie),
   FOREIGN KEY(id_categorie) REFERENCES categorie(id_categorie),
   FOREIGN KEY(id_caracteristique) REFERENCES caracteristique(id_caracteristique)
);

CREATE TABLE gare(
   id_gare SERIAL,
   nom VARCHAR(50) NOT NULL,
   adresse VARCHAR(50),
   active BOOLEAN NOT NULL,
   id_ville INTEGER NOT NULL,
   PRIMARY KEY(id_gare),
   FOREIGN KEY(id_ville) REFERENCES ville(id_ville)
);

CREATE TABLE paiement(
   id_paiement SERIAL,
   montant NUMERIC(15,2) NOT NULL,
   date_paiement TIMESTAMP NOT NULL,
   id_commande INTEGER NOT NULL,
   id_methode_paiement INTEGER NOT NULL,
   PRIMARY KEY(id_paiement),
   FOREIGN KEY(id_commande) REFERENCES commande(id_commande),
   FOREIGN KEY(id_methode_paiement) REFERENCES methode_paiement(id_methode_paiement)
);

CREATE TABLE statut_paiement(
   id_statut_paiement SERIAL,
   date_statut TIMESTAMP NOT NULL,
   id_paiement_statut INTEGER NOT NULL,
   id_paiement INTEGER NOT NULL,
   PRIMARY KEY(id_statut_paiement),
   FOREIGN KEY(id_paiement_statut) REFERENCES paiement_statut(id_paiement_statut),
   FOREIGN KEY(id_paiement) REFERENCES paiement(id_paiement)
);

CREATE TABLE trajet(
   id_trajet SERIAL,
   distance_km NUMERIC(15,2) NOT NULL,
   duree_estimee_minutes NUMERIC(15,2),
   id_gare_arrivee INTEGER NOT NULL,
   id_gare_depart INTEGER NOT NULL,
   PRIMARY KEY(id_trajet),
   FOREIGN KEY(id_gare_arrivee) REFERENCES gare(id_gare),
   FOREIGN KEY(id_gare_depart) REFERENCES gare(id_gare)
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

CREATE TABLE statut_voyage(
   id_statut_voyage SERIAL,
   date_statut TIMESTAMP NOT NULL,
   id_voyage_statut INTEGER NOT NULL,
   id_voyage INTEGER NOT NULL,
   PRIMARY KEY(id_statut_voyage),
   FOREIGN KEY(id_voyage_statut) REFERENCES voyage_statut(id_voyage_statut),
   FOREIGN KEY(id_voyage) REFERENCES voyage(id_voyage)
);

CREATE TABLE billet(
   id_billet SERIAL,
   code_billet VARCHAR(50) NOT NULL,
   montant_total NUMERIC(15,2) NOT NULL,
   statut VARCHAR(50),
   id_voyage INTEGER NOT NULL,
   id_place INTEGER NOT NULL,
   PRIMARY KEY(id_billet),
   FOREIGN KEY(id_voyage) REFERENCES voyage(id_voyage),
   FOREIGN KEY(id_place) REFERENCES place(id_place)
);

CREATE TABLE statut_billet(
   id_statut_billet SERIAL,
   date_statut TIMESTAMP NOT NULL,
   id_billet_statut INTEGER NOT NULL,
   id_billet INTEGER NOT NULL,
   PRIMARY KEY(id_statut_billet),
   FOREIGN KEY(id_billet_statut) REFERENCES billet_statut(id_billet_statut),
   FOREIGN KEY(id_billet) REFERENCES billet(id_billet)
);

CREATE TABLE bagage(
   id_bagage SERIAL,
   nombre_sacs INTEGER,
   poids_total NUMERIC(15,2) NOT NULL,
   id_billet INTEGER NOT NULL,
   PRIMARY KEY(id_bagage),
   FOREIGN KEY(id_billet) REFERENCES billet(id_billet)
);

CREATE TABLE details_commande(
   id_detail_commande SERIAL,
   id_billet INTEGER NOT NULL,
   id_commande INTEGER NOT NULL,
   PRIMARY KEY(id_detail_commande),
   FOREIGN KEY(id_billet) REFERENCES billet(id_billet),
   FOREIGN KEY(id_commande) REFERENCES commande(id_commande)
);

CREATE TABLE tarif(
   id_tarif SERIAL,
   date_tarif DATE NOT NULL,
   tarif NUMERIC(15,2) NOT NULL,
   id_categorie INTEGER NOT NULL,
   id_trajet INTEGER NOT NULL,
   PRIMARY KEY(id_tarif),
   FOREIGN KEY(id_categorie) REFERENCES categorie(id_categorie),
   FOREIGN KEY(id_trajet) REFERENCES trajet(id_trajet)
);

CREATE TABLE place_tarif (
    id_place_tarif SERIAL PRIMARY KEY,
    id_categorie INT NOT NULL,
    id_trajet INT NOT NULL,
    id_type_client INT NOT NULL,
    tarif DECIMAL(15, 2) NOT NULL,
    date_tarif TIMESTAMP NOT NULL,
    FOREIGN KEY (id_categorie) REFERENCES categorie(id_categorie),
    FOREIGN KEY (id_trajet) REFERENCES trajet(id_trajet),
    FOREIGN KEY (id_type_client) REFERENCES type_client(id_type_client)
);

CREATE TABLE reduction (
   id_reduction SERIAL PRIMARY KEY,
   id_type_client INT NOT NULL,
   reduction DECIMAL(5,2) NOT NULL,
   FOREIGN KEY (id_type_client) REFERENCES type_client(id_type_client)
   
);