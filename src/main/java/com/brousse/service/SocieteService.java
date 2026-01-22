package com.brousse.service;

import com.brousse.model.Societe;
import com.brousse.repository.SocieteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SocieteService {
    private final SocieteRepository societeRepository;

    public SocieteService(SocieteRepository societeRepository) {
        this.societeRepository = societeRepository;
    }

    public Societe create(Societe societe) {
        return societeRepository.save(societe);
    }

    public Optional<Societe> findById(Integer id) {
        return societeRepository.findById(id);
    }

    public List<Societe> findAll() {
        return societeRepository.findAll();
    }

    public Societe update(Integer id, Societe societeDetails) {
        Societe s = societeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Société non trouvée avec id: " + id));

        s.setNom(societeDetails.getNom());
        s.setAdresse(societeDetails.getAdresse());
        s.setTelephone(societeDetails.getTelephone());
        s.setEmail(societeDetails.getEmail());

        return societeRepository.save(s);
    }

    public void delete(Integer id) {
        societeRepository.deleteById(id);
    }
}
