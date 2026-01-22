package com.brousse.service;

import com.brousse.model.PubliciteDiffusion;
import com.brousse.repository.PubliciteDiffusionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PubliciteDiffusionService {

    private final PubliciteDiffusionRepository publiciteDiffusionRepository;

    public PubliciteDiffusionService(PubliciteDiffusionRepository publiciteDiffusionRepository) {
        this.publiciteDiffusionRepository = publiciteDiffusionRepository;
    }

    public PubliciteDiffusion save(PubliciteDiffusion diffusion) {
        return publiciteDiffusionRepository.save(diffusion);
    }

    public Optional<PubliciteDiffusion> findById(Integer id) {
        return publiciteDiffusionRepository.findById(id);
    }

    public List<PubliciteDiffusion> findAll() {
        return publiciteDiffusionRepository.findAll();
    }

    public List<PubliciteDiffusion> findByPubliciteId(Integer publiciteId) {
        return publiciteDiffusionRepository.findByPublicite_Id(publiciteId);
    }

    public List<PubliciteDiffusion> findBySocieteId(Integer societeId) {
        return publiciteDiffusionRepository.findByPublicite_Societe_Id(societeId);
    }

    public List<PubliciteDiffusion> findByVoyageId(Integer voyageId) {
        return publiciteDiffusionRepository.findByVoyage_Id(voyageId);
    }

    public List<PubliciteDiffusion> findNonPayees() {
        return publiciteDiffusionRepository.findByEstPayeFalse();
    }

    public List<PubliciteDiffusion> findNonPayeesBySocieteId(Integer societeId) {
        return publiciteDiffusionRepository.findByPublicite_Societe_IdAndEstPayeFalse(societeId);
    }

    public List<PubliciteDiffusion> findBySocieteIdAndAnneeMois(Integer societeId, Integer annee, Integer mois) {
        return publiciteDiffusionRepository.findBySocieteIdAndAnneeMois(societeId, annee, mois);
    }

    public Long countBySocieteId(Integer societeId) {
        return publiciteDiffusionRepository.countBySocieteId(societeId);
    }

    public Long countPayeesBySocieteId(Integer societeId) {
        return publiciteDiffusionRepository.countBySocieteIdAndEstPayeTrue(societeId);
    }

    public void delete(Integer id) {
        publiciteDiffusionRepository.deleteById(id);
    }

}

