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
('ECO', 'Economique', 1, true),
('VIP', 'VIP', 2, true);

INSERT INTO type_client (libelle) VALUES
('Adulte'),
('Enfant');

INSERT INTO type_client (libelle) VALUES
('Senior');

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
('Ford', 'Transit', 9.5, 20),
('Nissan', 'Civilian', 13.0, 25),
('Hyundai', 'County', 10.5, 12),
('Renault', 'Master', 12.0, 15),
('Peugeot', 'Boxer', 11.5, 22),
('Citroën', 'Jumper', 12.2, 28),
('Mitsubishi', 'Fuso', 14.5, 20),
('Isuzu', 'Elf', 13.5, 35),
('Hino', 'Dutro', 15.5, 45),
('Tata', 'Winger', 16.0, 30),
('Mahindra', 'Supro', 14.8, 38);

INSERT INTO vehicule_modele (marque, modele, consommation_l_100km, place) VALUES
('Force Motors', 'Traveller', 12.7, 20);

-- Insertion des véhicules
INSERT INTO vehicule (immatriculation, consommation_l_100km, id_vehicule_modele) VALUES
('1234-TAB', 12.5, 1),
('5678-TAM', 10.0, 2),
('9012-FIA', 15.0, 3),
('3456-MAH', 14.0, 4);

-- Insertion des places pour les véhicules (exemple pour le premier véhicule, 32 places)
-- ALTER TABLE place ADD COLUMN IF NOT EXISTS id_categorie INTEGER;

-- Mise à jour: on ajoute la colonne `id_categorie` (référence à `categorie`).
-- Pour ces données d'exemple on renseigne `1` (Standard) par défaut.
-- Véhicule 1 : 32 places (alternance Standard, VIP, Premium)
INSERT INTO place (numero, id_vehicule, id_categorie) VALUES
(1, 1, 1), (2, 1, 2), (3, 1, 3), (4, 1, 1), (5, 1, 2), (6, 1, 3), (7, 1, 1), (8, 1, 2), (9, 1, 3), (10, 1, 1),
(11, 1, 2), (12, 1, 3), (13, 1, 1), (14, 1, 2), (15, 1, 3), (16, 1, 1), (17, 1, 2), (18, 1, 3), (19, 1, 1), (20, 1, 2),
(21, 1, 3), (22, 1, 1), (23, 1, 2), (24, 1, 3), (25, 1, 1), (26, 1, 2), (27, 1, 3), (28, 1, 1), (29, 1, 2), (30, 1, 3),
(31, 1, 1), (32, 1, 2);

-- Véhicule 2 : 16 places (alternance Standard, VIP, Premium)
INSERT INTO place (numero, id_vehicule, id_categorie) VALUES
(1, 2, 1), (2, 2, 2), (3, 2, 3), (4, 2, 1), (5, 2, 2), (6, 2, 3), (7, 2, 1), (8, 2, 2), (9, 2, 3), (10, 2, 1),
(11, 2, 2), (12, 2, 3), (13, 2, 1), (14, 2, 2), (15, 2, 3), (16, 2, 1);

-- Véhicule 3 : 50 places (alternance Standard, VIP, Premium)
INSERT INTO place (numero, id_vehicule, id_categorie) VALUES
(1, 3, 1), (2, 3, 2), (3, 3, 3), (4, 3, 1), (5, 3, 2), (6, 3, 3), (7, 3, 1), (8, 3, 2), (9, 3, 3), (10, 3, 1),
(11, 3, 2), (12, 3, 3), (13, 3, 1), (14, 3, 2), (15, 3, 3), (16, 3, 1), (17, 3, 2), (18, 3, 3), (19, 3, 1), (20, 3, 2),
(21, 3, 3), (22, 3, 1), (23, 3, 2), (24, 3, 3), (25, 3, 1), (26, 3, 2), (27, 3, 3), (28, 3, 1), (29, 3, 2), (30, 3, 3),
(31, 3, 1), (32, 3, 2), (33, 3, 3), (34, 3, 1), (35, 3, 2), (36, 3, 3), (37, 3, 1), (38, 3, 2), (39, 3, 3), (40, 3, 1),
(41, 3, 2), (42, 3, 3), (43, 3, 1), (44, 3, 2), (45, 3, 3), (46, 3, 1), (47, 3, 2), (48, 3, 3), (49, 3, 1), (50, 3, 2);

-- Véhicule 4 : 45 places (alternance Standard, VIP, Premium)
INSERT INTO place (numero, id_vehicule, id_categorie) VALUES
(1, 4, 1), (2, 4, 2), (3, 4, 3), (4, 4, 1), (5, 4, 2), (6, 4, 3), (7, 4, 1), (8, 4, 2), (9, 4, 3), (10, 4, 1),
(11, 4, 2), (12, 4, 3), (13, 4, 1), (14, 4, 2), (15, 4, 3), (16, 4, 1), (17, 4, 2), (18, 4, 3), (19, 4, 1), (20, 4, 2),
(21, 4, 3), (22, 4, 1), (23, 4, 2), (24, 4, 3), (25, 4, 1), (26, 4, 2), (27, 4, 3), (28, 4, 1), (29, 4, 2), (30, 4, 3),
(31, 4, 1), (32, 4, 2), (33, 4, 3), (34, 4, 1), (35, 4, 2), (36, 4, 3), (37, 4, 1), (38, 4, 2), (39, 4, 3), (40, 4, 1),
(41, 4, 2), (42, 4, 3), (43, 4, 1), (44, 4, 2), (45, 4, 3);

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
('Solo', 'Andrianina', '+261 34 45 678 90'),
('Mamy', 'Randria', '+261 34 56 789 01'),
('Tiana', 'Rasoa', '+261 34 67 890 12'),
('Noro', 'Rakotomalala', '+261 34 78 901 23'),
('Lala', 'Ratsimbazafy', '+261 34 89 012 34'),
('Sitraka', 'Rasolofonirina', '+261 34 90 123 45'),
('Feno', 'Andriamasy', '+261 34 01 234 56'),
('Koto', 'Razanakoto', '+261 34 12 345 67'),
('Dina', 'Rasolofo', '+261 34 23 456 78'),
('Miora', 'Rakotomamonjy', '+261 34 34 567 89'),
('Hanta', 'Ratsimandrava', '+261 34 45 678 90'),
('Tovo', 'Razanaka', '+261 34 56 789 01'),
('Sariaka', 'Andrianarivo', '+261 34 67 890 12'),
('Boto', 'Rasolo', '+261 34 78 901 23'),
('Lova', 'Rakotobe', '+261 34 89 012 34'),
('Nina', 'Ratsimbazafy', '+261 34 90 123 45'),
('Dodo', 'Andrianina', '+261 34 01 234 56'),
('Kely', 'Rasoa', '+261 34 12 345 67'),
('Vola', 'Rakotoniaina', '+261 34 89 012 34'),
('Mamy', 'Ratsimalahelo', '+261 34 90 123 45');


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
-- Pour chaque trajet, on ajoute un tarif Standard (id_categorie=1), VIP (id_categorie=2) et Premium (id_categorie=3).
INSERT INTO place_tarif (id_categorie, id_trajet, tarif, date_tarif, id_type_client) VALUES
(1, 1, 50000.00, '2026-01-15 00:00:00', 1), -- Trajet 1 standard
(2, 1, 60000.00, '2026-01-15 00:00:00', 1), -- Trajet 1 VIP
(3, 1, 75000.00, '2026-01-15 00:00:00', 1), -- Trajet 1 Premium
(1, 2, 40000.00, '2026-01-15 00:00:00', 1), -- Trajet 2 standard
(2, 2, 48000.00, '2026-01-15 00:00:00', 1), -- Trajet 2 VIP    
(3, 2, 62500.00, '2026-01-15 00:00:00', 1), -- Trajet 2 Premium
(1, 3, 60000.00, '2026-01-15 00:00:00', 1), -- Trajet 3 standard
(2, 3, 72000.00, '2026-01-15 00:00:00', 1), -- Trajet 3 VIP
(3, 3, 90000.00, '2026-01-15 00:00:00', 1), -- Trajet 3 Premium
(1, 4, 50000.00, '2026-01-15 00:00:00', 1), -- Trajet 4 standard
(2, 4, 60000.00, '2026-01-15 00:00:00', 1), -- Trajet 4 VIP
(3, 4, 75000.00, '2026-01-15 00:00:00', 1), -- Trajet 4 Premium
(1, 5, 70000.00, '2026-01-15 00:00:00', 1), -- Trajet 5 standard
(2, 5, 84000.00, '2026-01-15 00:00:00', 1), -- Trajet 5 VIP
(3, 5, 105000.00, '2026-01-15 00:00:00', 1); -- Trajet 5 Premium

INSERT INTO place_tarif (id_categorie, id_trajet, tarif, date_tarif, id_type_client) VALUES
(2, 1, 65000, '2026-01-17 00:00:00', 2),
(3, 1, 50000, '2026-01-17 00:00:00', 2),
(1, 1, 40000, '2026-01-17 00:00:00', 2);

INSERT INTO place_tarif (id_categorie, id_trajet, tarif, date_tarif, id_type_client) VALUES
(1, 1, 55000, '2026-01-17 00:00:00', 1),
(2, 1, 70000, '2026-01-17 00:00:00', 1),
(3, 1, 60000, '2026-01-17 00:00:00', 1);

-- UPDATE place_tarif SET tarif = 55000  WHERE id_place_tarif = 68;


INSERT INTO reduction (id_type_client, reduction) VALUES
(3, 20.0); -- Senior : 20% de réduction


INSERT INTO societe (nom, adresse, telephone, email) VALUES
('Vaniala', 'Lot II A 123, Antananarivo', '+261341234567', 'contact@vaniala.mg'),
('Lewis', 'Av. de la Publicité, Toamasina', '+261342345678', 'info@lewis.mg');
-- ('Sponsor Malagasy', 'Rue du Sponsor, Fianarantsoa', '+261343456789', 'hello@sponsor.mg');


-- INSERT INTO publicite (date_diffusion, id_vehicule, id_societe, est_paye) VALUES
-- ('2025-12-01 08:00:00', 1, 1, FALSE),
-- ('2025-12-02 08:00:00', 2, 1, FALSE),
-- ('2025-12-03 08:00:00', 3, 1, FALSE),
-- ('2025-12-04 08:00:00', 4, 1, FALSE),
-- ('2025-12-05 08:00:00', 1, 1, FALSE),
-- ('2025-12-06 08:00:00', 2, 1, FALSE),
-- ('2025-12-07 08:00:00', 3, 1, FALSE),
-- ('2025-12-08 08:00:00', 4, 1, FALSE),
-- ('2025-12-09 08:00:00', 1, 1, FALSE),
-- ('2025-12-10 08:00:00', 2, 1, FALSE),
-- -- Vaniala (10 diffusions)
-- ('2025-12-11 08:00:00', 3, 1, FALSE),
-- ('2025-12-12 08:00:00', 4, 1, FALSE),
-- ('2025-12-13 08:00:00', 1, 1, FALSE),
--
-- ('2025-12-14 08:00:00', 2, 1, FALSE),
-- ('2025-12-15 08:00:00', 3, 1, FALSE),
-- ('2025-12-16 08:00:00', 4, 1, FALSE),
-- ('2025-12-17 08:00:00', 1, 1, FALSE),
-- ('2025-12-18 08:00:00', 2, 1, FALSE),
-- ('2025-12-19 08:00:00', 3, 1, FALSE),
-- ('2025-12-20 08:00:00', 4, 1, FALSE),
--
-- -- Lewis (10 diffusions)
-- ('2025-12-21 08:00:00', 1, 2, FALSE),
-- ('2025-12-22 08:00:00', 2, 2, FALSE),
-- ('2025-12-23 08:00:00', 3, 2, FALSE),
-- ('2025-12-24 08:00:00', 4, 2, FALSE),
-- ('2025-12-25 08:00:00', 1, 2, FALSE),
-- ('2025-12-26 08:00:00', 2, 2, FALSE),
-- ('2025-12-27 08:00:00', 3, 2, FALSE),
-- ('2025-12-28 08:00:00', 4, 2, FALSE),
-- ('2025-12-29 08:00:00', 1, 2, FALSE),
-- ('2025-12-30 08:00:00', 2, 2, FALSE);

INSERT INTO tarif_publicite (montant, date_tarif) VALUES (100000, NOW());

-- Insertion des publicités (table publicite : id, nom, id_societe)
-- Vaniala (id_societe = 1)
INSERT INTO publicite (nom, id_societe) VALUES
('Pub Vaniala - Boisson', 1),
('Pub Vaniala - Snack', 1),
('Pub Vaniala - Promo Été', 1);

-- Lewis (id_societe = 2)
INSERT INTO publicite (nom, id_societe) VALUES
('Pub Lewis - Mode', 2),
('Pub Lewis - Accessoires', 2);

-- Insertion des diffusions de publicités (table publicite_diffusion)
-- 20 diffusions pour Vaniala (id_societe = 1, publicites id 1, 2, 3)
-- 10 diffusions pour Lewis (id_societe = 2, publicites id 4, 5)
-- Toutes les diffusions sont non payées (est_paye = FALSE)
-- Voyages disponibles : 1, 2, 3, 4

INSERT INTO publicite_diffusion (date_diffusion, id_publicite, est_paye, id_voyage) VALUES
-- Vaniala - 20 diffusions
('2026-01-05 08:00:00', 1, FALSE, 1),
('2026-01-06 09:00:00', 1, FALSE, 2),
('2026-01-07 10:00:00', 2, FALSE, 3),
('2026-01-08 11:00:00', 2, FALSE, 4),
('2026-01-09 08:00:00', 3, FALSE, 1),
('2026-01-10 09:00:00', 1, FALSE, 2),
('2026-01-11 10:00:00', 1, FALSE, 3),
('2026-01-12 11:00:00', 2, FALSE, 4),
('2026-01-13 08:00:00', 2, FALSE, 1),
('2026-01-14 09:00:00', 3, FALSE, 2),
('2026-01-15 10:00:00', 3, FALSE, 3),
('2026-01-16 11:00:00', 1, FALSE, 4),
('2026-01-17 08:00:00', 1, FALSE, 1),
('2026-01-18 09:00:00', 2, FALSE, 2),
('2026-01-19 10:00:00', 2, FALSE, 3),
('2026-01-20 11:00:00', 3, FALSE, 4),
('2026-01-21 08:00:00', 3, FALSE, 1),
('2026-01-22 09:00:00', 1, FALSE, 2),
('2026-01-23 10:00:00', 2, FALSE, 3),
('2026-01-24 11:00:00', 3, FALSE, 4),

-- Lewis - 10 diffusions
('2026-01-05 08:30:00', 4, FALSE, 1),
('2026-01-06 09:30:00', 4, FALSE, 2),
('2026-01-07 10:30:00', 5, FALSE, 3),
('2026-01-08 11:30:00', 5, FALSE, 4),
('2026-01-09 08:30:00', 4, FALSE, 1),
('2026-01-10 09:30:00', 4, FALSE, 2),
('2026-01-11 10:30:00', 5, FALSE, 3),
('2026-01-12 11:30:00', 5, FALSE, 4),
('2026-01-13 08:30:00', 4, FALSE, 1),
('2026-01-14 09:30:00', 5, FALSE, 2);

