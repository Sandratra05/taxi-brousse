INSERT INTO voyage_statut (libelle) VALUES
('planifiee'),
('en_cours'),
('arrivee'),
('annulee'),
('accidente');


INSERT INTO gare (nom, ville, adresse) VALUES
('Gare Routière Analakely', 'Antananarivo', 'Centre-ville'),
('Gare Ambalavao', 'Ambalavao', 'RN7'),
('Gare Fianarantsoa', 'Fianarantsoa', 'Centre'),
('Gare Toliara', 'Toliara', 'Sud');

INSERT INTO trajet (id_gare_depart, id_gare_arrivee, distance_km, duree_estimee_minutes) VALUES
(1, 2, 350, 420),
(2, 3, 410, 480),
(3, 4, 570, 720);

INSERT INTO chauffeur (nom, prenom, telephone) VALUES
('Rakoto', 'Jean', '0341234567'),
('Rabe', 'Paul', '0329876543'),
('Andry', 'Michel', '0331122334');

INSERT INTO place_vehicule (id_place_vehicule, nb_place) VALUES
(1, 16),
(2, 20),
(3, 30);

INSERT INTO vehicule (immatriculation, modele, statut, id_place_vehicule) VALUES
('1234-TAA', 'Toyota Hiace', 'disponible', 1),
('5678-TBB', 'Nissan Civilian', 'disponible', 2),
('9012-TCC', 'Coaster', 'maintenance', 3);


INSERT INTO voyage (date_depart, id_chauffeur, id_vehicule, id_trajet)
VALUES (
    NOW() - INTERVAL '3 hours',
    1,
    1,
    1
);

INSERT INTO voyage (date_depart, id_chauffeur, id_vehicule, id_trajet)
VALUES (
    NOW() + INTERVAL '5 hours',
    2,
    2,
    2
);

INSERT INTO statut_voyage (id_voyage, id_voyage_statut, date_statut, commentaire)
VALUES
(3, 1, NOW() - INTERVAL '4 hours', 'Course planifiée'),
(3, 2, NOW() - INTERVAL '3 hours', 'Départ effectué');

INSERT INTO statut_voyage (id_voyage, id_voyage_statut, date_statut, commentaire)
VALUES
(4, 1, NOW(), 'Course planifiée');
