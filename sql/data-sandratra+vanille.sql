-- ============================================
-- DONNÉES DE BASE
-- ============================================

-- Administrateurs
INSERT INTO admin (nom, email, mot_de_passe) VALUES
('Rakoto', 'rakoto@gmail.com', 'rakoto'),
('Rabe', 'rabe@gmail.com', 'rabe');

-- Statuts de véhicules
INSERT INTO vehicules_statut (libelle) VALUES
('Disponible'),
('En maintenance'),
('Hors service');

-- Statuts de voyage
INSERT INTO voyage_statut (libelle) VALUES
('planifiee'),
('en_cours'),
('arrivee'),
('annulee'),
('accidente'),
('Prévue'),
('En cours'),
('Terminée'),
('Annulée');

-- Statuts de paiement
INSERT INTO paiement_statut (libelle) VALUES
('Effectué'),
('En attente'),
('Échec');

-- Gares
INSERT INTO gare (nom, adresse, ville) VALUES
('Gare Routière Analakely', 'Centre-ville', 'Antananarivo'),
('Gare Ambalavao', 'RN7', 'Ambalavao'),
('Gare Fianarantsoa', 'Centre', 'Fianarantsoa'),
('Gare Toliara', 'Sud', 'Toliara'),
('Gare Antananarivo', 'Rue de la Gare, Antananarivo', 'Antananarivo'),
('Gare Toamasina', 'Port de Toamasina', 'Toamasina'),
('Gare Mahajanga', 'Aéroport de Mahajanga', 'Mahajanga');

-- Trajets
INSERT INTO trajet (id_gare_depart, id_gare_arrivee, distance_km, duree_estimee_minutes) VALUES
(1, 2, 350, 420),
(2, 3, 410, 480),
(3, 4, 570, 720),
(5, 6, 400.00, 480),
(5, 3, 300.00, 360),
(5, 7, 500.00, 600),
(6, 5, 400.00, 480),
(6, 3, 600.00, 720);

-- Chauffeurs
INSERT INTO chauffeur (nom, prenom, telephone) VALUES
('Rakoto', 'Jean', '0341234567'),
('Rabe', 'Paul', '0329876543'),
('Andry', 'Michel', '0331122334'),
('Andry', 'Rakoto', '+261 34 12 345 67'),
('Faly', 'Rabe', '+261 34 23 456 78'),
('Hery', 'Razafy', '+261 34 34 567 89'),
('Solo', 'Andrianina', '+261 34 45 678 90');

-- Tarifs
INSERT INTO tarif (prix_base, prix_bagage, id_trajet) VALUES
(50000.00, 5000.00, 4),
(40000.00, 4000.00, 5),
(60000.00, 6000.00, 6),
(50000.00, 5000.00, 7),
(70000.00, 7000.00, 8);

-- Clients
INSERT INTO client (nom, prenom, telephone, email) VALUES
('Rakoto', 'Jean', '+261 34 56 789 01', 'jean.rakoto@gmail.com'),
('Rabe', 'Marie', '+261 34 67 890 12', 'marie.rabe@gmail.com'),
('Razafy', 'Paul', '+261 34 78 901 23', 'paul.razafy@gmail.com'),
('Andrianina', 'Sophie', '+261 34 89 012 34', 'sophie.andrianina@gmail.com'),
('Randria', 'Michel', '+261 34 90 123 45', 'michel.randria@gmail.com');

-- Places véhicules
INSERT INTO place_vehicule (id_place_vehicule, nb_place) VALUES
(1, 16),
(2, 20),
(3, 30),
(4, 32),
(5, 12);

-- Places pour véhicule 1 (16 places)
INSERT INTO place (numero, id_place_vehicule) VALUES
('A1', 1), ('A2', 1), ('A3', 1), ('A4', 1), ('A5', 1), ('A6', 1), ('A7', 1), ('A8', 1),
('A9', 1), ('A10', 1), ('A11', 1), ('A12', 1), ('A13', 1), ('A14', 1), ('A15', 1), ('A16', 1);

-- Places pour véhicule 2 (20 places)
INSERT INTO place (numero, id_place_vehicule) VALUES
('B1', 2), ('B2', 2), ('B3', 2), ('B4', 2), ('B5', 2), ('B6', 2), ('B7', 2), ('B8', 2),
('B9', 2), ('B10', 2), ('B11', 2), ('B12', 2), ('B13', 2), ('B14', 2), ('B15', 2), ('B16', 2),
('B17', 2), ('B18', 2), ('B19', 2), ('B20', 2);

-- Places pour véhicule 3 (30 places)
INSERT INTO place (numero, id_place_vehicule) VALUES
('C1', 3), ('C2', 3), ('C3', 3), ('C4', 3), ('C5', 3), ('C6', 3), ('C7', 3), ('C8', 3),
('C9', 3), ('C10', 3), ('C11', 3), ('C12', 3), ('C13', 3), ('C14', 3), ('C15', 3), ('C16', 3),
('C17', 3), ('C18', 3), ('C19', 3), ('C20', 3), ('C21', 3), ('C22', 3), ('C23', 3), ('C24', 3),
('C25', 3), ('C26', 3), ('C27', 3), ('C28', 3), ('C29', 3), ('C30', 3);

-- Places pour véhicule 4 (32 places)
INSERT INTO place (numero, id_place_vehicule) VALUES
('D1', 4), ('D2', 4), ('D3', 4), ('D4', 4), ('D5', 4), ('D6', 4), ('D7', 4), ('D8', 4),
('D9', 4), ('D10', 4), ('D11', 4), ('D12', 4), ('D13', 4), ('D14', 4), ('D15', 4), ('D16', 4),
('D17', 4), ('D18', 4), ('D19', 4), ('D20', 4), ('D21', 4), ('D22', 4), ('D23', 4), ('D24', 4),
('D25', 4), ('D26', 4), ('D27', 4), ('D28', 4), ('D29', 4), ('D30', 4), ('D31', 4), ('D32', 4);

-- Places pour véhicule 5 (12 places)
INSERT INTO place (numero, id_place_vehicule) VALUES
('E1', 5), ('E2', 5), ('E3', 5), ('E4', 5), ('E5', 5), ('E6', 5),
('E7', 5), ('E8', 5), ('E9', 5), ('E10', 5), ('E11', 5), ('E12', 5);

-- Véhicules
INSERT INTO vehicule (immatriculation, modele, id_place_vehicule) VALUES
('1234-TAA', 'Toyota Hiace', 1),
('5678-TBB', 'Nissan Civilian', 2),
('9012-TCC', 'Coaster', 3),
('1234-TAB', 'Mercedes Benz Sprinter', 4),
('5678-TAM', 'Iveco Daily', 5),
('9012-FIA', 'Scania', 4),
('3456-MAH', 'Volvo', 4);

-- Maintenances véhicules
INSERT INTO maintenance_vehicule (date_maintenance, description, cout, id_vehicule) VALUES
('2023-05-15', 'Changement d''huile', 150000.00, 6),
('2023-06-20', 'Réparation moteur', 500000.00, 6);

-- Voyages
INSERT INTO voyage (date_depart, id_chauffeur, id_vehicule, id_trajet) VALUES
(NOW() - INTERVAL '3 hours', 1, 1, 1),
(NOW() + INTERVAL '5 hours', 2, 2, 2),
('2023-07-01 08:00:00', 4, 4, 4),
('2023-07-02 09:00:00', 5, 5, 5),
('2023-07-03 10:00:00', 6, 7, 6),
('2023-07-04 11:00:00', 7, 4, 7);

-- Statuts de voyage
INSERT INTO statut_voyage (id_voyage, id_voyage_statut, date_statut) VALUES
(1, 1, NOW() - INTERVAL '4 hours'),
(1, 2, NOW() - INTERVAL '3 hours'),
(2, 1, NOW()),
(3, 1, '2023-06-30'),
(4, 2, '2023-07-02'),
(5, 3, '2023-07-03'),
(6, 4, '2023-07-04');

-- Statuts de véhicules
INSERT INTO statut_vehicule (id_vehicule, id_vehicules_statut, date_statut) VALUES
(1, 1, '2023-01-01'),
(2, 1, '2023-01-01'),
(3, 2, '2023-05-01'),
(4, 1, '2023-01-01'),
(5, 1, '2023-01-01'),
(6, 2, '2023-05-01'),
(7, 1, '2023-01-01');

-- Billets
INSERT INTO billet (montant_total, statut, code_billet, id_place, id_client, id_voyage) VALUES
(55000.00, 'payé', 'BIL001', 1, 1, 3),
(44000.00, 'payé', 'BIL002', 17, 2, 4),
(66000.00, 'non_paye', 'BIL003', 33, 3, 5),
(55000.00, 'payé', 'BIL004', 49, 4, 6),
(77000.00, 'payé', 'BIL005', 2, 5, 3);

-- Paiements
INSERT INTO paiement (methode, montant, id_billet) VALUES
('mobile_money', 55000.00, 1),
('espece', 44000.00, 2),
('carte', 77000.00, 5),
('mobile_money', 55000.00, 4);

-- Statuts de paiement
INSERT INTO statut_paiement (id_paiement, id_paiement_statut, date_statut) VALUES
(1, 1, '2023-07-01'),
(2, 1, '2023-07-02'),
(3, 1, '2023-07-01'),
(4, 1, '2023-07-04');

-- Bagages
INSERT INTO bagage (nombre_sacs, poids_total, cout, id_billet) VALUES
(2, 15.50, 10000.00, 1),
(1, 8.00, 4000.00, 2),
(3, 22.00, 15000.00, 5);