package com.brousse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.brousse.dto.TrajetFilterDTO;
import com.brousse.model.Trajet;
import com.brousse.model.Tarif;
import com.brousse.repository.TrajetRepository;
import com.brousse.repository.TarifRepository;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrajetService {
    @Autowired
    private TrajetRepository trajetRepository;
    
    @Autowired
    private TarifRepository tarifRepository;

    // Create
    public Trajet create(Trajet trajet) {
        return trajetRepository.save(trajet);
    }

    // Read - Get by ID
    public Optional<Trajet> findById(Integer id) {
        return trajetRepository.findById(id);
    }

    // Read - Get all
    public List<Trajet> findAll() {
        return trajetRepository.findAll();
    }

    // Read - Get with filters
    public List<Trajet> findWithFilter(TrajetFilterDTO filtres) {
        Specification<Trajet> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtres.getGareDepart() != null) {
                predicates.add(criteriaBuilder.equal(
                    root.get("gareDepart").get("id"), 
                    filtres.getGareDepart()
                ));
            }

            if (filtres.getGareArrivee() != null) {
                predicates.add(criteriaBuilder.equal(
                    root.get("gareArrivee").get("id"), 
                    filtres.getGareArrivee()
                ));
            }

            if (filtres.getDistanceMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("distanceKm"),
                    filtres.getDistanceMin()
                ));
            }

            if (filtres.getDistanceMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("distanceKm"),
                    filtres.getDistanceMax()
                ));
            }

            if (filtres.getDureeMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("dureeEstimeeMinutes"),
                    filtres.getDureeMin()
                ));
            }

            if (filtres.getDureeMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("dureeEstimeeMinutes"),
                    filtres.getDureeMax()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return trajetRepository.findAll(spec);
    }

    // Update
    public Trajet update(Integer id, Trajet trajetDetails) {
        Trajet trajet = trajetRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Trajet non trouvé avec l'id: " + id));
        
        trajet.setGareDepart(trajetDetails.getGareDepart());
        trajet.setGareArrivee(trajetDetails.getGareArrivee());
        trajet.setDistanceKm(trajetDetails.getDistanceKm());
        trajet.setDureeEstimeeMinutes(trajetDetails.getDureeEstimeeMinutes());
        
        return trajetRepository.save(trajet);
    }

    // Delete
    public void delete(Integer id) {
        trajetRepository.deleteById(id);
    }

    // Get tarif for a trajet
    public Tarif getTarifForTrajet(Integer trajetId) {
        List<Tarif> tarifs = tarifRepository.findAll();
        return tarifs.stream()
            .filter(t -> t.getTrajet().getId().equals(trajetId))
            .findFirst()
            .orElse(null);
    }

    // Create or update tarif for a trajet
    public Tarif createOrUpdateTarif(Trajet trajet, BigDecimal prixBase, BigDecimal prixBagage) {
        Tarif tarif = getTarifForTrajet(trajet.getId());

        if (tarif == null) {
            tarif = new Tarif();
            tarif.setTrajet(trajet);
        }

        tarif.setPrixBase(prixBase);
        tarif.setPrixBagage(prixBagage != null ? prixBagage : BigDecimal.ZERO);

        return tarifRepository.save(tarif);
    }
}
