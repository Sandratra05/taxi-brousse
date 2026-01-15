package com.brousse.service;

import com.brousse.model.Place;
import com.brousse.repository.BilletRepository;
import com.brousse.repository.PlaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {
    
    private final PlaceRepository placeRepository;

    private final BilletRepository billetRepository;

    public PlaceService(PlaceRepository placeRepository, BilletRepository billetRepository) {
        this.placeRepository = placeRepository;
        this.billetRepository = billetRepository;
    }

    /**
     * Vérifie si une place est disponible pour un voyage donné
     * Une place est disponible si elle n'est pas rattachée à un billet pour ce voyage
     *
     * @param idPlace ID de la place
     * @param idVoyage ID du voyage
     * @return true si la place est disponible, false sinon
     */
    public boolean isPlaceDisponible(Integer idPlace, Integer idVoyage) {
        if (idPlace == null || idVoyage == null) {
            return false;
        }
        boolean billetExiste = billetRepository.findByVoyage_Id(idVoyage).stream()
                .anyMatch(billet -> billet.getPlace().getId().equals(idPlace));
        // Vérifier si un billet existe avec cette place ET ce voyage
//        boolean billetExiste = billetRepository.existsByPlace_IdAndVoyage_Id(idPlace, idVoyage);

        // La place est disponible si AUCUN billet n'existe
        return !billetExiste;
    }

    /**
     * Récupère toutes les places
     */
    public List<Place> findAll() {
        return placeRepository.findAll();
    }

    /**
     * Récupère une place par son ID
     */
    public Optional<Place> findById(Integer id) {
        return placeRepository.findById(id);
    }

    /**
     * Récupère toutes les places disponibles pour un voyage donné
     *
     * @param idVoyage ID du voyage
     * @return Liste des places disponibles
     */
    public List<Place> getPlacesDisponiblesPourVoyage(Integer idVoyage) {
        List<Place> toutesLesPlaces = placeRepository.findAll();
        
        return toutesLesPlaces.stream()
                .filter(place -> isPlaceDisponible(place.getId(), idVoyage))
                .toList();
    }

    /**
     * Récupère toutes les places d'une configuration de véhicule
     */
    public List<Place> getPlacesByVehicule(Integer idVehicule) {
        return placeRepository.findByVehicule_Id(idVehicule);
    }
}
