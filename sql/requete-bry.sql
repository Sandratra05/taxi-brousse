-- chiffre d'affaire par vehicule

SELECT
    v.id_vehicule,
    v.immatriculation,
    v.modele,
    SUM(b.montant_total * pv.nb_place) AS chiffreAffaire
FROM vehicule v
JOIN voyage vo ON v.id_vehicule = vo.id_vehicule
JOIN billet b ON vo.id_voyage = b.id_voyage
JOIN place_vehicule pv ON v.id_place_vehicule = pv.id_place_vehicule
GROUP BY
    v.id_vehicule,
    v.immatriculation,
    v.modele
ORDER BY v.id_vehicule ASC;

-- chiffre d'affaires par trajet

SELECT
    t.*,
    SUM(b.montant_total * pv.nb_place) AS chiffreAffaire
FROM trajet t
JOIN voyage vo ON t.id_trajet = vo.id_trajet
JOIN vehicule v ON vo.id_vehicule = v.id_vehicule
JOIN billet b ON vo.id_voyage = b.id_voyage
JOIN place_vehicule pv ON v.id_place_vehicule = pv.id_place_vehicule
GROUP BY
    t.id_trajet,
    t.id_gare_depart,
    t.id_gare_arrivee,
    t.distance_km,
    t.duree_estimee_minutes
ORDER BY id_trajet ASC;

