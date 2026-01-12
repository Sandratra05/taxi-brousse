# Récapitulatif des modifications des modèles

## Nouveaux modèles créés

1. **VehiculeModele.java** - Modèle de véhicule (marque, modèle, consommation)
2. **Categorie.java** - Catégorie de véhicule (code, nom, ordre, active)
3. **Unite.java** - Unité de mesure (nom, code)
4. **HistoriqueConsommation.java** - Historique de consommation des véhicules
5. **Ville.java** - Ville (libelle)
6. **Commande.java** - Commande client (montant_total, date, client)
7. **RegleGestion.java** - Règles de gestion (libelle, valeur, unite)
8. **Caracteristique.java** - Caractéristiques (libelle, unite)
9. **CaracteristiqueCategorie.java** - Caractéristiques par catégorie
10. **BilletStatut.java** - Statut de billet (libelle)
11. **StatutBillet.java** - Historique des statuts de billet
12. **DetailsCommande.java** - Détails de commande (billet, commande)
13. **VehiculeStatut.java** - Statut de véhicule (libelle)

## Modèles modifiés

### Admin.java
- Longueur des colonnes ajustée (nom: 50, email: 50, mot_de_passe: 50)
- date_creation rendu obligatoire

### Vehicule.java
- Suppression du champ `modele`
- Suppression de la relation `placeVehicule`
- Ajout du champ `consommationL100km` (NUMERIC(15,2))
- Ajout de la relation `categorie` (ManyToOne vers Categorie)
- Ajout de la relation `vehiculeModele` (ManyToOne vers VehiculeModele)
- Ajout de la contrainte `unique` sur immatriculation

### Gare.java
- Suppression du champ `ville` (String)
- Ajout du champ `active` (Boolean)
- Ajout de la relation `ville` (ManyToOne vers Ville)
- Ajustement des longueurs (nom: 50, adresse: 50)

### Tarif.java
- Suppression des champs `prixBase` et `prixBagage`
- Ajout du champ `dateTarif` (LocalDate)
- Ajout du champ `tarif` (NUMERIC(15,2))
- Ajout de la relation `categorie` (ManyToOne vers Categorie)

### Client.java
- Champs nom, prenom, telephone rendus obligatoires (nullable = false)

### Billet.java
- Suppression de la relation `client`
- Réorganisation de l'ordre des champs
- Ajustement de la précision de montant_total (15,2)
- code_billet réduit à 50 caractères

### Bagage.java
- Suppression du champ `cout`
- poids_total rendu obligatoire
- Ajustement de la précision (15,2)

### Place.java
- Changement du type de `numero` de String à Integer
- Suppression de la relation `placeVehicule`
- Ajout de la relation `vehicule` (ManyToOne vers Vehicule)

### Paiement.java
- Suppression de la relation `billet`
- Ajout du champ `datePaiement` (Instant)
- Ajout de la relation `commande` (ManyToOne vers Commande)
- Ajustement de la précision de montant (15,2)

### Chauffeur.java
- Ajustement des longueurs de colonnes (nom: 50, prenom: 50)
- telephone rendu obligatoire

### Trajet.java
- distance_km rendu obligatoire
- Ajustement de la précision (15,2)
- duree_estimee_minutes changé de Integer à BigDecimal(15,2)

### MethodePaiement.java
- libelle rendu obligatoire

### VoyageStatut.java
- libelle rendu obligatoire

### StatutVoyage.java
- Suppression de la clé composite @EmbeddedId
- Ajout de la clé primaire simple `id_statut_voyage`
- date_statut changé de String à Instant
- Réorganisation des relations

### StatutPaiement.java
- Suppression de la clé composite @EmbeddedId
- Ajout de la clé primaire simple `id_statut_paiement`
- date_statut changé de LocalDate à Instant
- Réorganisation des relations

### PaiementStatut.java
- libelle rendu obligatoire

### StatutVehicule.java
- Suppression de la clé composite @EmbeddedId
- Ajout de la clé primaire simple `id_statut_vehicule`
- date_statut changé de String à LocalDate
- Relation avec VehiculeStatut au lieu de VehiculesStatut

### MaintenanceVehicule.java
- Changement de la table de `maintenance_vehicule` à `vehicule_maintenance`
- Changement de l'id de `id_maintenance` à `id_vehicule_maintenance`
- date_maintenance changé de LocalDate à Instant
- Ajout du champ `dateFin` (Instant)
- description rendu obligatoire avec longueur de 100
- Ajustement de la précision de cout (15,2)

## Tables obsolètes à supprimer (selon le script SQL)

- PlaceVehicule (remplacé par la relation directe Vehicule-Place)
- VehiculesStatut (remplacé par VehiculeStatut)
- StatutVoyageId (clé composite n'est plus nécessaire)
- StatutPaiementId (clé composite n'est plus nécessaire)
- StatutVehiculeId (clé composite n'est plus nécessaire)

## Notes importantes

1. Les relations ont été réorganisées pour correspondre au nouveau schéma de base de données
2. Les précisions numériques ont été uniformisées à (15,2) au lieu de (10,2) ou (6,2)
3. Les contraintes NOT NULL ont été appliquées selon le nouveau schéma
4. Les types temporels ont été ajustés (LocalDate, Instant au lieu de String)
5. Le système de statuts a été simplifié avec des clés primaires simples au lieu de clés composites
6. La relation entre Billet et Client a été déplacée vers DetailsCommande via Commande

## Prochaines étapes recommandées

1. Exécuter le script `table-12-01-2025.sql` sur la base de données
2. Vérifier que l'application compile correctement
3. Mettre à jour les repositories, services et controllers selon les nouveaux modèles
4. Mettre à jour les vues Thymeleaf si nécessaire
5. Tester les fonctionnalités existantes pour vérifier la compatibilité

