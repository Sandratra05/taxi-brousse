-- Données d'exemple pour la nouvelle base de données Taxi-Brousse
-- Inspiré de data-sandratra.sql, adapté au nouveau schéma

-- Insertion des villes
INSERT INTO ville (libelle) VALUES
('Antananarivo'),
('Toamasina'),
('Fianarantsoa'),
('Mahajanga');

-- Insertion des unités
INSERT INTO unite (nom, code) VALUES
('Kilomètre', 'km'),
('Litre', 'L'),
('Heure', 'h'),
('Kilogramme', 'kg'),
('Places', 'place(s)'),
('km/h', 'km/h');

-- Insertion des catégories de véhicule
INSERT INTO categorie (code, nom, ordre, active) VALUES
('STD', 'Standard', 1, true),
('VIP', 'VIP', 2, true);

INSERT INTO categorie (code, nom, ordre, active) VALUES
('PRM', 'Premium', 3, true);

-- Insertion des modèles de véhicule
INSERT INTO vehicule_modele (marque, modele, consommation_l_100km, place) VALUES
('Mercedes Benz', 'Sprinter', 12.5, 32),
('Iveco', 'Daily', 10.0, 16),
('Scania', 'Scania', 15.0, 50),
('Volvo', 'Volvo', 14.0, 45);

INSERT INTO vehicule_modele (marque, modele, consommation_l_100km, place) VALUES
('Toyota', 'Coaster', 11.0, 18),
('Ford', 'Transit', 9.5, 20);

-- Insertion des véhicules
INSERT INTO vehicule (immatriculation, consommation_l_100km, id_categorie, id_vehicule_modele) VALUES
('1234-TAB', 12.5, 1, 1),
('5678-TAM', 10.0, 1, 2),
('9012-FIA', 15.0, 1, 3),
('3456-MAH', 14.0, 1, 4);

-- Insertion des places pour les véhicules (exemple pour le premier véhicule, 32 places)
INSERT INTO place (numero, id_vehicule) VALUES
ALTER TABLE place ADD COLUMN IF NOT EXISTS id_categorie INTEGER;

-- Mise à jour: on ajoute la colonne `id_categorie` (référence à `categorie`).
-- Pour ces données d'exemple on renseigne `1` (Standard) par défaut.
-- Véhicule 1 : 32 places (premières 4 places VIP)
INSERT INTO place (numero, id_vehicule, id_categorie) VALUES
(1, 1, 2), (2, 1, 2), (3, 1, 2), (4, 1, 2),
(5, 1, 1), (6, 1, 1), (7, 1, 1), (8, 1, 1), (9, 1, 1), (10, 1, 1),
(11, 1, 1), (12, 1, 1), (13, 1, 1), (14, 1, 1), (15, 1, 1), (16, 1, 1), (17, 1, 1), (18, 1, 1), (19, 1, 1), (20, 1, 1),
(21, 1, 1), (22, 1, 1), (23, 1, 1), (24, 1, 1), (25, 1, 1), (26, 1, 1), (27, 1, 1), (28, 1, 1), (29, 1, 1), (30, 1, 1),
(31, 1, 1), (32, 1, 1);

-- Véhicule 2 : 16 places (premières 2 places VIP)
INSERT INTO place (numero, id_vehicule, id_categorie) VALUES
(1, 2, 2), (2, 2, 2), (3, 2, 1), (4, 2, 1), (5, 2, 1), (6, 2, 1), (7, 2, 1), (8, 2, 1), (9, 2, 1), (10, 2, 1),
(11, 2, 1), (12, 2, 1), (13, 2, 1), (14, 2, 1), (15, 2, 1), (16, 2, 1);

-- Véhicule 3 : 50 places (premières 6 places VIP)
INSERT INTO place (numero, id_vehicule, id_categorie) VALUES
(1, 3, 2), (2, 3, 2), (3, 3, 2), (4, 3, 2), (5, 3, 2), (6, 3, 2),
(7, 3, 1), (8, 3, 1), (9, 3, 1), (10, 3, 1), (11, 3, 1), (12, 3, 1), (13, 3, 1), (14, 3, 1), (15, 3, 1), (16, 3, 1),
(17, 3, 1), (18, 3, 1), (19, 3, 1), (20, 3, 1), (21, 3, 1), (22, 3, 1), (23, 3, 1), (24, 3, 1), (25, 3, 1), (26, 3, 1),
(27, 3, 1), (28, 3, 1), (29, 3, 1), (30, 3, 1), (31, 3, 1), (32, 3, 1), (33, 3, 1), (34, 3, 1), (35, 3, 1), (36, 3, 1),
(37, 3, 1), (38, 3, 1), (39, 3, 1), (40, 3, 1), (41, 3, 1), (42, 3, 1), (43, 3, 1), (44, 3, 1), (45, 3, 1), (46, 3, 1),
(47, 3, 1), (48, 3, 1), (49, 3, 1), (50, 3, 1);

-- Véhicule 4 : 45 places (premières 5 places VIP)
INSERT INTO place (numero, id_vehicule, id_categorie) VALUES
(1, 4, 2), (2, 4, 2), (3, 4, 2), (4, 4, 2), (5, 4, 2),
(6, 4, 1), (7, 4, 1), (8, 4, 1), (9, 4, 1), (10, 4, 1),
(11, 4, 1), (12, 4, 1), (13, 4, 1), (14, 4, 1), (15, 4, 1), (16, 4, 1), (17, 4, 1), (18, 4, 1), (19, 4, 1), (20, 4, 1),
(21, 4, 1), (22, 4, 1), (23, 4, 1), (24, 4, 1), (25, 4, 1), (26, 4, 1), (27, 4, 1), (28, 4, 1), (29, 4, 1), (30, 4, 1),
(31, 4, 1), (32, 4, 1), (33, 4, 1), (34, 4, 1), (35, 4, 1), (36, 4, 1), (37, 4, 1), (38, 4, 1), (39, 4, 1), (40, 4, 1),
(41, 4, 1), (42, 4, 1), (43, 4, 1), (44, 4, 1), (45, 4, 1);

-- Insertion des statuts de véhicule
INSERT INTO vehicule_statut (libelle) VALUES
('Disponible'),
('En maintenance'),
('Hors service');

-- Insertion des statuts de véhicule pour les véhicules
INSERT INTO statut_vehicule (date_statut, id_vehicule_statut, id_vehicule) VALUES
('2026-01-01', 1, 1),
('2026-01-01', 1, 2),
('2026-05-01', 2, 3),
('2026-01-01', 1, 4);

-- Insertion des maintenances de véhicule
INSERT INTO vehicule_maintenance (date_maintenance, date_fin, description, cout, id_vehicule) VALUES
('2026-05-15', '2026-05-16', 'Changement d''huile', 150000.00, 3),
('2026-06-20', '2026-06-22', 'Réparation moteur', 500000.00, 3);

-- Insertion des historiques de consommation
INSERT INTO historique_consommation (date_conso, consommation_l_100km, id_vehicule) VALUES
('2026-07-01', 12.5, 1),
('2026-07-02', 10.0, 2);

-- Insertion des statuts de voyage
INSERT INTO voyage_statut (libelle) VALUES
('Prévue'),
('En cours'),
('Terminée'),
('Annulée');

-- Insertion des statuts de billet
INSERT INTO billet_statut (libelle) VALUES
('Réservé'),
('Confirmé'),
('Annulé');

-- Insertion des méthodes de paiement
INSERT INTO methode_paiement (libelle) VALUES
('Espèce'),
('Mobile Money'),
('Carte Bancaire');

-- Insertion des statuts de paiement
INSERT INTO paiement_statut (libelle) VALUES
('Payé'),
('Non payé'),
('En attente');

-- Insertion des administrateurs
INSERT INTO admin (nom, email, mot_de_passe, date_creation) VALUES
('Rakoto', 'rakoto@gmail.com', 'rakoto', '2026-01-01 00:00:00'),
('Rabe', 'rabe@gmail.com', 'rabe', '2026-01-01 00:00:00');

-- Insertion des chauffeurs
INSERT INTO chauffeur (nom, prenom, telephone) VALUES
('Andry', 'Rakoto', '+261 34 12 345 67'),
('Faly', 'Rabe', '+261 34 23 456 78'),
('Hery', 'Razafy', '+261 34 34 567 89'),
('Solo', 'Andrianina', '+261 34 45 678 90');

-- Insertion des caractéristiques
INSERT INTO caracteristique (libelle, id_unite) VALUES
('Vitesse maximale', 6), -- km/h
('Capacité bagage', 4), -- kg
('Capacité places', 5); -- places

-- Insertion des caractéristiques par catégorie
INSERT INTO caracteristique_categorie (valeur, id_categorie, id_caracteristique) VALUES
('120', 1, 1),
('100', 2, 1),
('20', 1, 2),
('30', 2, 2),
('32', 2, 3),
('16', 1, 3);

-- Insertion des règles de gestion
INSERT INTO regle_gestion (libelle, valeur, id_unite) VALUES
('Distance maximale', '1000', 1),
('Temps d''attente', '30', 3);

-- Insertion des gares
INSERT INTO gare (nom, adresse, active, id_ville) VALUES
('Gare Antananarivo', 'Rue de la Gare, Antananarivo', true, 1),
('Gare Toamasina', 'Port de Toamasina', true, 2),
('Gare Fianarantsoa', 'Centre-ville, Fianarantsoa', true, 3),
('Gare Mahajanga', 'Aéroport de Mahajanga', true, 4);

-- Insertion des trajets
INSERT INTO trajet (distance_km, duree_estimee_minutes, id_gare_arrivee, id_gare_depart) VALUES
(400.00, 480, 2, 1), -- Antananarivo -> Toamasina
(300.00, 360, 3, 1), -- Antananarivo -> Fianarantsoa
(500.00, 600, 4, 1), -- Antananarivo -> Mahajanga
(400.00, 480, 1, 2), -- Toamasina -> Antananarivo
(600.00, 720, 3, 2); -- Toamasina -> Fianarantsoa

-- Insertion des voyages
INSERT INTO voyage (date_depart, id_chauffeur, id_vehicule, id_trajet) VALUES
('2026-07-01 08:00:00', 1, 1, 1),
('2026-07-02 09:00:00', 2, 2, 2),
('2026-07-03 10:00:00', 3, 4, 3),
('2026-07-04 11:00:00', 4, 1, 4);

-- Insertion des statuts de voyage
INSERT INTO statut_voyage (date_statut, id_voyage_statut, id_voyage) VALUES
('2026-06-30 00:00:00', 1, 1),
('2026-07-02 00:00:00', 2, 2),
('2026-07-03 00:00:00', 3, 3),
('2026-07-04 00:00:00', 4, 4);

-- Insertion des clients
INSERT INTO client (nom, prenom, telephone, email) VALUES
('Rakoto', 'Jean', '+261 34 56 789 01', 'jean.rakoto@gmail.com'),
('Rabe', 'Marie', '+261 34 67 890 12', 'marie.rabe@gmail.com'),
('Razafy', 'Paul', '+261 34 78 901 23', 'paul.razafy@gmail.com'),
('Andrianina', 'Sophie', '+261 34 89 012 34', 'sophie.andrianina@gmail.com'),
('Randria', 'Michel', '+261 34 90 123 45', 'michel.randria@gmail.com');

-- Insertion des commandes
INSERT INTO commande (montant_total, date_, id_client) VALUES
(55000.00, '2026-07-01 00:00:00', 1),
(44000.00, '2026-07-02 00:00:00', 2),
(66000.00, '2026-07-03 00:00:00', 3),
(55000.00, '2026-07-04 00:00:00', 4),
(77000.00, '2026-07-01 00:00:00', 5);

-- Insertion des billets
INSERT INTO billet (code_billet, montant_total, statut, id_voyage, id_place) VALUES
('BIL001', 55000.00, 'Confirmé', 1, 1),
('BIL002', 44000.00, 'Confirmé', 2, 2),
('BIL003', 66000.00, 'Réservé', 3, 3),
('BIL004', 55000.00, 'Confirmé', 4, 4),
('BIL005', 77000.00, 'Confirmé', 1, 5);

-- Insertion des statuts de billet
INSERT INTO statut_billet (date_statut, id_billet_statut, id_billet) VALUES
('2026-07-01 00:00:00', 2, 1),
('2026-07-02 00:00:00', 2, 2),
('2026-07-03 00:00:00', 1, 3),
('2026-07-04 00:00:00', 2, 4),
('2026-07-01 00:00:00', 2, 5);

-- Insertion des détails de commande
INSERT INTO details_commande (id_billet, id_commande) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- Insertion des paiements
INSERT INTO paiement (montant, date_paiement, id_commande, id_methode_paiement) VALUES
(55000.00, '2026-07-01 00:00:00', 1, 1),
(44000.00, '2026-07-02 00:00:00', 2, 2),
(66000.00, '2026-07-03 00:00:00', 3, 3),
(55000.00, '2026-07-04 00:00:00', 4, 1),
(77000.00, '2026-07-01 00:00:00', 5, 3);

-- Insertion des statuts de paiement
INSERT INTO statut_paiement (date_statut, id_paiement_statut, id_paiement) VALUES
('2026-07-01 00:00:00', 1, 1),
('2026-07-02 00:00:00', 1, 2),
('2026-07-03 00:00:00', 2, 3),
('2026-07-04 00:00:00', 1, 4),
('2026-07-01 00:00:00', 1, 5);

-- Insertion des bagages
INSERT INTO bagage (nombre_sacs, poids_total, id_billet) VALUES
(2, 15.50, 1),
(1, 8.00, 2),
(3, 22.00, 5);

-- Insertion des tarifs
INSERT INTO tarif (date_tarif, tarif, id_categorie, id_trajet) VALUES
('2026-01-01', 50000.00, 1, 1),
('2026-01-01', 40000.00, 1, 2),
('2026-01-01', 60000.00, 1, 3),
('2026-01-01', 50000.00, 1, 4),
('2026-01-01', 70000.00, 1, 5),
('2026-01-01', 60000.00, 2, 1),
('2026-01-01', 50000.00, 2, 2);

-- Insertion des tarifs de places (place_tarif) — date : 2026-01-15
-- Pour chaque trajet, on ajoute un tarif Standard (id_categorie=1) et un tarif VIP (id_categorie=2).
INSERT INTO place_tarif (id_categorie, id_trajet, tarif, date_tarif) VALUES
(1, 1, 50000.00, '2026-01-15 00:00:00'), -- Trajet 1 standard
(2, 1, 60000.00, '2026-01-15 00:00:00'), -- Trajet 1 VIP
(1, 2, 40000.00, '2026-01-15 00:00:00'), -- Trajet 2 standard
(2, 2, 50000.00, '2026-01-15 00:00:00'), -- Trajet 2 VIP
(1, 3, 60000.00, '2026-01-15 00:00:00'), -- Trajet 3 standard
(2, 3, 72000.00, '2026-01-15 00:00:00'), -- Trajet 3 VIP (estimation: +20%)
(1, 4, 50000.00, '2026-01-15 00:00:00'), -- Trajet 4 standard
(2, 4, 60000.00, '2026-01-15 00:00:00'), -- Trajet 4 VIP (estimation: +20%)
(1, 5, 70000.00, '2026-01-15 00:00:00'), -- Trajet 5 standard
(2, 5, 84000.00, '2026-01-15 00:00:00'); -- Trajet 5 VIP (estimation: +20%)
