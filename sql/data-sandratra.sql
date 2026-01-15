-- Données d'exemple pour la base de données Taxi-Brousse
-- Utilisant des données malgaches

-- Insertion des administrateurs
INSERT INTO admin (nom, email, mot_de_passe) VALUES
('Rakoto', 'rakoto@gmail.com', 'rakoto'),
('Rabe', 'rabe@gmail.com', 'rabe');

-- Insertion des gares
INSERT INTO gare (nom, adresse, ville) VALUES
('Gare Antananarivo', 'Rue de la Gare, Antananarivo', 'Antananarivo'),
('Gare Toamasina', 'Port de Toamasina', 'Toamasina'),
('Gare Fianarantsoa', 'Centre-ville, Fianarantsoa', 'Fianarantsoa'),
('Gare Mahajanga', 'Aéroport de Mahajanga', 'Mahajanga');

-- Insertion des trajets
INSERT INTO trajet (distance_km, duree_estimee_minutes, id_gare_arrivee, id_gare_depart) VALUES
(400.00, 480, 2, 1), -- Antananarivo -> Toamasina
(300.00, 360, 3, 1), -- Antananarivo -> Fianarantsoa
(500.00, 600, 4, 1), -- Antananarivo -> Mahajanga
(400.00, 480, 1, 2), -- Toamasina -> Antananarivo
(600.00, 720, 3, 2); -- Toamasina -> Fianarantsoa

-- Insertion des chauffeurs
INSERT INTO chauffeur (nom, prenom, telephone) VALUES
('Andry', 'Rakoto', '+261 34 12 345 67'),
('Faly', 'Rabe', '+261 34 23 456 78'),
('Hery', 'Razafy', '+261 34 34 567 89'),
('Solo', 'Andrianina', '+261 34 45 678 90');

-- Insertion des tarifs
INSERT INTO tarif (prix_base, prix_bagage, id_trajet) VALUES
(50000.00, 5000.00, 1),
(40000.00, 4000.00, 2),
(60000.00, 6000.00, 3),
(50000.00, 5000.00, 4),
(70000.00, 7000.00, 5);

-- Insertion des statuts de véhicules
INSERT INTO vehicules_statut (libelle) VALUES
('Disponible'),
('En maintenance'),
('Hors service');

-- Insertion des clients
INSERT INTO client (nom, prenom, telephone, email) VALUES
('Rakoto', 'Jean', '+261 34 56 789 01', 'jean.rakoto@gmail.com'),
('Rabe', 'Marie', '+261 34 67 890 12', 'marie.rabe@gmail.com'),
('Razafy', 'Paul', '+261 34 78 901 23', 'paul.razafy@gmail.com'),
('Andrianina', 'Sophie', '+261 34 89 012 34', 'sophie.andrianina@gmail.com'),
('Randria', 'Michel', '+261 34 90 123 45', 'michel.randria@gmail.com');

-- Insertion des statuts de voyage
INSERT INTO voyage_statut (libelle) VALUES
('Prévue'),
('En cours'),
('Terminée'),
('Annulée');

-- Insertion des places de véhicules
INSERT INTO place_vehicule (id_place_vehicule, nb_place) VALUES
(1, 32),
(2, 12),
(3, 16);

-- Insertion des places
INSERT INTO place (numero, id_place_vehicule) VALUES
('A1', 1),
('A2', 1),
('A3', 1),
('A4', 1),
('A5', 1),
('A6', 1),
('A7', 1),
('A8', 1),
('A9', 1),
('A10', 1),
('A11', 1),
('A12', 1),
('A13', 1),
('A14', 1),
('A15', 1),
('A16', 1),
('A17', 1),
('A18', 1),
('A19', 1),
('A20', 1),
('A21', 1),
('A22', 1),
('A23', 1),
('A24', 1),
('A25', 1),
('A26', 1),
('A27', 1),
('A28', 1),
('A29', 1),
('A30', 1),
('A31', 1),
('A32', 1);

INSERT INTO place (numero, id_place_vehicule) VALUES
('B1', 2),
('B2', 2),
('B3', 2),
('B4', 2),
('B5', 2),
('B6', 2),
('B7', 2),
('B8', 2),
('B9', 2),
('B10', 2),
('B11', 2),
('B12', 2);

INSERT INTO place (numero, id_place_vehicule) VALUES
('C1', 3),
('C2', 3),
('C3', 3),
('C4', 3),
('C5', 3),
('C6', 3),
('C7', 3),
('C8', 3),
('C9', 3),
('C10', 3),
('C11', 3),
('C12', 3),
('C13', 3),
('C14', 3),
('C15', 3),
('C16', 3);

-- Insertion des statuts de paiement
INSERT INTO paiement_statut (libelle) VALUES
('Payé'),
('Non payé');

INSERT INTO methode_paiement (libelle) VALUES
('Espèce'),
('Mobile Money'),
('Carte Bancaire');

-- Insertion des véhicules
INSERT INTO vehicule (immatriculation, modele, id_place_vehicule) VALUES
('1234-TAB', 'Mercedes Benz Sprinter', 1),
('5678-TAM', 'Iveco Daily', 2),
('9012-FIA', 'Scania', 3),
('3456-MAH', 'Volvo', 1);

-- Insertion des maintenances de véhicules
INSERT INTO maintenance_vehicule (date_maintenance, description, cout, id_vehicule) VALUES
('2023-05-15', 'Changement d''huile', 150000.00, 3),
('2023-06-20', 'Réparation moteur', 500000.00, 3);

-- Insertion des voyages
INSERT INTO voyage (date_depart, id_chauffeur, id_vehicule, id_trajet) VALUES
('2023-07-01 08:00:00', 1, 1, 1),
('2023-07-02 09:00:00', 2, 2, 2),
('2023-07-03 10:00:00', 3, 4, 3),
('2023-07-04 11:00:00', 4, 1, 4);

-- Insertion des billets
INSERT INTO billet (montant_total, statut, code_billet, id_place, id_client, id_voyage) VALUES
(55000.00, 'Payé', 'BIL001', 1, 1, 1),
(44000.00, 'Payé', 'BIL002', 2, 2, 2),
(66000.00, 'Non Payé', 'BIL003', 3, 3, 3),
(55000.00, 'Payé', 'BIL004', 4, 4, 4),
(77000.00, 'Payé', 'BIL005', 5, 5, 1);

-- Insertion des paiements
-- INSERT INTO paiement (methode, montant, statut, id_billet) VALUES
-- ('mobile_money', 55000.00, 'effectue', 1),
-- ('espece', 44000.00, 'effectue', 2),
-- ('carte', 77000.00, 'effectue', 5),
-- ('mobile_money', 55000.00, 'effectue', 4);

INSERT INTO paiement (montant, id_methode_paiement, id_billet) VALUES
(55000.00, 1,  1),
(44000.00, 2,  2),
(77000.00, 3,  5),
(55000.00, 1,  4);


-- Insertion des bagages
INSERT INTO bagage (nombre_sacs, poids_total, cout, id_billet) VALUES
(2, 15.50, 10000.00, 1),
(1, 8.00, 4000.00, 2),
(3, 22.00, 15000.00, 5);

-- Insertion des statuts de véhicules
INSERT INTO statut_vehicule (id_vehicule, id_vehicules_statut, date_statut) VALUES
(1, 1, '2023-01-01'),
(2, 1, '2023-01-01'),
(3, 2, '2023-05-01'),
(4, 1, '2023-01-01');

-- Insertion des statuts de voyage
INSERT INTO statut_voyage (id_voyage, id_voyage_statut, date_statut) VALUES
(1, 1, '2023-06-30'),
(2, 2, '2023-07-02'),
(3, 3, '2023-07-03'),
(4, 4, '2023-07-04');

-- Insertion des statuts de paiement
INSERT INTO statut_paiement (id_paiement, id_paiement_statut, date_statut) VALUES
(1, 1, '2023-07-01'),
(2, 1, '2023-07-02'),
(3, 1, '2023-07-01'),
(4, 1, '2023-07-04');


INSERT INTO categorie(nom, code, ) VALUES
('Premium');
