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
('Places', 'place(s)');

-- Insertion des catégories de véhicule
INSERT INTO categorie (code, nom, ordre, active) VALUES
('STD', 'Standard', 1, true),
('VIP', 'VIP', 2, true);

-- Insertion des modèles de véhicule
INSERT INTO vehicule_modele (marque, modele, consommation_l_100km) VALUES
('Mercedes Benz', 'Sprinter', 12.5),
('Iveco', 'Daily', 10.0),
('Scania', 'Scania', 15.0),
('Volvo', 'Volvo', 14.0);

-- Insertion des véhicules
INSERT INTO vehicule (immatriculation, consommation_l_100km, id_categorie, id_vehicule_modele) VALUES
('1234-TAB', 12.5, 1, 1),
('5678-TAM', 10.0, 1, 2),
('9012-FIA', 15.0, 1, 3),
('3456-MAH', 14.0, 1, 4);

-- Insertion des places pour les véhicules (exemple pour le premier véhicule, 32 places)
INSERT INTO place (numero, id_vehicule) VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 1), (8, 1), (9, 1), (10, 1),
(11, 1), (12, 1), (13, 1), (14, 1), (15, 1), (16, 1), (17, 1), (18, 1), (19, 1), (20, 1),
(21, 1), (22, 1), (23, 1), (24, 1), (25, 1), (26, 1), (27, 1), (28, 1), (29, 1), (30, 1),
(31, 1), (32, 1);

-- Insertion des statuts de véhicule
INSERT INTO vehicule_statut (libelle) VALUES
('Disponible'),
('En maintenance'),
('Hors service');

-- Insertion des statuts de véhicule pour les véhicules
INSERT INTO statut_vehicule (date_statut, id_vehicule_statut, id_vehicule) VALUES
('2023-01-01', 1, 1),
('2023-01-01', 1, 2),
('2023-05-01', 2, 3),
('2023-01-01', 1, 4);

-- Insertion des maintenances de véhicule
INSERT INTO vehicule_maintenance (date_maintenance, date_fin, description, cout, id_vehicule) VALUES
('2023-05-15', '2023-05-16', 'Changement d''huile', 150000.00, 3),
('2023-06-20', '2023-06-22', 'Réparation moteur', 500000.00, 3);

-- Insertion des historiques de consommation
INSERT INTO historique_consommation (date_conso, consommation_l_100km, id_vehicule) VALUES
('2023-07-01', 12.5, 1),
('2023-07-02', 10.0, 2);

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
('Rakoto', 'rakoto@gmail.com', 'rakoto', '2023-01-01 00:00:00'),
('Rabe', 'rabe@gmail.com', 'rabe', '2023-01-01 00:00:00');

-- Insertion des chauffeurs
INSERT INTO chauffeur (nom, prenom, telephone) VALUES
('Andry', 'Rakoto', '+261 34 12 345 67'),
('Faly', 'Rabe', '+261 34 23 456 78'),
('Hery', 'Razafy', '+261 34 34 567 89'),
('Solo', 'Andrianina', '+261 34 45 678 90');

-- Insertion des caractéristiques
INSERT INTO caracteristique (libelle, id_unite) VALUES
('Vitesse maximale', 1), -- km/h, but unite has km, need to add h
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
('2023-07-01 08:00:00', 1, 1, 1),
('2023-07-02 09:00:00', 2, 2, 2),
('2023-07-03 10:00:00', 3, 4, 3),
('2023-07-04 11:00:00', 4, 1, 4);

-- Insertion des statuts de voyage
INSERT INTO statut_voyage (date_statut, id_voyage_statut, id_voyage) VALUES
('2023-06-30 00:00:00', 1, 1),
('2023-07-02 00:00:00', 2, 2),
('2023-07-03 00:00:00', 3, 3),
('2023-07-04 00:00:00', 4, 4);

-- Insertion des clients
INSERT INTO client (nom, prenom, telephone, email) VALUES
('Rakoto', 'Jean', '+261 34 56 789 01', 'jean.rakoto@gmail.com'),
('Rabe', 'Marie', '+261 34 67 890 12', 'marie.rabe@gmail.com'),
('Razafy', 'Paul', '+261 34 78 901 23', 'paul.razafy@gmail.com'),
('Andrianina', 'Sophie', '+261 34 89 012 34', 'sophie.andrianina@gmail.com'),
('Randria', 'Michel', '+261 34 90 123 45', 'michel.randria@gmail.com');

-- Insertion des commandes
INSERT INTO commande (montant_total, date_, id_client) VALUES
(55000.00, '2023-07-01 00:00:00', 1),
(44000.00, '2023-07-02 00:00:00', 2),
(66000.00, '2023-07-03 00:00:00', 3),
(55000.00, '2023-07-04 00:00:00', 4),
(77000.00, '2023-07-01 00:00:00', 5);

-- Insertion des billets
INSERT INTO billet (code_billet, montant_total, statut, id_voyage, id_place) VALUES
('BIL001', 55000.00, 'Confirmé', 1, 1),
('BIL002', 44000.00, 'Confirmé', 2, 2),
('BIL003', 66000.00, 'Réservé', 3, 3),
('BIL004', 55000.00, 'Confirmé', 4, 4),
('BIL005', 77000.00, 'Confirmé', 1, 5);

-- Insertion des statuts de billet
INSERT INTO statut_billet (date_statut, id_billet_statut, id_billet) VALUES
('2023-07-01 00:00:00', 2, 1),
('2023-07-02 00:00:00', 2, 2),
('2023-07-03 00:00:00', 1, 3),
('2023-07-04 00:00:00', 2, 4),
('2023-07-01 00:00:00', 2, 5);

-- Insertion des détails de commande
INSERT INTO details_commande (id_billet, id_commande) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- Insertion des paiements
INSERT INTO paiement (montant, date_paiement, id_commande, id_methode_paiement) VALUES
(55000.00, '2023-07-01 00:00:00', 1, 1),
(44000.00, '2023-07-02 00:00:00', 2, 2),
(66000.00, '2023-07-03 00:00:00', 3, 3),
(55000.00, '2023-07-04 00:00:00', 4, 1),
(77000.00, '2023-07-01 00:00:00', 5, 3);

-- Insertion des statuts de paiement
INSERT INTO statut_paiement (date_statut, id_paiement_statut, id_paiement) VALUES
('2023-07-01 00:00:00', 1, 1),
('2023-07-02 00:00:00', 1, 2),
('2023-07-03 00:00:00', 2, 3),
('2023-07-04 00:00:00', 1, 4),
('2023-07-01 00:00:00', 1, 5);

-- Insertion des bagages
INSERT INTO bagage (nombre_sacs, poids_total, id_billet) VALUES
(2, 15.50, 1),
(1, 8.00, 2),
(3, 22.00, 5);

-- Insertion des tarifs
INSERT INTO tarif (date_tarif, tarif, id_categorie, id_trajet) VALUES
('2023-01-01', 50000.00, 1, 1),
('2023-01-01', 40000.00, 1, 2),
('2023-01-01', 60000.00, 1, 3),
('2023-01-01', 50000.00, 1, 4),
('2023-01-01', 70000.00, 1, 5),
('2023-01-01', 60000.00, 2, 1),
('2023-01-01', 50000.00, 2, 2);
