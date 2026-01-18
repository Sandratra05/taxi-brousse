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


-- Voir les billets payés (statut = 'Payé')
SELECT b.*
FROM billet b 
JOIN details_commande dc ON b.id_billet = dc.id_billet
JOIN commande c ON dc.id_commande = c.id_commande
JOIN paiement p ON c.id_commande = p.id_commande
JOIN statut_paiement sp ON p.id_paiement = sp.id_paiement
JOIN paiement_statut ps ON sp.id_paiement_statut = ps.id_paiement_statut
WHERE ps.libelle = 'Payé';

-- Prix de chaque billet basé sur place_tarif (pour adultes, id_type_client=1)
SELECT b.id_voyage, b.id_billet, b.code_billet, p.numero AS numero_place, pt.tarif AS prix_tarif
FROM billet b
JOIN details_commande dc ON b.id_billet = dc.id_billet
JOIN commande c ON dc.id_commande = c.id_commande
JOIN paiement pay ON c.id_commande = pay.id_commande
JOIN statut_paiement sp ON pay.id_paiement = sp.id_paiement
JOIN paiement_statut ps ON sp.id_paiement_statut = ps.id_paiement_statut
JOIN place p ON b.id_place = p.id_place
JOIN voyage v ON b.id_voyage = b.id_voyage
JOIN place_tarif pt ON p.id_categorie = pt.id_categorie AND v.id_trajet = pt.id_trajet
WHERE b.id_voyage = 5 AND ps.libelle = 'Payé';
-- WHERE pt.date_tarif = (
--     SELECT MAX(pt2.date_tarif)
--     FROM place_tarif pt2
--     WHERE pt2.id_categorie = p.id_categorie
--       AND pt2.id_trajet = v.id_trajet
--       AND pt2.date_tarif <= v.date_depart
);


