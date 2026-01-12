# Guide de migration du code vers le nouveau schéma

## Modifications nécessaires dans les repositories

### 1. PlaceRepository.java
**Avant :**
```java
List<Place> findByPlaceVehicule_Id(Integer idPlaceVehicule);
```

**Après :**
```java
List<Place> findByVehicule_Id(Integer idVehicule);
```

### 2. StatutVoyageRepository.java
**Avant :**
```java
public interface StatutVoyageRepository extends JpaRepository<StatutVoyage, StatutVoyageId> {
```

**Après :**
```java
public interface StatutVoyageRepository extends JpaRepository<StatutVoyage, Integer> {
    List<StatutVoyage> findByVoyage_Id(Integer idVoyage);
}
```

### 3. VehiculeRepository.java
**À mettre à jour :** La requête qui utilise `sv.id.idVehiculesStatut` doit être modifiée pour utiliser `sv.vehiculeStatut.id`

### 4. Supprimer PlaceVehiculeRepository.java
Ce repository n'est plus nécessaire.

### 5. Renommer VehiculesStatutRepository.java
Renommer en `VehiculeStatutRepository.java` et mettre à jour la référence au modèle.

## Modifications nécessaires dans les services

### 1. PlaceService.java
**Avant :**
```java
public List<Place> getPlacesByPlaceVehicule(Integer idPlaceVehicule) {
    return placeRepository.findByPlaceVehicule_Id(idPlaceVehicule);
}
```

**Après :**
```java
public List<Place> getPlacesByVehicule(Integer idVehicule) {
    return placeRepository.findByVehicule_Id(idVehicule);
}
```

### 2. VehiculeService.java

**À supprimer :**
- L'injection de `PlaceVehiculeRepository`
- L'injection de `VehiculesStatutRepository` (à remplacer par `VehiculeStatutRepository`)

**Méthode create() - Avant :**
```java
PlaceVehicule pv = placeVehiculeRepository.findById(placeVehiculeId)
    .orElseThrow(() -> new IllegalArgumentException("PlaceVehicule introuvable: " + placeVehiculeId));
v.setPlaceVehicule(pv);
```

**Méthode create() - Après :**
```java
Categorie categorie = categorieRepository.findById(categorieId)
    .orElseThrow(() -> new IllegalArgumentException("Categorie introuvable: " + categorieId));
VehiculeModele vehiculeModele = vehiculeModeleRepository.findById(vehiculeModeleId)
    .orElseThrow(() -> new IllegalArgumentException("VehiculeModele introuvable: " + vehiculeModeleId));
v.setCategorie(categorie);
v.setVehiculeModele(vehiculeModele);
```

**Méthodes de statut - Avant :**
```java
VehiculesStatut vs = vehiculesStatutRepository.findByLibelle(libelleStatut)...
svId.setIdVehiculesStatut(vs.getId());
sv.setVehiculesStatut(vs);
```

**Méthodes de statut - Après :**
```java
VehiculeStatut vs = vehiculeStatutRepository.findByLibelle(libelleStatut)...
sv.setVehiculeStatut(vs);
sv.setDateStatut(LocalDate.now());
// Plus besoin de StatutVehiculeId
```

### 3. VoyageService.java

**À modifier :**
- L'injection de `VehiculesStatutRepository` (remplacer par `VehiculeStatutRepository`)

**Méthode avec StatutVoyageId - Avant :**
```java
hist.setId(new StatutVoyageId(voyage.getId(), idVoyageStatut));
```

**Méthode avec StatutVoyageId - Après :**
```java
hist.setVoyage(voyage);
hist.setVoyageStatut(voyageStatut);
hist.setDateStatut(Instant.now());
```

**Méthode getStatusLabel - Avant :**
```java
Integer statusId = statuts.get(0).getId().getIdVehiculesStatut();
return vehiculesStatutRepository.findById(statusId).map(VehiculesStatut::getLibelle).orElse(null);
```

**Méthode getStatusLabel - Après :**
```java
return statuts.get(0).getVehiculeStatut().getLibelle();
```

## Modifications nécessaires dans les controllers

### 1. BilletController.java
**Avant :**
```java
Integer idPlaceVehicule = voyage.getVehicule().getPlaceVehicule().getId();
List<Place> toutesLesPlaces = placeService.getPlacesByPlaceVehicule(idPlaceVehicule);
```

**Après :**
```java
Integer idVehicule = voyage.getVehicule().getId();
List<Place> toutesLesPlaces = placeService.getPlacesByVehicule(idVehicule);
```

### 2. VehiculeController.java
**À supprimer :**
- L'injection de `PlaceVehiculeRepository`
- Les méthodes utilisant `PlaceVehicule`

**À ajouter :**
- L'injection de `CategorieRepository`
- L'injection de `VehiculeModeleRepository`

### 3. VoyageController.java
**À modifier :**
- Remplacer `VehiculesStatutRepository` par `VehiculeStatutRepository`
- Remplacer `VehiculesStatut` par `VehiculeStatut`

```java
VehiculeStatut statutDisponible = vehiculeStatutRepository.findByLibelle("Disponible")
    .orElseThrow(() -> new RuntimeException("Statut 'Disponible' introuvable"));
```

## Nouveaux repositories à créer

### 1. CategorieRepository.java
```java
package com.brousse.repository;

import com.brousse.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Integer> {
    Optional<Categorie> findByCode(String code);
    List<Categorie> findByActiveTrue();
}
```

### 2. VehiculeModeleRepository.java
```java
package com.brousse.repository;

import com.brousse.model.VehiculeModele;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculeModeleRepository extends JpaRepository<VehiculeModele, Integer> {
}
```

### 3. VilleRepository.java
```java
package com.brousse.repository;

import com.brousse.model.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VilleRepository extends JpaRepository<Ville, Integer> {
}
```

### 4. CommandeRepository.java
```java
package com.brousse.repository;

import com.brousse.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    List<Commande> findByClient_Id(Integer idClient);
}
```

### 5. DetailsCommandeRepository.java
```java
package com.brousse.repository;

import com.brousse.model.DetailsCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailsCommandeRepository extends JpaRepository<DetailsCommande, Integer> {
    List<DetailsCommande> findByCommande_Id(Integer idCommande);
    List<DetailsCommande> findByBillet_Id(Integer idBillet);
}
```

## Ordre de migration recommandé

1. **Phase 1 : Préparer la base de données**
   - Exécuter le script `table-12-01-2025.sql`
   - Vérifier que toutes les tables sont créées

2. **Phase 2 : Créer les nouveaux repositories**
   - CategorieRepository
   - VehiculeModeleRepository
   - VilleRepository
   - CommandeRepository
   - DetailsCommandeRepository
   - Renommer VehiculesStatutRepository en VehiculeStatutRepository

3. **Phase 3 : Modifier les repositories existants**
   - PlaceRepository
   - StatutVoyageRepository
   - VehiculeRepository

4. **Phase 4 : Modifier les services**
   - PlaceService
   - VehiculeService
   - VoyageService

5. **Phase 5 : Modifier les controllers**
   - BilletController
   - VehiculeController
   - VoyageController

6. **Phase 6 : Supprimer les fichiers obsolètes**
   - PlaceVehicule.java et PlaceVehiculeRepository.java
   - VehiculesStatut.java (gardé pour référence temporaire)
   - StatutVoyageId.java, StatutPaiementId.java, StatutVehiculeId.java

7. **Phase 7 : Tests**
   - Tester toutes les fonctionnalités
   - Vérifier les relations entre les entités
   - Valider la création/modification/suppression

