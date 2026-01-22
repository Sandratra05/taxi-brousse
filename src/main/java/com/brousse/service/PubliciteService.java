package com.brousse.service;

import com.brousse.model.Publicite;
import com.brousse.repository.PubliciteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PubliciteService {

    private final PubliciteRepository publiciteRepository;

    public PubliciteService(PubliciteRepository publiciteRepository) {
        this.publiciteRepository = publiciteRepository;
    }

    public Publicite save(Publicite publicite) {
        return publiciteRepository.save(publicite);
    }

    public Optional<Publicite> findById(Integer id) {
        return publiciteRepository.findById(id);
    }

    public List<Publicite> findAll() {
        return publiciteRepository.findAll();
    }

    public List<Publicite> findBySocieteId(Integer societeId) {
        return publiciteRepository.findBySociete_Id(societeId);
    }

    public Publicite update(Integer id, Publicite details) {
        Publicite p = publiciteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicité non trouvée avec id: " + id));

        p.setNom(details.getNom());
        p.setSociete(details.getSociete());

        return publiciteRepository.save(p);
    }

    public void delete(Integer id) {
        publiciteRepository.deleteById(id);
    }


}
