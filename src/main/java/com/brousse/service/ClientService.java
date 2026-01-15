package com.brousse.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.brousse.dto.ClientFilterDTO;
import com.brousse.model.Client;
import com.brousse.repository.ClientRepository;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Create
    public Client create(Client client) {
        return clientRepository.save(client);
    }

    // Read - Get by ID
    public Optional<Client> findById(Integer id) {
        return clientRepository.findById(id);
    }

    // Read - Get all
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    // Read - Get with filters
    public List<Client> findWithFilter(ClientFilterDTO filtres) {
        Specification<Client> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtres.getNom() != null && !filtres.getNom().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nom")), 
                    "%" + filtres.getNom().toLowerCase() + "%"
                ));
            }

            if (filtres.getPrenom() != null && !filtres.getPrenom().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("prenom")), 
                    "%" + filtres.getPrenom().toLowerCase() + "%"
                ));
            }

            if (filtres.getTelephone() != null && !filtres.getTelephone().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    root.get("telephone"), 
                    "%" + filtres.getTelephone() + "%"
                ));
            }

            if (filtres.getEmail() != null && !filtres.getEmail().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")), 
                    "%" + filtres.getEmail().toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return clientRepository.findAll(spec);
    }

    // Update
    public Client update(Integer id, Client clientDetails) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + id));
        
        client.setNom(clientDetails.getNom());
        client.setPrenom(clientDetails.getPrenom());
        client.setTelephone(clientDetails.getTelephone());
        client.setEmail(clientDetails.getEmail());
        
        return clientRepository.save(client);
    }

    // Delete
    public void delete(Integer id) {
        clientRepository.deleteById(id);
    }
}
