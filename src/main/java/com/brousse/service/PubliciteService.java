package com.brousse.service;

import com.brousse.model.Publicite;
import com.brousse.repository.PubliciteRepository;
import com.brousse.repository.SocieteRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.brousse.model.Societe;

@Service
public class PubliciteService {
    private final PubliciteRepository publiciteRepository;
    private final SocieteRepository societeRepository;

    public PubliciteService(PubliciteRepository publiciteRepository, SocieteRepository societeRepository) {
        this.publiciteRepository = publiciteRepository;
        this.societeRepository = societeRepository;
    }

    public Publicite create(Publicite publicite) {
        return publiciteRepository.save(publicite);
    }

    public Optional<Publicite> findById(Integer id) {
        return publiciteRepository.findById(id);
    }

    public List<Publicite> findAll() {
        return publiciteRepository.findAll();
    }

    public List<Publicite> findBySocieteId(Integer idSociete) {
        return publiciteRepository.findBySociete_Id(idSociete);
    }

    public List<Publicite> findByVehiculeId(Integer idVehicule) {
        return publiciteRepository.findByVehicule_Id(idVehicule);
    }

    public Publicite update(Integer id, Publicite details) {
        Publicite p = publiciteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicité non trouvée avec id: " + id));

        p.setDateDiffusion(details.getDateDiffusion());
        p.setCout(details.getCout());
        p.setVehicule(details.getVehicule());
        p.setSociete(details.getSociete());

        return publiciteRepository.save(p);
    }

    public void delete(Integer id) {
        publiciteRepository.deleteById(id);
    }

    public List<Publicite> getByAnneeAndMonth(int year, int month) {
        return publiciteRepository.findAll().stream()
                .filter(p -> p.getDateDiffusion().getYear() == year && p.getDateDiffusion().getMonthValue() == month)
                .toList();
       
    }


    public BigDecimal totalCout(List<Publicite> publicites) {
        float total = 0;
        for (Publicite p : publicites) {
            total += p.getCout().floatValue();
        }
        return BigDecimal.valueOf(total);
    }

    public BigDecimal totalCoutBySocieteAndAnneeMois(Integer societeId, Integer annee, Integer mois) {
        List<Publicite> publicites = publiciteRepository.findBySociete_Id(societeId);
        publicites = publicites.stream()
                .filter(p -> p.getDateDiffusion().getYear() == annee && p.getDateDiffusion().getMonthValue() == mois)
                .toList();
        return totalCout(publicites);
    }

    public Map<Integer, BigDecimal> totalCoutSociete(Integer annee, Integer mois) {
        Map<Integer, BigDecimal> result = new HashMap<>();
        List<Societe> societes = societeRepository.findAll();
        for (Societe s : societes) {
            result.put(s.getId(), totalCoutBySocieteAndAnneeMois(s.getId(), annee, mois));
        }
        return result;
    }

}
