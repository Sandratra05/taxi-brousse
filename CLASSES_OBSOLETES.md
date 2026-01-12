# Classes obsolètes à supprimer

Les classes suivantes sont obsolètes selon le nouveau schéma de base de données et peuvent être supprimées après avoir vérifié qu'elles ne sont plus utilisées dans le code :

## À supprimer

1. **PlaceVehicule.java** - Cette table n'existe plus dans le nouveau schéma. Les places sont maintenant directement liées aux véhicules via la table `place`.

2. **VehiculesStatut.java** - Remplacé par `VehiculeStatut.java` (avec une orthographe corrigée).

3. **StatutVoyageId.java** - Clé composite n'est plus nécessaire, remplacée par une clé primaire simple dans `StatutVoyage`.

4. **StatutPaiementId.java** - Clé composite n'est plus nécessaire, remplacée par une clé primaire simple dans `StatutPaiement`.

5. **StatutVehiculeId.java** - Clé composite n'est plus nécessaire, remplacée par une clé primaire simple dans `StatutVehicule`.

## Commandes pour supprimer les fichiers obsolètes

```bash
cd /home/zark/Bureau/ITU/Annee-3/Taxi-brousse/taxi-brousse/src/main/java/com/brousse/model

# Sauvegarder au cas où (optionnel)
mkdir -p ../../../../../backup/model
cp PlaceVehicule.java VehiculesStatut.java StatutVoyageId.java StatutPaiementId.java StatutVehiculeId.java ../../../../../backup/model/

# Supprimer les fichiers obsolètes
rm -f PlaceVehicule.java VehiculesStatut.java StatutVoyageId.java StatutPaiementId.java StatutVehiculeId.java
```

## Actions à effectuer avant la suppression

1. **Rechercher les utilisations** de ces classes dans tout le projet :
   - Repositories
   - Services
   - Controllers
   - DTOs
   - Vues Thymeleaf

2. **Mettre à jour** les références vers les nouvelles classes

3. **Vérifier la compilation** du projet

4. **Tester** l'application pour s'assurer qu'il n'y a pas de régressions

## Commande pour rechercher les utilisations

```bash
# Rechercher PlaceVehicule
grep -r "PlaceVehicule" /home/zark/Bureau/ITU/Annee-3/Taxi-brousse/taxi-brousse/src --include="*.java"

# Rechercher VehiculesStatut
grep -r "VehiculesStatut" /home/zark/Bureau/ITU/Annee-3/Taxi-brousse/taxi-brousse/src --include="*.java"

# Rechercher les Id composites
grep -r "StatutVoyageId\|StatutPaiementId\|StatutVehiculeId" /home/zark/Bureau/ITU/Annee-3/Taxi-brousse/taxi-brousse/src --include="*.java"
```

