
-- Nouvelle table publicite (stocke les informations de la publicité d'une société)
CREATE TABLE publicite (
    id_publicite SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    id_societe INTEGER NOT NULL,
    FOREIGN KEY (id_societe) REFERENCES societe(id_societe)
);

-- Nouvelle table publicite_diffusion (stocke les diffusions de publicités durant les voyages)
CREATE TABLE publicite_diffusion (
    id_publicite_diffusion SERIAL PRIMARY KEY,
    date_diffusion TIMESTAMP NOT NULL,
    id_publicite INTEGER NOT NULL,
    est_paye BOOLEAN NOT NULL DEFAULT FALSE,
    id_voyage INTEGER NOT NULL,
    FOREIGN KEY (id_publicite) REFERENCES publicite(id_publicite),
    FOREIGN KEY (id_voyage) REFERENCES voyage(id_voyage)
);

-- Table paiement_publicite (mise à jour pour référencer publicite_diffusion)
CREATE TABLE paiement_publicite (
    id_paiement_publicite SERIAL PRIMARY KEY,
    montant NUMERIC(15,2) NOT NULL,
    date_paiement TIMESTAMP NOT NULL,
    id_publicite_diffusion INTEGER NOT NULL,
    id_methode_paiement INTEGER NOT NULL,
    FOREIGN KEY (id_publicite_diffusion) REFERENCES publicite_diffusion(id_publicite_diffusion),
    FOREIGN KEY (id_methode_paiement) REFERENCES methode_paiement(id_methode_paiement)
);

-- Table tarif_publicite (pour le tarif par diffusion)
CREATE TABLE tarif_publicite (
    id_tarif_publicite SERIAL PRIMARY KEY,
    montant NUMERIC(15,2) NOT NULL,
    date_tarif TIMESTAMP NOT NULL
);
