package com.brousse.service;

import com.brousse.dto.VehiculeDTO;
import com.brousse.model.*;
import com.brousse.repository.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehiculeService {
    private final VehiculeRepository vehiculeRepository;
    private final CategorieRepository categorieRepository;
    private final VehiculeModeleRepository vehiculeModeleRepository;
    private final VehiculesStatutRepository vehiculesStatutRepository;
    private final StatutVehiculeRepository statutVehiculeRepository;
    private final MaintenanceVehiculeRepository maintenanceVehiculeRepository;
    public final PlaceRepository placeRepository;

    public VehiculeService(
            VehiculeRepository vehiculeRepository,
            CategorieRepository categorieRepository,
            VehiculeModeleRepository vehiculeModeleRepository,
            VehiculesStatutRepository vehiculesStatutRepository,
            StatutVehiculeRepository statutVehiculeRepository,
            MaintenanceVehiculeRepository maintenanceVehiculeRepository,
            PlaceRepository placeRepository
    ) {
        this.vehiculeRepository = vehiculeRepository;
        this.categorieRepository = categorieRepository;
        this.vehiculeModeleRepository = vehiculeModeleRepository;
        this.vehiculesStatutRepository = vehiculesStatutRepository;
        this.statutVehiculeRepository = statutVehiculeRepository;
        this.maintenanceVehiculeRepository = maintenanceVehiculeRepository;
        this.placeRepository = placeRepository;
    }

    // Avoir chiffre d affaire par vehicule
    public List<VehiculeDTO> getChiffreAffaireParVehicule() {
        List<Object[]> rows = vehiculeRepository.findChiffreAffaireParVehicule();
        List<VehiculeDTO> result = new ArrayList<>();

        for (Object[] row : rows) {

            VehiculeDTO dto = new VehiculeDTO();

            dto.setId(((Number) row[0]).intValue());
            dto.setImmatriculation((String) row[1]);
            dto.setModele((String) row[2]);

            dto.setChiffreAffaire(
                (Number) row[3] != null ? ((Number) row[3]).doubleValue() : 0.0
            );

            result.add(dto);
        }

        return result;
    }

    // Création d'un véhicule avec statut initial "disponible" et association à une configuration de places existante
    @Transactional
    public Vehicule createVehicule(String immatriculation, BigDecimal consommation, Integer categorieId, Integer vehiculeModeleId) {
        if (immatriculation == null || immatriculation.isBlank()) {
            throw new IllegalArgumentException("Immatriculation obligatoire");
        }
        if (consommation == null) {
            throw new IllegalArgumentException("Consommation obligatoire");
        }
        Categorie categorie = categorieRepository.findById(categorieId)
                .orElseThrow(() -> new IllegalArgumentException("Categorie introuvable: " + categorieId));
        VehiculeModele vehiculeModele = vehiculeModeleRepository.findById(vehiculeModeleId)
                .orElseThrow(() -> new IllegalArgumentException("VehiculeModele introuvable: " + vehiculeModeleId));

        Vehicule v = new Vehicule();
        v.setImmatriculation(immatriculation);
        v.setConsommationL100km(consommation);
        v.setCategorie(categorie);
        v.setVehiculeModele(vehiculeModele);
        Vehicule saved = vehiculeRepository.save(v);

        // Créer les places pour ce véhicule: 5 VIP (id_categorie=2) puis le reste Standard (id_categorie=1)
        createPlacesForVehicule(saved, vehiculeModele.getPlace());
        // Historiser le statut initial
        historiserStatut(saved, 1);
        return saved;
    }

    // Crée et enregistre les places pour un véhicule donné
    private void createPlacesForVehicule(Vehicule vehicule, int totalPlaces) {
        if (vehicule == null || totalPlaces <= 0) return;

        // Récupérer les catégories Standard (1) et VIP (2)
        Categorie categorieStandard = categorieRepository.findById(1).orElse(null);
        Categorie categorieVip = categorieRepository.findById(2).orElse(null);
        Categorie categoriePrem = categorieRepository.findById(4).orElse(null);

        List<Place> places = new ArrayList<>();

        int vipCount = Math.min(2, totalPlaces); // 2 places VIP maximum
        int premCount = Math.min(6, totalPlaces); // 6 places Premium maximum

        for (int i = 1; i <= totalPlaces; i++) {
            Place p = new Place();
            p.setNumero(i);
            p.setVehicule(vehicule);
            if (i <= vipCount && categorieVip != null) {
                p.setCategorie(categorieVip);
            } else if((i <= vipCount + premCount) && (categoriePrem != null)) {
                p.setCategorie(categoriePrem);
            } else if (categorieStandard != null) {
                p.setCategorie(categorieStandard);
            } else {
                // si aucune catégorie trouvée, on ignore l'enregistrement
                continue;
            }
            places.add(p);
        }

        if (!places.isEmpty()) {
            placeRepository.saveAll(places);
        }
    }

    public List<Vehicule> listVehicules() {
        return vehiculeRepository.findAll();
    }

    public Optional<Vehicule> getVehicule(Integer id) {
        return vehiculeRepository.findById(id);
    }

    @Transactional
    public Vehicule updateVehicule(Integer id, String immatriculation, BigDecimal consommation, Integer categorieId, Integer vehiculeModeleId) {
        Vehicule v = vehiculeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicule introuvable: " + id));
        if (immatriculation != null && !immatriculation.isBlank()) v.setImmatriculation(immatriculation);
        if (consommation != null) v.setConsommationL100km(consommation);
        if (categorieId != null) {
            Categorie categorie = categorieRepository.findById(categorieId)
                    .orElseThrow(() -> new IllegalArgumentException("Categorie introuvable: " + categorieId));
            v.setCategorie(categorie);
        }
        if (vehiculeModeleId != null) {
            VehiculeModele vehiculeModele = vehiculeModeleRepository.findById(vehiculeModeleId)
                    .orElseThrow(() -> new IllegalArgumentException("VehiculeModele introuvable: " + vehiculeModeleId));
            v.setVehiculeModele(vehiculeModele);
        }
        return vehiculeRepository.save(v);
    }

    @Transactional
    public void deleteVehicule(Integer id) {
        vehiculeRepository.deleteById(id);
    }

    // Statut: maintenance
    @Transactional
    public Vehicule mettreEnMaintenance(Integer id, Instant dateMaintenance, String description, BigDecimal cout) {
        Vehicule v = vehiculeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicule introuvable: " + id));
        Vehicule saved = vehiculeRepository.save(v);

        // Enregistrer maintenance
        MaintenanceVehicule m = new MaintenanceVehicule();
        m.setVehicule(saved);
        m.setDateMaintenance(dateMaintenance != null ? dateMaintenance : Instant.now());
        m.setDescription(description);
        m.setCout(cout);
        maintenanceVehiculeRepository.save(m);

        // Historiser
        historiserStatut(saved, 2);
        return saved;
    }

    // Statut: actif (disponible)
    @Transactional
    public Vehicule activer(Integer id) {
        Vehicule v = vehiculeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicule introuvable: " + id));
        Vehicule saved = vehiculeRepository.save(v);
        historiserStatut(saved, 1);
        return saved;
    }

    // Statut: hors service
    @Transactional
    public Vehicule mettreHorsService(Integer id) {
        Vehicule v = vehiculeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicule introuvable: " + id));
        Vehicule saved = vehiculeRepository.save(v);
        historiserStatut(saved, 3);
        return saved;
    }

    public List<StatutVehicule> historiqueStatuts(Integer idVehicule) {
        return statutVehiculeRepository.findByVehiculeIdOrderByDateStatutDesc(idVehicule);
    }

    public List<MaintenanceVehicule> historiqueMaintenances(Integer idVehicule) {
        return maintenanceVehiculeRepository.findByVehiculeIdOrderByDateMaintenanceDesc(idVehicule);
    }

    // Utilitaires
    public void historiserStatut(Vehicule vehicule, Integer idStatut) {
        VehiculeStatut vs = vehiculesStatutRepository.findById(idStatut)
                .orElseThrow(() -> new IllegalArgumentException("VehiculeStatut introuvable: " + idStatut));
        StatutVehicule sv = new StatutVehicule();
        sv.setVehicule(vehicule);
        sv.setVehiculeStatut(vs);
        sv.setDateStatut(java.time.LocalDate.now());
        statutVehiculeRepository.save(sv);
    }

    public String getCurrentStatusLibelle(Vehicule vehicule) {
        List<StatutVehicule> statuts = statutVehiculeRepository.findByVehiculeIdOrderByDateStatutDesc(vehicule.getId());
        if (statuts.isEmpty()) {
            return null;
        }
        return statuts.get(0).getVehiculeStatut().getLibelle();
    }

    public Integer countPlace(Integer id_categorie) {
        List<Place> places = placeRepository.findAll();
        int count = 0;

        for(Place place : places) {
            if(place.getCategorie().getId() == id_categorie) {
                count++;
            }
        }
        return count;
    }

    // Compte le nombre de places pour un véhicule donné et une catégorie donnée
    public Integer countPlaceByVehiculeAndCategorie(Integer idVehicule, Integer idCategorie) {
        if (idVehicule == null || idCategorie == null) return 0;
        List<Place> places = placeRepository.findByVehicule_Id(idVehicule);
        int count = 0;
        for (Place p : places) {
            if (p.getCategorie() != null && p.getCategorie().getId() != null && p.getCategorie().getId().equals(idCategorie)) {
                count++;
            }
        }
        return count;
    }

}
