package com.brousse.service;

import com.brousse.dto.VehiculeDTO;
import com.brousse.model.*;
import com.brousse.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehiculeService {
    private final VehiculeRepository vehiculeRepository;
    private final PlaceVehiculeRepository placeVehiculeRepository;
    private final VehiculesStatutRepository vehiculesStatutRepository;
    private final StatutVehiculeRepository statutVehiculeRepository;
    private final MaintenanceVehiculeRepository maintenanceVehiculeRepository;

    public VehiculeService(
            VehiculeRepository vehiculeRepository,
            PlaceVehiculeRepository placeVehiculeRepository,
            VehiculesStatutRepository vehiculesStatutRepository,
            StatutVehiculeRepository statutVehiculeRepository,
            MaintenanceVehiculeRepository maintenanceVehiculeRepository
    ) {
        this.vehiculeRepository = vehiculeRepository;
        this.placeVehiculeRepository = placeVehiculeRepository;
        this.vehiculesStatutRepository = vehiculesStatutRepository;
        this.statutVehiculeRepository = statutVehiculeRepository;
        this.maintenanceVehiculeRepository = maintenanceVehiculeRepository;
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
                row[3] != null ? ((Number) row[3]).doubleValue() : 0.0
            );

            result.add(dto);
        }

        return result;
    }

    // Création d'un véhicule avec statut initial "disponible" et association à une configuration de places existante
    @Transactional
    public Vehicule createVehicule(String immatriculation, String modele, Integer placeVehiculeId) {
        if (immatriculation == null || immatriculation.isBlank()) {
            throw new IllegalArgumentException("Immatriculation obligatoire");
        }
        if (modele == null || modele.isBlank()) {
            throw new IllegalArgumentException("Modèle obligatoire");
        }
        PlaceVehicule pv = placeVehiculeRepository.findById(placeVehiculeId)
                .orElseThrow(() -> new IllegalArgumentException("PlaceVehicule introuvable: " + placeVehiculeId));

        Vehicule v = new Vehicule();
        v.setImmatriculation(immatriculation);
        v.setModele(modele);
        v.setPlaceVehicule(pv);
        Vehicule saved = vehiculeRepository.save(v);

        // Pas de création automatique des places: elles sont déjà configurées via place_vehicule.nb_place

        // Historiser le statut initial
        historiserStatut(saved, 1);
        return saved;
    }

    public List<Vehicule> listVehicules() {
        return vehiculeRepository.findAll();
    }

    public Optional<Vehicule> getVehicule(Integer id) {
        return vehiculeRepository.findById(id);
    }

    @Transactional
    public Vehicule updateVehicule(Integer id, String immatriculation, String modele, Integer placeVehiculeId) {
        Vehicule v = vehiculeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicule introuvable: " + id));
        if (immatriculation != null && !immatriculation.isBlank()) v.setImmatriculation(immatriculation);
        if (modele != null && !modele.isBlank()) v.setModele(modele);
        if (placeVehiculeId != null) {
            PlaceVehicule pv = placeVehiculeRepository.findById(placeVehiculeId)
                    .orElseThrow(() -> new IllegalArgumentException("PlaceVehicule introuvable: " + placeVehiculeId));
            v.setPlaceVehicule(pv);
        }
        return vehiculeRepository.save(v);
    }

    @Transactional
    public void deleteVehicule(Integer id) {
        vehiculeRepository.deleteById(id);
    }

    // Statut: maintenance
    @Transactional
    public Vehicule mettreEnMaintenance(Integer id, LocalDate dateMaintenance, String description, BigDecimal cout) {
        Vehicule v = vehiculeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicule introuvable: " + id));
        Vehicule saved = vehiculeRepository.save(v);

        // Enregistrer maintenance
        MaintenanceVehicule m = new MaintenanceVehicule();
        m.setVehicule(saved);
        m.setDateMaintenance(dateMaintenance != null ? dateMaintenance : LocalDate.now());
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
    private void historiserStatut(Vehicule vehicule, String libelleStatut) {
        VehiculesStatut vs = vehiculesStatutRepository.findByLibelle(libelleStatut)
                .orElseGet(() -> {
                    VehiculesStatut created = new VehiculesStatut();
                    created.setLibelle(libelleStatut);
                    return vehiculesStatutRepository.save(created);
                });
        StatutVehicule sv = new StatutVehicule();
        StatutVehiculeId svId = new StatutVehiculeId();
        svId.setIdVehicule(vehicule.getId());
        svId.setIdVehiculesStatut(vs.getId());
        sv.setId(svId);
        sv.setVehicule(vehicule);
        sv.setVehiculesStatut(vs);
        sv.setDateStatut(LocalDate.now().toString());
        statutVehiculeRepository.save(sv);
    }

    public void historiserStatut(Vehicule vehicule, Integer idStatut) {
        VehiculesStatut vs = vehiculesStatutRepository.findById(idStatut)
                .orElseThrow(() -> new IllegalArgumentException("VehiculesStatut introuvable: " + idStatut));
        StatutVehicule sv = new StatutVehicule();
        StatutVehiculeId svId = new StatutVehiculeId();
        svId.setIdVehicule(vehicule.getId());
        svId.setIdVehiculesStatut(vs.getId());
        sv.setId(svId);
        sv.setVehicule(vehicule);
        sv.setVehiculesStatut(vs);
        sv.setDateStatut(LocalDate.now().toString());
        statutVehiculeRepository.save(sv);
    }

    public String getCurrentStatusLibelle(Vehicule vehicule) {
        List<StatutVehicule> statuts = statutVehiculeRepository.findByVehiculeIdOrderByDateStatutDesc(vehicule.getId());
        if (statuts.isEmpty()) {
            return null;
        }
        Integer statusId = statuts.get(0).getId().getIdVehiculesStatut();
        return vehiculesStatutRepository.findById(statusId).map(VehiculesStatut::getLibelle).orElse(null);
    }
}
