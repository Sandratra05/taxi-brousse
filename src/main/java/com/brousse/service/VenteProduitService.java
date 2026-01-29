package com.brousse.service;

import org.springframework.stereotype.Service;

import com.brousse.model.VenteProduit;
import com.brousse.repository.VenteProduitRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VenteProduitService {
    private final VenteProduitRepository venteProduitRepository;

    public VenteProduitService(VenteProduitRepository venteProduitRepository) {
        this.venteProduitRepository = venteProduitRepository;
    }

    // Create
    public VenteProduit create(VenteProduit venteProduit) {
        if (venteProduit.getDateVente() == null) {
            venteProduit.setDateVente(LocalDateTime.now());
        }
        return venteProduitRepository.save(venteProduit);
    }

    // Read - Get by ID
    public Optional<VenteProduit> findById(Integer id) {
        return venteProduitRepository.findById(id);
    }

    // Read - Get all
    public List<VenteProduit> findAll() {
        return venteProduitRepository.findAll();
    }

    // Read - Get by Voyage ID
    public List<VenteProduit> findByVoyageId(Integer voyageId) {
        return venteProduitRepository.findByVoyageId(voyageId);
    }

    // Read - Get by Client ID
    public List<VenteProduit> findByClientId(Integer clientId) {
        return venteProduitRepository.findByClientId(clientId);
    }

    // Read - Get by Produit ID
    public List<VenteProduit> findByProduitId(Integer produitId) {
        return venteProduitRepository.findByProduitId(produitId);
    }

    // Update
    public VenteProduit update(Integer id, VenteProduit venteProduitDetails) {
        VenteProduit venteProduit = venteProduitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vente produit non trouvée avec l'id: " + id));
        
        venteProduit.setVoyage(venteProduitDetails.getVoyage());
        venteProduit.setProduit(venteProduitDetails.getProduit());
        venteProduit.setClient(venteProduitDetails.getClient());
        venteProduit.setQuantite(venteProduitDetails.getQuantite());
        venteProduit.setDateVente(venteProduitDetails.getDateVente());
        
        return venteProduitRepository.save(venteProduit);
    }

    // Delete
    public void delete(Integer id) {
        VenteProduit venteProduit = venteProduitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vente produit non trouvée avec l'id: " + id));
        venteProduitRepository.delete(venteProduit);
    }

    // Calculer le montant total d'une vente
    public Integer calculerMontantTotal(VenteProduit venteProduit) {
        if (venteProduit.getProduit() != null && venteProduit.getProduit().getPrixUnitaire() != null) {
            return venteProduit.getProduit().getPrixUnitaire() * venteProduit.getQuantite();
        }
        return 0;
    }
}
